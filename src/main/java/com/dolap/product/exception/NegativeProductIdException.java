package com.dolap.product.exception;

import com.dolap.product.strings.ValidationMessages;

public class NegativeProductIdException extends RuntimeException{

	public NegativeProductIdException() {
		super(ValidationMessages.NEGATIVE_PRODUCT_ID_VALIDATION_MESSAGE);
	}
}
