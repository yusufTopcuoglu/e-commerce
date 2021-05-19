package com.dolap.product.exception;

import com.dolap.product.strings.ValidationMessages;

public class NullProductAttributeException extends RuntimeException {
	public NullProductAttributeException() {
		super(ValidationMessages.NULL_PRODUCT_ATTRIBUTE_EXCEPTION);
	}
}
