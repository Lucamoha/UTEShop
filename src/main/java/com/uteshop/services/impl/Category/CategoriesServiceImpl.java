package com.uteshop.services.impl.Category;

import java.util.List;

import com.uteshop.dao.impl.Category.CategoriesDaoImpl;
import com.uteshop.entities.Categories;
import com.uteshop.services.Category.ICategoriesService;

public class CategoriesServiceImpl implements ICategoriesService {

	CategoriesDaoImpl categoriesDao = new CategoriesDaoImpl();
	@Override
	public List<Categories> findAll() {
		return categoriesDao.findAll();
	}

}
