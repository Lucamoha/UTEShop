package com.uteshop.dao.impl.admin;

import java.io.File;
import java.util.List;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.admin.IProductImagesDao;
import com.uteshop.entity.catalog.ProductImages;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class ProductImagesDaoImpl extends AbstractDao<ProductImages> implements IProductImagesDao {

	public ProductImagesDaoImpl() {
		super(ProductImages.class);
	}

	@Override
	public List<ProductImages> getImageById(int id) {
		EntityManager enma = JPAConfigs.getEntityManager();
		String jpql = """
					SELECT pi
					FROM ProductImages pi
					WHERE pi.product.Id = :id
				""";
		return enma.createQuery(jpql, ProductImages.class).setParameter("id", id).getResultList();
	}

	@Override
	public void deleteByProductId(Integer productId, String uploadPath) {

		//Lấy danh sách ảnh để xóa file vật lý
		List<ProductImages> imageList = this.getImageById(productId);

		// 2. Xóa file vật lý trong thư mục /uploads
		for (ProductImages img : imageList) {
			if (img.getImageUrl() != null) {
				File file = new File(uploadPath, img.getImageUrl());
				if (file.exists()) {
					boolean deleted = file.delete();
					System.out.println("Xóa file: " + file.getAbsolutePath() + " -> " + deleted);
				}
			}
		}

		// Xóa trong db
		super.delete(super.findById(productId));
	}

	@Override
	public void deleteRemovedImages(Integer productId, List<String> remainingFileNames, String uploadPath) {
	    EntityManager em = JPAConfigs.getEntityManager();
	    EntityTransaction tx = em.getTransaction();
	    try {
	        // Lấy toàn bộ ảnh cũ trong DB
	        List<ProductImages> oldImages = this.getImageById(productId);

	        tx.begin();
	        for (ProductImages img : oldImages) {
	            String fileName = img.getImageUrl();
	            if (!remainingFileNames.contains(fileName)) {
	                // Xóa file vật lý
	                File file = new File(uploadPath, fileName);
	                if (file.exists()) {
	                    boolean deleted = file.delete();
	                    System.out.println("[ẢNH] Đã xóa file: " + file.getAbsolutePath() + " → " + deleted);
	                }
	                // Xóa record DB
	                em.remove(em.contains(img) ? img : em.merge(img));
	                System.out.println("[ẢNH] Đã xóa record DB: " + fileName);
	            }
	        }
	        tx.commit();
	    } catch (Exception e) {
	        e.printStackTrace();
	        if (tx.isActive()) tx.rollback();
	    } finally {
	        em.close();
	    }
	}

}
