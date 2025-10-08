package com.uteshop.services.Product;

import java.util.List;

import com.uteshop.entity.catalog.Products;

public interface IProductsService {
	List<Products> findAll();

	Products findById(int id);

	void insert(Products product);

	void update(Products products);

	void delete(int id);

	List<Products> findAll(boolean all, int firstResult, int maxResult, String searchKeyword);

	int count(String searchKeyword);

	Products findBySlug(String slug);

	List<Products> topLatestProducts();

	List<Products> findAll(int page, int pageSize);

	List<Products> findByCategoryId(int catId, int page, int pageSize);

	long countByCategoryId(int catId);

	List<Products> findByCategoryIds(List<Integer> categoryIds, int page, int pageSize);

	long countByCategoryIds(List<Integer> categoryIds);

}
