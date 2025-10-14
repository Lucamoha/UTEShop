package com.uteshop.services.impl.admin;

import java.util.List;

import com.uteshop.dao.impl.admin.CategoriesDaoImpl;
import com.uteshop.entity.catalog.Categories;
import com.uteshop.services.admin.ICategoriesService;

public class CategoriesServiceImpl implements ICategoriesService {

	CategoriesDaoImpl categoriesDao = new CategoriesDaoImpl();

    @Override
    public Categories findBySlug(String slug) {
        return categoriesDao.findBySlug(slug);
    }

    @Override
    public Categories findById(int id) {
        return categoriesDao.findById(id);
    }

	@Override
	public List<Categories> findAll(boolean all, int firstResult, int maxResult, String searchKeyword, String searchKeywordColumnName) {
		return categoriesDao.findAll(all, firstResult, maxResult, searchKeyword, searchKeywordColumnName);
	}

	@Override
	public int count(String searchKeyword, String searchKeywordColumnName) {
		return categoriesDao.count(searchKeyword, searchKeywordColumnName);
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

	@Override
	public List<Categories> findAll() {
		return categoriesDao.findAll();
	}
}
