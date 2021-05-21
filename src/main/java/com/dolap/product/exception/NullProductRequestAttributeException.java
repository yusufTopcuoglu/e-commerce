package com.dolap.product.exception;

import com.dolap.product.strings.ValidationMessages;

public class NullProductRequestAttributeException extends RuntimeException {
	public NullProductRequestAttributeException() {
		super(ValidationMessages.NULL_PRODUCT_REQUEST_ATTRIBUTE_EXCEPTION);
	}
}
