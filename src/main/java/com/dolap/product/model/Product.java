package com.dolap.product.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
	@NotBlank(message = "Name is mandatory for a product")
	private String name;

	@Column
	@NotNull
	private String category;

	@Column
	@NotNull
	@Min(value = 0, message = "price must be a non negative value")
	private Double price;

	@Column
	@NotNull
	private String imageLink;
}
