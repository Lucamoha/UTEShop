package com.uteshop.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public class ValidInput {
	public static boolean isValidPhoneNumber(String phone) {
	    return phone != null && phone.matches("^\\d{1,15}$");
	}
	
	public static boolean isNumeric(String str) {
		//true nếu toàn số
	    return str != null && str.matches("\\d+");
	}
	
	public static Integer safeParseInteger(String paramName, HttpServletRequest req) throws ServletException {
		String value = req.getParameter(paramName);
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		try {
			return Integer.valueOf(value.trim());
		} catch (NumberFormatException e) {
			throw new ServletException("Định dạng số không hợp lệ cho " + paramName + ": " + value);
		}
	}

	public static BigDecimal safeParseBigDecimal(String paramName, HttpServletRequest req) throws ServletException {
		String value = req.getParameter(paramName);
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		try {
			BigDecimal result = new BigDecimal(value.trim());
			if (result.compareTo(BigDecimal.ZERO) < 0) {
				throw new ServletException(paramName + " không được âm");
			}
			return result;
		} catch (NumberFormatException e) {
			throw new ServletException("Định dạng số thập phân không hợp lệ cho " + paramName + ": " + value);
		}
	}
	
	public static LocalDateTime safeParseDateTime(String paramName, HttpServletRequest req) throws ServletException {
        String value = req.getParameter(paramName);
        if (value == null || value.trim().isEmpty()) {
            throw new ServletException("Thiếu hoặc tham số không hợp lệ: " + paramName);
        }
        try {
            DateTimeFormatter fmtWithSec = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            return LocalDateTime.parse(value.trim(), fmtWithSec);
        } catch (Exception e1) {
            try {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                return LocalDateTime.parse(value.trim(), fmt);
            } catch (Exception e2) {
                throw new ServletException("Định dạng ngày giờ không hợp lệ cho " + paramName + ": " + value);
            }
        }
    }
}
