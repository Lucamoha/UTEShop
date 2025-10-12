package com.uteshop.dao.impl.manager;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.manager.IInventoryManagerDao;
import com.uteshop.dao.manager.common.PageResult;
import com.uteshop.dto.manager.inventory.InventoryRow;
import com.uteshop.entity.branch.BranchInventory;
import com.uteshop.entity.branch.Branches;
import com.uteshop.entity.catalog.ProductVariants;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.util.*;

public class InventoryManagerDaoImpl extends AbstractDao<BranchInventory> implements IInventoryManagerDao {
    public InventoryManagerDaoImpl() {
        super(BranchInventory.class);
    }

    private java.util.Set<Integer> fetchProductIdsByCategorySP(EntityManager em, Integer categoryId) {
        jakarta.persistence.StoredProcedureQuery spq =
                em.createStoredProcedureQuery("sp_GetProductsByCategory");
        spq.registerStoredProcedureParameter("CategoryId", Integer.class, jakarta.persistence.ParameterMode.IN);
        spq.setParameter("CategoryId", categoryId);

        java.util.List<Object[]> res = spq.getResultList();
        java.util.Set<Integer> pids = new java.util.LinkedHashSet<>(res.size());
        for (Object[] row : res) {
            Object id0 = row[0];
            if (id0 instanceof Integer) {
                pids.add((Integer) id0);
            } else if (id0 != null) {
                if (id0 instanceof java.math.BigInteger bi) pids.add(bi.intValue());
                else if (id0 instanceof Long lg) pids.add(lg.intValue());
            }
        }
        return pids;
    }

    @Override
    public PageResult<InventoryRow> search(
            Integer branchId,
            Integer categoryId,
            String keyword,
            int page, int size,
            String sort, String dir) {

        EntityManager em = JPAConfigs.getEntityManager();

        Set<Integer> productIds = null;
        if (categoryId != null) {
            productIds = fetchProductIdsByCategorySP(em, categoryId);
            if (productIds == null || productIds.isEmpty()) {
                // không có sản phẩm nào trong cây danh mục -> trả rỗng
                int pn = Math.max(page, 1);
                int pz = Math.max(size, 1);
                return new PageResult<>(Collections.emptyList(), 0, pn, pz);
            }
        }

        // WHERE động
        StringBuilder where = new StringBuilder(" where v.Status = true ");
        if (productIds != null) {
            where.append(" and p.Id in :pids ");
        }
        if (keyword != null && !keyword.isBlank()) {
            where.append(" and ( lower(p.Name) like :kw or lower(v.SKU) like :kw ) ");
        }

        // SELECT (Object[]) + map về InventoryRow
        StringBuilder select = new StringBuilder()
                .append("select v.Id, p.Id, p.Name, v.SKU, v.Price, ")
                .append("       coalesce( (select s.BranchStock from BranchInventory s ")
                .append("                 where s.variant.Id = v.Id and s.branch.Id = :b), 0) ")
                .append("from ProductVariants v ")
                .append("join v.product p ");

        // ORDER BY theo sort/dir
        String safeSort = (sort == null || sort.isBlank()) ? "created" : sort.toLowerCase();
        String safeDir  = "asc".equalsIgnoreCase(dir) ? "asc" : "desc";

        String order;
        switch (safeSort) {
            case "product":
                order = " order by p.Name " + safeDir + ", v.Id asc";
                break;
            case "sku":
                order = " order by v.SKU " + safeDir + ", v.Id asc";
                break;
            case "price":
                order = " order by v.Price " + safeDir + ", v.Id asc";
                break;
            case "qty":
                // sắp theo tồn kho chi nhánh (subquery cùng như phần select)
                order = " order by coalesce( (select s.BranchStock from BranchInventory s "
                        + "                  where s.variant.Id = v.Id and s.branch.Id = :b), 0) "
                        + safeDir + ", v.Id asc";
                break;
            default: // created
                order = " order by p.CreatedAt " + safeDir + ", v.Id asc";
        }

        TypedQuery<Object[]> q = em.createQuery(select.toString() + where + order, Object[].class)
                .setParameter("b", branchId);

        // COUNT query (không có order by)
        TypedQuery<Long> cq = em.createQuery(
                "select count(v) from ProductVariants v join v.product p " + where,
                Long.class
        );

        if (productIds != null) {
            q.setParameter("pids", productIds);
            cq.setParameter("pids", productIds);
        }
        if (keyword != null && !keyword.isBlank()) {
            String kw = "%" + keyword.toLowerCase().trim() + "%";
            q.setParameter("kw", kw);
            cq.setParameter("kw", kw);
        }

        int pz = Math.max(size, 1);
        int pn = Math.max(page, 1);
        q.setFirstResult((pn - 1) * pz);
        q.setMaxResults(pz);

        List<Object[]> raw = q.getResultList();
        List<InventoryRow> rows = new ArrayList<>(raw.size());
        for (Object[] r : raw) {
            Integer variantId   = (Integer) r[0];
            Integer productId   = (Integer) r[1];
            String  productName = (String)  r[2];
            String  sku         = (String)  r[3];
            BigDecimal price    = (BigDecimal) r[4];
            Integer qty         = (Integer) r[5];
            rows.add(new InventoryRow(productId, productName, variantId, sku, qty, price));
        }

        long total = cq.getSingleResult();
        return new PageResult<>(rows, total, pn, pz);
    }

    @Override
    public Map<String, Integer> bulkAdjustBySku(Integer branchId, Map<String, Integer> deltaBySku) {
        EntityManager em = JPAConfigs.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        Map<String, Integer> result = new LinkedHashMap<>(); // giữ đúng thứ tự gửi vào

        if (deltaBySku == null || deltaBySku.isEmpty()) return result;

        try {
            tx.begin();

            // Lấy map SKU -> variantId một lượt
            List<String> skus = new ArrayList<>(deltaBySku.keySet());
            List<Object[]> rows = em.createQuery(
                            "select v.SKU, v.Id from ProductVariants v where v.SKU in :skus", Object[].class)
                    .setParameter("skus", skus)
                    .getResultList();

            Map<String, Integer> skuToVid = new HashMap<>();
            for (Object[] r : rows) skuToVid.put((String) r[0], (Integer) r[1]);

            // Lấy reference Branch
            Branches branch = em.getReference(Branches.class, branchId);

            for (String sku : skus) {
                Integer vid = skuToVid.get(sku);
                Integer delta = deltaBySku.get(sku);
                if (vid == null || delta == null) { result.put(sku, null); continue; }

                // PESSIMISTIC_WRITE để tránh race
                BranchInventory inv = em.find(BranchInventory.class,
                        new BranchInventory.Id(branchId, vid), LockModeType.PESSIMISTIC_WRITE);

                if (inv == null) {
                    inv = new BranchInventory();
                    inv.setId(new BranchInventory.Id(branchId, vid));
                    inv.setBranch(branch);
                    inv.setVariant(em.getReference(ProductVariants.class, vid));
                    inv.setBranchStock(0);
                    em.persist(inv);
                }

                int after = inv.getBranchStock() + delta;
                if (after < 0) after = 0;

                inv.setBranchStock(after);
                result.put(sku, after);
            }

            em.flush();
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    @Override
    public List<InventoryRow> findAllForExport(Integer branchId) {
        EntityManager em = JPAConfigs.getEntityManager();

        // Một dòng cho mỗi variant; tồn lấy theo chi nhánh 'branchId' (null -> 0)
        final String jpql = """
            select
                p.Id,
                p.Name,
                v.Id,
                v.SKU,
                coalesce(
                  (select s.BranchStock from BranchInventory s
                   where s.variant.Id = v.Id and s.branch.Id = :b),
                  0
                ),
                v.Price
            from ProductVariants v
                join v.product p
            where v.Status = true
              and p.Status = true
            order by p.Name asc, v.SKU asc
            """;

        TypedQuery<Object[]> q = em.createQuery(jpql, Object[].class)
                .setParameter("b", branchId);

        List<Object[]> raw = q.getResultList();
        List<InventoryRow> out = new ArrayList<>(raw.size());
        for (Object[] r : raw) {
            Integer productId   = (Integer) r[0];
            String  productName = (String)  r[1];
            Integer variantId   = (Integer) r[2];
            String  sku         = (String)  r[3];
            Number  qtyNum      = (Number)  r[4];
            int     qty         = qtyNum == null ? 0 : qtyNum.intValue();
            java.math.BigDecimal price = (java.math.BigDecimal) r[5];

            out.add(new InventoryRow(productId, productName, variantId, sku, qty, price));
        }
        return out;
    }

    public Integer findVariantIdBySku(String sku) {
        EntityManager em = JPAConfigs.getEntityManager();

        if (sku == null || sku.isBlank()) return null;
        List<Integer> ids = em.createQuery(
                        "select v.Id from ProductVariants v where v.SKU = :sku and v.Status = true",
                        Integer.class
                ).setParameter("sku", sku.trim())
                .setMaxResults(1)
                .getResultList();
        return ids.isEmpty() ? null : ids.get(0);
    }
}
