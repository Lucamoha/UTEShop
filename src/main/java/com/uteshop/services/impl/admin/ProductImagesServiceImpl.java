package com.uteshop.services.impl.admin;

import java.util.List;

import com.uteshop.dao.admin.IProductImagesDao;
import com.uteshop.dao.impl.admin.ProductImagesDaoImpl;
import com.uteshop.entity.catalog.ProductImages;
import com.uteshop.services.admin.IProductImagesService;

public class ProductImagesServiceImpl implements IProductImagesService{

	IProductImagesDao productImagesDao = new ProductImagesDaoImpl();
	ProductImagesDaoImpl productImagesDaoImpl = new ProductImagesDaoImpl();
	@Override
	public List<ProductImages> getImageById(int id) {
		return productImagesDao.getImageById(id);
	}
	@Override
	public void insert(ProductImages productImage) {
		productImagesDaoImpl.insert(productImage);
	}
	@Override
	public void deleteByProductId(Integer productId, String uploadPath) {
		productImagesDaoImpl.deleteByProductId(productId, uploadPath);		
	}
	@Override
	public void deleteRemovedImages(Integer productId, List<String> remainingFileNames, String uploadPath) {
		productImagesDaoImpl.deleteRemovedImages(productId, remainingFileNames, uploadPath);
	}

}
