package com.uteshop.services.impl;

import java.util.List;

import com.uteshop.dao.IProductsDao;
import com.uteshop.dao.impl.ProductsDaoImpl;
import com.uteshop.entities.Products;
import com.uteshop.services.IProductsService;

public class ProductsServiceImpl implements IProductsService {

	IProductsDao productsDao = new ProductsDaoImpl();
	@Override
	public List<Products> findAll() {	
		return productsDao.findAll();
	}
}
