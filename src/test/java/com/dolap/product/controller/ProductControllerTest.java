package com.dolap.product.controller;

import com.dolap.product.dto.ErrorDTO;
import com.dolap.product.dto.ProductDTO;
import com.dolap.product.dto.ProductRequestDTO;
import com.dolap.product.enums.ProductCategory;
import com.dolap.product.model.Product;
import com.dolap.product.service.ProductService;
import com.dolap.product.strings.ValidationMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static com.dolap.product.TestUtils.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class ProductControllerTest {


	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ProductService productService;

	@Test
	public void createProductTest_Successful() throws Exception {
		Product product = Product.builder().id(1L).category(ProductCategory.HOME).price(5.0).name("test_name")
				.imageLink("test_link").build();
		ProductDTO productDTO = ProductDTO.fromProduct(product);

		String productJSON = objectToJson(product);
		String productDTOJSON = objectToJson(productDTO);

		when(productService.createAndSaveProduct(Mockito.any())).thenReturn(product);

		this.mockMvc
				.perform(post(CREATE_PRODUCT_URL).contentType(MediaType.APPLICATION_JSON_VALUE).content(productDTOJSON))
				.andExpect(status().isCreated()).andExpect(content().string(productJSON));
	}

	@Test
	public void createProductTest_NegativePrice_BadRequest() throws Exception {
		Product product = Product.builder().id(1L).category(ProductCategory.CLOTHE).price(-1.0).name("test_name")
				.imageLink("test_link").build();
		ProductDTO productDTO = ProductDTO.fromProduct(product);

		String productDTOJSON = objectToJson(productDTO);

		MvcResult mvcResult = this.mockMvc
				.perform(post(CREATE_PRODUCT_URL).contentType(MediaType.APPLICATION_JSON_VALUE).content(productDTOJSON))
				.andExpect(status().isBadRequest()).andReturn();

		String responseString = mvcResult.getResponse().getContentAsString();

		ErrorDTO errorDTO = objectFromJson(responseString, ErrorDTO.class);

		Assertions.assertTrue(errorDTO.getDetails().contains(ValidationMessages.NEGATIVE_PRICE_VALIDATION_MESSAGE));
	}

	@Test
	public void createProductTest_BlankName_BadRequest() throws Exception {
		Product product = Product.builder().id(1L).category(ProductCategory.CLOTHE).price(3.0).name("")
				.imageLink("test_link").build();

		String productJSON = objectToJson(product);

		MvcResult mvcResult = this.mockMvc
				.perform(post(CREATE_PRODUCT_URL).contentType(MediaType.APPLICATION_JSON_VALUE).content(productJSON))
				.andExpect(status().isBadRequest()).andReturn();

		String responseString = mvcResult.getResponse().getContentAsString();

		ErrorDTO errorDTO = objectFromJson(responseString, ErrorDTO.class);

		Assertions.assertTrue(errorDTO.getDetails().contains(ValidationMessages.BLANK_NAME_VALIDATION_MESSAGE));
	}

	@Test
	void getProductOfCategoryWithPageNumber_InvalidPageNumber_BadRequest() throws Exception {
		ProductCategory category = ProductCategory.CLOTHE;
		int page = -1;
		int count = 1;
		this.mockMvc.perform(getProductOfCategoryRequestBuilder(category, page, count))
				.andExpect(status().isBadRequest());
	}

	@Test
	void getProductOfCategoryWithPageNumber_InvalidProductCategory_BadRequest() throws Exception {
		String category = "NOT_VALID_PRODUCT_CATEGORY";
		int page = 5;
		int count = 1;
		this.mockMvc.perform(getProductOfCategoryRequestBuilder(category, page, count))
				.andExpect(status().isBadRequest());
	}

	@Test
	void getProductOfCategoryWithPageNumber_InvalidProductCount_BadRequest() throws Exception {
		ProductCategory category = ProductCategory.CLOTHE;
		int page = 5;
		int count = 0;
		this.mockMvc.perform(getProductOfCategoryRequestBuilder(category, page, count))
				.andExpect(status().isBadRequest());
	}

	@Test
	void getProductOfCategoryWithPageNumber_Successful() throws Exception {
		List<Product> productList = Arrays.asList(new Product(1L, "test_name", ProductCategory.HOME, 3.0, "tet_link"),
												  new Product(2L, "test_name", ProductCategory.HOME, 3.0, "tet_link"),
												  new Product(3L, "test_name", ProductCategory.HOME, 3.0, "tet_link"));
		when(productService.getProductOfCategoryWithPageNumber(Mockito.any(ProductRequestDTO.class)))
				.thenReturn(productList);
		String productsJson = objectToJson(productList);

		ProductCategory category = ProductCategory.CLOTHE;
		int page = 5;
		int count = 3;
		this.mockMvc.perform(getProductOfCategoryRequestBuilder(category, page, count))
				.andExpect(status().isOk()).andExpect(content().json(productsJson));
	}
}