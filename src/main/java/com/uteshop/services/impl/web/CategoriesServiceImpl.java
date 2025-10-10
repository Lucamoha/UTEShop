package com.uteshop.services.impl.web;

import java.util.List;

import com.uteshop.dao.impl.web.CategoriesDaoImpl;
import com.uteshop.entity.catalog.Categories;
import com.uteshop.services.web.ICategoriesService;

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

	@Override
	public List<Categories> findAll(boolean all, int firstResult, int maxResult, String searchKeyword) {
		return categoriesDao.findAll(all, firstResult, maxResult, searchKeyword);
	}

	@Override
	public int count(String searchKeyword) {
		return categoriesDao.count(searchKeyword);
	}

	@Override
	public void insert(Categories category) {
		categoriesDao.insert(category);
	}

	@Override
	public void update(Categories category) {
		categoriesDao.update(category);
	}

	@Override
	public void delete(int id) {
		categoriesDao.delete(id);
	}
}
