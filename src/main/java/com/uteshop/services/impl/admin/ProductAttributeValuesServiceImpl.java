package com.uteshop.services.impl.admin;

import com.uteshop.dao.admin.IProductAttributeValuesDao;
import com.uteshop.dao.impl.admin.ProductAttributeValuesDaoImpl;
import com.uteshop.entity.catalog.ProductAttributeValues;
import com.uteshop.services.admin.IProductAttributeValuesService;

public class ProductAttributeValuesServiceImpl implements IProductAttributeValuesService {

	IProductAttributeValuesDao productAttributeValuesDaoImpl = new ProductAttributeValuesDaoImpl();
	@Override
	public void insert(ProductAttributeValues productAttributeValue) {
		productAttributeValuesDaoImpl.insert(productAttributeValue);
	}

}
