package com.uteshop.dao.admin;

import java.util.List;

import com.uteshop.entity.catalog.ProductImages;

public interface IProductImagesDao {
	List<ProductImages> getImageById(int id);
}
