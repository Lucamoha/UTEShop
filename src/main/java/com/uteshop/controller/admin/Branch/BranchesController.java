package com.uteshop.controller.admin.Branch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.uteshop.dto.admin.BranchDetailModel;
import com.uteshop.entity.branch.BranchInventory;
import com.uteshop.entity.branch.Branches;
import com.uteshop.services.admin.IBranchesService;
import com.uteshop.services.admin.IUsersService;
import com.uteshop.services.impl.admin.BranchesServiceImpl;
import com.uteshop.services.impl.admin.UsersServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/admin/Branch/Branches/searchpaginated", "/admin/Branch/Branches/saveOrUpdate",
		"/admin/Branch/Branches/delete", "/admin/Branch/Branches/view" })
public class BranchesController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	IBranchesService branchesService = new BranchesServiceImpl();
	IUsersService usersService = new UsersServiceImpl();

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

			List<Branches> branchList = branchesService.findAllFetch(false, firstResult, size, searchKeyword, "Name",
					"manager");

			// Đếm tổng số bản ghi để tính tổng trang
			int totalBranches = branchesService.count(searchKeyword, "Name");
			int totalPages = (int) Math.ceil((double) totalBranches / size);

			List<BranchDetailModel> branchDetailModels = new ArrayList<>();
			for (Branches branch : branchList) {
				// if (branch == null || branch.getId() == null) continue;
				BranchDetailModel detailModel = new BranchDetailModel();
				detailModel.setBranch(branch);
				detailModel.setBranchInventories(branchesService.findInventoryByBranchId(branch.getId()));
				detailModel.setTotalInventory(branchesService.countInventory(branch.getId()).intValue());
				branchDetailModels.add(detailModel);
			}

			req.setAttribute("branchDetailModels", branchDetailModels);
			req.setAttribute("currentPage", page);
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("size", size);
			req.setAttribute("searchKeyword", searchKeyword);

			req.getRequestDispatcher("/views/admin/Branch/Branches/searchpaginated.jsp").forward(req, resp);

		} else if (uri.contains("saveOrUpdate")) {
			String id = req.getParameter("id");
			Branches branch;

			if (id != null && !id.isBlank()) {
				// Chế độ edit
				branch = branchesService.findByIdFetchColumn(Integer.parseInt(id), "manager");
			} else {
				// Chế độ thêm mới
				branch = new Branches();
			}

			req.setAttribute("branch", branch);

			// Thêm danh sách tồn kho/biến thể
			List<BranchInventory> inventories;
			if (branch.getId() != null) {
				inventories = branchesService.findOrCreateInventoriesByBranchId(branch.getId());
			} else {
				inventories = branchesService.createEmptyInventoriesForAllVariants();
			}
			req.setAttribute("inventories", inventories);
			req.getRequestDispatcher("/views/admin/Branch/Branches/addOrEdit.jsp").forward(req, resp);

		} else if (uri.contains("view")) {
			String id = req.getParameter("id");
			Branches branch = branchesService.findByIdFetchColumn(Integer.parseInt(id), "manager");
			BranchDetailModel detailModel = new BranchDetailModel();
			detailModel.setBranch(branch);
			detailModel.setBranchInventories(branchesService.findInventoryByBranchId(branch.getId()));
			detailModel.setTotalInventory(branchesService.countInventory(branch.getId()).intValue());

			req.setAttribute("detailModel", detailModel);
			req.getRequestDispatcher("/views/admin/Branch/Branches/view.jsp").forward(req, resp);
		} else if (uri.contains("delete")) {
			try {
				String idStr = req.getParameter("id");
		        if (idStr == null || idStr.isBlank()) {
		            req.getSession().setAttribute("errorMessage", "Thiếu ID chi nhánh cần xóa!");
		            resp.sendRedirect(req.getContextPath() + "/admin/Branch/Branches/searchpaginated");
		            return;
		        }
		        int id = Integer.parseInt(idStr);
		        
		        //Kiểm tra chi nhánh có tồn tại không
		        Branches branch = branchesService.findByIdFetchColumn(id, "manager");
		        if (branch == null) {
		            req.getSession().setAttribute("errorMessage", "Chi nhánh không tồn tại hoặc đã bị xóa!");
		            resp.sendRedirect(req.getContextPath() + "/admin/Branch/Branches/searchpaginated");
		            return;
		        }
		        
		        //Kiểm tra có tồn kho hay không
		        int totalInventory = branchesService.countInventory(id).intValue();
		        if (totalInventory > 0) {
		            req.getSession().setAttribute("errorMessage", 
		                "Không thể xóa chi nhánh vì vẫn còn hàng tồn kho (" + totalInventory + " sản phẩm).");
		            resp.sendRedirect(req.getContextPath() + "/admin/Branch/Branches/searchpaginated");
		            return;
		        }
		        
				branchesService.delete(id);
				req.getSession().setAttribute("message", "Đã xóa chi nhánh " + branch.getName() + " thành công!");
			} catch (NumberFormatException e) {
		        req.getSession().setAttribute("errorMessage", "ID chi nhánh không hợp lệ!");
		    } catch (EntityNotFoundException e) {
		        req.getSession().setAttribute("errorMessage", "Chi nhánh không tồn tại hoặc đã bị xóa!");
		    } catch (PersistenceException e) {
		        // Xử lý lỗi foreign key
		        req.getSession().setAttribute("errorMessage", 
		            "Không thể xóa chi nhánh vì dữ liệu đang được sử dụng ở nơi khác.");
		    } catch (Exception e) {
		        e.printStackTrace();
		        req.getSession().setAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn khi xóa chi nhánh!");
		    }
			resp.sendRedirect(req.getContextPath() + "/admin/Branch/Branches/searchpaginated");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String uri = req.getRequestURI();

		if (uri.contains("saveOrUpdate")) {
			Branches branch = new Branches();

			String idStr = req.getParameter("id");
			String name = req.getParameter("name");
			String address = req.getParameter("address");
			String phone = req.getParameter("phone");
			String isActive = req.getParameter("isActive");
			String managerId = req.getParameter("managerId");

			// Nếu có id -> update
			Integer id = null;
			if (idStr != null && !idStr.isEmpty()) {
				id = Integer.parseInt(idStr);
				branch = branchesService.findByIdFetchColumn(id, "manager");
			}

			// Gán giá trị tạm thời để giữ lại form nếu lỗi
			branch.setName(name);
			branch.setAddress(address);
			branch.setPhone(phone);
			branch.setIsActive(Boolean.parseBoolean(isActive));
			if (managerId != null && !managerId.isBlank()) {
				branch.setManager(usersService.findById(Integer.parseInt(managerId)));
			} else {
				branch.setManager(null);
			}

			// Kiểm tra tên chi nhánh trùng
			Branches existing = branchesService.findByName(name);
			if (existing != null && !Objects.equals(existing.getId(), id)) {
				req.setAttribute("error", "Tên chi nhánh đã tồn tại! Vui lòng nhập tên khác!");
			}

			// foward lại form nếu lỗi
			if (req.getAttribute("error") != null) {
				req.setAttribute("branch", branch);
				req.getRequestDispatcher("/views/admin/Branch/Branches/addOrEdit.jsp").forward(req, resp);
				return;
			}

			// Lưu vào db nếu không lỗi
			String message;
			if (idStr != null && !idStr.isEmpty()) {
				branch.setId(id);
				branchesService.update(branch);
				message = "Sửa thông tin chi nhánh thành công!";
			} else {
				branchesService.insert(branch);
				message = "Thêm chi nhánh thành công!";
				// Lấy lại entity đã có ID
				branch = branchesService.findByName(name);
			}

			// Tạo tồn kho nếu chưa có
			List<BranchInventory> inventories = branchesService.findOrCreateInventoriesByBranchId(branch.getId());

			// Cập nhật số lượng tồn
			for (BranchInventory bi : inventories) {
				String paramName = "stock_" + bi.getVariant().getId();
				String stockStr = req.getParameter(paramName);
				if (stockStr != null && !stockStr.isBlank()) {
					int stock = Integer.parseInt(stockStr);
					if (stock > 0) {
						bi.setBranchStock(stock);

						// Gán lại branch và id nếu null khi thêm mới
						if (bi.getBranch() == null) {
							bi.setBranch(branch);
						}
						if (bi.getId() == null) {
							BranchInventory.Id bid = new BranchInventory.Id();
							bid.setBranchId(branch.getId());
							bid.setVariantId(bi.getVariant().getId());
							bi.setId(bid);
						}

						branchesService.update(bi);
					}
				}
			}

			req.getSession().setAttribute("message", message);
			resp.sendRedirect(req.getContextPath() + "/admin/Branch/Branches/searchpaginated");
		}
	}
}
