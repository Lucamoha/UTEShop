package com.uteshop.services.impl.web;

import com.uteshop.dao.impl.web.ProductVariantsDaoImpl;
import com.uteshop.dao.web.IProductVariantsDao;
import com.uteshop.dto.web.VariantDto;
import com.uteshop.entity.catalog.ProductVariants;
import com.uteshop.services.web.IProductVariantsService;

import java.util.List;

public class ProductVariantsServiceImpl implements IProductVariantsService {

    private final IProductVariantsDao variantsDao = new ProductVariantsDaoImpl();

    @Override
    public VariantDto findVariantByOptions(Integer productId, List<Integer> optionValueIds) {
        // Tìm variant từ DAO
        ProductVariants variant = variantsDao.findByProductAndOptionValues(productId, optionValueIds);

        if (variant == null) {
            return null;
        }

        // Chuyển entity sang DTO
        VariantDto dto = VariantDto.builder()
                .variantId(variant.getId())
                .sku(variant.getSKU())
                .price(variant.getPrice())
                .status(variant.isStatus() ? "active" : "inactive")
                .build();
        return dto;
    }

    @Override
    public List<ProductVariants> findAllByProductId(Integer productId) {
        return variantsDao.findByProductId(productId);
    }

    @Override
    public ProductVariants findById(Integer id) {
        return variantsDao.findById(id);
    }
}
