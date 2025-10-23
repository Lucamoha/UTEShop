package com.uteshop.exception;

public class DuplicateSkuException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateSkuException(String sku) {
        super("SKU_EXISTS", "SKU '" + sku + "' đã tồn tại! Vui lòng nhập SKU khác.");
    }

}
