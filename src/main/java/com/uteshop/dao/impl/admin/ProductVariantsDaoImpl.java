package com.uteshop.dao.impl.admin;

import java.util.List;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.admin.IProductVariantsDao;
import com.uteshop.dto.admin.ProductVariantDetailsModel;
import com.uteshop.entity.catalog.ProductVariants;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

public class ProductVariantsDaoImpl extends AbstractDao<ProductVariants> implements IProductVariantsDao {
	public ProductVariantsDaoImpl() {
		super(ProductVariants.class);
	}

	@Override
	public long getLowStockCount(int threshold) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			String sql = """

					SELECT COUNT(*)
					FROM(
						SELECT p.Id, SUM(bi.BranchStock) AS TotalStock
						FROM Products p
						JOIN ProductVariants pv ON pv.ProductId = p.Id
						JOIN BranchInventory bi ON bi.VariantId = pv.Id
						WHERE p.Status = 1
						GROUP BY p.Id
						HAVING SUM(bi.BranchStock) <= :threshold
					) AS Q
					""";
			return ((Number) enma.createNativeQuery(sql).setParameter("threshold", threshold).getSingleResult())
					.longValue();
		} finally {
			enma.close();
		}
	}

	@Override
	public List<Object[]> getLowStockProducts(int limit, int threshold) {
		EntityManager enma = JPAConfigs.getEntityManager();
		// threshold = 5;
		try {
			String sql = """
					SELECT TOP(:limit)
						p.Id AS ProductId,
						p.Name AS ProductName,
						SUM(bi.BranchStock) AS TotalStock
					FROM Products p
					JOIN ProductVariants pv ON pv.ProductId = p.Id
					JOIN BranchInventory bi ON bi.VariantId = pv.Id
					WHERE p.Status = 1
					GROUP BY p.Id, p.Name
					HAVING SUM(bi.BranchStock) <= :threshold
					ORDER BY SUM(bi.BranchStock) ASC
					""";
			return enma.createNativeQuery(sql).setParameter("limit", limit).setParameter("threshold", threshold)
					.getResultList();
		} finally {
			enma.close();
		}
	}

	@Override
	public List<ProductVariantDetailsModel> getVariantsByProductId(int productId) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			String jpql = """
						SELECT new ProductVariantDetailsModel(
					        pv.Id,
					        pv.product.Id,
					        pv.SKU,
					        pv.price,
					        pv.Status,
					        pv.CreatedAt,
					        pv.UpdatedAt,
					        vo.optionType.Id,
					        vo.optionType.Code,
					        vo.optionValue.Id,
					        vo.optionValue.Value
						)
						FROM ProductVariants pv
						JOIN pv.options vo
						WHERE pv.product.Id = :productId
					""";
			return enma.createQuery(jpql, ProductVariantDetailsModel.class).setParameter("productId", productId)
					.getResultList();
		} finally {
			enma.close();
		}

	}

	@Override
	public int countVariantsByProductId(int productId) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			String jpql = """
						SELECT count(pv)
						FROM ProductVariants pv
						WHERE pv.product.Id = :productId
					""";
			Long count = enma.createQuery(jpql, Long.class).setParameter("productId", productId).getSingleResult();
			return count.intValue();
		} finally {
			enma.close();
		}
	}

	@Override
	public void deleteAllByProductId(Integer productId) {
		EntityManager em = JPAConfigs.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // Lấy tất cả các biến thể thuộc productId
            TypedQuery<ProductVariants> query = em.createQuery(
                    "SELECT v FROM ProductVariants v WHERE v.product.id = :productId", ProductVariants.class);
            query.setParameter("productId", productId);
            List<ProductVariants> variants = query.getResultList();

            for (ProductVariants variant : variants) {
                // Xóa bản ghi VariantOptions liên quan trước để tránh lỗi ràng buộc khóa ngoại
                em.createQuery("DELETE FROM VariantOptions vo WHERE vo.variant.id = :variantId")
                  .setParameter("variantId", variant.getId())
                  .executeUpdate();

                // Xóa chính ProductVariants
                em.remove(em.contains(variant) ? variant : em.merge(variant));
            }
            tx.commit();
            System.out.println("✅ Đã xóa tất cả biến thể cho productId=" + productId);
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
	}

	@Override
	public ProductVariants findById(int variantId) {
		return this.findById(variantId);
	}
	
	@Override
	public List<ProductVariants> findAll() {
		return this.findAll();
	}
	
	@Override
	public void update(ProductVariants variant) {
		this.update(variant);
	}

}
