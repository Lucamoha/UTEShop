package com.uteshop.controller.admin.Catalog;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.uteshop.entity.catalog.Products;
import com.uteshop.exception.DuplicateSkuException;
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

	IProductsService productsService = new ProductsServiceImpl();
	IProductsVariantsService productsVariantsService = new ProductVariantsServiceImpl();
	IProductImagesService productImagesService = new ProductImagesServiceImpl();
	ICategoriesService categoriesService = new CategoriesServiceImpl();
	IOptionTypesService optionTypeService = new OptionTypesServiceImpl();
	IOptionValueService optionValueService = new OptionValueServiceImpl();
	IAttributesService attributesService = new AttributesServiceImpl();
	IProductAttributesService productAttributesService = new ProductAttributesServiceImpl();

	private void loadProductDetails(HttpServletRequest req, int id) {

		Products product = productsService.findById(id);

		ProductsDetailModel productsDetailModel = new ProductsDetailModel();
		productsDetailModel.setProductVariantsDetails(productsVariantsService.getVariantsByProductId(product.getId()));
		productsDetailModel.setProductImages(productImagesService.getImageById(product.getId()));
		req.setAttribute("product", product);

		Map<Integer, ProductVariantDisplayModel> grouped = new LinkedHashMap<>();
		//List<Integer> optionTypeIds = new ArrayList<>();
		

		for (ProductVariantDetailsModel d : productsDetailModel.getProductVariantsDetails()) {
			ProductVariantDisplayModel view = grouped.get(d.getId());
			List<Integer> optionValueIds = new ArrayList<>();
			if (view == null) {
				view = new ProductVariantDisplayModel();
				view.setId(d.getId());
				view.setSku(d.getSKU());
				view.setPrice(d.getPrice());
				view.setStatus(d.getStatus());
				for (ProductVariantDetailsModel pvdm : productsDetailModel.getProductVariantsDetails()) {
					if(pvdm.getId().equals(view.getId())) {
						optionValueIds.add(pvdm.getOptionValueId());
					}
				}
				view.setOptionValueIds(optionValueIds);
				grouped.put(d.getId(), view);

			}
			// Ghép option dạng "TYPE: VALUE"
			view.getOptions().add(d.getCode() + ": " + d.getValue());
		}

		// Chuyển thành list để JSP dễ duyệt
		List<ProductVariantDisplayModel> displayList = new ArrayList<>(grouped.values());

		List<ProductAttributeDisplayModel> attributes = productAttributesService
				.getAttributesByProductId(product.getId());

		req.setAttribute("variantList", displayList);
		req.setAttribute("productsDetailModel", productsDetailModel);
		req.setAttribute("productAttributes", attributes);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String uri = req.getRequestURI();

		if (uri.contains("loadAttributes")) {
			productsService.handleLoadAttributes(req, resp);
			return;

		} else if (uri.contains("loadOptionValues")) {
			productsService.handleLoadOptionValues(req, resp);
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

				// Kiểm tra sản phẩm có tồn tại không
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
					req.getSession().setAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn khi xóa sản phẩm!");
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
				productsService.prepareFormDataForError(req, product, category, isUpdate);

				String forwardPage = isUpdate ? "/views/admin/Catalog/Products/update.jsp"
						: "/views/admin/Catalog/Products/add.jsp";
				req.getRequestDispatcher(forwardPage).forward(req, resp);
				return;
			}

			// Sử dụng ProductTransactionService để xử lý transaction với rollback tự động
			try {
				// Lấy đường dẫn tuyệt đối đến thư mục src/main/webapp/uploads trong project
				String uploadPath = productsService.getSourceUploadPath(req);

				// Gọi service để lưu sản phẩm với tất cả dữ liệu liên quan trong 1 transaction
				productsService.saveOrUpdateProductWithTransaction(product, isUpdate, req, uploadPath);

				String message = isUpdate ? "Sản phẩm đã được sửa!" : "Sản phẩm đã được thêm!";
				req.getSession().setAttribute("message", message);
				resp.sendRedirect(req.getContextPath() + "/admin/Catalog/Products/searchpaginated");

			} catch (DuplicateSkuException e) {
				e.printStackTrace();	
				req.setAttribute("error", e.getMessage());
				productsService.prepareFormDataForError(req, product, category, isUpdate);

				String forwardPage = isUpdate ? "/views/admin/Catalog/Products/update.jsp"
						: "/views/admin/Catalog/Products/add.jsp";
				req.getRequestDispatcher(forwardPage).forward(req, resp);
			} catch (Exception e) {
				// Lỗi đã được rollback tự động trong service
				e.printStackTrace();
				req.setAttribute("error", "Đã xảy ra lỗi khi lưu sản phẩm: " + e.getMessage());
				productsService.prepareFormDataForError(req, product, category, isUpdate);
				String forwardPage = isUpdate ? "/views/admin/Catalog/Products/update.jsp"
						: "/views/admin/Catalog/Products/add.jsp";
				req.getRequestDispatcher(forwardPage).forward(req, resp);
			}
		}
	}
}
