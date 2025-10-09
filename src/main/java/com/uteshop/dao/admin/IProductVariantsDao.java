package com.uteshop.dao.admin;

import java.util.List;

public interface IProductVariantsDao {
	long getLowStockCount(int threshold);
	List<Object[]> getLowStockProducts(int limit, int threshold);
}
