package com.uteshop.services.impl.web.catalog;

import java.util.List;

import com.uteshop.dao.impl.web.catalog.CategoriesDaoImpl;
import com.uteshop.entity.catalog.Categories;
import com.uteshop.services.web.catalog.ICategoriesService;

public class CategoriesServiceImpl implements ICategoriesService {

	CategoriesDaoImpl categoriesDao = new CategoriesDaoImpl();
	@Override
	public List<Categories> findAll() {
		return categoriesDao.findAll();
	}

    @Override
    public List<Categories> findParents() {
        return categoriesDao.findParents();
    }

    @Override
    public List<Categories> findChildren(int parentId) {
        return categoriesDao.findChildren(parentId);
    }

    @Override
    public Categories findBySlug(String slug) {
        return categoriesDao.findBySlug(slug);
    }

    @Override
    public Categories findById(int id) {
        return categoriesDao.findById(id);
    }
}
