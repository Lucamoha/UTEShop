package com.uteshop.controller.admin.Catalog;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.gson.Gson;
import com.uteshop.dto.admin.OptionValueDTO;
import com.uteshop.dto.admin.ProductAttributeDisplayModel;
import com.uteshop.dto.admin.ProductVariantDetailsModel;
import com.uteshop.dto.admin.ProductVariantDisplayModel;
import com.uteshop.dto.admin.ProductsDetailModel;
import com.uteshop.entity.branch.Branches;
import com.uteshop.entity.catalog.Attributes;
import com.uteshop.entity.catalog.Categories;
import com.uteshop.entity.catalog.OptionTypes;
import com.uteshop.entity.catalog.OptionValues;
import com.uteshop.entity.catalog.ProductAttributeValues;
import com.uteshop.entity.catalog.ProductImages;
import com.uteshop.entity.catalog.ProductVariants;
import com.uteshop.entity.catalog.Products;
import com.uteshop.entity.catalog.VariantOptions;
import com.uteshop.services.admin.IAttributesService;
import com.uteshop.services.admin.ICategoriesService;
import com.uteshop.services.admin.IOptionTypesService;
import com.uteshop.services.admin.IOptionValueService;
import com.uteshop.services.admin.IProductAttributesService;
import com.uteshop.services.admin.IProductAttributeValuesService;
import com.uteshop.services.admin.IProductImagesService;
import com.uteshop.services.admin.IProductsService;
import com.uteshop.services.admin.IProductsVariantsService;
import com.uteshop.services.admin.IVariantOptionsService;
import com.uteshop.services.impl.admin.AttributesServiceImpl;
import com.uteshop.services.impl.admin.CategoriesServiceImpl;
import com.uteshop.services.impl.admin.OptionTypesServiceImpl;
import com.uteshop.services.impl.admin.OptionValueServiceImpl;
import com.uteshop.services.impl.admin.ProductAttributesServiceImpl;
import com.uteshop.services.impl.admin.ProductAttributeValuesServiceImpl;
import com.uteshop.services.impl.admin.ProductImagesServiceImpl;
import com.uteshop.services.impl.admin.ProductVariantsServiceImpl;
import com.uteshop.services.impl.admin.ProductsServiceImpl;
import com.uteshop.services.impl.admin.VariantOptionsServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@WebServlet(urlPatterns = { "/admin/Catalog/Products/searchpaginated", "/admin/Catalog/Products/saveOrUpdate",
		"/admin/Catalog/Products/delete", "/admin/Catalog/Products/view", "/admin/Catalog/Products/image/saveOrUpdate",
		"/admin/Catalog/Products/image/delete", "/admin/Catalog/Products/loadAttributes",
		"/admin/Catalog/Products/loadOptionValues" })
@MultipartConfig
public class ProductController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private IProductsService productsService = new ProductsServiceImpl();
	private IProductsVariantsService productsVariantsService = new ProductVariantsServiceImpl();
	private IProductImagesService productImagesService = new ProductImagesServiceImpl();
	private ICategoriesService categoriesService = new CategoriesServiceImpl();
	private IOptionTypesService optionTypeService = new OptionTypesServiceImpl();
	private IOptionValueService optionValueService = new OptionValueServiceImpl();
	private IVariantOptionsService variantOptionsService = new VariantOptionsServiceImpl();
	private IAttributesService attributesService = new AttributesServiceImpl();
	private IProductAttributeValuesService productAttributeValuesService = new ProductAttributeValuesServiceImpl();
	private IProductAttributesService productAttributesService = new ProductAttributesServiceImpl();

	private void loadProductDetails(HttpServletRequest req, int id) {

		Products product = productsService.findById(id);

		ProductsDetailModel productsDetailModel = new ProductsDetailModel();
		productsDetailModel.setProductVariantsDetails(productsVariantsService.getVariantsByProductId(product.getId()));
		productsDetailModel.setProductImages(productImagesService.getImageById(product.getId()));
		req.setAttribute("product", product);

		Map<Integer, ProductVariantDisplayModel> grouped = new LinkedHashMap<>();

		for (ProductVariantDetailsModel d : productsDetailModel.getProductVariantsDetails()) {
			ProductVariantDisplayModel view = grouped.get(d.getId());
			if (view == null) {
				view = new ProductVariantDisplayModel();
				view.setId(d.getId());
				view.setSKU(d.getSKU());
				view.setPrice(d.getPrice());
				view.setStatus(d.getStatus());
				grouped.put(d.getId(), view);
			}
			// Ghép option dạng "TYPE: VALUE"
			view.getOptions().add(d.getCode() + ": " + d.getValue());
		}

		// Chuyển thành list để JSP dễ duyệt
		List<ProductVariantDisplayModel> displayList = new ArrayList<>(grouped.values());

		// Load thông số kỹ thuật
		List<ProductAttributeDisplayModel> attributes = productAttributesService
				.getAttributesByProductId(product.getId());

		List<String> imageNames = productsDetailModel.getProductImages().stream().map(ProductImages::getImageUrl)
				.toList();
		req.setAttribute("tempImages", imageNames);

		// Gửi sang JSP
		req.setAttribute("variantList", displayList);
		req.setAttribute("productsDetailModel", productsDetailModel);
		req.setAttribute("productAttributes", attributes);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String uri = req.getRequestURI();

		if (uri.contains("loadAttributes")) {
			handleLoadAttributes(req, resp);
			return;

		} else if (uri.contains("loadOptionValues")) {
			handleLoadOptionValues(req, resp);
			return;

		} else if (uri.contains("searchpaginated")) {

			int page = 1;
			int size = 6;

			if (req.getParameter("page") != null) {
				page = Integer.parseInt(req.getParameter("page"));
			}
			if (req.getParameter("size") != null) {
				size = Integer.parseInt(req.getParameter("size"));
			}

			String searchKeyword = req.getParameter("searchKeyword");
			if (searchKeyword != null) {
				searchKeyword = searchKeyword.trim();
			}

			// Tính offset (vị trí bắt đầu)
			int firstResult = (page - 1) * size;

			List<Products> productList = productsService.findAll(false, firstResult, size, searchKeyword, "Name");
			List<ProductsDetailModel> productsDetailModels = new ArrayList<>();

			// Đếm tổng số bản ghi để tính tổng trang
			int totalProducts = productsService.count(searchKeyword, "Name");
			int totalPages = (int) Math.ceil((double) totalProducts / size);

			for (Products product : productList) {
				ProductsDetailModel detail = new ProductsDetailModel();
				detail.setProduct(product);
				detail.setTotalVariants(productsVariantsService.countVariantsByProductId(product.getId()));
				productsDetailModels.add(detail);
			}
			req.setAttribute("productsDetailModels", productsDetailModels);
			req.setAttribute("currentPage", page);
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("size", size);
			req.setAttribute("searchKeyword", searchKeyword);

			req.getRequestDispatcher("/views/admin/Catalog/Products/searchpaginated.jsp").forward(req, resp);

		} else if (uri.contains("saveOrUpdate")) {
			String id = req.getParameter("id");
			List<Categories> categoryList = categoriesService.findAll();
			req.setAttribute("categoryList", categoryList);

			List<OptionTypes> optionTypes = optionTypeService.findAll();
			req.setAttribute("optionTypes", optionTypes);

			// nếu là sửa -> load thông tin sản phẩm hiện tại
			if (id != null && !id.isEmpty()) {
				loadProductDetails(req, Integer.parseInt(id));

				// Load thêm data cho form update
				List<Attributes> availableAttributes = attributesService.findAll();
				req.setAttribute("availableAttributes", availableAttributes);

				// Load option types với values để thêm biến thể mới
				List<OptionTypes> availableOptionTypes = optionTypeService.findAll();
				req.setAttribute("availableOptionTypes", availableOptionTypes);

				req.getRequestDispatcher("/views/admin/Catalog/Products/update.jsp").forward(req, resp);
			} else {
				req.getRequestDispatcher("/views/admin/Catalog/Products/add.jsp").forward(req, resp);
			}
		} else if (uri.contains("view")) {

			String id = req.getParameter("id");
			if (id != null) {
				loadProductDetails(req, Integer.parseInt(id));
			}
			req.getRequestDispatcher("/views/admin/Catalog/Products/view.jsp").forward(req, resp);

		} else if (uri.contains("delete")) {

			try {
				String idStr = req.getParameter("id");
				if (idStr == null || idStr.isBlank()) {
					req.getSession().setAttribute("errorMessage", "Thiếu ID sản phẩm cần xóa!");
					resp.sendRedirect(req.getContextPath() + "/admin/Catalog/Products/searchpaginated");
					return;
				}
				int id = Integer.parseInt(idStr);

				// Kiểm tra chi nhánh có tồn tại không
				Products product = productsService.findById(id);
				if (product == null) {
					req.getSession().setAttribute("errorMessage", "Sản phẩm không tồn tại hoặc đã bị xóa!");
					resp.sendRedirect(req.getContextPath() + "/admin/Catalog/Products/searchpaginated");
					return;
				}

				productsService.delete(id);
				req.getSession().setAttribute("message", "Đã xóa sản phẩm " + product.getName() + " thành công!");
			} catch (NumberFormatException e) {
				req.getSession().setAttribute("errorMessage", "ID sản phẩm không hợp lệ!");
			} catch (EntityNotFoundException e) {
				req.getSession().setAttribute("errorMessage", "Sản phẩm không tồn tại hoặc đã bị xóa!");
			} catch (PersistenceException e) {
				req.getSession().setAttribute("errorMessage",
						"Không thể xóa sản phẩm vì dữ liệu đang được sử dụng ở nơi khác.");
			} catch (Exception e) {
				e.printStackTrace();
				req.getSession().setAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn khi xóa sản phẩm!");
			}
			resp.sendRedirect(req.getContextPath() + "/admin/Catalog/Products/searchpaginated");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String uri = req.getRequestURI();

		if (uri.contains("saveOrUpdate")) {
			Products product = new Products();

			String idStr = req.getParameter("id");
			String name = req.getParameter("name");
			String slug = req.getParameter("slug");
			String description = req.getParameter("description");
			String basePriceStr = req.getParameter("basePrice");
			String status = req.getParameter("status");
			String categoryIdStr = req.getParameter("categoryId");

			// Nếu có id -> update
			Integer id = null;
			boolean isUpdate = false;
			if (idStr != null && !idStr.isEmpty()) {
				id = Integer.parseInt(idStr);
				product = productsService.findById(id);
				isUpdate = true;
			}

			// Gán giá trị tạm thời để giữ lại form nếu lỗi
			product.setName(name);
			product.setSlug(slug);
			product.setDescription(description);
			product.setStatus("true".equals(status));

			if (basePriceStr != null && !basePriceStr.isEmpty()) {
				try {
					product.setBasePrice(new BigDecimal(basePriceStr));
				} catch (NumberFormatException e) {
					req.setAttribute("error", "Giá tiền không hợp lệ!");
				}
			}

			Categories category = null;
			try {
				if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
					int categoryId = Integer.parseInt(categoryIdStr);
					category = categoriesService.findById(categoryId);
					product.setCategory(category);
				} else {
					req.setAttribute("error", "Vui lòng chọn danh mục!");
				}

			} catch (Exception e) {
				req.setAttribute("error", "Danh mục không hợp lệ!");
			}

			// Kiểm tra trùng tên
			Products existing = productsService.findByName(name);
			if (existing != null && !Objects.equals(existing.getId(), id)) {
				req.setAttribute("error", "Tên sản phẩm đã tồn tại! Vui lòng nhập tên khác!");

				List<Categories> categoryList = categoriesService.findAll();
				req.setAttribute("categoryList", categoryList);
				req.setAttribute("product", product);
				List<Attributes> availableAttributes = attributesService.findAll();
				req.setAttribute("availableAttributes", availableAttributes);
				List<OptionTypes> optionTypes = optionTypeService.findAll();
				req.setAttribute("availableOptionTypes", optionTypes);
				List<ProductVariants> tempVariants = new ArrayList<>();
				if (req.getParameterValues("newVariants.sku") != null) {
					String[] skusTmp = req.getParameterValues("newVariants.sku");
					String[] pricesTmp = req.getParameterValues("newVariants.price");
					String[] statusesTmp = req.getParameterValues("newVariants.status");

					for (int i = 0; i < skusTmp.length; i++) {
						ProductVariants pv = new ProductVariants();
						pv.setSKU(skusTmp[i]);
						pv.setPrice(new BigDecimal(pricesTmp[i]));
						pv.setStatus(Boolean.parseBoolean(statusesTmp[i]));
						tempVariants.add(pv);
					}
				}
				req.setAttribute("variantList", tempVariants);

				List<ProductAttributeDisplayModel> tempAttrs = new ArrayList<>();
				String[] attributeIdsTmp = req.getParameterValues("attributeIds");
				String[] attributeValuesTmp = req.getParameterValues("attributeValues");
				if (attributeIdsTmp != null) {
					for (int i = 0; i < attributeIdsTmp.length; i++) {
						ProductAttributeDisplayModel a = new ProductAttributeDisplayModel();
						a.setAttributeId(Integer.parseInt(attributeIdsTmp[i]));
						a.setValueText(attributeValuesTmp[i]);
						tempAttrs.add(a);
					}
				}
				req.setAttribute("productAttributes", tempAttrs);

				String[] tempImages = req.getParameterValues("tempImages");
				if (tempImages != null) {
					req.setAttribute("tempImages", Arrays.asList(tempImages));
				}

				// Forward lại form kèm dữ liệu
				req.getRequestDispatcher("/views/admin/Catalog/Products/add.jsp").forward(req, resp);
				return;
			}

			// Lưu sản phẩm vào db nếu không lỗi
			String message;
			if (isUpdate) {
				productsService.update(product);
				message = "Sản phẩm đã được sửa!";
			} else {
				productsService.insert(product);
				message = "Sản phẩm đã được thêm!";

				product = productsService.findById(product.getId());// lấy lại product để dùng về sau -> không có -> lỗi
																	// transient instance
			}

			if (isUpdate) {
			//XỬ LÝ CÁC BIẾN THỂ BỊ XÓA KHI SỬA - PHẢI XÓA TRƯỚC
			String deletedIdsParam = req.getParameter("deletedVariantIds");
			List<Integer> deletedIds = new ArrayList<>();
			if (deletedIdsParam != null && !deletedIdsParam.isEmpty()) {
			    String[] ids = deletedIdsParam.split(",");
			    System.out.println(ids);
			    for (String idSt : ids) {
			        try {
			            int idi = Integer.parseInt(idSt.trim());
			            deletedIds.add(idi);
			            productsVariantsService.delete(idi);
			            System.out.println("Đã xóa variant ID = " + idi);
			        } catch (Exception ex) {
			            System.err.println("Lỗi khi xóa variant ID = " + idSt);
			            ex.printStackTrace();
			        }
			    }
			}
			
			// Chỉ xóa khi có dữ liệu mới được gửi từ form
			String[] skus = req.getParameterValues("newVariants.sku");
			String[] attributeIdsCheck = req.getParameterValues("attributeIds");

			if (skus != null && skus.length > 0) {
				productsVariantsService.deleteAllByProductId(product.getId());
			}

			if (attributeIdsCheck != null && attributeIdsCheck.length > 0) {
				productAttributeValuesService.deleteByProductId(product.getId());
			}

			String uploadPath = req.getServletContext().getRealPath("/uploads");

			// Danh sách ảnh người dùng giữ lại
			String[] remainingImgs = req.getParameterValues("existingImages");
			List<String> remaining = (remainingImgs != null) ? Arrays.asList(remainingImgs) : new ArrayList<>();

			productImagesService.deleteRemovedImages(product.getId(), remaining, uploadPath);

			String[] existingVariantIds = req.getParameterValues("existingVariants.id");
			String[] existingVariantSkus = req.getParameterValues("existingVariants.sku");
			String[] existingVariantPrices = req.getParameterValues("existingVariants.price");
			String[] existingVariantStatuses = req.getParameterValues("existingVariants.status");

			if (existingVariantIds != null) {
				for (int i = 0; i < existingVariantIds.length; i++) {
					int variantId = Integer.parseInt(existingVariantIds[i]);
					
					// Bỏ qua nếu variant này đã bị xóa
					if (deletedIds.contains(variantId)) {
						System.out.println("Bỏ qua update variant ID = " + variantId + " (đã xóa)");
						continue;
					}
					
					ProductVariants variant = productsVariantsService.findById(variantId);

					if (variant != null) {
						variant.setSKU(existingVariantSkus[i]);
						variant.setPrice(new BigDecimal(existingVariantPrices[i]));
						variant.setStatus(Boolean.parseBoolean(existingVariantStatuses[i]));
						productsVariantsService.update(variant);
					}
				}
			}

			String[] existingAttrIds = req.getParameterValues("existingAttributes.attributeId");
			String[] existingAttrValues = req.getParameterValues("existingAttributes.value");

			if (existingAttrIds != null) {
				for (int i = 0; i < existingAttrIds.length; i++) {
					int attrId = Integer.parseInt(existingAttrIds[i]);
					String value = existingAttrValues[i];

					ProductAttributeValues pav = productAttributeValuesService
							.findByProductIdAndAttributeId(product.getId(), attrId);
					if (pav != null) {
						pav.setValueText(value);
						productAttributeValuesService.update(pav);
					}
				}
			}

		}

			// XỬ LÝ LƯU CÁC BIẾN THỂ
			String[] skus = req.getParameterValues("newVariants.sku");
			String[] prices = req.getParameterValues("newVariants.price");
			String[] statuses = req.getParameterValues("newVariants.status");
			String[] optionValueIds = req.getParameterValues("newVariants.optionValueIds[]");

			System.out.println("===> Số lượng SKUs: " + (skus != null ? skus.length : 0));
			System.out.println("===> Số lượng Prices: " + (prices != null ? prices.length : 0));
			System.out.println("===> Số lượng OptionValueIds: " + (optionValueIds != null ? optionValueIds.length : 0));

			if (skus != null && skus.length > 0) {
				// Kiểm tra tính hợp lệ của dữ liệu
				if (prices == null || prices.length != skus.length) {
					System.err.println("CẢNH BÁO: Số lượng price không khớp với số lượng SKU!");
				}
				if (statuses == null || statuses.length != skus.length) {
					System.err.println("CẢNH BÁO: Số lượng status không khớp với số lượng SKU!");
				}
				if (optionValueIds == null || optionValueIds.length == 0) {
					System.err.println("CẢNH BÁO: Không có optionValueIds nào được gửi!");
				}

				int optionsPerVariant = (optionValueIds != null && optionValueIds.length > 0)
						? optionValueIds.length / skus.length
						: 0;
				int optionIndex = 0;

				for (int i = 0; i < skus.length; i++) {
					try {
						// Tạo mới biến thể
						ProductVariants variant = new ProductVariants();
						variant.setProduct(product);
						variant.setSKU(skus[i].trim());
						variant.setStatus(Boolean.parseBoolean(statuses[i]));

						// Xử lý giá
						if (prices[i] != null && !prices[i].trim().isEmpty()) {
							variant.setPrice(new BigDecimal(prices[i].trim()));
						} else {
							variant.setPrice(product.getBasePrice()); // Dùng giá gốc nếu không có giá riêng
						}

						// Lưu biến thể vào DB để sinh Id
						productsVariantsService.insert(variant);

						// Lưu các option của biến thể
						if (optionValueIds != null && optionsPerVariant > 0) {
							for (int j = 0; j < optionsPerVariant; j++) {
								if (optionIndex < optionValueIds.length) {
									int optionValueId = Integer.parseInt(optionValueIds[optionIndex++]);

									OptionValues ov = optionValueService.findById(optionValueId);
									if (ov != null) {
										OptionTypes ot = optionTypeService.findById(ov.getOptionType().getId());

										VariantOptions vo = new VariantOptions();
										vo.setVariant(variant);
										vo.setOptionValue(ov);
										vo.setOptionType(ot);

										variantOptionsService.insert(vo);
									}
								}
							}
						}
					} catch (Exception ex) {
						System.err.println("Lỗi khi lưu biến thể thứ " + (i + 1) + ": " + ex.getMessage());
						ex.printStackTrace();
					}
				}
			} else {
				System.out.println("===> Không có biến thể nào được gửi từ form.");
			}

			// XỬ LÝ LƯU CÁC THÔNG SỐ KỸ THUẬT
			String[] attributeIds = req.getParameterValues("attributeIds");
			String[] attributeValues = req.getParameterValues("attributeValues");

			System.out.println("===> Số lượng AttributeIds: " + (attributeIds != null ? attributeIds.length : 0));
			System.out.println("===> Số lượng AttributeValues: " + (attributeValues != null ? attributeValues.length : 0));

			if (attributeIds != null) {
				for (int i = 0; i < attributeIds.length; i++) {
					System.out.println("    [" + i + "] attributeId=" + attributeIds[i] + ", value="
							+ (attributeValues != null && i < attributeValues.length ? attributeValues[i] : "NULL"));
				}
			}

			if (attributeIds != null && attributeIds.length > 0) {
				if (attributeValues == null || attributeValues.length != attributeIds.length) {
					System.err.println("CẢNH BÁO: Số lượng attributeValues không khớp với attributeIds!");
				}

				for (int i = 0; i < attributeIds.length; i++) {
					try {
						int attrId = Integer.parseInt(attributeIds[i]);
						String attrValue = (attributeValues != null && i < attributeValues.length) ? attributeValues[i]
								: "";

						// Bỏ qua nếu giá trị rỗng (thuộc tính không bắt buộc)
						if (attrValue == null || attrValue.trim().isEmpty()) {
							continue;
						}

						ProductAttributeValues pav = new ProductAttributeValues();
						pav.setProduct(product);
						pav.setAttribute(attributesService.findById(attrId));
						pav.setValueText(attrValue.trim());

						productAttributeValuesService.insert(pav);
						System.out.println(
								">>> Đã lưu thông số kỹ thuật: attributeId=" + attrId + ", value=" + attrValue);
					} catch (Exception ex) {
						System.err.println("Lỗi khi lưu thuộc tính thứ " + (i + 1) + ": " + ex.getMessage());
						ex.printStackTrace();
					}
				}
			} else {
				System.out.println("===> Không có thông số kỹ thuật nào được nhập.");
			}

			// Lưu hình ảnh
			// ================= XỬ LÝ ẢNH TẠM ==================
			try {
				System.out.println("--- BẮT ĐẦU XỬ LÝ ẢNH TẠM ---");
				String[] tempImages = req.getParameterValues("tempImages");
				System.out.println("Danh sách ảnh tạm: " + Arrays.toString(tempImages));

				if (tempImages != null && tempImages.length > 0) {
					Path tmpDir = Paths.get(req.getServletContext().getRealPath("/uploads/tmp"));
					Path uploadDir = Paths.get(req.getServletContext().getRealPath("/uploads"));
					if (!Files.exists(uploadDir))
						Files.createDirectories(uploadDir);

					for (String imgName : tempImages) {
						if (imgName == null || imgName.isBlank())
							continue; // bỏ qua nếu rỗng

						Path src = tmpDir.resolve(imgName);
						Path dest = uploadDir.resolve(imgName);

						// Đảm bảo không phải thư mục
						if (Files.isDirectory(src)) {
							System.out.println("Bỏ qua vì không phải file: " + src);
							continue;
						}

						if (Files.exists(src)) {
							Files.move(src, dest, StandardCopyOption.REPLACE_EXISTING);
							System.out.println("===> ĐÃ CHUYỂN FILE: " + imgName);

							// Lưu DB
							ProductImages img = new ProductImages();
							img.setImageUrl(imgName);
							img.setProduct(product);
							productImagesService.insert(img);
						} else {
							System.out.println("Ảnh tạm không tồn tại: " + imgName);
						}
					}

				} else {
					System.out.println("Không có ảnh tạm nào để lưu.");
				}

			} catch (Exception e) {
				e.printStackTrace();
				req.setAttribute("error", "Lỗi khi xử lý ảnh tạm.");
			}

			req.getSession().setAttribute("message", message);
			resp.sendRedirect(req.getContextPath() + "/admin/Catalog/Products/searchpaginated");
		}
	}

	private void handleLoadAttributes(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html;charset=UTF-8");

		try {
			int categoryId = Integer.parseInt(req.getParameter("categoryId"));
			List<Attributes> attributes = productsService.findAttributesByCategoryId(categoryId);

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

	private void handleLoadOptionValues(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
}
