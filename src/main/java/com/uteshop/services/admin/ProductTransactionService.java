package com.uteshop.services.admin;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.impl.admin.ProductAttributeValuesDaoImpl;
import com.uteshop.dao.impl.admin.ProductImagesDaoImpl;
import com.uteshop.dao.impl.admin.ProductVariantsDaoImpl;
import com.uteshop.dao.impl.admin.ProductsDaoImpl;
import com.uteshop.dao.impl.admin.VariantOptionsDaoImpl;
import com.uteshop.entity.catalog.Attributes;
import com.uteshop.entity.catalog.Categories;
import com.uteshop.entity.catalog.OptionTypes;
import com.uteshop.entity.catalog.OptionValues;
import com.uteshop.entity.catalog.ProductAttributeValues;
import com.uteshop.entity.catalog.ProductImages;
import com.uteshop.entity.catalog.ProductVariants;
import com.uteshop.entity.catalog.Products;
import com.uteshop.entity.catalog.VariantOptions;
import com.uteshop.exception.DuplicateOptionCombinationException;
import com.uteshop.exception.DuplicateSkuException;
import com.uteshop.services.impl.admin.AttributesServiceImpl;
import com.uteshop.services.impl.admin.ProductImagesServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.servlet.http.HttpServletRequest;

/*Tất cả các thao tác được thực hiện trong cùng một transaction và sẽ rollback nếu có lỗi xảy ra.*/
public class ProductTransactionService {

	private ProductsDaoImpl productsDao = new ProductsDaoImpl();
	private ProductVariantsDaoImpl productVariantsDao = new ProductVariantsDaoImpl();
	private VariantOptionsDaoImpl variantOptionsDao = new VariantOptionsDaoImpl();
	private ProductAttributeValuesDaoImpl productAttributeValuesDao = new ProductAttributeValuesDaoImpl();
	private ProductImagesDaoImpl productImagesDao = new ProductImagesDaoImpl();
	private ProductImagesServiceImpl productImagesService = new ProductImagesServiceImpl();
	private AttributesServiceImpl attributesServiceImpl = new AttributesServiceImpl();

	/*
	 * Lưu hoặc cập nhật sản phẩm với tất cả thông tin liên quan (variants,
	 * attributes, images) trong một transaction duy nhất
	 */

	public void saveOrUpdateProductWithTransaction(Products product, boolean isUpdate, HttpServletRequest req,
			String uploadPath) throws Exception {

		EntityManager enma = JPAConfigs.getEntityManager();
		EntityTransaction transaction = null;
		try {
			transaction = enma.getTransaction();
			transaction.begin();

			System.out.println("===BẮT ĐẦU TRANSACTION===");

			// Lưu/cập nhật sản phẩm
			if (isUpdate) {
				productsDao.update(product, enma);
			} else {
				productsDao.insert(product, enma);
			}

			if (isUpdate) {
				handleDeletedVariants(req, enma);
			}

			// List để lưu các SKU cũ đã được thay đổi (để bỏ qua khi check duplicate)
			List<String> releasedSkus = new ArrayList<>();

			// Xử lý các biến thể hiện có (chỉ khi update)
			if (isUpdate) {
				releasedSkus = handleExistingVariants(req, enma);
			}

			// Xử lý các biến thể mới
			handleNewVariants(product, req, enma, releasedSkus);

			// Xóa các thuộc tính cũ không thuộc category mới (khi đổi category)
			if (isUpdate) {
				deleteAttributesNotInCategory(product, enma);
			}

			// Xóa các thuộc tính đã được đánh dấu xóa bởi người dùng
			if (isUpdate) {
				handleDeletedAttributes(product, req, enma);
			}

			// Xử lý các thuộc tính hiện có
			if (isUpdate) {
				handleExistingAttributes(product, req, enma);
			}

			// Xử lý các thuộc tính mới
			handleNewAttributes(product, req, enma);

			// Xử lý ảnh (xóa ảnh cũ và thêm ảnh mới)
			if (isUpdate) {
				handleImages(product, req, uploadPath, enma);
			} else {
				handleImagesForNewProduct(product, req, uploadPath, enma);
			}

			transaction.commit();
			System.out.println("===TRANSACTION COMMITTED===");

		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
				System.err.println("===TRANSACTION ROLLED BACK===");
			}
			System.err.println("Lỗi khi lưu sản phẩm: " + e.getMessage());
			e.printStackTrace();
			throw e; // Re-throw để controller xử lý
		} finally {
			if (enma != null && enma.isOpen()) {
				enma.close();
			}
		}
	}

	private void handleDeletedVariants(HttpServletRequest req, EntityManager em) {
		String deletedIdsParam = req.getParameter("deletedVariantIds");
		System.out.println(">>> deletedVariantIds parameter: " + deletedIdsParam);
		
		if (deletedIdsParam != null && !deletedIdsParam.isEmpty()) {
			String[] ids = deletedIdsParam.split(",");
			System.out.println(">>> Số lượng variants cần xóa: " + ids.length);
			for (String idStr : ids) {
				try {
					int id = Integer.parseInt(idStr.trim());
					
					// Xóa variant options trước (foreign key constraint)
					String deleteOptionsJpql = "DELETE FROM VariantOptions vo WHERE vo.variant.id = :variantId";
					int deletedOptions = em.createQuery(deleteOptionsJpql)
						.setParameter("variantId", id)
						.executeUpdate();
					System.out.println("Đã xóa " + deletedOptions + " variant options cho variant ID: " + id);
					
					// Xóa variant
					ProductVariants variant = em.find(ProductVariants.class, id);
					if (variant != null) {
						em.remove(variant);
						em.flush(); // Force sync với DB ngay lập tức
						System.out.println("Đã xóa variant ID: " + id + " (flushed)");
					} else {
						System.out.println("Variant ID " + id + " không tồn tại trong DB");
					}
				} catch (Exception ex) {
					System.err.println("Lỗi khi xóa variant ID: " + idStr);
					ex.printStackTrace();
					throw ex;
				}
			}
		}
	}

	private void handleDeletedAttributes(Products product, HttpServletRequest req, EntityManager em) {
		String deletedIdsParam = req.getParameter("deletedAttributeIds");
		if (deletedIdsParam != null && !deletedIdsParam.isEmpty()) {
			String[] ids = deletedIdsParam.split(",");
			for (String idStr : ids) {
				try {
					int attributeId = Integer.parseInt(idStr.trim());
					// Xóa ProductAttributeValue dựa trên productId và attributeId
					String jpql = "DELETE FROM ProductAttributeValues pav WHERE pav.product.Id = :productId AND pav.attribute.Id = :attributeId";
					int deleted = em.createQuery(jpql)
							.setParameter("productId", product.getId())
							.setParameter("attributeId", attributeId)
							.executeUpdate();
					System.out.println("Đã xóa attribute ID: " + attributeId + " (số bản ghi: " + deleted + ")");
				} catch (Exception ex) {
					System.err.println("Lỗi khi xóa attribute ID: " + idStr);
					throw ex;
				}
			}
		}
	}

	private List<String> handleExistingVariants(HttpServletRequest req, EntityManager em) {
		List<String> releasedSkus = new ArrayList<>(); // SKUs đã được giải phóng (thay đổi)

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
					String oldSKU = variant.getSKU(); // Lưu SKU cũ
					String newSKU = existingVariantSkus[i].trim();

					// Kiểm tra SKU trùng khi update (cho phép giữ nguyên SKU cũ)
					if (!newSKU.equals(oldSKU)) {
						// SKU đã thay đổi - thêm SKU cũ vào danh sách released
						releasedSkus.add(oldSKU);
						System.out.println(">>> SKU đã thay đổi: " + oldSKU + " → " + newSKU);

						ProductVariants existingVariant = findVariantBySKU(em, newSKU, deletedIds);
						if (existingVariant != null) {
							throw new DuplicateSkuException(newSKU);
						}
					}

					variant.setSKU(newSKU);
					variant.setPrice(new BigDecimal(existingVariantPrices[i]));
					variant.setStatus(Boolean.parseBoolean(existingVariantStatuses[i]));
					productVariantsDao.update(variant, em);
					System.out.println("✓ Đã cập nhật variant SKU: " + variant.getSKU());
				}
			}
		}

		return releasedSkus;
	}

	private void handleNewVariants(Products product, HttpServletRequest req, EntityManager em,
			List<String> releasedSkus) {
		// Lấy danh sách variant đã xóa
		String[] deletedIdsParam = req.getParameter("deletedVariantIds") != null
				? req.getParameter("deletedVariantIds").split(",")
				: new String[0];
		List<Integer> deletedIds = new ArrayList<>();
		for (String id : deletedIdsParam) {
			if (!id.trim().isEmpty()) {
				deletedIds.add(Integer.parseInt(id.trim()));
			}
		}

		System.out.println(">>> Deleted Variant IDs: " + deletedIds);
		System.out.println(">>> Released SKUs (changed): " + releasedSkus);

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
				String sku = skus[i].trim();

				System.out.println(
						">>> Checking new variant SKU: " + sku + " (excluding deleted IDs: " + deletedIds + ")");

				// Nếu SKU này là SKU cũ đã được giải phóng (variant đã đổi SKU), cho phép tái
				// sử dụng
				if (releasedSkus.contains(sku)) {
					System.out.println(">>> SKU '" + sku + "' đã được giải phóng, cho phép tái sử dụng");
				} else {
					// Kiểm tra SKU trùng trước khi insert, bỏ qua các variant đã xóa
					ProductVariants existingVariant = findVariantBySKU(em, sku, deletedIds);
					if (existingVariant != null) {
						System.err.println(">>> DUPLICATE SKU FOUND: " + sku + " (Existing Variant ID: "
								+ existingVariant.getId() + ")");
						throw new DuplicateSkuException(sku);
					}
				}

				// Thu thập option values của variant này
				List<Integer> currentVariantOptions = new ArrayList<>();
				if (optionValueIds != null && optionsPerVariant > 0) {
					for (int j = 0; j < optionsPerVariant; j++) {
						if (optionIndex < optionValueIds.length) {
							int optionValueId = Integer.parseInt(optionValueIds[optionIndex]);
							currentVariantOptions.add(optionValueId);
							optionIndex++;
						}
					}
				}
				
				// Kiểm tra trùng tổ hợp options
				if (!currentVariantOptions.isEmpty()) {
					if (isDuplicateOptionCombination(product.getId(), currentVariantOptions, deletedIds, em)) {
						// Lấy tên các option values để hiển thị trong message lỗi
						StringBuilder optionNames = new StringBuilder();
						for (int ovId : currentVariantOptions) {
							OptionValues ov = em.find(OptionValues.class, ovId);
							if (ov != null) {
								if (optionNames.length() > 0) optionNames.append(", ");
								optionNames.append(ov.getOptionType().getCode()).append(": ").append(ov.getValue());
							}
						}
						throw new DuplicateOptionCombinationException(optionNames.toString());
					}
				}

				ProductVariants variant = new ProductVariants();
				variant.setProduct(product);
				variant.setSKU(sku);
				variant.setStatus(Boolean.parseBoolean(statuses[i]));

				if (prices[i] != null && !prices[i].trim().isEmpty()) {
					variant.setPrice(new BigDecimal(prices[i].trim()));
				} else {
					variant.setPrice(product.getBasePrice());
				}

				productVariantsDao.insert(variant, em);
				System.out.println("Đã lưu variant mới SKU: " + variant.getSKU());

				// Lưu các option của biến thể
				if (!currentVariantOptions.isEmpty()) {
					for (int ovId : currentVariantOptions) {
						OptionValues ov = em.find(OptionValues.class, ovId);
						if (ov != null) {
							OptionTypes ot = ov.getOptionType();

							VariantOptions vo = new VariantOptions();
							vo.setVariant(variant);
							vo.setOptionValue(ov);
							vo.setOptionType(ot);

							variantOptionsDao.insertByMerge(vo, em);
							System.out.println("Đã lưu variant option: " + ot.getCode() + " = " + ov.getValue());
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
				String value = existingAttrValues[i].trim();

				// Tìm ProductAttributeValues hiện có
				ProductAttributeValues pav = findProductAttributeValue(product.getId(), attrId, em);
				Attributes attribute = attributesServiceImpl.findById(attrId);
				if (pav != null) {
					if (attribute.getDataType() == 2) {//Là number
						pav.setValueNumber(new BigDecimal(value));
						pav.setValueText(null);
					} else if(attribute.getDataType() == 3){//Là boolean
						if(value.equals("1")) {
							pav.setValueNumber(new BigDecimal(1));
							pav.setValueText(null);
						} else {
							pav.setValueNumber(new BigDecimal(0));
							pav.setValueText(null);;
						}
					}
					else {
						pav.setValueText(value);
						pav.setValueNumber(null);
					}

					productAttributeValuesDao.update(pav, em);
					System.out.println("Đã cập nhật thuộc tính ID: " + attrId);
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
				String attrValue = (attributeValues != null && i < attributeValues.length) ? attributeValues[i].trim()
						: "";

				if (attrValue == null || attrValue.isEmpty()) {
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

				if (attribute.getDataType() == 2) {//la number
					pav.setValueNumber(new BigDecimal(attrValue));
					pav.setValueText(null);
				}  else if(attribute.getDataType() == 3){//Là boolean
					if(attrValue.equals("1")) {
						pav.setValueNumber(new BigDecimal(1));
						pav.setValueText(null);
					} else {
						pav.setValueNumber(new BigDecimal(0));
						pav.setValueText(null);;
					}
				} else {
					pav.setValueText(attrValue);
					pav.setValueNumber(null);
				}

				//productAttributeValuesDao.insert(pav, em);
				productAttributeValuesDao.insertByMerge(pav, em);
				System.out.println("Đã lưu thuộc tính mới ID: " + attrId);
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
			
			// Xây dựng đường dẫn vật lý: uploads/danh_mục_cha/danh_mục/
			String productFolderPath = buildProductFolderPath(product);
			Path uploadDir = Paths.get(uploadPath, productFolderPath);
			
			if (!Files.exists(uploadDir)) {
				Files.createDirectories(uploadDir);
			}

			for (String imgName : tempImages) {
				if (imgName == null || imgName.isBlank()) {
					continue;
				}

				Path src = tmpDir.resolve(imgName);
				
				// Tạo tên file mới: tên_sản_phẩm_uuid.extension
				String newFileName = generateProductImageFileName(product, imgName);
				Path dest = uploadDir.resolve(newFileName);

				if (Files.isDirectory(src)) {
					continue;
				}

				if (Files.exists(src)) {
					Files.move(src, dest, StandardCopyOption.REPLACE_EXISTING);

					// Lưu trong DB: chỉ products/tên_file (không theo category)
					String relativeImagePath = "products/" + newFileName;
					
					ProductImages img = new ProductImages();
					img.setImageUrl(relativeImagePath);
					img.setProduct(product);
					productImagesDao.insert(img, em);
					
					System.out.println("✓ File vật lý: " + productFolderPath + "/" + newFileName);
					System.out.println("✓ DB lưu: " + relativeImagePath);
				}
			}
		}
	}

	// Xóa các thuộc tính không thuộc category hiện tại (khi đổi category)
	private void deleteAttributesNotInCategory(Products product, EntityManager em) {
		try {
			// Lấy danh sách attribute IDs thuộc category hiện tại của product
			String jpql = "SELECT ca.id.attributeId FROM CategoryAttributes ca WHERE ca.id.categoryId = :categoryId";
			List<Integer> validAttributeIds = em.createQuery(jpql, Integer.class)
					.setParameter("categoryId", product.getCategory().getId())
					.getResultList();

			if (validAttributeIds.isEmpty()) {
				// Nếu category mới không có attributes, xóa tất cả attributes của product
				String deleteJpql = "DELETE FROM ProductAttributeValues pav WHERE pav.product.Id = :productId";
				int deleted = em.createQuery(deleteJpql)
						.setParameter("productId", product.getId())
						.executeUpdate();
				System.out.println("Đã xóa " + deleted + " thuộc tính cũ (category mới không có attributes)");
			} else {
				// Xóa các attributes không thuộc category mới
				String deleteJpql = "DELETE FROM ProductAttributeValues pav WHERE pav.product.Id = :productId AND pav.attribute.Id NOT IN :validIds";
				int deleted = em.createQuery(deleteJpql)
						.setParameter("productId", product.getId())
						.setParameter("validIds", validAttributeIds)
						.executeUpdate();
				
				if (deleted > 0) {
					System.out.println("Đã xóa " + deleted + " thuộc tính không thuộc category mới");
				}
			}
		} catch (Exception e) {
			System.err.println("Lỗi khi xóa attributes cũ: " + e.getMessage());
			// Không throw exception để không làm gián đoạn transaction
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

	// Helper method to find ProductVariants by SKU (excluding deleted variants)
	private ProductVariants findVariantBySKU(EntityManager em, String sku, List<Integer> deletedIds) {
		try {
			String jpql = "SELECT pv FROM ProductVariants pv WHERE pv.SKU = :sku";
			if (deletedIds != null && !deletedIds.isEmpty()) {
				jpql += " AND pv.Id NOT IN :deletedIds";
			}

			var query = em.createQuery(jpql, ProductVariants.class).setParameter("sku", sku);

			if (deletedIds != null && !deletedIds.isEmpty()) {
				query.setParameter("deletedIds", deletedIds);
			}

			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Xây dựng đường dẫn thư mục cho sản phẩm theo cấu trúc: danh_mục_cha/danh_mục
	 * Nếu không có danh mục cha, trả về: danh_mục
	 */
	private String buildProductFolderPath(Products product) {
		Categories category = product.getCategory();
		Categories parentCategory = category.getParent();
		
		String categorySlug = sanitizeForPath(category.getName());
		
		if (parentCategory != null) {
			String parentSlug = sanitizeForPath(parentCategory.getName());
			return parentSlug + "/" + categorySlug;
		} else {
			return categorySlug;
		}
	}
	
	/**
	 * Tạo tên file ảnh mới theo định dạng: tên_sản_phẩm_uuid.extension
	 */
	private String generateProductImageFileName(Products product, String originalFileName) {
		// Lấy extension từ file gốc
		String extension = "";
		if (originalFileName.contains(".")) {
			extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		}
		
		// Chuẩn hóa tên sản phẩm thành slug
		String productSlug = sanitizeForPath(product.getName());
		
		// Tạo UUID ngắn (8 ký tự đầu) để tránh trùng
		String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
		
		return productSlug + "-" + uniqueSuffix + extension;
	}
	
	/**
	 * Chuẩn hóa chuỗi để sử dụng làm đường dẫn file/folder
	 * - Loại bỏ dấu tiếng Việt
	 * - Chuyển thành chữ thường
	 * - Thay khoảng trắng và ký tự đặc biệt bằng dấu gạch dưới
	 */
	private String sanitizeForPath(String text) {
		if (text == null || text.isEmpty()) {
			return "unknown";
		}
		
		// Loại bỏ dấu tiếng Việt
		String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		String withoutAccents = pattern.matcher(normalized).replaceAll("");
		
		// Chuyển thành chữ thường và thay thế ký tự đặc biệt
		String result = withoutAccents.toLowerCase()
				.replaceAll("đ", "d")
				.replaceAll("[^a-z0-9]+", "-")
				.replaceAll("^-+|-+$", ""); // Loại bỏ _ ở đầu và cuối
		
		return result.isEmpty() ? "unknown" : result;
	}
	
	/**
	 * Kiểm tra xem tổ hợp option values đã tồn tại cho product này chưa
	 * @param productId ID sản phẩm
	 * @param optionValueIds Danh sách option value IDs (đã sắp xếp)
	 * @param excludedVariantIds Danh sách variant IDs cần loại trừ (variant đã xóa)
	 * @param em EntityManager
	 * @return true nếu tổ hợp đã tồn tại, false nếu chưa
	 */
	private boolean isDuplicateOptionCombination(Integer productId, List<Integer> optionValueIds, 
			List<Integer> excludedVariantIds, EntityManager em) {
		
		// Query tìm các variants của product này (không bao gồm variants đã xóa)
		String jpql = "SELECT v FROM ProductVariants v WHERE v.product.id = :productId";
		if (excludedVariantIds != null && !excludedVariantIds.isEmpty()) {
			jpql += " AND v.id NOT IN :excludedIds";
		}
		
		var query = em.createQuery(jpql, ProductVariants.class)
				.setParameter("productId", productId);
		
		if (excludedVariantIds != null && !excludedVariantIds.isEmpty()) {
			query.setParameter("excludedIds", excludedVariantIds);
		}
		
		List<ProductVariants> existingVariants = query.getResultList();
		
		// Sắp xếp optionValueIds để so sánh
		List<Integer> sortedNewOptions = new ArrayList<>(optionValueIds);
		Collections.sort(sortedNewOptions);
		
		// Kiểm tra từng variant hiện có
		for (ProductVariants variant : existingVariants) {
			// Lấy option values của variant này
			String voJpql = "SELECT vo.optionValue.id FROM VariantOptions vo WHERE vo.variant.id = :variantId";
			List<Integer> existingOptions = em.createQuery(voJpql, Integer.class)
					.setParameter("variantId", variant.getId())
					.getResultList();
			
			// Sắp xếp để so sánh
			Collections.sort(existingOptions);
			
			// So sánh 2 danh sách
			if (sortedNewOptions.equals(existingOptions)) {
				System.err.println(">>> DUPLICATE OPTIONS: Variant ID " + variant.getId() 
						+ " đã có tổ hợp options: " + existingOptions);
				return true;
			}
		}
		
		return false;
	}
}
