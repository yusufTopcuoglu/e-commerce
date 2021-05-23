package com.dolap.product.request;

import com.dolap.product.enums.ProductCategory;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetProductOfCategoryRequest {

	@NotNull
	private ProductCategory productCategory;

	@Min(0)
	private int page;

	@Min(1)
	private int count;
}
