package com.uteshop.dao;

import com.uteshop.entity.catalog.Products;

import java.util.List;

public interface IProductsDao {
	List<Products> topLatestProducts(); // Sản phẩm mới

	List<Products> findAll(int page, int pageSize); // Phân trang

	Products findBySlug(String slug);

	List<Products> findByCategoryId(int catId, int page, int pageSize);

	long countByCategoryId(int catId);

	List<Products> findByCategoryIds(List<Integer> catIds, int page, int pageSize);

	long countByCategoryIds(List<Integer> catIds);

}
