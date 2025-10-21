package com.uteshop.dao.impl.admin;

import java.util.List;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.admin.IProductAttributeValuesDao;
import com.uteshop.entity.catalog.ProductAttributeValues;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

public class ProductAttributeValuesDaoImpl extends AbstractDao<ProductAttributeValues> implements IProductAttributeValuesDao {

	public ProductAttributeValuesDaoImpl() {
		super(ProductAttributeValues.class);
	}

	@Override
	public void insert(ProductAttributeValues pav) {
		EntityManager em = JPAConfigs.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // ⚡ phải gắn ID composite thủ công, nếu không JPA sẽ bỏ qua
            if (pav.getId() == null) {
                ProductAttributeValues.Id id = new ProductAttributeValues.Id();
                id.setProductId(pav.getProduct().getId());
                id.setAttributeId(pav.getAttribute().getId());
                pav.setId(id);
            }

            // Đảm bảo entity con đã attach
            pav.setProduct(em.merge(pav.getProduct()));
            pav.setAttribute(em.merge(pav.getAttribute()));

            em.persist(pav);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (tx.isActive()) tx.rollback();
        } finally {
            em.close();
        }
	}

	@Override
	public void deleteByProductId(Integer productId) {
		EntityManager em = JPAConfigs.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createQuery("DELETE FROM ProductAttributeValues pav WHERE pav.product.id = :productId")
              .setParameter("productId", productId)
              .executeUpdate();
            tx.commit();
            System.out.println("Đã xóa tất cả thuộc tính kỹ thuật cho productId=" + productId);
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
	}
	
	@Override
	public void update(ProductAttributeValues productAttributeValue) {
		super.update(productAttributeValue);
	}

	@Override
	public ProductAttributeValues findByProductIdAndAttributeId(int productId, int attributeId) {
		EntityManager em = JPAConfigs.getEntityManager();
		try {
            String jpql = "SELECT pav FROM ProductAttributeValues pav "
                       + "WHERE pav.product.id = :productId AND pav.attribute.id = :attributeId";

            TypedQuery<ProductAttributeValues> query = em.createQuery(jpql, ProductAttributeValues.class);
            query.setParameter("productId", productId);
            query.setParameter("attributeId", attributeId);

            List<ProductAttributeValues> result = query.getResultList();
            if (!result.isEmpty()) {
                return result.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}
}
