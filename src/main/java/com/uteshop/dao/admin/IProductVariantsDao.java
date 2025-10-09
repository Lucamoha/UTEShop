package com.uteshop.dao.admin;

import java.util.List;

public interface IProductVariantsDao {
	long getLowStockCount(int threshold);
	
}
