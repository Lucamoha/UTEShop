package com.uteshop.services.admin;

import java.util.List;

import com.uteshop.dto.admin.ProductVariantDetailsModel;
import com.uteshop.entity.catalog.ProductVariants;

public interface IProductsVariantsService {
	int count(String searchKeyword, String searchKeywordColumnName);
	long getLowStockCount(int threshold);
	List<Object[]> getLowStockProducts(int limit, int threshold);
	List<ProductVariantDetailsModel> getVariantsByProductId(int productId);
	int countVariantsByProductId(int productId);
	List<ProductVariants> findAll(boolean all, int firstResult, int maxResult, String searchKeyword, String searchKeywordColumnName);
	ProductVariants findById(int variantId);
	void insert(ProductVariants productVariant);
	void deleteAllByProductId(Integer productId);
	void delete(int id);
	void update(ProductVariants variant);
}
