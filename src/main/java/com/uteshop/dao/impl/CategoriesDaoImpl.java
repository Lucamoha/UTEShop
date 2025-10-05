package com.uteshop.dao.impl;

import java.util.List;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.ICategoriesDao;
import com.uteshop.entity.catalog.Categories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class CategoriesDaoImpl implements ICategoriesDao {
	@Override
	public List<Categories> findAll() {
		EntityManager enma = JPAConfigs.getEntityManager();
		TypedQuery<Categories> query = enma.createNamedQuery("Categories.findAll", Categories.class);
		return query.getResultList();
	}

	@Override
	public List<Categories> findParents() {
		EntityManager enma = JPAConfigs.getEntityManager();
		TypedQuery<Categories> query = enma.createNamedQuery("Categories.findParents", Categories.class);
		return query.getResultList();
	}

	@Override
	public List<Categories> findChildren(int parentId) {
		EntityManager enma = JPAConfigs.getEntityManager();
		TypedQuery<Categories> query = enma.createNamedQuery("Categories.findChildren", Categories.class);
		query.setParameter("parentId", parentId);
		return query.getResultList();
	}

	@Override
	public Categories findBySlug(String slug) {
		EntityManager enma = JPAConfigs.getEntityManager();
		TypedQuery<Categories> query = enma.createNamedQuery("Categories.findBySlug", Categories.class);
		query.setParameter("slug", slug);
		List<Categories> result = query.getResultList();
		return result.isEmpty() ? null : result.get(0);
	}

	@Override
	public Categories findById(int id) {
		EntityManager enma = JPAConfigs.getEntityManager();
		return enma.find(Categories.class, id);
	}

}
