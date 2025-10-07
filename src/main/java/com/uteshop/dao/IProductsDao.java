package com.uteshop.dao;

import com.uteshop.entity.catalog.Products;

import java.util.List;

public interface IProductsDao {
    List<Products> topLatestProducts (); // Sản phẩm mới
    List<Products> findAll(int page, int pageSize); // Phân trang
    Products findBySlug(String slug);
}
