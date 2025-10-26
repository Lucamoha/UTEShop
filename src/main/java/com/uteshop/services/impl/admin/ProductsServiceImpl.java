package com.uteshop.services.impl.admin;

import java.util.List;

import com.uteshop.dao.impl.admin.ProductsDaoImpl;
import com.uteshop.entity.catalog.Attributes;
import com.uteshop.entity.catalog.Products;
import com.uteshop.services.admin.IProductsService;

public class ProductsServiceImpl implements IProductsService {

	ProductsDaoImpl productsDaoImpl = new ProductsDaoImpl();
	ProductsDaoImpl productsDao = new ProductsDaoImpl();
	@Override
	public List<Products> findAll() {	
		return productsDaoImpl.findAll();
	}
	@Override
	public Products findById(int id) {
		return productsDaoImpl.findById(id);
	}
	@Override
	public void insert(Products product) {
		productsDaoImpl.insert(product);
	}
	@Override
	public void update(Products products) {
		productsDaoImpl.update(products);
	}
	@Override
	public void delete(int id) {
		productsDaoImpl.delete(id);
	}

	@Override
	public int count(String searchKeyword, String searchKeywordColumnName) {
		return productsDaoImpl.count(searchKeyword, searchKeywordColumnName);
	}
	@Override
	public Products findByName(String name) {
		return productsDaoImpl.findByName(name);
	}
	@Override
	public List<Object[]> getTopSellingProducts(int limit, int branchId) {
		return productsDaoImpl.getTopSellingProducts(limit, branchId);
	}
	@Override
	public List<Products> findAll(boolean all, int firstResult, int maxResult, String searchKeyword, String searchKeywordColumnName) {
		return productsDaoImpl.findAll(all, firstResult, maxResult, searchKeyword, searchKeywordColumnName);
	}
	@Override
	public List<Attributes> findAttributesByCategoryId(Integer categoryId) {
		return productsDaoImpl.findAttributesByCategoryId(categoryId);
	}
	@Override
	public Products findByIdFetchColumns(Object id, List<String> fetchColumnsName) {
		return productsDaoImpl.findByIdFetchColumns(id, fetchColumnsName);
	}
}
