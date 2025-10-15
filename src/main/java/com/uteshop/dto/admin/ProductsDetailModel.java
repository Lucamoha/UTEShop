package com.uteshop.dto.admin;

import java.util.List;

import com.uteshop.entity.catalog.ProductImages;
import com.uteshop.entity.catalog.Products;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductsDetailModel {
	Products product;
	List<ProductVariantDetailsModel> productVariantsDetails;
	Integer TotalVariants;
	List<ProductImages> productImages;
}
