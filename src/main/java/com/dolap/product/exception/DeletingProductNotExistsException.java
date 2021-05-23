package com.dolap.product.exception;

import com.dolap.product.strings.ValidationMessages;

public class DeletingProductNotExistsException extends RuntimeException{

	public DeletingProductNotExistsException() {
		super(ValidationMessages.DELETING_PRODUCT_NOT_EXIST);
	}
}
