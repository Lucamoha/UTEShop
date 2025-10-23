package com.uteshop.controller.admin.Catalog;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.uteshop.entity.catalog.Attributes;
import com.uteshop.entity.catalog.Categories;
import com.uteshop.entity.catalog.OptionTypes;
import com.uteshop.entity.catalog.OptionValues;
import com.uteshop.entity.catalog.ProductImages;
import com.uteshop.entity.catalog.ProductVariants;
import com.uteshop.entity.catalog.Products;
import com.uteshop.services.admin.IAttributesService;
import com.uteshop.services.admin.ICategoriesService;
import com.uteshop.services.admin.IOptionTypesService;
import com.uteshop.services.admin.IOptionValueService;
import com.uteshop.services.admin.IProductAttributesService;
import com.uteshop.services.admin.IProductImagesService;
import com.uteshop.services.admin.IProductsService;
import com.uteshop.services.admin.IProductsVariantsService;
import com.uteshop.services.impl.admin.AttributesServiceImpl;
import com.uteshop.services.impl.admin.CategoriesServiceImpl;
import com.uteshop.services.impl.admin.OptionTypesServiceImpl;
import com.uteshop.services.impl.admin.OptionValueServiceImpl;
import com.uteshop.services.impl.admin.ProductAttributesServiceImpl;
import com.uteshop.services.impl.admin.ProductImagesServiceImpl;
import com.uteshop.services.impl.admin.ProductVariantsServiceImpl;
import com.uteshop.services.impl.admin.ProductsServiceImpl;
import com.uteshop.services.admin.ProductTransactionService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
	private IAttributesService attributesService = new AttributesServiceImpl();
	private IProductAttributesService productAttributesService = new ProductAttributesServiceImpl();
	
	// Service để xử lý transaction với rollback tự động
	private ProductTransactionService productTransactionService = new ProductTransactionService();

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
				view.setSku(d.getSKU());
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
			} catch (RuntimeException e) {
			    if ("FOREIGN_KEY_CONSTRAINT".equals(e.getMessage())) {
			        req.getSession().setAttribute("errorMessage",
			            "Không thể xóa sản phẩm vì dữ liệu đang được sử dụng ở nơi khác!");
			    } else {
			        e.printStackTrace();
			        req.getSession().setAttribute("errorMessage",
			            "Đã xảy ra lỗi không mong muốn khi xóa sản phẩm!");
			    }
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
				prepareFormDataForError(req, product, category, isUpdate);
				
				String forwardPage = isUpdate ? "/views/admin/Catalog/Products/update.jsp" 
						: "/views/admin/Catalog/Products/add.jsp";
				req.getRequestDispatcher(forwardPage).forward(req, resp);
				return;
			}
			
			// Kiểm tra trùng SKU trong các variants
			String duplicateSKU = checkDuplicateSKU(req, id);
			if (duplicateSKU != null) {
				req.setAttribute("error", "SKU '" + duplicateSKU + "' đã tồn tại! Vui lòng nhập SKU khác!");
				prepareFormDataForError(req, product, category, isUpdate);
				
				String forwardPage = isUpdate ? "/views/admin/Catalog/Products/update.jsp" 
						: "/views/admin/Catalog/Products/add.jsp";
				req.getRequestDispatcher(forwardPage).forward(req, resp);
				return;
			}

			// Sử dụng ProductTransactionService để xử lý transaction với rollback tự động
			try {
				String uploadPath = req.getServletContext().getRealPath("/uploads");
				
				// Gọi service để lưu sản phẩm với tất cả dữ liệu liên quan trong 1 transaction
				productTransactionService.saveOrUpdateProductWithTransaction(product, isUpdate, req, uploadPath);
				
				String message = isUpdate ? "Sản phẩm đã được sửa!" : "Sản phẩm đã được thêm!";
				req.getSession().setAttribute("message", message);
				resp.sendRedirect(req.getContextPath() + "/admin/Catalog/Products/searchpaginated");
				
			} catch (Exception e) {
				// Lỗi đã được rollback tự động trong service
				e.printStackTrace();
				req.getSession().setAttribute("errorMessage", 
					"Đã xảy ra lỗi khi lưu sản phẩm: " + e.getMessage());
				resp.sendRedirect(req.getContextPath() + "/admin/Catalog/Products/searchpaginated");
			}
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

	/**
	 * Chuẩn bị dữ liệu để hiển thị lại form khi có lỗi validation
	 */
	private void prepareFormDataForError(HttpServletRequest req, Products product, Categories category, boolean isUpdate) {
		List<Categories> categoryList = categoriesService.findAll();
		req.setAttribute("categoryList", categoryList);
		req.setAttribute("product", product);
		
		// Load lại attributes của category đã chọn và giữ lại giá trị đã nhập
		if (category != null) {
			List<Attributes> categoryAttributes = productsService.findAttributesByCategoryId(category.getId());
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

		// Giữ lại ảnh tạm
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
	}
	
	/**
	 * Kiểm tra trùng SKU trong các variants mới và hiện có
	 * @return SKU trùng nếu có, null nếu không trùng
	 */
	private String checkDuplicateSKU(HttpServletRequest req, Integer currentProductId) {
		// Kiểm tra SKU của các variants mới
		String[] newSkus = req.getParameterValues("newVariants.sku");
		if (newSkus != null) {
			for (String sku : newSkus) {
				if (sku != null && !sku.trim().isEmpty()) {
					ProductVariants existing = productsVariantsService.findBySKU(sku.trim());
					if (existing != null) {
						// Nếu đang update, chỉ báo lỗi nếu SKU thuộc sản phẩm khác
						if (currentProductId == null || 
							!Objects.equals(existing.getProduct().getId(), currentProductId)) {
							return sku.trim();
						}
					}
				}
			}
		}
		
		// Kiểm tra SKU của các variants hiện có (khi update)
		String[] existingSkus = req.getParameterValues("existingVariants.sku");
		String[] existingIds = req.getParameterValues("existingVariants.id");
		if (existingSkus != null && existingIds != null) {
			for (int i = 0; i < existingSkus.length; i++) {
				String sku = existingSkus[i];
				int variantId = Integer.parseInt(existingIds[i]);
				
				if (sku != null && !sku.trim().isEmpty()) {
					ProductVariants existing = productsVariantsService.findBySKU(sku.trim());
					if (existing != null && existing.getId() != variantId) {
						// SKU trùng với một variant khác
						return sku.trim();
					}
				}
			}
		}
		
		return null; // Không có SKU trùng
	}
	
	/**
	 * Xây dựng danh sách variants tạm thời từ request để giữ lại form
	 */
	private List<ProductVariantDisplayModel> buildTempVariantList(HttpServletRequest req) {
		List<ProductVariantDisplayModel> tempVariants = new ArrayList<>();
		
		// Lấy variants mới
		String[] newSkus = req.getParameterValues("newVariants.sku");
		String[] newPrices = req.getParameterValues("newVariants.price");
		String[] newStatuses = req.getParameterValues("newVariants.status");
		
		if (newSkus != null) {
			for (int i = 0; i < newSkus.length; i++) {
				ProductVariantDisplayModel pv = new ProductVariantDisplayModel();
				pv.setSku(newSkus[i]);
				if (newPrices != null && i < newPrices.length) {
					try {
						pv.setPrice(new BigDecimal(newPrices[i]));
					} catch (Exception e) {
						pv.setPrice(BigDecimal.ZERO);
					}
				}
				if (newStatuses != null && i < newStatuses.length) {
					pv.setStatus(Boolean.parseBoolean(newStatuses[i]));
				}
				tempVariants.add(pv);
			}
		}
		
		// Lấy variants hiện có (nếu có)
		String[] existingIds = req.getParameterValues("existingVariants.id");
		String[] existingSkus = req.getParameterValues("existingVariants.sku");
		String[] existingPrices = req.getParameterValues("existingVariants.price");
		String[] existingStatuses = req.getParameterValues("existingVariants.status");
		
		if (existingIds != null) {
			for (int i = 0; i < existingIds.length; i++) {
				ProductVariantDisplayModel pv = new ProductVariantDisplayModel();
				pv.setId(Integer.parseInt(existingIds[i]));
				if (existingSkus != null && i < existingSkus.length) {
					pv.setSku(existingSkus[i]);
				}
				if (existingPrices != null && i < existingPrices.length) {
					try {
						pv.setPrice(new BigDecimal(existingPrices[i]));
					} catch (Exception e) {
						pv.setPrice(BigDecimal.ZERO);
					}
				}
				if (existingStatuses != null && i < existingStatuses.length) {
					pv.setStatus(Boolean.parseBoolean(existingStatuses[i]));
				}
				tempVariants.add(pv);
			}
		}
		
		return tempVariants;
	}
	
	/**
	 * Xây dựng danh sách attributes tạm thời từ request để giữ lại form
	 */
	private List<ProductAttributeDisplayModel> buildTempAttributeList(HttpServletRequest req) {
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
