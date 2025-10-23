package com.uteshop.dao.admin;

import java.util.List;

import com.uteshop.dto.admin.ProductVariantDetailsModel;
import com.uteshop.entity.catalog.ProductVariants;

public interface IProductVariantsDao {
	int countVariantsByProductId(int productId);
	long getLowStockCount(int threshold);
	List<Object[]> getLowStockProducts(int limit, int threshold);
	List<ProductVariantDetailsModel> getVariantsByProductId(int productId);
	void deleteAllByProductId(Integer productId);
	ProductVariants findById(int variantId);
	ProductVariants findBySKU(String sku);
	List<ProductVariants> findAll();
	void update(ProductVariants variant);
}
