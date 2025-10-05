package com.uteshop.filters;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

@WebFilter("/*")
public class LogFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//Cấm xóa
		
		/*
		 * HttpServletRequest req = (HttpServletRequest) request; HttpServletResponse
		 * resp = (HttpServletResponse) response; String uri = req.getRequestURI();
		 * 
		 * HttpSession session = req.getSession(false);//lay ko tao moi -> ko co tra ve
		 * null Users user = (session != null) ? (Users) session.getAttribute("account")
		 * : null;
		 */
		
		/*
		 * //Filter if(user != null) { if(uri.contains("/admin")) {
		 * if(user.getUserRole() != Constant.ADMIN) {
		 * resp.sendError(HttpServletResponse.SC_FORBIDDEN,
		 * "Khong co quyen truy cap admin"); return; } else{ chain.doFilter(request,
		 * response); } } else if(uri.contains("/employee")) { if(user.getUserRole() !=
		 * Constant.EMPLOYEE) { resp.sendError(HttpServletResponse.SC_FORBIDDEN,
		 * "Khong co quyen truy cap employee"); return; } else{ chain.doFilter(request,
		 * response); } } else { chain.doFilter(request, response); } } else {
		 * resp.sendRedirect(req.getContextPath() + "/login"); }
		 */
		chain.doFilter(request, response);
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("Filter init");
	}
	
	@Override
	public void destroy() {
		System.out.println("Filter destroy");
	}

}
