package com.uteshop.services.impl.web;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.uteshop.dao.impl.web.AttributesDaoImpl;
import com.uteshop.dao.impl.web.ProductsDaoImpl;
import com.uteshop.dao.web.IAttributesDao;
import com.uteshop.dto.web.AttributeFilterDto;
import com.uteshop.dto.web.FilterOptionsDto;
import com.uteshop.entity.catalog.Attributes;
import com.uteshop.entity.catalog.Products;
import com.uteshop.services.web.IProductsService;

public class ProductsServiceImpl implements IProductsService {

	ProductsDaoImpl productsDao = new ProductsDaoImpl();
	IAttributesDao attributesDao = new AttributesDaoImpl();

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

    @Override
    public List<Products> getRelevantProducts(int productId) {
        return productsDao.getRelativeProducts(productId);
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
	                                       Map<Integer, Object> attributeFilters,
	                                       int page, int pageSize) {
		return productsDao.searchAndFilter(categoryIds, keyword, colorIds, minPrice, maxPrice, sortBy, attributeFilters, page, pageSize);
	}

	@Override
	public long countSearchAndFilter(List<Integer> categoryIds, String keyword, 
	                                  List<Integer> colorIds, BigDecimal minPrice, 
	                                  BigDecimal maxPrice,
	                                  Map<Integer, Object> attributeFilters) {
		return productsDao.countSearchAndFilter(categoryIds, keyword, colorIds, minPrice, maxPrice, attributeFilters);
	}

	@Override
	public FilterOptionsDto getFilterOptions(List<Integer> categoryIds) {
		FilterOptionsDto filterOptions = new FilterOptionsDto();
		
		// Get price range
		BigDecimal minPrice = productsDao.getMinPriceByCategoryIds(categoryIds);
		BigDecimal maxPrice = productsDao.getMaxPriceByCategoryIds(categoryIds);
		filterOptions.setMinPrice(minPrice);
		filterOptions.setMaxPrice(maxPrice);
		
		// Get attributes for filtering
		List<Attributes> attributes = attributesDao.getAttributesByCategoryIds(categoryIds);
		Map<Integer, AttributeFilterDto> attributeMap = new LinkedHashMap<>();
		
		for (Attributes attr : attributes) {
			AttributeFilterDto attrDto = new AttributeFilterDto();
			attrDto.setId(attr.getId());
			attrDto.setName(attr.getName());
			attrDto.setDataType(attr.getDataType());
			attrDto.setUnit(attr.getUnit());
			
			if (attr.getDataType() == 1) {
				// TEXT type - get possible values
				List<String> possibleValues = attributesDao.getPossibleTextValues(attr.getId(), categoryIds);
				attrDto.setPossibleValues(possibleValues);
			} else if (attr.getDataType() == 2) {
				// NUMBER type - get distinct number values
				List<Double> possibleNumberValues = attributesDao.getPossibleNumberValues(attr.getId(), categoryIds);
				attrDto.setPossibleNumberValues(possibleNumberValues);
				
				// Vẫn giữ range để tương thích (nếu cần)
				if (!possibleNumberValues.isEmpty()) {
					attrDto.setMinValue(possibleNumberValues.get(0));
					attrDto.setMaxValue(possibleNumberValues.get(possibleNumberValues.size() - 1));
				}
			}
			// BOOLEAN type (3) không cần xử lý thêm
			
			attributeMap.put(attr.getId(), attrDto);
		}
		
		filterOptions.setAttributes(attributeMap);
		return filterOptions;
	}

}
