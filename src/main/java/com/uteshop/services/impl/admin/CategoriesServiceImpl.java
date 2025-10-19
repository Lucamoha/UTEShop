package com.uteshop.services.impl.admin;

import java.util.List;

import com.uteshop.dao.admin.ICategoriesDao;
import com.uteshop.dao.impl.admin.CategoriesDaoImpl;
import com.uteshop.entity.catalog.Categories;
import com.uteshop.services.admin.ICategoriesService;

public class CategoriesServiceImpl implements ICategoriesService {

	CategoriesDaoImpl categoriesDaoImpl = new CategoriesDaoImpl();
	ICategoriesDao categoriesDao = new CategoriesDaoImpl();

    @Override
    public Categories findBySlug(String slug) {
        return categoriesDaoImpl.findBySlug(slug);
    }

    @Override
    public Categories findById(int id) {
        return categoriesDaoImpl.findById(id);
    }

	@Override
	public int count(String searchKeyword, String searchKeywordColumnName) {
		return categoriesDaoImpl.count(searchKeyword, searchKeywordColumnName);
	}

	@Override
	public void insert(Categories category) {
		categoriesDaoImpl.insert(category);
	}

	@Override
	public void update(Categories category) {
		categoriesDaoImpl.update(category);
	}

	@Override
	public void delete(int id) {
		categoriesDaoImpl.delete(id);
	}

	@Override
	public List<Categories> findAll() {
		return categoriesDaoImpl.findAll();
	}

	@Override
	public List<Categories> findAllFetchParent(boolean all, int firstResult, int maxResult, String searchKeyword,
			String searchKeywordColumnName, String fetchColumnName) {
		return categoriesDaoImpl.findAllFetchParent(all, firstResult, maxResult, searchKeyword, searchKeywordColumnName, fetchColumnName);
	}

	@Override
	public List<Categories> findAllFetchColumns(boolean all, int firstResult, int maxResult, String searchKeyword,
			String searchKeywordColumnName, List<String> fetchColumnsName) {
		return categoriesDaoImpl.findAllFetchColumns(all, firstResult, maxResult, searchKeyword, searchKeywordColumnName, fetchColumnsName);
	}

	@Override
	public Categories findByIdFetchColumns(Object id, List<String> columns) {
		return categoriesDao.findByIdFetchColumns(id, columns);
	}

	@Override
	public Categories findByIdFetchColumns(Object id, int firstResult, int maxResult, List<String> fetchColumnsName) {
		return categoriesDao.findByIdFetchColumns(id, firstResult, maxResult, fetchColumnsName);
	}
}
