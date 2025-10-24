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
		//productAttributeValuesDaoImpl.in(productAttributeValue);
	}
	@Override
	public void deleteByProductId(Integer productId) {
		productAttributeValuesDaoImpl.deleteByProductId(productId);
	}
	@Override
	public void update(ProductAttributeValues productAttributeValue) {
		productAttributeValuesDaoImpl.update(productAttributeValue);
	}
	@Override
	public ProductAttributeValues findByProductIdAndAttributeId(int productId, int attributeId) {
		 return productAttributeValuesDaoImpl.findByProductIdAndAttributeId(productId, attributeId);
	}

}
