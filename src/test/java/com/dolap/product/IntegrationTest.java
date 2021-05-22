package com.dolap.product;

import com.dolap.product.dto.ProductDTO;
import com.dolap.product.enums.ProductCategory;
import com.dolap.product.model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

	private static final String CREATE_PRODUCT_URL = "/product/create";
	private static final String REQUEST_PRODUCT_URL = "/product/{productCategory}/{page}/{count}";

	@Autowired
	private MockMvc mockMvc;

	@Test
	@Transactional
	public void createProduct_Successful_ShouldReturnSavedProduct() throws Exception {
		ProductDTO productDTO = ProductDTO.builder().category(ProductCategory.HOME).price(5.0).name("test_name")
				.imageLink("test_link").build();
		String productDtoJSON = objectToJson(productDTO);

		MvcResult mvcResult = this.mockMvc
				.perform(post(CREATE_PRODUCT_URL).contentType(MediaType.APPLICATION_JSON).content(productDtoJSON))
				.andExpect(status().isCreated()).andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();
		Gson gson = new Gson();
		Product savedProduct = gson.fromJson(contentAsString, Product.class);

		assertEquals(productDTO.getImageLink(), savedProduct.getImageLink());
		assertEquals(productDTO.getPrice(), savedProduct.getPrice());
		assertEquals(productDTO.getName(), savedProduct.getName());
		assertEquals(productDTO.getCategory(), savedProduct.getCategory());
	}

	@Test
	@Transactional
	public void getProductOfCategoryWithPageNumber_Successful_ShouldReturnProductsOfSpecifiedCategory()
			throws Exception {
		ProductCategory category = ProductCategory.ELECTRONIC;
		int count = 2;
		MvcResult mvcResult = this.mockMvc.perform(get(REQUEST_PRODUCT_URL, category, 1, count))
				.andExpect(status().isOk()).andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();
		Gson gson = new Gson();
		Type listOfProductObject = new TypeToken<ArrayList<Product>>() {}.getType();

		List<Product> returnedProducts = gson.fromJson(contentAsString, listOfProductObject);

		Assertions.assertTrue(returnedProducts.size() <= count);
		for (Product returnedProduct : returnedProducts) {
			Assertions.assertEquals(category, returnedProduct.getCategory());
		}
	}

	private String objectToJson(Object product) {
		Gson gson = new Gson();
		return gson.toJson(product);
	}
}
