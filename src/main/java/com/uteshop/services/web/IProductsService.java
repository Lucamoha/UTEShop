package com.uteshop.services.web;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.uteshop.dto.web.FilterOptionsDto;
import com.uteshop.entity.catalog.Products;

public interface IProductsService {
	List<Products> findAll();

	Products findById(int id);

    List<Products> getRelevantProducts(int productId);

	Products findBySlug(String slug);

	List<Products> topLatestProducts();

	List<Products> findAll(int page, int pageSize);

	List<Products> findByCategoryId(int catId, int page, int pageSize);

	long countByCategoryId(int catId);

	List<Products> findByCategoryIds(List<Integer> categoryIds, int page, int pageSize);

	long countByCategoryIds(List<Integer> categoryIds);
	
	// Search & Filter
	List<Products> searchAndFilter(List<Integer> categoryIds, String keyword, 
	                                List<Integer> colorIds, BigDecimal minPrice, 
	                                BigDecimal maxPrice, String sortBy, 
	                                Map<Integer, Object> attributeFilters,
	                                int page, int pageSize);
	
	long countSearchAndFilter(List<Integer> categoryIds, String keyword, 
	                           List<Integer> colorIds, BigDecimal minPrice, 
	                           BigDecimal maxPrice,
	                           Map<Integer, Object> attributeFilters);
	
	FilterOptionsDto getFilterOptions(List<Integer> categoryIds);

}
