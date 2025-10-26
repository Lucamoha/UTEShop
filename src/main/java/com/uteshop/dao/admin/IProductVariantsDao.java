package com.uteshop.dao.admin;

import java.util.List;

import com.uteshop.dto.admin.ProductVariantDetailsModel;
import com.uteshop.entity.catalog.ProductVariants;

public interface IProductVariantsDao {
	int countVariantsByProductId(int productId);
	List<Object[]> getLowStockProducts(int threshold, int branchId);
	List<ProductVariantDetailsModel> getVariantsByProductId(int productId);
	List<ProductVariantDetailsModel> getVariantDetailsById(int variantId);
	void deleteAllByProductId(Integer productId);
	ProductVariants findById(int variantId);
	ProductVariants findBySKU(String sku);
	List<ProductVariants> findAll();
	void update(ProductVariants variant);
}
