package com.dolap.product.exception;

import com.dolap.product.strings.ValidationMessages;

public class NegativePriceException extends RuntimeException{

	public NegativePriceException() {
		super(ValidationMessages.NEGATIVE_PRICE_VALIDATION_MESSAGE);
	}
}
