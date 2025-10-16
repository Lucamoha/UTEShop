package com.uteshop.dao.admin;

import java.util.List;

import com.uteshop.entity.catalog.ProductImages;

public interface IProductImagesDao {
	List<ProductImages> getImageById(int id);
	void deleteByProductId(Integer productId, String uploadPath);
	void deleteRemovedImages(Integer productId, List<String> remainingFileNames, String uploadPath);
}
