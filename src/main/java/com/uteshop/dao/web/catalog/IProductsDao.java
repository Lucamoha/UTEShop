package com.uteshop.dao.web.catalog;

import com.uteshop.entity.catalog.Products;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IProductsDao {
	List<Products> topLatestProducts(); // Sản phẩm mới

	List<Products> findAll(int page, int pageSize); // Phân trang

	Products findBySlug(String slug);

    List<Products> getRelativeProducts(int productId);

    List<Products> findByCategoryId(int catId, int page, int pageSize);

	long countByCategoryId(int catId);

	List<Products> findByCategoryIds(List<Integer> catIds, int page, int pageSize);

	long countByCategoryIds(List<Integer> catIds);
	
	// Search & Filter methods
	List<Products> searchAndFilter(List<Integer> categoryIds, String keyword, 
	                                List<Integer> colorIds, BigDecimal minPrice, 
	                                BigDecimal maxPrice, String sortBy, 
	                                Map<Integer, Object> attributeFilters,
	                                int page, int pageSize);
	
	long countSearchAndFilter(List<Integer> categoryIds, String keyword, 
	                           List<Integer> colorIds, BigDecimal minPrice, 
	                           BigDecimal maxPrice,
	                           Map<Integer, Object> attributeFilters);
	
	BigDecimal getMinPriceByCategoryIds(List<Integer> categoryIds);
	
	BigDecimal getMaxPriceByCategoryIds(List<Integer> categoryIds);
}
