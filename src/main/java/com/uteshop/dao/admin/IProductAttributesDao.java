package com.uteshop.dao.admin;

import java.util.List;

import com.uteshop.dto.admin.ProductAttributeDisplayModel;

public interface IProductAttributesDao {
	List<ProductAttributeDisplayModel> getAttributesByProductId(int productId);
}
