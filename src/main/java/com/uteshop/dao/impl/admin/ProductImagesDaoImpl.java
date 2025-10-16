package com.uteshop.dao.impl.admin;

import java.util.List;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.admin.IProductImagesDao;
import com.uteshop.entity.catalog.ProductImages;

import jakarta.persistence.EntityManager;

public class ProductImagesDaoImpl extends AbstractDao<ProductImages> implements IProductImagesDao {

	public ProductImagesDaoImpl() {
		super(ProductImages.class);
	}

	@Override
	public List<ProductImages> getImageById(int id) {
		EntityManager enma = JPAConfigs.getEntityManager();
		String jpql = """
					SELECT pi
					FROM ProductImages pi
					WHERE pi.product.Id = :id
				""";
		return enma.createQuery(jpql, ProductImages.class).setParameter("id", id).getResultList();
	}
}
