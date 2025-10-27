package com.uteshop.services.impl.web.catalog;

import com.uteshop.dao.impl.web.catalog.ProductComparisonDaoImpl;
import com.uteshop.dao.web.catalog.IProductComparisonDao;
import com.uteshop.dto.web.ProductComparisonDto;
import com.uteshop.services.web.catalog.IProductComparisonService;

/**
 * Implementation của Service cho chức năng so sánh sản phẩm
 */
public class ProductComparisonServiceImpl implements IProductComparisonService {
    
    private final IProductComparisonDao productComparisonDao;
    
    public ProductComparisonServiceImpl() {
        this.productComparisonDao = new ProductComparisonDaoImpl();
    }

    @Override
    public ProductComparisonDto compareProducts(int product1Id, int product2Id) {
        // Gọi DAO để thực hiện so sánh
        ProductComparisonDto result = productComparisonDao.compareProducts(product1Id, product2Id);
        
        if (result == null) {
            throw new RuntimeException("Không thể so sánh 2 sản phẩm này. Vui lòng kiểm tra lại.");
        }
        
        return result;
    }


    @Override
    public ProductComparisonDto compareProductsBySlug(String product1Slug, String product2Slug) {
        // Validate input
        if (product1Slug == null || product1Slug.trim().isEmpty() ||
            product2Slug == null || product2Slug.trim().isEmpty()) {
            throw new IllegalArgumentException("Slug không được để trống");
        }
        
        if (product1Slug.equals(product2Slug)) {
            throw new IllegalArgumentException("Không thể so sánh cùng một sản phẩm");
        }
        
        // Gọi DAO để thực hiện so sánh
        ProductComparisonDto result = productComparisonDao.compareProductsBySlug(product1Slug, product2Slug);
        
        if (result == null) {
            throw new RuntimeException("Không thể so sánh 2 sản phẩm này. Vui lòng kiểm tra lại.");
        }
        
        return result;
    }
}
