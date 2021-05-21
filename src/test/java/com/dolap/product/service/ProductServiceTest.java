package com.dolap.product.service;

import com.dolap.product.dto.ProductDTO;
import com.dolap.product.enums.ProductCategory;
import com.dolap.product.exception.BlankNameException;
import com.dolap.product.exception.NegativePriceException;
import com.dolap.product.exception.NullProductAttributeException;
import com.dolap.product.model.Product;
import com.dolap.product.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductServiceTest {

	@MockBean
	private ProductRepository productRepository;

	@Autowired
	@InjectMocks
	private ProductService productService;

	@Test
	void createAndSaveProduct_Successful() {
		Product product = Product.builder().name("test_name").imageLink("test_link").price(3.0)
				.category(ProductCategory.HEALTH).build();
		ProductDTO productDTO = ProductDTO.fromProduct(product);
		when(productRepository.save(any())).thenReturn(product);

		Product savedProduct = productService.createAndSaveProduct(productDTO);

		assertEquals(product.getCategory(), savedProduct.getCategory());
		assertEquals(product.getName(), savedProduct.getName());
		assertEquals(product.getPrice(), savedProduct.getPrice());
		assertEquals(product.getImageLink(), savedProduct.getImageLink());
	}

	@Test
	void createAndSaveProduct_NullProduct_ThrowsNullProductAttributeException() {
		Assertions.assertThrows(NullProductAttributeException.class, () -> productService.createAndSaveProduct(null));
		Assertions.assertThrows(NullProductAttributeException.class,
								() -> productService.createAndSaveProduct(new ProductDTO()));
	}

	@Test
	void createAndSaveProduct_NegativePrice_ThrowsNegativePriceException() {
		Product product = Product.builder().name("test_name").imageLink("test_link").price(-1.0)
				.category(ProductCategory.CLOTHE).build();
		ProductDTO productDTO = ProductDTO.fromProduct(product);
		Assertions.assertThrows(NegativePriceException.class, () -> productService.createAndSaveProduct(productDTO));
	}

	@Test
	void createAndSaveProduct_BlankName_ThrowsBlankNameException() {
		Product product = Product.builder().name("").imageLink("test_link").price(3.0)
				.category(ProductCategory.ELECTRONIC).build();
		ProductDTO productDTO = ProductDTO.fromProduct(product);
		Assertions.assertThrows(BlankNameException.class, () -> productService.createAndSaveProduct(productDTO));
	}
}