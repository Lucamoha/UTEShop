package com.uteshop.services.admin;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.impl.admin.ProductAttributeValuesDaoImpl;
import com.uteshop.dao.impl.admin.ProductImagesDaoImpl;
import com.uteshop.dao.impl.admin.ProductVariantsDaoImpl;
import com.uteshop.dao.impl.admin.ProductsDaoImpl;
import com.uteshop.dao.impl.admin.VariantOptionsDaoImpl;
import com.uteshop.entity.catalog.Attributes;
import com.uteshop.entity.catalog.OptionTypes;
import com.uteshop.entity.catalog.OptionValues;
import com.uteshop.entity.catalog.ProductAttributeValues;
import com.uteshop.entity.catalog.ProductImages;
import com.uteshop.entity.catalog.ProductVariants;
import com.uteshop.entity.catalog.Products;
import com.uteshop.entity.catalog.VariantOptions;
import com.uteshop.services.impl.admin.AttributesServiceImpl;
import com.uteshop.services.impl.admin.OptionTypesServiceImpl;
import com.uteshop.services.impl.admin.OptionValueServiceImpl;
import com.uteshop.services.impl.admin.ProductImagesServiceImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Service class để xử lý các thao tác thêm/sửa sản phẩm với transaction management.
 * Tất cả các operations được thực hiện trong cùng một transaction và sẽ được rollback
 * nếu có lỗi xảy ra.
 */
public class ProductTransactionService {

	private ProductsDaoImpl productsDao = new ProductsDaoImpl();
	private ProductVariantsDaoImpl productVariantsDao = new ProductVariantsDaoImpl();
	private VariantOptionsDaoImpl variantOptionsDao = new VariantOptionsDaoImpl();
	private ProductAttributeValuesDaoImpl productAttributeValuesDao = new ProductAttributeValuesDaoImpl();
	private ProductImagesDaoImpl productImagesDao = new ProductImagesDaoImpl();

	// Services for read operations
	private AttributesServiceImpl attributesService = new AttributesServiceImpl();
	private OptionValueServiceImpl optionValueService = new OptionValueServiceImpl();
	private OptionTypesServiceImpl optionTypeService = new OptionTypesServiceImpl();
	private ProductImagesServiceImpl productImagesService = new ProductImagesServiceImpl();

	/**
	 * Lưu hoặc cập nhật sản phẩm với tất cả thông tin liên quan (variants,
	 * attributes, images) trong một transaction duy nhất.
	 * 
	 * @param product    Sản phẩm cần lưu/cập nhật
	 * @param isUpdate   true nếu là cập nhật, false nếu là thêm mới
	 * @param req        HttpServletRequest chứa các tham số từ form
	 * @param uploadPath Đường dẫn thư mục upload ảnh
	 * @throws Exception nếu có lỗi xảy ra (sẽ trigger rollback)
	 */
	public void saveOrUpdateProductWithTransaction(Products product, boolean isUpdate, HttpServletRequest req,
			String uploadPath) throws Exception {

		EntityManager em = null;
		EntityTransaction transaction = null;

		try {
			// Khởi tạo EntityManager và bắt đầu transaction
			em = JPAConfigs.getEntityManager();
			transaction = em.getTransaction();
			transaction.begin();

			System.out.println("=== BẮT ĐẦU TRANSACTION ===");

			// 1. Lưu/cập nhật sản phẩm
			if (isUpdate) {
				productsDao.update(product, em);
				System.out.println("✓ Đã cập nhật sản phẩm: " + product.getName());
			} else {
				productsDao.insert(product, em);
				System.out.println("✓ Đã thêm sản phẩm mới: " + product.getName());
			}

			// 2. Xử lý các biến thể bị xóa (chỉ khi update)
			if (isUpdate) {
				handleDeletedVariants(req, em);
			}

			// 3. Xử lý các biến thể hiện có (chỉ khi update)
			if (isUpdate) {
				handleExistingVariants(req, em);
			}

			// 4. Xử lý các biến thể mới
			handleNewVariants(product, req, em);

			// 5. Xử lý các thuộc tính hiện có (chỉ khi update)
			if (isUpdate) {
				handleExistingAttributes(product, req, em);
			}

			// 6. Xử lý các thuộc tính mới
			handleNewAttributes(product, req, em);

			// 7. Xử lý ảnh (xóa ảnh cũ và thêm ảnh mới)
			if (isUpdate) {
				handleImages(product, req, uploadPath, em);
			} else {
				handleImagesForNewProduct(product, req, uploadPath, em);
			}

			// Commit transaction nếu tất cả thành công
			transaction.commit();
			System.out.println("=== ✓ TRANSACTION COMMITTED SUCCESSFULLY ===");

		} catch (Exception e) {
			// Rollback nếu có lỗi
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
				System.err.println("=== ✗ TRANSACTION ROLLED BACK ===");
			}
			System.err.println("Lỗi khi lưu sản phẩm: " + e.getMessage());
			e.printStackTrace();
			throw e; // Re-throw để controller xử lý
		} finally {
			// Đóng EntityManager
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	private void handleDeletedVariants(HttpServletRequest req, EntityManager em) {
		String deletedIdsParam = req.getParameter("deletedVariantIds");
		if (deletedIdsParam != null && !deletedIdsParam.isEmpty()) {
			String[] ids = deletedIdsParam.split(",");
			for (String idStr : ids) {
				try {
					int id = Integer.parseInt(idStr.trim());
					productVariantsDao.delete(id, em);
					System.out.println("✓ Đã xóa variant ID: " + id);
				} catch (Exception ex) {
					System.err.println("Lỗi khi xóa variant ID: " + idStr);
					throw ex;
				}
			}
		}
	}

	private void handleExistingVariants(HttpServletRequest req, EntityManager em) {
		String[] deletedIdsParam = req.getParameter("deletedVariantIds") != null
				? req.getParameter("deletedVariantIds").split(",")
				: new String[0];
		List<Integer> deletedIds = new ArrayList<>();
		for (String id : deletedIdsParam) {
			if (!id.trim().isEmpty()) {
				deletedIds.add(Integer.parseInt(id.trim()));
			}
		}

		String[] existingVariantIds = req.getParameterValues("existingVariants.id");
		String[] existingVariantSkus = req.getParameterValues("existingVariants.sku");
		String[] existingVariantPrices = req.getParameterValues("existingVariants.price");
		String[] existingVariantStatuses = req.getParameterValues("existingVariants.status");

		if (existingVariantIds != null) {
			for (int i = 0; i < existingVariantIds.length; i++) {
				int variantId = Integer.parseInt(existingVariantIds[i]);

				// Bỏ qua nếu variant này đã bị xóa
				if (deletedIds.contains(variantId)) {
					continue;
				}

				ProductVariants variant = productVariantsDao.findById(variantId, em);
				if (variant != null) {
					variant.setSKU(existingVariantSkus[i]);
					variant.setPrice(new BigDecimal(existingVariantPrices[i]));
					variant.setStatus(Boolean.parseBoolean(existingVariantStatuses[i]));
					productVariantsDao.update(variant, em);
					System.out.println("✓ Đã cập nhật variant SKU: " + variant.getSKU());
				}
			}
		}
	}

	private void handleNewVariants(Products product, HttpServletRequest req, EntityManager em) {
		String[] skus = req.getParameterValues("newVariants.sku");
		String[] prices = req.getParameterValues("newVariants.price");
		String[] statuses = req.getParameterValues("newVariants.status");
		String[] optionValueIds = req.getParameterValues("newVariants.optionValueIds[]");

		if (skus != null && skus.length > 0) {
			int optionsPerVariant = (optionValueIds != null && optionValueIds.length > 0)
					? optionValueIds.length / skus.length
					: 0;
			int optionIndex = 0;

			for (int i = 0; i < skus.length; i++) {
				ProductVariants variant = new ProductVariants();
				variant.setProduct(product);
				variant.setSKU(skus[i].trim());
				variant.setStatus(Boolean.parseBoolean(statuses[i]));

				if (prices[i] != null && !prices[i].trim().isEmpty()) {
					variant.setPrice(new BigDecimal(prices[i].trim()));
				} else {
					variant.setPrice(product.getBasePrice());
				}

				productVariantsDao.insert(variant, em);
				System.out.println("✓ Đã lưu variant mới SKU: " + variant.getSKU());

				// Lưu các option của biến thể
				if (optionValueIds != null && optionsPerVariant > 0) {
					for (int j = 0; j < optionsPerVariant; j++) {
						if (optionIndex < optionValueIds.length) {
							int optionValueId = Integer.parseInt(optionValueIds[optionIndex++]);

							// Load OptionValues và OptionTypes trong cùng EntityManager
							OptionValues ov = em.find(OptionValues.class, optionValueId);
							if (ov != null) {
								// OptionType đã được load cùng OptionValue, không cần load riêng
								OptionTypes ot = ov.getOptionType();

								VariantOptions vo = new VariantOptions();
								vo.setVariant(variant);
								vo.setOptionValue(ov);
								vo.setOptionType(ot);

								variantOptionsDao.insertByMerge(vo, em);
								System.out.println("  ✓ Đã lưu variant option: " + ot.getCode() + " = " + ov.getValue());
							}
						}
					}
				}
			}
		}
	}

	private void handleExistingAttributes(Products product, HttpServletRequest req, EntityManager em) {
		String[] existingAttrIds = req.getParameterValues("existingAttributes.attributeId");
		String[] existingAttrValues = req.getParameterValues("existingAttributes.value");

		if (existingAttrIds != null) {
			for (int i = 0; i < existingAttrIds.length; i++) {
				int attrId = Integer.parseInt(existingAttrIds[i]);
				String value = existingAttrValues[i];

				// Tìm ProductAttributeValues hiện có
				ProductAttributeValues pav = findProductAttributeValue(product.getId(), attrId, em);
				if (pav != null) {
					pav.setValueText(value);
					productAttributeValuesDao.update(pav, em);
					System.out.println("✓ Đã cập nhật thuộc tính ID: " + attrId);
				}
			}
		}
	}

	private void handleNewAttributes(Products product, HttpServletRequest req, EntityManager em) {
		String[] attributeIds = req.getParameterValues("attributeIds");
		String[] attributeValues = req.getParameterValues("attributeValues");

		if (attributeIds != null && attributeIds.length > 0) {
			for (int i = 0; i < attributeIds.length; i++) {
				int attrId = Integer.parseInt(attributeIds[i]);
				String attrValue = (attributeValues != null && i < attributeValues.length) ? attributeValues[i] : "";

				if (attrValue == null || attrValue.trim().isEmpty()) {
					continue;
				}

				// Load Attributes trong cùng EntityManager để tránh detached entity
				Attributes attribute = em.find(Attributes.class, attrId);
				if (attribute == null) {
					System.err.println("Không tìm thấy attribute ID: " + attrId);
					continue;
				}
				
				// Tạo composite key trước
				ProductAttributeValues.Id pavId = new ProductAttributeValues.Id();
				pavId.setProductId(product.getId());
				pavId.setAttributeId(attribute.getId());
				
				ProductAttributeValues pav = new ProductAttributeValues();
				pav.setId(pavId);
				pav.setProduct(product);
				pav.setAttribute(attribute);
				pav.setValueText(attrValue.trim());

				productAttributeValuesDao.insert(pav, em);
				System.out.println("✓ Đã lưu thuộc tính mới ID: " + attrId);
			}
		}
	}

	private void handleImages(Products product, HttpServletRequest req, String uploadPath, EntityManager em)
			throws IOException {
		// Xóa các ảnh đã bị remove
		String[] remainingImgs = req.getParameterValues("existingImages");
		List<String> remaining = (remainingImgs != null) ? Arrays.asList(remainingImgs) : new ArrayList<>();
		productImagesService.deleteRemovedImages(product.getId(), remaining, uploadPath);

		// Thêm ảnh mới từ temp
		handleImagesForNewProduct(product, req, uploadPath, em);
	}

	private void handleImagesForNewProduct(Products product, HttpServletRequest req, String uploadPath,
			EntityManager em) throws IOException {
		String[] tempImages = req.getParameterValues("tempImages");

		if (tempImages != null && tempImages.length > 0) {
			Path tmpDir = Paths.get(uploadPath, "tmp");
			Path uploadDir = Paths.get(uploadPath);
			if (!Files.exists(uploadDir)) {
				Files.createDirectories(uploadDir);
			}

			for (String imgName : tempImages) {
				if (imgName == null || imgName.isBlank()) {
					continue;
				}

				Path src = tmpDir.resolve(imgName);
				Path dest = uploadDir.resolve(imgName);

				if (Files.isDirectory(src)) {
					continue;
				}

				if (Files.exists(src)) {
					Files.move(src, dest, StandardCopyOption.REPLACE_EXISTING);

					ProductImages img = new ProductImages();
					img.setImageUrl(imgName);
					img.setProduct(product);
					productImagesDao.insert(img, em);
					System.out.println("✓ Đã lưu ảnh: " + imgName);
				}
			}
		}
	}

	// Helper method to find ProductAttributeValues
	private ProductAttributeValues findProductAttributeValue(int productId, int attributeId, EntityManager em) {
		try {
			String jpql = "SELECT pav FROM ProductAttributeValues pav WHERE pav.product.Id = :productId AND pav.attribute.Id = :attributeId";
			return em.createQuery(jpql, ProductAttributeValues.class).setParameter("productId", productId)
					.setParameter("attributeId", attributeId).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
}
