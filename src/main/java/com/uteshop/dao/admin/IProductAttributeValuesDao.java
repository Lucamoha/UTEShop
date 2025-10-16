package com.uteshop.dao.admin;

import com.uteshop.entity.catalog.ProductAttributeValues;

public interface IProductAttributeValuesDao {

	void insert(ProductAttributeValues pav);
	void deleteByProductId(Integer productId);
	void update(ProductAttributeValues productAttributeValue);
	ProductAttributeValues findByProductIdAndAttributeId(int productId, int attributeId);
}
