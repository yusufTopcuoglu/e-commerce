package com.dolap.product.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class ProductDTO {
	@NotBlank(message = "Name is mandatory for a product")
	private String name;

	@NotNull
	private String category;

	@NotNull(message = "price is mandatory for a product")
	@Min(value = 0, message = "price must be a non negative value")
	private Double price;

	@NotNull
	private String imageLink;
}
