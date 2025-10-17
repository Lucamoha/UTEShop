package com.uteshop.controller.admin.Branch;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.uteshop.entity.branch.Branches;
import com.uteshop.services.admin.IBranchesService;
import com.uteshop.services.admin.IUsersService;
import com.uteshop.services.impl.admin.BranchesServiceImpl;
import com.uteshop.services.impl.admin.UsersServiceImpl;

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

			List<Branches> branchList = branchesService.findAll(false, firstResult, size, searchKeyword,
					"Name");

			// Đếm tổng số bản ghi để tính tổng trang
			int totalBranches = branchesService.count(searchKeyword, "Name");
			int totalPages = (int) Math.ceil((double) totalBranches / size);

			req.setAttribute("branchList", branchList);
			req.setAttribute("currentPage", page);
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("size", size);
			req.setAttribute("searchKeyword", searchKeyword);

			req.getRequestDispatcher("/views/admin/Branch/Branches/searchpaginated.jsp").forward(req, resp);

		} else if (uri.contains("saveOrUpdate")) {
			String id = req.getParameter("id");
			if (id != null) {
				// dang o che do edit -> nguoc lai la add
				Branches branch = branchesService.findById(Integer.parseInt(id));
				req.setAttribute("branch", branch);
			}
			req.getRequestDispatcher("/views/admin/Branch/Branches/addOrEdit.jsp").forward(req, resp);
		} else if (uri.contains("view")) {
			String id = req.getParameter("id");
			Branches branch = branchesService.findById(Integer.parseInt(id));
			req.setAttribute("branch", branch);
			req.getRequestDispatcher("/views/admin/Branch/Branches/view.jsp").forward(req, resp);
		} else if (uri.contains("delete")) {
			try {
				int id = Integer.parseInt(req.getParameter("id"));
				branchesService.delete(id);
				req.getSession().setAttribute("message", "Đã xóa chi nhánh thành công!");
			} catch (Exception e) {
				req.getSession().setAttribute("errorMessage", "Không thể xóa vì dữ liệu đang được sử dụng ở nơi khác");
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
				branch = branchesService.findById(id);
			}

			// Gán giá trị tạm thời để giữ lại form nếu lỗi
			branch.setName(name);
			branch.setAddress(address);
			branch.setPhone(phone);
			branch.setIsActive(Boolean.parseBoolean(isActive));	
			if(managerId != null) {
				branch.setManager(usersService.findById(Integer.parseInt(managerId)));
			}

			// Kiểm tra code trùng
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
				branchesService.update(branch);
				message = "Sửa thông tin chi nhánh thành công!";
			} else {
				branchesService.insert(branch);
				message = "Thêm chi nhánh thành công!";
			}

			req.getSession().setAttribute("message", message);
			resp.sendRedirect(req.getContextPath() + "/admin/Branch/Branches/searchpaginated");
		}
	}
}
