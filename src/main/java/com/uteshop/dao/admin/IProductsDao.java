package com.uteshop.dao.admin;

import java.util.List;

import com.uteshop.entity.catalog.Attributes;
import com.uteshop.entity.catalog.Products;

public interface IProductsDao {
	Products findByName(String name);
	List<Object[]> getTopSellingProducts(int limit);
	List<Attributes> findAttributesByCategoryId(Integer categoryId);
}
