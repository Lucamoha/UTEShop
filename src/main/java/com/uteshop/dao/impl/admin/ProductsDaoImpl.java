package com.uteshop.dao.impl.admin;

import java.util.List;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.admin.IProductsDao;
import com.uteshop.entity.catalog.Products;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class ProductsDaoImpl implements IProductsDao {

	@Override
	public Products findBySlug(String slug) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			String jpql = "SELECT p FROM Products p WHERE p.Slug = :slug";
			return enma.createQuery(jpql, Products.class).setParameter("slug", slug).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<Products> getTopSellingProducts(int limit) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			String jpql = "SELECT ot.product.name, SUM(ot.quantity) AS totalSold FROM OrderItems ot GROUP BY ot.product.name ORDER BY totalSold DESC";
			return enma.createQuery(jpql, Products.class).setMaxResults(limit).getResultList();
		} finally {
			enma.close();
		}
	}

}
