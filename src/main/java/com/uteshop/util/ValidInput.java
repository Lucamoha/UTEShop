package com.uteshop.util;

public class ValidInput {
	public static boolean isValidPhoneNumber(String phone) {
	    return phone != null && phone.matches("^\\d{1,15}$");
	}
}
