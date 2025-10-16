package com.uteshop.services.impl.admin;

import java.util.List;

import com.uteshop.dao.impl.admin.ProductsDaoImpl;
import com.uteshop.entity.catalog.Products;
import com.uteshop.services.admin.IProductsService;

public class ProductsServiceImpl implements IProductsService {

	ProductsDaoImpl productsDao = new ProductsDaoImpl();
	@Override
	public List<Products> findAll() {	
		return productsDao.findAll();
	}
	@Override
	public Products findById(int id) {
		return productsDao.findById(id);
	}
	@Override
	public void insert(Products product) {
		productsDao.insert(product);
	}
	@Override
	public void update(Products products) {
		productsDao.update(products);
	}
	@Override
	public void delete(int id) {
		productsDao.delete(id);
	}

	@Override
	public int count(String searchKeyword, String searchKeywordColumnName) {
		return productsDao.count(searchKeyword, searchKeywordColumnName);
	}
	@Override
	public Products findByName(String name) {
		return productsDao.findByName(name);
	}
	@Override
	public List<Object[]> getTopSellingProducts(int limit) {
		return productsDao.getTopSellingProducts(limit);
	}
	@Override
	public List<Products> findAll(boolean all, int firstResult, int maxResult, String searchKeyword, String searchKeywordColumnName) {
		return productsDao.findAll(all, firstResult, maxResult, searchKeyword, searchKeywordColumnName);
	}
}
