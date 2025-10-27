package com.uteshop.services.web.catalog;

import com.uteshop.dto.web.ProductComparisonDto;

/**
 * Service Interface cho chức năng so sánh sản phẩm
 */
public interface IProductComparisonService {

    ProductComparisonDto compareProducts(int product1Id, int product2Id);
    
    ProductComparisonDto compareProductsBySlug(String product1Slug, String product2Slug);
    
}
