package com.uteshop.services.web.catalog;

import com.uteshop.dto.web.VariantDto;
import com.uteshop.entity.catalog.ProductVariants;

import java.util.List;

public interface IProductVariantsService {

    // Tìm variant theo productId và danh sách option values đã chọn
    VariantDto findVariantByOptions(Integer productId, List<Integer> optionValueIds);

    // Tìm tất cả các variant của một sản phẩm
    List<ProductVariants> findAllByProductId(Integer productId);

    // Tìm variant theo ID
    ProductVariants findById(Integer id);
}
