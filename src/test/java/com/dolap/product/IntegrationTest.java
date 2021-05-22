package com.dolap.product;

import com.dolap.product.dto.ProductDTO;
import com.dolap.product.enums.ProductCategory;
import com.dolap.product.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.dolap.product.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

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
		Product savedProduct = objectFromJson(contentAsString, Product.class);

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
		int page = 1;
		MvcResult mvcResult = this.mockMvc.perform(getProductOfCategoryRequestBuilder(category, page, count))
				.andExpect(status().isOk()).andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();
		List<Product> returnedProducts = productListFromJson(contentAsString);

		Assertions.assertTrue(returnedProducts.size() <= count);
		for (Product returnedProduct : returnedProducts) {
			Assertions.assertEquals(category, returnedProduct.getCategory());
		}
	}


}
