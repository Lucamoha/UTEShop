package com.uteshop.services.impl.web;

import java.math.BigDecimal;
import java.util.List;

import com.uteshop.dao.impl.web.ProductsDaoImpl;
import com.uteshop.dto.web.FilterOptionsDto;
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

	@Override
	public List<Products> searchAndFilter(List<Integer> categoryIds, String keyword, 
	                                       List<Integer> colorIds, BigDecimal minPrice, 
	                                       BigDecimal maxPrice, String sortBy, 
	                                       int page, int pageSize) {
		return productsDao.searchAndFilter(categoryIds, keyword, colorIds, minPrice, maxPrice, sortBy, page, pageSize);
	}

	@Override
	public long countSearchAndFilter(List<Integer> categoryIds, String keyword, 
	                                  List<Integer> colorIds, BigDecimal minPrice, 
	                                  BigDecimal maxPrice) {
		return productsDao.countSearchAndFilter(categoryIds, keyword, colorIds, minPrice, maxPrice);
	}

	@Override
	public FilterOptionsDto getFilterOptions(List<Integer> categoryIds) {
		FilterOptionsDto filterOptions = new FilterOptionsDto();
		
		// Get price range only (no colors)
		BigDecimal minPrice = productsDao.getMinPriceByCategoryIds(categoryIds);
		BigDecimal maxPrice = productsDao.getMaxPriceByCategoryIds(categoryIds);
		filterOptions.setMinPrice(minPrice);
		filterOptions.setMaxPrice(maxPrice);
		
		return filterOptions;
	}

}
