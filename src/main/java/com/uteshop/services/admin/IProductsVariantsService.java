package com.uteshop.services.admin;

import java.util.List;

public interface IProductsVariantsService {
	long getLowStockCount(int threshold);
	List<Object[]> getLowStockProducts(int limit, int threshold);
}
