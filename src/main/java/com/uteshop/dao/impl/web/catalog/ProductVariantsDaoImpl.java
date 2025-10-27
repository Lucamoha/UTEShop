package com.uteshop.dao.impl.web.catalog;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.web.catalog.IProductVariantsDao;
import com.uteshop.entity.catalog.ProductVariants;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ProductVariantsDaoImpl extends AbstractDao<ProductVariants> implements IProductVariantsDao {

    public ProductVariantsDaoImpl() {
        super(ProductVariants.class);
    }

    @Override
    public ProductVariants findByProductAndOptionValues(Integer productId, List<Integer> optionValueIds) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            // Tìm variant có đủ tất cả option values được chọn
            String jpql = """
                    SELECT pv
                    FROM ProductVariants pv
                    LEFT JOIN FETCH pv.options vo
                    WHERE pv.product.Id = :productId
                      AND pv.Id IN (
                          SELECT vo2.variant.Id
                          FROM VariantOptions vo2
                          WHERE vo2.optionValue.Id IN :optionValueIds
                          GROUP BY vo2.variant.Id
                          HAVING COUNT(DISTINCT vo2.optionValue.Id) = :optionCount
                      )
                    """;

            System.out.println("JPQL Query: " + jpql);

            TypedQuery<ProductVariants> query = em.createQuery(jpql, ProductVariants.class);
            query.setParameter("productId", productId);
            query.setParameter("optionValueIds", optionValueIds);
            query.setParameter("optionCount", (long) optionValueIds.size());

            ProductVariants result = query.getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<ProductVariants> findByProductId(Integer productId) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            String jpql = """
                    SELECT DISTINCT pv
                    FROM ProductVariants pv
                    LEFT JOIN FETCH pv.options
                    WHERE pv.product.Id = :productId
                    """;

            TypedQuery<ProductVariants> query = em.createQuery(jpql, ProductVariants.class);
            query.setParameter("productId", productId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public ProductVariants findById(Integer id) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            String jpql = """
                    SELECT pv
                    FROM ProductVariants pv
                    LEFT JOIN FETCH pv.options vo
                    LEFT JOIN FETCH vo.optionValue
                    WHERE pv.Id = :id
                    """;

            TypedQuery<ProductVariants> query = em.createQuery(jpql, ProductVariants.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
