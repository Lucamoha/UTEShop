package com.uteshop.services.admin;

import com.uteshop.entity.catalog.ProductAttributeValues;

public interface IProductAttributeValuesService {
	void insert(ProductAttributeValues productAttributeValue);
	void deleteByProductId(Integer productId);
	void update(ProductAttributeValues productAttributeValue);
	ProductAttributeValues findByProductIdAndAttributeId(int productId, int attributeId);
}
