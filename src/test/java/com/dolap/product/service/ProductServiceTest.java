package com.dolap.product.service;

import com.dolap.product.enums.ProductCategory;
import com.dolap.product.exception.*;
import com.dolap.product.model.Product;
import com.dolap.product.repository.ProductRepository;
import com.dolap.product.request.CreateProductRequest;
import com.dolap.product.request.GetProductOfCategoryRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.dolap.product.TestUtils.createProductRequestFromProduct;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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
		CreateProductRequest createProductRequest = createProductRequestFromProduct(product);
		when(productRepository.save(any())).thenReturn(product);

		Product savedProduct = productService.createAndSaveProduct(createProductRequest);

		assertEquals(product.getCategory(), savedProduct.getCategory());
		assertEquals(product.getName(), savedProduct.getName());
		assertEquals(product.getPrice(), savedProduct.getPrice());
		assertEquals(product.getImageLink(), savedProduct.getImageLink());
	}

	@Test
	void createAndSaveProduct_NullProduct_ThrowsNullProductAttributeException() {
		Assertions.assertThrows(NullProductAttributeException.class, () -> productService.createAndSaveProduct(null));
		Assertions.assertThrows(NullProductAttributeException.class,
								() -> productService.createAndSaveProduct(new CreateProductRequest()));
	}

	@Test
	void createAndSaveProduct_NegativePrice_ThrowsNegativePriceException() {
		Product product = Product.builder().name("test_name").imageLink("test_link").price(-1.0)
				.category(ProductCategory.CLOTHE).build();
		CreateProductRequest createProductRequest = createProductRequestFromProduct(product);
		Assertions.assertThrows(NegativePriceException.class, () -> productService.createAndSaveProduct(
				createProductRequest));
	}

	@Test
	void createAndSaveProduct_BlankName_ThrowsBlankNameException() {
		Product product = Product.builder().name("").imageLink("test_link").price(3.0)
				.category(ProductCategory.ELECTRONIC).build();
		CreateProductRequest createProductRequest = createProductRequestFromProduct(product);
		Assertions.assertThrows(BlankNameException.class, () -> productService.createAndSaveProduct(
				createProductRequest));
	}

	@Test
	void updateProduct_Successful() {
		Product product = Product.builder().id(1L).name("test_name").imageLink("test_link").price(3.0)
				.category(ProductCategory.HEALTH).build();
		when(productRepository.save(any())).thenReturn(product);
		when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

		Product savedProduct = productService.updateProduct(product);

		assertEquals(product.getId(), savedProduct.getId());
		assertEquals(product.getCategory(), savedProduct.getCategory());
		assertEquals(product.getName(), savedProduct.getName());
		assertEquals(product.getPrice(), savedProduct.getPrice());
		assertEquals(product.getImageLink(), savedProduct.getImageLink());
	}

	@Test
	void updateProduct_NotExistProduct_ThrowsUpdatingProductNotExistsException() {
		Product product = Product.builder().id(1L).name("test_name").imageLink("test_link").price(3.0)
				.category(ProductCategory.HEALTH).build();

		when(productRepository.findById(product.getId())).thenReturn(Optional.empty());
		Assertions.assertThrows(UpdatingProductNotExistsException.class, () -> productService.updateProduct(product));
	}

	@Test
	void updateProduct_NullProduct_ThrowsNullProductAttributeException() {
		Assertions.assertThrows(NullProductAttributeException.class, () -> productService.updateProduct(null));
		Assertions.assertThrows(NullProductAttributeException.class,
								() -> productService.updateProduct(new Product()));
	}

	@Test
	void updateProduct_NegativePrice_ThrowsNegativePriceException() {
		Product product = Product.builder().id(1L).name("test_name").imageLink("test_link").price(-1.0)
				.category(ProductCategory.CLOTHE).build();
		Assertions.assertThrows(NegativePriceException.class, () -> productService.updateProduct(product));
	}

	@Test
	void updateProduct_BlankName_ThrowsBlankNameException() {
		Product product = Product.builder().id(1L).name("").imageLink("test_link").price(3.0)
				.category(ProductCategory.ELECTRONIC).build();
		Assertions.assertThrows(BlankNameException.class, () -> productService.updateProduct(product));
	}

	@Test
	void getProductOfCategoryWithPageNumber_NegativePageNumber_NegativePageNumberException() {
		Assertions.assertThrows(NegativePageIndexException.class, () -> productService
				.getProductOfCategoryWithPageNumber(
						GetProductOfCategoryRequest.builder().page(-1).productCategory(ProductCategory.HOME).count(1).build()));
	}

	@Test
	void getProductOfCategoryWithPageNumber_NullProductRequest_NullProductRequestAttributeException() {
		Assertions.assertThrows(NullProductRequestAttributeException.class,
								() -> productService.getProductOfCategoryWithPageNumber(null));

		Assertions.assertThrows(NullProductRequestAttributeException.class, () -> productService
				.getProductOfCategoryWithPageNumber(GetProductOfCategoryRequest.builder().page(1).count(1).build()));
	}

	@Test
	void getProductOfCategoryWithPageNumber_InvalidProductCount_InvalidProductCountException() {
		Assertions.assertThrows(InvalidProductCountException.class, () -> productService
				.getProductOfCategoryWithPageNumber(
						GetProductOfCategoryRequest.builder().page(0).productCategory(ProductCategory.HOME).count(0).build()));
	}

	@Test
	void getProductOfCategoryWithPageNumber_Successful() {
		List<Product> products = Arrays.asList(new Product(1L, "test_name", ProductCategory.HOME, 3.0, "tet_link"),
											   new Product(2L, "test_name", ProductCategory.HOME, 3.0, "tet_link"),
											   new Product(3L, "test_name", ProductCategory.HOME, 3.0, "tet_link"));

		when(productRepository.findAllByCategory(any(), any())).thenReturn(products);

		List<Product> actualProducts = productService.getProductOfCategoryWithPageNumber(
				GetProductOfCategoryRequest.builder().page(1).productCategory(ProductCategory.HOME).count(3).build());

		assertEquals(products, actualProducts);
	}

	@Test
	public void deleteProduct_ProductDoesNotExists_ThrowsDeletingProductNotExistsException() {
		long productId = 3L;
		doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(productId);

		assertThrows(DeletingProductNotExistsException.class, () -> productService.deleteProduct(productId));
	}

	@Test
	public void deleteProduct_Successful() {
		long productId = 3L;
		productService.deleteProduct(productId);
	}

}