package com.uteshop.services.admin;

import java.util.List;

import com.uteshop.entity.catalog.CategoryAttributes;
import com.uteshop.entity.catalog.CategoryAttributes.Id;

public interface ICategoryAttributeService {
	List<CategoryAttributes> findByCategoryId(int categoryId);
	CategoryAttributes findById(Id id);
	CategoryAttributes findByIdFetchColumns(Object id, List<String> fetchColumnsName);
	CategoryAttributes findByIdFetchColumns(Object id, int firstResult, int maxResult, List<String> fetchColumnsName);
	void insert(CategoryAttributes categoryAttribute);
	void update(CategoryAttributes categoryAttribute);
	CategoryAttributes findByCategoryIdAndAttributeId(int categoryId, int attributeId);
}
