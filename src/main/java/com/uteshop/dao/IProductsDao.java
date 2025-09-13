package com.uteshop.dao;

import java.util.List;

import com.uteshop.entities.Products;

public interface IProductsDao {
	List<Products> findAll();
}
