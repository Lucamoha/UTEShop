package com.uteshop.controller.admin.Catalog;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.uteshop.dto.admin.OptionValueDTO;
import com.uteshop.entity.catalog.OptionValues;
import com.uteshop.services.admin.IOptionValueService;
import com.uteshop.services.impl.admin.OptionValueServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//@WebServlet("/admin/Catalog/Products/loadOptionValues")
public class LoadOptionValuesServlet extends HttpServlet {

	/*
	 * private static final long serialVersionUID = 1L; private final
	 * IOptionValueService optionValueService = new OptionValueServiceImpl();
	 * 
	 * @Override protected void doGet(HttpServletRequest req, HttpServletResponse
	 * resp) throws ServletException, IOException {
	 * resp.setContentType("application/json; charset=UTF-8");
	 * 
	 * String typeIdParam = req.getParameter("typeId"); if (typeIdParam == null ||
	 * typeIdParam.isEmpty()) { resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	 * resp.getWriter().write("{\"error\":\"Missing typeId\"}"); return; }
	 * 
	 * try { int typeId = Integer.parseInt(typeIdParam); List<OptionValues> values =
	 * optionValueService.findByOptionTypeId(typeId); List<OptionValueDTO> dtoValues
	 * = values.stream() .map(v -> new OptionValueDTO(v.getId(), v.getValue()))
	 * .toList(); new Gson().toJson(dtoValues, resp.getWriter()); } catch (Exception
	 * e) { e.printStackTrace();
	 * resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	 * resp.getWriter().write("{\"error\":\"Server error\"}"); } }
	 */
}
