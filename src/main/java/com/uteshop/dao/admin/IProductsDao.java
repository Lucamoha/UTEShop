package com.uteshop.dao.admin;

import java.util.List;

import com.uteshop.entity.catalog.Products;

public interface IProductsDao {
	Products findBySlug(String slug);
	List<Products> getTopSellingProducts(int limit);
}
