package com.uteshop.services.impl.Product;

import java.util.List;

import com.uteshop.dao.impl.Product.ProductsDaoImpl;
import com.uteshop.entity.catalog.Products;
import com.uteshop.services.Product.IProductsService;

public class ProductsServiceImpl implements IProductsService {

	ProductsDaoImpl productsDaoImpl = new ProductsDaoImpl();
	@Override
	public List<Products> findAll() {	
		return productsDaoImpl.findAll();
	}
	@Override
	public Products findById(int id) {
		return productsDaoImpl.findById(id);
	}
}
