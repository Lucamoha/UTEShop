package com.uteshop.services.impl.admin;

import java.util.List;

import com.uteshop.dao.impl.admin.ProductVariantsDaoImpl;
import com.uteshop.dto.admin.ProductVariantDetailsModel;
import com.uteshop.entity.catalog.ProductVariants;
import com.uteshop.services.admin.IProductsVariantsService;

public class ProductVariantsServiceImpl implements IProductsVariantsService {

	ProductVariantsDaoImpl productVariantsDao = new ProductVariantsDaoImpl();

	@Override
	public long getLowStockCount(int threshold) {
		return productVariantsDao.getLowStockCount(threshold);
	}

	@Override
	public List<Object[]> getLowStockProducts(int limit, int threshold) {
		return productVariantsDao.getLowStockProducts(limit, threshold);
	}

	@Override
	public List<ProductVariantDetailsModel> getVariantsByProductId(int productId) {
		return productVariantsDao.getVariantsByProductId(productId);
	}

	@Override
	public int countVariantsByProductId(int productId) {
		return productVariantsDao.countVariantsByProductId(productId);
	}

	@Override
	public List<ProductVariants> findAll(boolean all, int firstResult, int maxResult, String searchKeyword,
			String searchKeywordColumnName) {
		return productVariantsDao.findAll(all, firstResult, maxResult, searchKeyword, searchKeywordColumnName);
	}

	@Override
	public int count(String searchKeyword, String searchKeywordColumnName) {
		return productVariantsDao.count(searchKeyword, searchKeywordColumnName);
	}

	@Override
	public void insert(ProductVariants productVariant) {
		productVariantsDao.insert(productVariant);
	}

	@Override
	public void deleteAllByProductId(Integer productId) {
		productVariantsDao.deleteAllByProductId(productId);
	}

	@Override
	public ProductVariants findById(int variantId) {
		return productVariantsDao.findById(variantId);
	}

	@Override
	public void update(ProductVariants variant) {
		productVariantsDao.update(variant);
	}
}
