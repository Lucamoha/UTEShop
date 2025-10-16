package com.uteshop.services.admin;

import java.util.List;

import com.uteshop.entity.catalog.ProductImages;

public interface IProductImagesService {
	List<ProductImages> getImageById(int id);
	void insert(ProductImages productImage);
}
