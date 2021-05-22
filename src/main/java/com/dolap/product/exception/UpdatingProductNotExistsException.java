package com.dolap.product.exception;

import com.dolap.product.strings.ValidationMessages;

public class UpdatingProductNotExistsException extends RuntimeException{

	public UpdatingProductNotExistsException() {
		super(ValidationMessages.UPDATING_PRODUCT_NOT_EXIST);
	}
}
