package com.dolap.product.exception;

import com.dolap.product.strings.ValidationMessages;

public class BlankNameException extends RuntimeException{

	public BlankNameException() {
		super(ValidationMessages.BLANK_NAME_VALIDATION_MESSAGE);
	}
}
