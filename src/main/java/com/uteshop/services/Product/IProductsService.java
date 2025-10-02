package com.uteshop.services.Product;

import java.util.List;

import com.uteshop.entities.Products;

public interface IProductsService {
	List<Products> findAll();
	Products findById(int id);
}
