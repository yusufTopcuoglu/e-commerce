package com.dolap.product.exception;

import com.dolap.product.strings.ValidationMessages;

public class InvalidProductCountException extends RuntimeException {

	public InvalidProductCountException() {
		super(ValidationMessages.INVALID_PRODUCT_COUNT_EXCEPTION);
	}
}
