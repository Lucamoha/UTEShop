package com.uteshop.dao.impl.admin;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.admin.IProductAttributeValuesDao;
import com.uteshop.entity.catalog.ProductAttributeValues;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

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
}
