package com.dolap.product;

import com.dolap.product.enums.ProductCategory;
import com.dolap.product.model.Product;
import com.dolap.product.repository.ProductRepository;
import com.dolap.product.request.CreateProductRequest;
import com.dolap.product.request.DeleteProductRequest;
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
import java.util.Optional;

import static com.dolap.product.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductRepository productRepository;

	@Test
	@Transactional
	public void createProduct_Successful_ShouldReturnSavedProduct() throws Exception {
		CreateProductRequest createProductRequest = CreateProductRequest.builder().category(ProductCategory.HOME)
				.price(5.0).name("test_name").imageLink("test_link").build();
		String createProductRequestJSON = objectToJson(createProductRequest);

		MvcResult mvcResult = this.mockMvc.perform(
				post(CREATE_PRODUCT_URL).contentType(MediaType.APPLICATION_JSON).content(createProductRequestJSON))
				.andExpect(status().isCreated()).andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();
		Product savedProduct = objectFromJson(contentAsString, Product.class);

		assertEquals(createProductRequest.getImageLink(), savedProduct.getImageLink());
		assertEquals(createProductRequest.getPrice(), savedProduct.getPrice());
		assertEquals(createProductRequest.getName(), savedProduct.getName());
		assertEquals(createProductRequest.getCategory(), savedProduct.getCategory());
	}

	@Test
	@Transactional
	public void updateProduct_Successful_ShouldReturnUpdatedProduct() throws Exception {
		Product product = Product.builder().id(1L).category(ProductCategory.HOME).price(5.0).name("test_name")
				.imageLink("test_link").build();

		Optional<Product> productFromDb = productRepository.findById(product.getId());
		if (!productFromDb.isPresent()) {
			product = productRepository.save(product);
		}

		String productJSON = objectToJson(product);

		MvcResult mvcResult = this.mockMvc
				.perform(post(UPDATE_PRODUCT_URL).contentType(MediaType.APPLICATION_JSON).content(productJSON))
				.andExpect(status().isOk()).andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();
		Product updatedProduct = objectFromJson(contentAsString, Product.class);

		assertEquals(product.getId(), updatedProduct.getId());
		assertEquals(product.getImageLink(), updatedProduct.getImageLink());
		assertEquals(product.getPrice(), updatedProduct.getPrice());
		assertEquals(product.getName(), updatedProduct.getName());
		assertEquals(product.getCategory(), updatedProduct.getCategory());
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

	@Test
	@Transactional
	public void deleteProduct_Successful_ShouldReturnDeleteResponse() throws Exception {
		long productId = 3L;
		DeleteProductRequest deleteProductRequest = DeleteProductRequest.builder().productId(productId).build();

		Optional<Product> productByIdFromDb = productRepository.findById(productId);
		if (!productByIdFromDb.isPresent()) {
			Product product = Product.builder().id(productId).name("test_name").price(3.0)
					.category(ProductCategory.HOME).imageLink("test_link").build();
			Product savedProduct = productRepository.save(product);
			deleteProductRequest.setProductId(savedProduct.getId());
		}

		String deleteProductRequestJSON = objectToJson(deleteProductRequest);

		this.mockMvc.perform(
				delete(DELETE_PRODUCT_URL).contentType(MediaType.APPLICATION_JSON).content(deleteProductRequestJSON))
				.andExpect(status().isOk());

		Optional<Product> productAfterDelete = productRepository.findById(productId);
		assertFalse(productAfterDelete.isPresent());

	}

}
