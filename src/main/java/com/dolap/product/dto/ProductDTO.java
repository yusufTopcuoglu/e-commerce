package com.dolap.product.dto;

import com.dolap.product.enums.ProductCategory;
import com.dolap.product.model.Product;
import com.dolap.product.strings.ValidationMessages;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductDTO {
	@NotBlank(message = ValidationMessages.BLANK_NAME_VALIDATION_MESSAGE)
	private String name;

	@NotNull
	private ProductCategory category;

	@NotNull(message = ValidationMessages.NULL_PRICE_VALIDATION_MESSAGE)
	@Min(value = 0, message = ValidationMessages.NEGATIVE_PRICE_VALIDATION_MESSAGE)
	private Double price;

	@NotNull
	private String imageLink;

	public static ProductDTO fromProduct(Product product) {
		return ProductDTO.builder().name(product.getName()).category(product.getCategory()).price(product.getPrice())
				.imageLink(product.getImageLink()).build();
	}
}
