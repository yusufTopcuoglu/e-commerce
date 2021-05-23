package com.dolap.product.request;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.dolap.product.strings.ValidationMessages.NEGATIVE_PRODUCT_ID_VALIDATION_MESSAGE;
import static com.dolap.product.strings.ValidationMessages.NULL_PRODUCT_ATTRIBUTE_EXCEPTION;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DeleteProductRequest {
	@NotNull(message = NULL_PRODUCT_ATTRIBUTE_EXCEPTION)
	@Min(value = 0, message = NEGATIVE_PRODUCT_ID_VALIDATION_MESSAGE)
	private Long productId;

}
