package com.uteshop.services.admin;

import java.util.List;

import com.uteshop.entity.catalog.Products;

public interface IProductsService {
	List<Products> findAll();
	Products findById(int id);
	void insert(Products product);
	void update(Products products);
	void delete(int id);
	List<Products> findAll(boolean all, int firstResult, int maxResult, String searchKeyword, String searchKeywordColumnName);
	int count(String searchKeyword, String searchKeywordColumnName);
	Products findBySlug(String slug);
    List<Object[]> getTopSellingProducts(int limit);
}
