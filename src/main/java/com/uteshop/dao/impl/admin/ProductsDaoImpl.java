package com.uteshop.dao.impl.admin;

import java.util.List;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.admin.IProductsDao;
import com.uteshop.entity.catalog.Attributes;
import com.uteshop.entity.catalog.Products;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class ProductsDaoImpl extends AbstractDao<Products> implements IProductsDao {
	public ProductsDaoImpl() {
		super(Products.class);
	}

	@Override
	public List<Object[]> getTopSellingProducts(int limit) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			String jpql = """
						SELECT ot.product.Name, SUM(ot.Quantity) AS totalSold
						FROM OrderItems ot
						GROUP BY ot.product.Name
						ORDER BY totalSold DESC
					""";
			return enma.createQuery(jpql, Object[].class).setMaxResults(limit).getResultList();
		} finally {
			enma.close();
		}
	}

	@Override
	public Products findByName(String name) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			String jpql = "SELECT p FROM Products p WHERE p.Name = :name";
			return enma.createQuery(jpql, Products.class).setParameter("name", name).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<Attributes> findAttributesByCategoryId(Integer categoryId) {
		EntityManager em = JPAConfigs.getEntityManager();
	    try {
	        return em.createQuery("""
	            SELECT DISTINCT a
	            FROM CategoryAttributes ca
	            JOIN ca.attribute a
	            WHERE ca.category.Id = :categoryId
	        """, Attributes.class)
	        .setParameter("categoryId", categoryId)
	        .getResultList();
	    } finally {
	        em.close();
	    }
	}
}
