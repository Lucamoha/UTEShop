package com.uteshop.dao.impl;

import java.util.List;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.IProductsDao;
import com.uteshop.entities.Products;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class ProductsDaoImpl implements IProductsDao {

	@Override
	public List<Products> findAll() {
		EntityManager enma = JPAConfigs.getEntityManager();
		TypedQuery<Products> query = enma.createNamedQuery("Products.findAll", Products.class);
		return query.getResultList();
	}

}
