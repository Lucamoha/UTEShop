package com.uteshop.dao.web.catalog;

import com.uteshop.entity.catalog.ProductVariants;

import java.util.List;

public interface IProductVariantsDao {

    ProductVariants findByProductAndOptionValues(Integer productId, List<Integer> optionValueIds);

    List<ProductVariants> findByProductId(Integer productId);

    ProductVariants findById(Integer id);
}
