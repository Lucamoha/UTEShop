package com.uteshop.services.impl.admin;

import java.util.List;
import java.util.Map;

import com.uteshop.dao.admin.IProductsDao;
import com.uteshop.dao.impl.admin.ProductAttributeValuesDaoImpl;
import com.uteshop.dao.impl.admin.ProductImagesDaoImpl;
import com.uteshop.dao.impl.admin.ProductVariantsDaoImpl;
import com.uteshop.dao.impl.admin.ProductsDaoImpl;
import com.uteshop.dao.impl.admin.VariantOptionsDaoImpl;
import com.uteshop.dto.admin.OptionValueDTO;
import com.uteshop.dto.admin.ProductAttributeDisplayModel;
import com.uteshop.dto.admin.ProductVariantDetailsModel;
import com.uteshop.dto.admin.ProductVariantDisplayModel;
import com.uteshop.dto.admin.ProductsDetailModel;
import com.uteshop.entity.catalog.Attributes;
import com.uteshop.entity.catalog.ProductAttributeValues;
import com.uteshop.entity.catalog.Products;
import com.uteshop.services.admin.IAttributesService;
import com.uteshop.services.admin.ICategoriesService;
import com.uteshop.services.admin.IOptionTypesService;
import com.uteshop.services.admin.IOptionValueService;
import com.uteshop.services.admin.IProductsService;
import com.uteshop.services.admin.IProductsVariantsService;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.uteshop.configs.JPAConfigs;
import com.uteshop.entity.catalog.Categories;
import com.uteshop.entity.catalog.OptionTypes;
import com.uteshop.entity.catalog.OptionValues;
import com.uteshop.entity.catalog.ProductImages;
import com.uteshop.entity.catalog.ProductVariants;
import com.uteshop.entity.catalog.VariantOptions;
import com.uteshop.exception.DuplicateSkuException;
import jakarta.persistence.EntityTransaction;

public class ProductsServiceImpl implements IProductsService {

	ProductsDaoImpl productsDaoImpl = new ProductsDaoImpl();
	IProductsDao productsDao = new ProductsDaoImpl();
	ProductVariantsDaoImpl productVariantsDao = new ProductVariantsDaoImpl();
	VariantOptionsDaoImpl variantOptionsDao = new VariantOptionsDaoImpl();
	ProductAttributeValuesDaoImpl productAttributeValuesDao = new ProductAttributeValuesDaoImpl();
	ProductImagesDaoImpl productImagesDao = new ProductImagesDaoImpl();
	ProductImagesServiceImpl productImagesService = new ProductImagesServiceImpl();
	AttributesServiceImpl attributesServiceImpl = new AttributesServiceImpl();
	IOptionValueService optionValueService = new OptionValueServiceImpl();
	IProductsVariantsService productsVariantsService = new ProductVariantsServiceImpl();
	ICategoriesService categoriesService = new CategoriesServiceImpl();
	IAttributesService attributesService = new AttributesServiceImpl();
	IOptionTypesService optionTypeService = new OptionTypesServiceImpl();

	@Override
	public List<Products> findAll() {
		return productsDaoImpl.findAll();
	}

	@Override
	public Products findById(int id) {
		return productsDaoImpl.findById(id);
	}

	@Override
	public void insert(Products product) {
		productsDaoImpl.insert(product);
	}

	@Override
	public void update(Products products) {
		productsDaoImpl.update(products);
	}

	@Override
	public void delete(int id) {
		productsDaoImpl.delete(id);
	}

	@Override
	public int count(String searchKeyword, String searchKeywordColumnName) {
		return productsDaoImpl.count(searchKeyword, searchKeywordColumnName);
	}

	@Override
	public Products findByName(String name) {
		return productsDaoImpl.findByName(name);
	}

	@Override
	public List<Object[]> getTopSellingProducts(int limit, int branchId) {
		return productsDaoImpl.getTopSellingProducts(limit, branchId);
	}

	@Override
	public List<Products> findAll(boolean all, int firstResult, int maxResult, String searchKeyword,
			String searchKeywordColumnName) {
		return productsDaoImpl.findAll(all, firstResult, maxResult, searchKeyword, searchKeywordColumnName);
	}

	@Override
	public List<Attributes> findAttributesByCategoryId(Integer categoryId) {
		return productsDaoImpl.findAttributesByCategoryId(categoryId);
	}

	@Override
	public Products findByIdFetchColumns(Object id, List<String> fetchColumnsName) {
		return productsDaoImpl.findByIdFetchColumns(id, fetchColumnsName);
	}

	/*
	 * Lưu hoặc cập nhật sản phẩm với tất cả thông tin liên quan (variants,
	 * attributes, images) trong một transaction duy nhất
	 */
	@Override
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
				productsDaoImpl.update(product, enma);
			} else {
				productsDaoImpl.insert(product, enma);
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

	@Override
	public void handleDeletedVariants(HttpServletRequest req, EntityManager enma) {
		String deletedIdsParam = req.getParameter("deletedVariantIds");
		System.out.println(">>> deletedVariantIds parameter: " + deletedIdsParam);

		if (deletedIdsParam != null && !deletedIdsParam.isEmpty()) {
			String[] ids = deletedIdsParam.split(",");
			System.out.println(">>> Số lượng variants cần xóa: " + ids.length);
			for (String idStr : ids) {
				try {
					int id = Integer.parseInt(idStr.trim());

					// Xóa variant options trước (foreign key constraint)
					productsDao.deleteVariantOptionsByVariantsId(id, enma);

					// Xóa variant
					ProductVariants variant = enma.find(ProductVariants.class, id);
					if (variant != null) {
						enma.remove(variant);
						enma.flush(); // Force sync với DB ngay lập tức
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

	@Override
	public void handleDeletedAttributes(Products product, HttpServletRequest req, EntityManager enma) {
		String deletedIdsParam = req.getParameter("deletedAttributeIds");
		if (deletedIdsParam != null && !deletedIdsParam.isEmpty()) {
			String[] ids = deletedIdsParam.split(",");
			for (String idStr : ids) {
				try {
					int attributeId = Integer.parseInt(idStr.trim());
					// Xóa ProductAttributeValue dựa trên productId và attributeId
					productsDao.deleteProductAttributeValueByProductIdAndAttributeId(product.getId(), attributeId,
							enma);
				} catch (Exception ex) {
					System.err.println("Lỗi khi xóa attribute ID: " + idStr);
					throw ex;
				}
			}
		}
	}

	@Override
	public ProductVariants findVariantBySKU(EntityManager enma, String sku, List<Integer> deletedIds) {
		try {
			return productsDao.findVariantBySKU(enma, sku, deletedIds);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<String> handleExistingVariants(HttpServletRequest req, EntityManager enma) {
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

				ProductVariants variant = productVariantsDao.findById(variantId, enma);
				if (variant != null) {
					String oldSKU = variant.getSKU(); // Lưu SKU cũ
					String newSKU = existingVariantSkus[i].trim();

					// Kiểm tra SKU trùng khi update (cho phép giữ nguyên SKU cũ)
					if (!newSKU.equals(oldSKU)) {
						// SKU đã thay đổi - thêm SKU cũ vào danh sách released
						releasedSkus.add(oldSKU);
						System.out.println(">>> SKU đã thay đổi: " + oldSKU + " -> " + newSKU);

						ProductVariants existingVariant = findVariantBySKU(enma, newSKU, deletedIds);
						if (existingVariant != null) {
							throw new DuplicateSkuException(newSKU);
						}
					}

					variant.setSKU(newSKU);
					variant.setPrice(new BigDecimal(existingVariantPrices[i]));
					variant.setStatus(Boolean.parseBoolean(existingVariantStatuses[i]));
					productVariantsDao.update(variant, enma);
					System.out.println("Đã cập nhật variant SKU: " + variant.getSKU());
				}
			}
		}

		return releasedSkus;
	}

	@Override
	public void handleNewVariants(Products product, HttpServletRequest req, EntityManager enma,
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
					ProductVariants existingVariant = findVariantBySKU(enma, sku, deletedIds);
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

				ProductVariants variant = new ProductVariants();
				variant.setProduct(product);
				variant.setSKU(sku);
				variant.setStatus(Boolean.parseBoolean(statuses[i]));

				if (prices[i] != null && !prices[i].trim().isEmpty()) {
					variant.setPrice(new BigDecimal(prices[i].trim()));
				} else {
					variant.setPrice(product.getBasePrice());
				}

				productVariantsDao.insert(variant, enma);
				System.out.println("Đã lưu variant mới SKU: " + variant.getSKU());

				// Lưu các option của biến thể
				if (!currentVariantOptions.isEmpty()) {
					for (int ovId : currentVariantOptions) {
						OptionValues ov = enma.find(OptionValues.class, ovId);
						if (ov != null) {
							OptionTypes ot = ov.getOptionType();

							VariantOptions vo = new VariantOptions();
							vo.setVariant(variant);
							vo.setOptionValue(ov);
							vo.setOptionType(ot);

							variantOptionsDao.insertByMerge(vo, enma);
							System.out.println("Đã lưu variant option: " + ot.getCode() + " = " + ov.getValue());
						}
					}
				}
			}
		}
	}

	@Override
	public void handleExistingAttributes(Products product, HttpServletRequest req, EntityManager enma) {
		String[] existingAttrIds = req.getParameterValues("existingAttributes.attributeId");
		String[] existingAttrValues = req.getParameterValues("existingAttributes.value");

		if (existingAttrIds != null) {
			for (int i = 0; i < existingAttrIds.length; i++) {
				int attrId = Integer.parseInt(existingAttrIds[i]);
				String value = existingAttrValues[i].trim();

				// Tìm ProductAttributeValues hiện có
				ProductAttributeValues pav = findProductAttributeValue(product.getId(), attrId, enma);
				Attributes attribute = attributesServiceImpl.findById(attrId);
				if (pav != null) {
					if (attribute.getDataType() == 2) {// Là number
						pav.setValueNumber(new BigDecimal(value));
						pav.setValueText(null);
					} else if (attribute.getDataType() == 3) {// Là boolean
						if (value.equals("1")) {
							pav.setValueNumber(new BigDecimal(1));
							pav.setValueText(null);
						} else {
							pav.setValueNumber(new BigDecimal(0));
							pav.setValueText(null);
							;
						}
					} else {
						pav.setValueText(value);
						pav.setValueNumber(null);
					}

					productAttributeValuesDao.update(pav, enma);
					System.out.println("Đã cập nhật thuộc tính ID: " + attrId);
				}
			}
		}
	}

	@Override
	public void handleNewAttributes(Products product, HttpServletRequest req, EntityManager enma) {
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
				Attributes attribute = enma.find(Attributes.class, attrId);
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

				if (attribute.getDataType() == 2) {// la number
					pav.setValueNumber(new BigDecimal(attrValue));
					pav.setValueText(null);
				} else if (attribute.getDataType() == 3) {// Là boolean
					if (attrValue.equals("1")) {
						pav.setValueNumber(new BigDecimal(1));
						pav.setValueText(null);
					} else {
						pav.setValueNumber(new BigDecimal(0));
						pav.setValueText(null);
						;
					}
				} else {
					pav.setValueText(attrValue);
					pav.setValueNumber(null);
				}

				// productAttributeValuesDao.insert(pav, enma);
				productAttributeValuesDao.insertByMerge(pav, enma);
				System.out.println("Đã lưu thuộc tính mới ID: " + attrId);
			}
		}
	}

	@Override
	public void handleImages(Products product, HttpServletRequest req, String uploadPath, EntityManager enma)
			throws IOException {
		// Xóa các ảnh đã bị remove
		String[] remainingImgs = req.getParameterValues("existingImages");
		List<String> remaining = (remainingImgs != null) ? Arrays.asList(remainingImgs) : new ArrayList<>();
		productImagesService.deleteRemovedImages(product.getId(), remaining, uploadPath);

		// Thêm ảnh mới từ temp
		handleImagesForNewProduct(product, req, uploadPath, enma);
	}

	/**
	 * Xây dựng đường dẫn thư mục cho sản phẩm theo cấu trúc: danh_mục_cha/danh_mục
	 * Nếu không có danh mục cha, trả về: danh_mục
	 */
	@Override
	public String buildProductFolderPath(Products product) {
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

	/* Chuẩn hóa chuỗi để sử dụng làm đường dẫn file/folder */
	@Override
	public String sanitizeForPath(String text) {
		if (text == null || text.isEmpty()) {
			return "unknown";
		}

		// Loại bỏ dấu tiếng Việt
		String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		String withoutAccents = pattern.matcher(normalized).replaceAll("");

		// Chuyển thành chữ thường và thay thế ký tự đặc biệt
		String result = withoutAccents.toLowerCase().replaceAll("đ", "d").replaceAll("[^a-z0-9]+", "-")
				.replaceAll("^-+|-+$", ""); // Loại bỏ - ở đầu và cuối

		return result.isEmpty() ? "unknown" : result;
	}

	/**
	 * Tạo tên file ảnh mới theo định dạng: tên_sản_phẩm_uuid.extension
	 */
	@Override
	public String generateProductImageFileName(Products product, String originalFileName) {
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

	@Override
	public void handleImagesForNewProduct(Products product, HttpServletRequest req, String uploadPath,
			EntityManager enma) throws IOException {
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
					productImagesDao.insert(img, enma);

					System.out.println("✓ File vật lý: " + productFolderPath + "/" + newFileName);
					System.out.println("✓ DB lưu: " + relativeImagePath);
				}
			}
		}
	}

	// Xóa các thuộc tính không thuộc category hiện tại (khi đổi category)
	@Override
	public void deleteAttributesNotInCategory(Products product, EntityManager enma) {
		try {
			// Lấy danh sách attribute IDs thuộc category hiện tại của product
			List<Integer> validAttributeIds = productsDao
					.getAttributeIdsByCurrentCategoryOfProducts(product.getCategory().getId(), enma);

			if (validAttributeIds.isEmpty()) {
				// Nếu category mới không có tất cả attributes cũ của category cũ, xóa tất cả
				// attributes cũ của product
				productsDao.deleteAllProductAttributeValuesByProductId(product.getId(), enma);
			} else {
				// Xóa các attributes không thuộc category mới
				productsDao.deleteProductAttributeValuesNotInNewCategoryOfProduct(product.getId(), validAttributeIds,
						enma);
			}
		} catch (Exception e) {
			System.err.println("Lỗi khi xóa attributes cũ: " + e.getMessage());
			// Không throw exception để không làm gián đoạn transaction
		}
	}

	@Override
	public ProductAttributeValues findProductAttributeValue(int productId, int attributeId, EntityManager enma) {
		try {
			return productsDao.findProductAttributeValueByProductIdAndAttributeId(productId, attributeId, enma);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void handleLoadAttributes(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html;charset=UTF-8");

		try {
			int categoryId = Integer.parseInt(req.getParameter("categoryId"));
			List<Attributes> attributes = this.findAttributesByCategoryId(categoryId);

			PrintWriter out = resp.getWriter();

			if (attributes.isEmpty()) {
				out.print("<p class='text-muted fst-italic'>Không có thuộc tính nào cho danh mục này.</p>");
				return;
			}

			for (Attributes attr : attributes) {
				out.print("<div class='mb-3'>");
				out.print("<label class='form-label fw-bold'>" + attr.getName()
						+ (attr.getUnit() != null && !attr.getUnit().isEmpty() ? " (" + attr.getUnit() + ")" : "")
						+ "</label>");
				// Hidden input để lưu attributeId
				out.print("<input type='hidden' name='attributeIds' value='" + attr.getId() + "'/>");

				switch (attr.getDataType()) {
				case 2:
					out.print(
							"<input type='number' name='attributeValues' class='form-control' placeholder='Nhập giá trị số' step='0.01'/>");
					break;
				case 3:
					out.print("<select name='attributeValues' class='form-select'>"
							+ "<option value=''>-- Chọn --</option>" + "<option value='true'>Có</option>"
							+ "<option value='false'>Không</option>" + "</select>");
					break;
				default:
					out.print(
							"<input type='text' name='attributeValues' class='form-control' placeholder='Nhập giá trị'/>");
					break;
				}
				out.print("</div>");
			}

		} catch (Exception e) {
			e.printStackTrace();
			resp.getWriter().print("<p class='text-danger'>Lỗi khi tải thuộc tính: " + e.getMessage() + "</p>");
		}
	}

	/**
	 * Xây dựng danh sách variants tạm thời từ request để giữ lại form
	 */
	@Override
	public List<ProductVariantDisplayModel> buildTempVariantList(HttpServletRequest req) {
		Map<Integer, ProductVariantDisplayModel> grouped = new LinkedHashMap<>();

		// Lấy variants mới
		String[] newSkus = req.getParameterValues("newVariants.sku");
		String[] newPrices = req.getParameterValues("newVariants.price");
		String[] newStatuses = req.getParameterValues("newVariants.status");
		String[] newOptionValueIds = req.getParameterValues("newVariants.optionValueIds[]");
		int virtualId = 0;

		if (newSkus != null) {
			int optionsPerVariant = (newOptionValueIds != null && newOptionValueIds.length > 0)
					? newOptionValueIds.length / newSkus.length
					: 0;
			int optionIndex = 0;

			for (int i = 0; i < newSkus.length; i++) {
				ProductVariantDisplayModel view = new ProductVariantDisplayModel();
				view.setSku(newSkus[i]);
				if (newPrices != null && i < newPrices.length) {
					try {
						view.setPrice(new BigDecimal(newPrices[i]));
					} catch (Exception e) {
						view.setPrice(BigDecimal.ZERO);
					}
				}
				if (newStatuses != null && i < newStatuses.length) {
					view.setStatus(Boolean.parseBoolean(newStatuses[i]));
				}

				// Build options cho variant mới
				List<String> options = new ArrayList<>();
				List<Integer> variantOptionValueIds = new ArrayList<>();

				for (int j = 0; j < optionsPerVariant; j++) {
					if (optionIndex < newOptionValueIds.length) {
						int optionValueId = Integer.parseInt(newOptionValueIds[optionIndex++]);
						variantOptionValueIds.add(optionValueId);

						// Load option value với OptionType để hiển thị
						OptionValues optionValue = optionValueService.findByIdFetchColumn(optionValueId, "optionType");
						if (optionValue != null && optionValue.getOptionType() != null) {
							options.add(optionValue.getOptionType().getCode() + ": " + optionValue.getValue());
						}
					}
				}

				view.setOptions(options);
				view.setOptionValueIds(variantOptionValueIds);

				grouped.put(virtualId--, view);
			}
		}

		// Lấy variants hiện có (nếu có)
		String[] existingIds = req.getParameterValues("existingVariants.id");
		String[] existingSkus = req.getParameterValues("existingVariants.sku");
		String[] existingPrices = req.getParameterValues("existingVariants.price");
		String[] existingStatuses = req.getParameterValues("existingVariants.status");

		// Lấy danh sách variants đã xóa để loại bỏ
		String deletedIdsParam = req.getParameter("deletedVariantIds");
		List<Integer> deletedIds = new ArrayList<>();
		if (deletedIdsParam != null && !deletedIdsParam.isEmpty()) {
			for (String id : deletedIdsParam.split(",")) {
				if (!id.trim().isEmpty()) {
					deletedIds.add(Integer.parseInt(id.trim()));
				}
			}
		}

		if (existingIds != null) {
			// Thay vì parse options từ request (có thể sai), load từ DB để đảm bảo đúng
			for (int i = 0; i < existingIds.length; i++) {
				int variantId = Integer.parseInt(existingIds[i]);

				// Bỏ qua variants đã được đánh dấu xóa
				if (deletedIds.contains(variantId)) {
					System.out.println("Skip variant ID " + variantId + " (đã xóa)");
					continue;
				}

				// Load variant details từ DB để lấy options chính xác
				List<ProductVariantDetailsModel> variantDetails = productsVariantsService
						.getVariantDetailsById(variantId);

				ProductVariantDisplayModel view = new ProductVariantDisplayModel();
				view.setId(variantId);

				// Cập nhật SKU, price, status từ form (user có thể đã sửa)
				if (existingSkus != null && i < existingSkus.length) {
					view.setSku(existingSkus[i]);
				}
				if (existingPrices != null && i < existingPrices.length) {
					try {
						view.setPrice(new BigDecimal(existingPrices[i]));
					} catch (Exception e) {
						view.setPrice(BigDecimal.ZERO);
					}
				}
				if (existingStatuses != null && i < existingStatuses.length) {
					view.setStatus(Boolean.parseBoolean(existingStatuses[i]));
				}

				// Load options từ DB (chính xác)
				List<String> options = new ArrayList<>();
				List<Integer> variantOptionValueIds = new ArrayList<>();

				for (ProductVariantDetailsModel detail : variantDetails) {
					variantOptionValueIds.add(detail.getOptionValueId());
					options.add(detail.getCode() + ": " + detail.getValue());
				}

				view.setOptions(options);
				view.setOptionValueIds(variantOptionValueIds);

				grouped.put(view.getId(), view);
			}
		}

		return new ArrayList<>(grouped.values());
	}

	/**
	 * Xây dựng danh sách attributes tạm thời từ request để giữ lại form
	 */
	@Override
	public List<ProductAttributeDisplayModel> buildTempAttributeList(HttpServletRequest req) {
		List<ProductAttributeDisplayModel> tempAttrs = new ArrayList<>();

		// Lấy attributes mới
		String[] attributeIds = req.getParameterValues("attributeIds");
		String[] attributeValues = req.getParameterValues("attributeValues");
		if (attributeIds != null) {
			for (int i = 0; i < attributeIds.length; i++) {
				ProductAttributeDisplayModel a = new ProductAttributeDisplayModel();
				a.setAttributeId(Integer.parseInt(attributeIds[i]));
				if (attributeValues != null && i < attributeValues.length) {
					a.setValueText(attributeValues[i]);
				}
				tempAttrs.add(a);
			}
		}

		// Lấy attributes hiện có (nếu có)
		String[] existingAttrIds = req.getParameterValues("existingAttributes.attributeId");
		String[] existingAttrValues = req.getParameterValues("existingAttributes.value");
		if (existingAttrIds != null) {
			for (int i = 0; i < existingAttrIds.length; i++) {
				ProductAttributeDisplayModel a = new ProductAttributeDisplayModel();
				a.setAttributeId(Integer.parseInt(existingAttrIds[i]));
				if (existingAttrValues != null && i < existingAttrValues.length) {
					a.setValueText(existingAttrValues[i]);
				}
				tempAttrs.add(a);
			}
		}

		return tempAttrs;
	}

	/**
	 * Lấy đường dẫn tuyệt đối đến thư mục src/main/webapp/uploads trong project
	 */
	@Override
	public String getSourceUploadPath(HttpServletRequest req) {
		// Lấy context path thực tế (deployed)
		String realPath = req.getServletContext().getRealPath("/uploads");
		System.out.println("[ProductController] Real path: " + realPath);

		if (realPath == null) {
			return null;
		}

		String sourcePath = null;

		// Xử lý đường dẫn Eclipse server:
		// .metadata\.plugins\org.eclipse.wst.server.core\tmp1\wtpwebapps\UTEShop\\uploads
		if (realPath.contains(".metadata")) {
			// Tìm workspace root (trước .metadata)
			int metadataIndex = realPath.indexOf(".metadata");
			String workspaceRoot = realPath.substring(0, metadataIndex);

			// Tìm tên project (sau wtpwebapps/)
			String projectName = "UTEShop"; // Hoặc extract từ path
			if (realPath.contains("wtpwebapps")) {
				int wtpIndex = realPath.indexOf("wtpwebapps") + "wtpwebapps".length() + 1;
				int uploadIndex = realPath.indexOf(java.io.File.separator + "uploads", wtpIndex);
				if (uploadIndex > wtpIndex) {
					projectName = realPath.substring(wtpIndex, uploadIndex);
				}
			}

			sourcePath = workspaceRoot + projectName + java.io.File.separator + "src" + java.io.File.separator + "main"
					+ java.io.File.separator + "webapp" + java.io.File.separator + "uploads";
		}
		// Xử lý đường dẫn Maven: target/UTEShop-1.0/uploads
		else if (realPath.contains("target")) {
			String projectRoot = realPath.substring(0, realPath.indexOf("target"));
			sourcePath = projectRoot + "src" + java.io.File.separator + "main" + java.io.File.separator + "webapp"
					+ java.io.File.separator + "uploads";
		}
		// Fallback: trả về realPath nếu không nhận diện được
		else {
			sourcePath = realPath;
		}

		System.out.println("[ProductController] Source path: " + sourcePath);
		return sourcePath;
	}

	@Override
	public void handleLoadOptionValues(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json; charset=UTF-8");

		String typeIdParam = req.getParameter("typeId");
		if (typeIdParam == null || typeIdParam.isEmpty()) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write("{\"error\":\"Missing typeId\"}");
			return;
		}

		try {
			int typeId = Integer.parseInt(typeIdParam);
			List<OptionValues> values = optionValueService.findByOptionTypeId(typeId);
			List<OptionValueDTO> dtoValues = values.stream().map(v -> new OptionValueDTO(v.getId(), v.getValue()))
					.toList();
			new Gson().toJson(dtoValues, resp.getWriter());
		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write("{\"error\":\"Server error\"}");
		}
	}

	/**
	 * Chuẩn bị dữ liệu để hiển thị lại form khi có lỗi validation
	 */
	@Override
	public void prepareFormDataForError(HttpServletRequest req, Products product, Categories category,
			boolean isUpdate) {
		List<Categories> categoryList = categoriesService.findAll();
		req.setAttribute("categoryList", categoryList);
		req.setAttribute("product", product);

		// Load lại attributes của category đã chọn và giữ lại giá trị đã nhập
		if (category != null) {
			List<Attributes> categoryAttributes = findAttributesByCategoryId(category.getId());
			List<ProductAttributeDisplayModel> tempAttrs = buildTempAttributeList(req);

			req.setAttribute("categoryAttributes", categoryAttributes);
			req.setAttribute("productAttributes", tempAttrs);
		}

		List<Attributes> availableAttributes = attributesService.findAll();
		req.setAttribute("availableAttributes", availableAttributes);
		List<OptionTypes> optionTypes = optionTypeService.findAll();
		req.setAttribute("availableOptionTypes", optionTypes);

		// Giữ lại variants đã nhập
		List<ProductVariantDisplayModel> tempVariants = buildTempVariantList(req);
		req.setAttribute("variantList", tempVariants);

		// Xử lý ảnh
		if (isUpdate && product.getId() != null) {
			// Khi update: load lại ảnh từ DB
			ProductsDetailModel productsDetailModel = new ProductsDetailModel();
			productsDetailModel.setProductImages(productImagesService.getImageById(product.getId()));
			req.setAttribute("productsDetailModel", productsDetailModel);
			System.out.println("Load lại " + productsDetailModel.getProductImages().size() + " ảnh từ DB");
		}
		
		// Giữ lại ảnh tạm (cho cả add và update)
		String[] tempImages = req.getParameterValues("tempImages");
		if (tempImages != null && tempImages.length > 0) {
			List<String> imageList = new ArrayList<>();
			for (String img : tempImages) {
				if (img != null && !img.trim().isEmpty()) {
					imageList.add(img);
				}
			}
			req.setAttribute("tempImages", imageList);
			System.out.println("Giữ lại " + imageList.size() + " ảnh tạm: " + imageList);
		}
		
		// Preserve deleted IDs để không mất khi reload form
		String deletedVariantIds = req.getParameter("deletedVariantIds");
		if (deletedVariantIds != null && !deletedVariantIds.isEmpty()) {
			req.setAttribute("deletedVariantIds", deletedVariantIds);
			System.out.println("Preserve deletedVariantIds: " + deletedVariantIds);
		}
		
		String deletedAttributeIds = req.getParameter("deletedAttributeIds");
		if (deletedAttributeIds != null && !deletedAttributeIds.isEmpty()) {
			req.setAttribute("deletedAttributeIds", deletedAttributeIds);
			System.out.println("Preserve deletedAttributeIds: " + deletedAttributeIds);
		}
	}
}
