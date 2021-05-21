package com.dolap.product.model;

import com.dolap.product.enums.ProductCategory;
import com.dolap.product.strings.ValidationMessages;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class Product {
	@Id
	@GeneratedValue
	private Long id;

	@Column
	@NotBlank(message = ValidationMessages.BLANK_NAME_VALIDATION_MESSAGE)
	private String name;

	@Column
	@NotNull
	@Enumerated(EnumType.STRING)
	private ProductCategory category;

	@Column
	@NotNull(message = ValidationMessages.NULL_PRICE_VALIDATION_MESSAGE)
	@Min(value = 0, message = ValidationMessages.NEGATIVE_PRICE_VALIDATION_MESSAGE)
	private Double price;

	@Column
	@NotNull
	private String imageLink;
}
