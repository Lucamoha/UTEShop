package com.uteshop.services.impl.admin;

import java.util.List;

import com.uteshop.dao.admin.ICategoryAttributeDao;
import com.uteshop.dao.impl.admin.CategoryAttributesDaoImpl;
import com.uteshop.entity.catalog.CategoryAttributes;
import com.uteshop.entity.catalog.CategoryAttributes.Id;
import com.uteshop.services.admin.ICategoryAttributeService;

public class CategoryAttributeServiceImpl implements ICategoryAttributeService{

	ICategoryAttributeDao categoryAttributeDao = new CategoryAttributesDaoImpl();

	@Override
	public List<CategoryAttributes> findByCategoryId(int categoryId) {
		return categoryAttributeDao.findByCategoryId(categoryId);
	}

	@Override
	public CategoryAttributes findById(Id id) {
		return categoryAttributeDao.findById(id);
	}

	@Override
	public CategoryAttributes findByIdFetchColumns(Object id, int firstResult, int maxResult, List<String> fetchColumnsName){
		return categoryAttributeDao.findByIdFetchColumns(id, firstResult, maxResult, fetchColumnsName);
	}

	@Override
	public void insert(CategoryAttributes categoryAttribute) {
		categoryAttributeDao.insert(categoryAttribute);
	}

	@Override
	public void update(CategoryAttributes categoryAttribute) {
		categoryAttributeDao.update(categoryAttribute);
	}

	@Override
	public CategoryAttributes findByCategoryIdAndAttributeId(int categoryId, int attributeId) {
		return categoryAttributeDao.findByCategoryIdAndAttributeId(categoryId, attributeId);
	}

	@Override
	public CategoryAttributes findByIdFetchColumns(Object id, List<String> fetchColumnsName) {
		return categoryAttributeDao.findByIdFetchColumns(id, fetchColumnsName);
	}

	@Override
	public boolean existsById(Id id) {
		return categoryAttributeDao.existsById(id);
	}
}
