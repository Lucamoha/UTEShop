package com.uteshop.services.Product;

import java.util.List;

import com.uteshop.entities.Products;

public interface IProductsService {
	List<Products> findAll();
	Products findById(int id);
	void insert(Products product);
	void update(Products products);
	void delete(int id);
	List<Products> findAll(boolean all, int firstResult, int maxResult);
}
