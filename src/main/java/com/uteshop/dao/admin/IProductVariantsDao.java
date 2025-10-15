package com.uteshop.dao.admin;

import java.util.List;

import com.uteshop.dto.admin.ProductVariantDetailsModel;

public interface IProductVariantsDao {
	int countVariantsByProductId(int productId);
	long getLowStockCount(int threshold);
	List<Object[]> getLowStockProducts(int limit, int threshold);
	List<ProductVariantDetailsModel> getVariantsByProductId(int productId);
}
