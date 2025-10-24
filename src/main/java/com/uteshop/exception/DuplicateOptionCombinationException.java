package com.uteshop.exception;

public class DuplicateOptionCombinationException extends BusinessException {

	private static final long serialVersionUID = 1L;

	public DuplicateOptionCombinationException(String optionDetails) {
        super("OPTIONS_EXISTS", "Tổ hợp tùy chọn '" + optionDetails + "' đã tồn tại! Vui lòng chọn tổ hợp khác.");
    }

}
