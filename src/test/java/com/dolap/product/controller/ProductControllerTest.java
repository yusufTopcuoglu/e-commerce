package com.dolap.product.controller;

import com.dolap.product.dto.ErrorDTO;
import com.dolap.product.dto.ProductDTO;
import com.dolap.product.dto.ProductRequestDTO;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class ProductControllerTest {

	private static final String CREATE_PRODUCT_URL = "/product/create";
	private static final String REQUEST_PRODUCT_URL = "/product/{productCategory}/{page}/{count}";

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

		Gson gson = new Gson();
		ErrorDTO errorDTO = gson.fromJson(responseString, ErrorDTO.class);

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

		Gson gson = new Gson();
		ErrorDTO errorDTO = gson.fromJson(responseString, ErrorDTO.class);

		Assertions.assertTrue(errorDTO.getDetails().contains(ValidationMessages.BLANK_NAME_VALIDATION_MESSAGE));
	}

	@Test
	void getProductOfCategoryWithPageNumber_InvalidPageNumber_BadRequest() throws Exception {
		this.mockMvc.perform(get(REQUEST_PRODUCT_URL, ProductCategory.CLOTHE, -1, 1)).andExpect(status().isBadRequest());
	}

	@Test
	void getProductOfCategoryWithPageNumber_InvalidProductCategory_BadRequest() throws Exception {
		this.mockMvc.perform(get(REQUEST_PRODUCT_URL, "NOT_VALID_PRODUCT_CATEGORY", 5, 1))
				.andExpect(status().isBadRequest());
	}

	@Test
	void getProductOfCategoryWithPageNumber_InvalidProductCount_BadRequest() throws Exception {
		this.mockMvc.perform(get(REQUEST_PRODUCT_URL, ProductCategory.CLOTHE, 5, 0))
				.andExpect(status().isBadRequest());
	}

	@Test
	void getProductOfCategoryWithPageNumber() throws Exception {
		List<Product> productList = Arrays.asList(new Product(1L, "test_name", ProductCategory.HOME, 3.0, "tet_link"),
												  new Product(2L, "test_name", ProductCategory.HOME, 3.0, "tet_link"),
												  new Product(3L, "test_name", ProductCategory.HOME, 3.0, "tet_link"));
		List<ProductDTO> productDTOS = productList.stream().map(ProductDTO::fromProduct).collect(Collectors.toList());

		when(productService.getProductOfCategoryWithPageNumber(Mockito.any(ProductRequestDTO.class)))
				.thenReturn(productList);
		Gson gson = new Gson();
		String productDTOSJson = gson.toJson(productDTOS);
		this.mockMvc.perform(get(REQUEST_PRODUCT_URL, ProductCategory.CLOTHE, 5, 3)).andExpect(status().isOk())
				.andExpect(content().json(productDTOSJson));
	}

	private String objectToJson(Object product) {
		Gson gson = new Gson();
		return gson.toJson(product);
	}
}