package com.uteshop.controller.admin.Catalog;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.uteshop.dto.admin.ProductAttributeDisplayModel;
import com.uteshop.dto.admin.ProductVariantDetailsModel;
import com.uteshop.dto.admin.ProductVariantDisplayModel;
import com.uteshop.dto.admin.ProductsDetailModel;
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
import com.uteshop.services.admin.IProductAttributeValuesService;
import com.uteshop.services.admin.IProductAttributesService;
import com.uteshop.services.admin.IProductImagesService;
import com.uteshop.services.admin.IProductsService;
import com.uteshop.services.admin.IProductsVariantsService;
import com.uteshop.services.admin.IVariantOptionsService;
import com.uteshop.services.impl.admin.AttributesServiceImpl;
import com.uteshop.services.impl.admin.CategoriesServiceImpl;
import com.uteshop.services.impl.admin.OptionTypesServiceImpl;
import com.uteshop.services.impl.admin.OptionValueServiceImpl;
import com.uteshop.services.impl.admin.ProductAttributeValuesServiceImpl;
import com.uteshop.services.impl.admin.ProductAttributesServiceImpl;
import com.uteshop.services.impl.admin.ProductImagesServiceImpl;
import com.uteshop.services.impl.admin.ProductVariantsServiceImpl;
import com.uteshop.services.impl.admin.ProductsServiceImpl;
import com.uteshop.services.impl.admin.VariantOptionsServiceImpl;

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
		"/admin/Catalog/Products/image/delete" })
@MultipartConfig
public class ProductController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private IProductsService productsService = new ProductsServiceImpl();
	private IProductsVariantsService productsVariantsService = new ProductVariantsServiceImpl();
	private IProductImagesService productImagesService = new ProductImagesServiceImpl();
	private IProductAttributesService productAttributesService = new ProductAttributesServiceImpl();
	private ICategoriesService categoriesService = new CategoriesServiceImpl();
	private IOptionTypesService optionTypeService = new OptionTypesServiceImpl();
	private IOptionValueService optionValueService = new OptionValueServiceImpl();
	private IVariantOptionsService variantOptionsService = new VariantOptionsServiceImpl();
	private IAttributesService attributesService = new AttributesServiceImpl();
	private IProductAttributeValuesService productAttributeValuesService = new ProductAttributeValuesServiceImpl();
	// private IProductAttributeValuesService productAttributeValuesService = new
	// ProductAttributeValuesServiceImpl();

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

		if (uri.contains("searchpaginated")) {

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

			List<Attributes> availableAttributes = attributesService.findAll();
			req.setAttribute("availableAttributes", availableAttributes);

			List<OptionTypes> optionTypes = optionTypeService.findAll();
			req.setAttribute("availableOptionTypes", optionTypes);

			// nếu là sửa -> load thông tin sản phẩm hiện tại
			if (id != null && !id.isEmpty()) {
				loadProductDetails(req, Integer.parseInt(id));
				req.getRequestDispatcher("/views/admin/Catalog/Products/update.jsp").forward(req, resp);
			}
			else {
				req.getRequestDispatcher("/views/admin/Catalog/Products/addOrEdit.jsp").forward(req, resp);
			}
		} else if (uri.contains("view")) {

			String id = req.getParameter("id");
			if (id != null) {
				loadProductDetails(req, Integer.parseInt(id));
			}
			req.getRequestDispatcher("/views/admin/Catalog/Products/view.jsp").forward(req, resp);
		} else if (uri.contains("delete")) {
			String id = req.getParameter("id");
			productsService.delete(Integer.parseInt(id));
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

				// Nạp lại dữ liệu vào form
				List<Categories> categoryList = categoriesService.findAll();
				req.setAttribute("categoryList", categoryList);
				req.setAttribute("product", product);

				// Giữ lại các thông số người dùng đã nhập
				List<Attributes> availableAttributes = attributesService.findAll();
				req.setAttribute("availableAttributes", availableAttributes);
				List<OptionTypes> optionTypes = optionTypeService.findAll();
				req.setAttribute("availableOptionTypes", optionTypes);

				// Nạp lại dữ liệu người dùng đã nhập tạm (chưa lưu DB)
				// Biến thể
				List<ProductVariants> tempVariants = new ArrayList<>();
				if (req.getParameterValues("newVariants[].sku") != null) {
					String[] skusTmp = req.getParameterValues("newVariants[].sku");
					String[] pricesTmp = req.getParameterValues("newVariants[].price");
					String[] statusesTmp = req.getParameterValues("newVariants[].status");

					for (int i = 0; i < skusTmp.length; i++) {
						ProductVariants pv = new ProductVariants();
						pv.setSKU(skusTmp[i]);
						pv.setPrice(new BigDecimal(pricesTmp[i]));
						pv.setStatus(Boolean.parseBoolean(statusesTmp[i]));
						tempVariants.add(pv);
					}
				}
				req.setAttribute("variantList", tempVariants);

				// Thông số kỹ thuật
				List<ProductAttributeDisplayModel> tempAttrs = new ArrayList<>();
				String[] attributeIdsTmp = req.getParameterValues("newAttributes[].attributeId");
				String[] attributeValuesTmp = req.getParameterValues("newAttributes[].value");
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

				// Forward lại form mà không mất dữ liệu
				req.getRequestDispatcher("/views/admin/Catalog/Products/addOrEdit.jsp").forward(req, resp);
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

				System.out.println("===> Product ID sau khi insert: " + product.getId());

				product = productsService.findById(product.getId());// lấy lại product để dùng về sau -> không có -> lỗi
																	// // transient instance
			}

			if (isUpdate) {
				// Chỉ xóa khi có dữ liệu mới được gửi từ form
				String[] skus = req.getParameterValues("newVariants.sku");
				String[] attributeIds = req.getParameterValues("newAttributes[].attributeId");

				if (skus != null && skus.length > 0) {
					productsVariantsService.deleteAllByProductId(product.getId());
				}

				if (attributeIds != null && attributeIds.length > 0) {
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

				        ProductAttributeValues pav = productAttributeValuesService.findByProductIdAndAttributeId(product.getId(), attrId);
				        if (pav != null) {
				            pav.setValueText(value);
				            productAttributeValuesService.update(pav);
				        }
				    }
				}

			}

			// XỬ LÝ LƯU CÁC BIẾN THỂ (ProductVariants)
			String[] skus = req.getParameterValues("newVariants.sku");
			String[] prices = req.getParameterValues("newVariants.price");
			String[] statuses = req.getParameterValues("newVariants.status");
			String[] optionValueIds = req.getParameterValues("newVariants.optionValueIds[]");

			if (skus != null && skus.length > 0) {
				int optionIndex = 0; // dùng để duyệt các optionValueIds
				for (int i = 0; i < skus.length; i++) {

					// 1. Tạo mới biến thể
					ProductVariants variant = new ProductVariants();
					variant.setProduct(product); // liên kết với sản phẩm đã insert
					variant.setSKU(skus[i]);
					variant.setPrice(new BigDecimal(prices[i]));
					variant.setStatus(Boolean.parseBoolean(statuses[i]));

					if (prices[i] != null && !prices[i].isEmpty()) {
						variant.setPrice(new BigDecimal(prices[i]));
					} else {
						variant.setPrice(BigDecimal.ZERO);
					}
					System.out.println("-> variant.price = " + variant.getPrice());

					// Lưu biến thể vào DB để sinh Id
					productsVariantsService.insert(variant);
					System.out.println(">>> Đã lưu biến thể: " + variant.getSKU() + " (ID = " + variant.getId() + ")");

					int optionsPerVariant = optionValueIds.length / skus.length;
					for (int j = 0; j < optionsPerVariant; j++) {
						int optionValueId = Integer.parseInt(optionValueIds[optionIndex++]);

						OptionValues ov = optionValueService.findById(optionValueId);
						OptionTypes ot = optionTypeService.findById(ov.getOptionType().getId());

						VariantOptions vo = new VariantOptions();
						vo.setVariant(variant);
						vo.setOptionValue(ov);
						vo.setOptionType(ot);

						variantOptionsService.insert(vo);
					}
				}
			} else {
				System.out.println("===> Không có biến thể nào được gửi từ form.");
			}

			// XỬ LÝ LƯU CÁC THÔNG SỐ KỸ THUẬT
			String[] attributeIds = req.getParameterValues("newAttributes[].attributeId");
			String[] attributeValues = req.getParameterValues("newAttributes[].value");

			if (attributeIds != null && attributeIds.length > 0) {
				for (int i = 0; i < attributeIds.length; i++) {
					try {
						int attrId = Integer.parseInt(attributeIds[i]);
						String attrValue = attributeValues[i];

						ProductAttributeValues pav = new ProductAttributeValues();
						pav.setProduct(product);
						pav.setAttribute(attributesService.findById(attrId));
						pav.setValueText(attrValue);

						productAttributeValuesService.insert(pav);
						System.out.println(
								">>> Đã lưu thông số kỹ thuật: attributeId=" + attrId + ", value=" + attrValue);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			} else {
				System.out.println("===> Không có thông số kỹ thuật nào được nhập.");
			}

			// Lưu hình ảnh
			// ================== XỬ LÝ ẢNH TẠM ==================
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
}
