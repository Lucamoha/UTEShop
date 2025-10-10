package com.uteshop.services.impl.web;

import java.util.List;

import com.uteshop.dao.impl.web.ProductsDaoImpl;
import com.uteshop.entity.catalog.Products;
import com.uteshop.services.web.IProductsService;

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
	public void insert(Products product) {
		productsDao.insert(product);
	}

	@Override
	public void update(Products products) {
		productsDao.update(products);
	}

	@Override
	public void delete(int id) {
		productsDao.delete(id);
	}

	@Override
	public List<Products> findAll(boolean all, int firstResult, int maxResult, String searchKeyword) {
		return productsDao.findAll(all, firstResult, maxResult, searchKeyword);
	}

	@Override
	public List<Products> topLatestProducts() {
		return productsDao.topLatestProducts();
	}

	@Override
	public List<Products> findAll(int page, int pageSize) {
		return productsDao.findAll(page, pageSize);
	}

    @Override
    public List<Products> getRelevantProducts(int productId) {
        return productsDao.getRelativeProducts(productId);
    }

    @Override
	public int count(String searchKeyword) {
		return productsDao.count(searchKeyword);
	}

	@Override
	public Products findBySlug(String slug) {
		return productsDao.findBySlug(slug);
	}

	@Override
	public List<Products> findByCategoryId(int catId, int page, int pageSize) {
		return productsDao.findByCategoryId(catId, page, pageSize);
	}

	@Override
	public long countByCategoryId(int catId) {
		return productsDao.countByCategoryId(catId);
	}

	@Override
	public List<Products> findByCategoryIds(List<Integer> categoryIds, int page, int pageSize) {
		return productsDao.findByCategoryIds(categoryIds, page, pageSize);
	}

	@Override
	public long countByCategoryIds(List<Integer> categoryIds) {
		return productsDao.countByCategoryIds(categoryIds);
	}

}
