package com.uteshop.services;

import java.util.List;

import com.uteshop.entities.Products;

public interface IProductsService {
	List<Products> findAll();
}
