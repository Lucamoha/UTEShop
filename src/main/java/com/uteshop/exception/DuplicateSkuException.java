package com.uteshop.exception;

public class DuplicateSkuException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateSkuException(String sku) {
        super("SKU_EXISTS", "Tổ hợp tùy chọn có SKU " + sku + " đã tồn tại! Vui lòng xóa và chọn tổ hợp khác!");
    }

}
