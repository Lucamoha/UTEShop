package com.uteshop.util;

public class ValidInput {
	public static boolean isValidPhoneNumber(String phone) {
	    return phone != null && phone.matches("^\\d{1,15}$");
	}
	
	public static boolean isNumeric(String str) {
		//true nếu toàn số
	    return str != null && str.matches("\\d+");
	}

}
