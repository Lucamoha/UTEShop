package com.uteshop.dao.web.catalog;

import com.uteshop.dto.web.ProductComparisonDto;


public interface IProductComparisonDao {
    ProductComparisonDto compareProducts(int product1Id, int product2Id);
    ProductComparisonDto compareProductsBySlug(String product1Slug, String product2Slug);
    Integer getProductIdBySlug(String slug);
}
