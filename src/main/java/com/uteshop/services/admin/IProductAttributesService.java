package com.uteshop.services.admin;

import java.util.List;

import com.uteshop.dto.admin.ProductAttributeDisplayModel;

public interface IProductAttributesService {
	List<ProductAttributeDisplayModel> getAttributesByProductId(int productId);
}
