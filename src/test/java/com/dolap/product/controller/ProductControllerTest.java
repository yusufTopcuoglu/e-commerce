package com.dolap.product.controller;

import com.dolap.product.enums.ProductCategory;
import com.dolap.product.exception.UpdatingProductNotExistsException;
import com.dolap.product.model.Product;
import com.dolap.product.reponse.DeleteResponse;
import com.dolap.product.reponse.ErrorResponse;
import com.dolap.product.request.CreateProductRequest;
import com.dolap.product.request.DeleteProductRequest;
import com.dolap.product.request.GetProductOfCategoryRequest;
import com.dolap.product.service.ProductService;
import com.dolap.product.strings.ValidationMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static com.dolap.product.TestUtils.*;
import static com.dolap.product.strings.Constants.PRODUCT_DELETED_STATEMENT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
		CreateProductRequest createProductRequest = createProductRequestFromProduct(product);

		String productJSON = objectToJson(product);
		String createProductRequestJSON = objectToJson(createProductRequest);

		when(productService.createAndSaveProduct(any())).thenReturn(product);

		this.mockMvc.perform(post(CREATE_PRODUCT_URL).contentType(MediaType.APPLICATION_JSON_VALUE)
									 .content(createProductRequestJSON)).andExpect(status().isCreated())
				.andExpect(content().string(productJSON));
	}

	@Test
	public void createProductTest_NegativePrice_PreconditionFailed() throws Exception {
		Product product = Product.builder().id(1L).category(ProductCategory.CLOTHE).price(-1.0).name("test_name")
				.imageLink("test_link").build();
		CreateProductRequest createProductRequest = createProductRequestFromProduct(product);

		String createProductRequestJSON = objectToJson(createProductRequest);

		MvcResult mvcResult = this.mockMvc.perform(
				post(CREATE_PRODUCT_URL).contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(createProductRequestJSON)).andExpect(status().isPreconditionFailed()).andReturn();

		String responseString = mvcResult.getResponse().getContentAsString();

		ErrorResponse errorResponse = objectFromJson(responseString, ErrorResponse.class);

		Assertions
				.assertTrue(errorResponse.getDetails().contains(ValidationMessages.NEGATIVE_PRICE_VALIDATION_MESSAGE));
	}

	@Test
	public void createProductTest_BlankName_PreconditionFailed() throws Exception {
		Product product = Product.builder().id(1L).category(ProductCategory.CLOTHE).price(3.0).name("")
				.imageLink("test_link").build();

		String productJSON = objectToJson(product);

		MvcResult mvcResult = this.mockMvc
				.perform(post(CREATE_PRODUCT_URL).contentType(MediaType.APPLICATION_JSON_VALUE).content(productJSON))
				.andExpect(status().isPreconditionFailed()).andReturn();

		String responseString = mvcResult.getResponse().getContentAsString();

		ErrorResponse errorResponse = objectFromJson(responseString, ErrorResponse.class);

		Assertions.assertTrue(errorResponse.getDetails().contains(ValidationMessages.BLANK_NAME_VALIDATION_MESSAGE));
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
		when(productService.getProductOfCategoryWithPageNumber(any(GetProductOfCategoryRequest.class)))
				.thenReturn(productList);
		String productsJson = objectToJson(productList);

		ProductCategory category = ProductCategory.CLOTHE;
		int page = 5;
		int count = 3;
		this.mockMvc.perform(getProductOfCategoryRequestBuilder(category, page, count)).andExpect(status().isOk())
				.andExpect(content().json(productsJson));
	}

	@Test
	public void updateProductTest_Successful() throws Exception {
		Product product = Product.builder().id(1L).category(ProductCategory.HOME).price(5.0).name("test_name")
				.imageLink("test_link").build();
		String productJSON = objectToJson(product);

		when(productService.updateProduct(any())).thenReturn(product);

		this.mockMvc
				.perform(post(UPDATE_PRODUCT_URL).contentType(MediaType.APPLICATION_JSON_VALUE).content(productJSON))
				.andExpect(status().isOk()).andExpect(content().string(productJSON));
	}

	@Test
	public void updateProductTest_NegativePrice_PreconditionFailed() throws Exception {
		Product product = Product.builder().id(1L).category(ProductCategory.CLOTHE).price(-1.0).name("test_name")
				.imageLink("test_link").build();
		String productJSON = objectToJson(product);

		MvcResult mvcResult = this.mockMvc
				.perform(post(UPDATE_PRODUCT_URL).contentType(MediaType.APPLICATION_JSON_VALUE).content(productJSON))
				.andExpect(status().isPreconditionFailed()).andReturn();

		String responseString = mvcResult.getResponse().getContentAsString();
		ErrorResponse errorResponse = objectFromJson(responseString, ErrorResponse.class);

		Assertions
				.assertTrue(errorResponse.getDetails().contains(ValidationMessages.NEGATIVE_PRICE_VALIDATION_MESSAGE));
	}

	@Test
	public void updateProductTest_NotExistProduct_PreconditionFailed() throws Exception {
		Product product = Product.builder().id(1L).category(ProductCategory.CLOTHE).price(5.0).name("test_name")
				.imageLink("test_link").build();
		String productJSON = objectToJson(product);

		when(productService.updateProduct(any())).thenThrow(new UpdatingProductNotExistsException());

		MvcResult mvcResult = this.mockMvc
				.perform(post(UPDATE_PRODUCT_URL).contentType(MediaType.APPLICATION_JSON_VALUE).content(productJSON))
				.andExpect(status().isPreconditionFailed()).andReturn();

		String responseString = mvcResult.getResponse().getContentAsString();
		ErrorResponse errorResponse = objectFromJson(responseString, ErrorResponse.class);

		Assertions.assertTrue(errorResponse.getDetails().contains(ValidationMessages.UPDATING_PRODUCT_NOT_EXIST));
	}

	@Test
	public void updateProductTest_BlankName_PreconditionFailed() throws Exception {
		Product product = Product.builder().id(1L).category(ProductCategory.CLOTHE).price(3.0).name("")
				.imageLink("test_link").build();

		String productJSON = objectToJson(product);

		MvcResult mvcResult = this.mockMvc
				.perform(post(UPDATE_PRODUCT_URL).contentType(MediaType.APPLICATION_JSON_VALUE).content(productJSON))
				.andExpect(status().isPreconditionFailed()).andReturn();

		String responseString = mvcResult.getResponse().getContentAsString();

		ErrorResponse errorResponse = objectFromJson(responseString, ErrorResponse.class);
		Assertions.assertTrue(errorResponse.getDetails().contains(ValidationMessages.BLANK_NAME_VALIDATION_MESSAGE));
	}

	@Test
	public void deleteProduct_NegativeProductId_NegativeProductIdException() throws Exception {
		DeleteProductRequest deleteProductRequest = DeleteProductRequest.builder().productId(-1L).build();
		String deleteProductRequestJSON = objectToJson(deleteProductRequest);

		MvcResult mvcResult = this.mockMvc.perform(
				delete(DELETE_PRODUCT_URL).contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(deleteProductRequestJSON)).andExpect(status().isPreconditionFailed()).andReturn();

		String responseString = mvcResult.getResponse().getContentAsString();
		ErrorResponse errorResponse = objectFromJson(responseString, ErrorResponse.class);
		Assertions.assertTrue(
				errorResponse.getDetails().contains(ValidationMessages.NEGATIVE_PRODUCT_ID_VALIDATION_MESSAGE));
	}

	@Test
	public void deleteProduct_NullProductId_NullProductAttributeException() throws Exception {
		MvcResult mvcResult = this.mockMvc
				.perform(delete(DELETE_PRODUCT_URL).contentType(MediaType.APPLICATION_JSON_VALUE).content("{}"))
				.andExpect(status().isPreconditionFailed()).andReturn();

		String responseString = mvcResult.getResponse().getContentAsString();
		ErrorResponse errorResponse = objectFromJson(responseString, ErrorResponse.class);
		Assertions.assertTrue(errorResponse.getDetails().contains(ValidationMessages.NULL_PRODUCT_ATTRIBUTE_EXCEPTION));
	}

	@Test
	public void deleteProduct_Successful() throws Exception {
		DeleteProductRequest deleteProductRequest = DeleteProductRequest.builder().productId(3L).build();
		String deleteProductRequestJSON = objectToJson(deleteProductRequest);

		MvcResult mvcResult = this.mockMvc.perform(
				delete(DELETE_PRODUCT_URL).contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(deleteProductRequestJSON)).andExpect(status().isOk()).andReturn();

		String responseString = mvcResult.getResponse().getContentAsString();
		DeleteResponse errorResponse = objectFromJson(responseString, DeleteResponse.class);

		Assertions.assertEquals(errorResponse.getProductId(), deleteProductRequest.getProductId());
		Assertions.assertEquals(errorResponse.getDeleteStatement(), PRODUCT_DELETED_STATEMENT);
	}

}