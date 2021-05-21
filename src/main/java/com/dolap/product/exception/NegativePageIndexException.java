package com.dolap.product.exception;

import com.dolap.product.strings.ValidationMessages;

public class NegativePageIndexException extends RuntimeException {

	public NegativePageIndexException() {
		super(ValidationMessages.NEGATIVE_PAGE_INDEX_EXCEPTION);
	}
}
