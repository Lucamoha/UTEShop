package com.uteshop.services.Product;

import java.util.List;

import com.uteshop.entity.catalog.Products;

public interface IProductsService {
	List<Products> findAll();
	Products findById(int id);
    List<Products> top10LatestProducts();
    List<Products> findAll(int page, int pageSize);
}
