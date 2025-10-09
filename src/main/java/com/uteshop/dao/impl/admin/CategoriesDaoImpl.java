package com.uteshop.dao.impl.admin;

import java.util.List;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.admin.ICategoriesDao;
import com.uteshop.entity.catalog.Categories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class CategoriesDaoImpl extends AbstractDao<Categories> implements ICategoriesDao{

	public CategoriesDaoImpl() {
		super(Categories.class);
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
