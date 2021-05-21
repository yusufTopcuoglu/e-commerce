package com.dolap.product.controller;

import com.dolap.product.dto.ErrorDTO;
import com.dolap.product.dto.ProductDTO;
import com.dolap.product.enums.ProductCategory;
import com.dolap.product.model.Product;
import com.dolap.product.service.ProductService;
import com.dolap.product.strings.ValidationMessages;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class ProductControllerTest {

	private static final String CREATE_PRODUCT_URL = "/product/create";
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ProductService productService;

	@Test
	public void createProductTest_Successful() throws Exception {
		Product product = Product.builder().category(ProductCategory.HOME).price(5.0).name("test_name")
				.imageLink("test_link").build();
		ProductDTO productDTO = ProductDTO.fromProduct(product);
		String productJSON = productToJson(productDTO);

		Mockito.when(productService.createAndSaveProduct(Mockito.any())).thenReturn(product);

		this.mockMvc
				.perform(post(CREATE_PRODUCT_URL).contentType(MediaType.APPLICATION_JSON_VALUE).content(productJSON))
				.andExpect(status().isCreated()).andExpect(content().string(productJSON));
	}

	@Test
	public void createProductTest_NegativePrice_BadRequest() throws Exception {
		ProductDTO productDTO = ProductDTO.builder().category(ProductCategory.CLOTHE).price(-1.0).name("test_name")
				.imageLink("test_link").build();

		String productJSON = productToJson(productDTO);

		MvcResult mvcResult = this.mockMvc
				.perform(post(CREATE_PRODUCT_URL).contentType(MediaType.APPLICATION_JSON_VALUE).content(productJSON))
				.andExpect(status().isBadRequest()).andReturn();

		String responseString = mvcResult.getResponse().getContentAsString();

		Gson gson = new Gson();
		ErrorDTO errorDTO = gson.fromJson(responseString, ErrorDTO.class);

		Assertions.assertTrue(errorDTO.getDetails().contains(ValidationMessages.NEGATIVE_PRICE_VALIDATION_MESSAGE));
	}

	@Test
	public void createProductTest_BlankName_BadRequest() throws Exception {
		ProductDTO productDTO = ProductDTO.builder().category(ProductCategory.CLOTHE).price(3.0).name("")
				.imageLink("test_link").build();

		String productJSON = productToJson(productDTO);

		MvcResult mvcResult = this.mockMvc
				.perform(post(CREATE_PRODUCT_URL).contentType(MediaType.APPLICATION_JSON_VALUE).content(productJSON))
				.andExpect(status().isBadRequest()).andReturn();

		String responseString = mvcResult.getResponse().getContentAsString();

		Gson gson = new Gson();
		ErrorDTO errorDTO = gson.fromJson(responseString, ErrorDTO.class);

		Assertions.assertTrue(errorDTO.getDetails().contains(ValidationMessages.BLANK_NAME_VALIDATION_MESSAGE));
	}

	private String productToJson(ProductDTO productDTO) {
		Gson gson = new Gson();
		return gson.toJson(productDTO);
	}
}