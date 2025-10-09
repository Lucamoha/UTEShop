package com.uteshop.services.impl.admin;

import com.uteshop.dao.admin.IProductVariantsDao;
import com.uteshop.dao.impl.admin.ProductVariantsDaoImpl;
import com.uteshop.services.admin.IProductsVariantsService;

public class ProductVariantsServiceImpl implements IProductsVariantsService {

	IProductVariantsDao productVariantsDao = new ProductVariantsDaoImpl();

	@Override
	public long getLowStockCount(int threshold) {
		return productVariantsDao.getLowStockCount(threshold);
	}

}
