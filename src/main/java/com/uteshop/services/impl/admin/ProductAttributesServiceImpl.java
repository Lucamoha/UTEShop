package com.uteshop.services.impl.admin;

import java.util.List;

import com.uteshop.dao.admin.IProductAttributesDao;
import com.uteshop.dao.impl.admin.ProductAttributesDaoImpl;
import com.uteshop.dto.admin.ProductAttributeDisplayModel;
import com.uteshop.services.admin.IProductAttributesService;

public class ProductAttributesServiceImpl implements IProductAttributesService {

	IProductAttributesDao productAttributesDao = new ProductAttributesDaoImpl();
	@Override
	public List<ProductAttributeDisplayModel> getAttributesByProductId(int productId) {
		return productAttributesDao.getAttributesByProductId(productId);
	}
}
