package com.uteshop.services.impl.Product;

import java.util.List;

import com.uteshop.dao.IProductsDao;
import com.uteshop.dao.impl.Product.ProductsDaoImpl;
import com.uteshop.entity.catalog.Products;
import com.uteshop.services.Product.IProductsService;

public class ProductsServiceImpl implements IProductsService {

	ProductsDaoImpl productsDao = new ProductsDaoImpl();
	@Override
	public List<Products> findAll() {	
		return productsDao.findAll();
	}
	@Override
	public Products findById(int id) {
		return productsDao.findById(id);
	}

    @Override
    public List<Products> topLatestProducts() {
        return productsDao.topLatestProducts();
    }

    @Override
    public List<Products> findAll(int page, int pageSize) {
        return productsDao.findAll(page, pageSize);
    }

}
