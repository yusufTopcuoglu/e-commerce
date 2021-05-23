package com.dolap.product.service;

import com.dolap.product.exception.*;
import com.dolap.product.model.Product;
import com.dolap.product.repository.ProductRepository;
import com.dolap.product.request.CreateProductRequest;
import com.dolap.product.request.GetProductOfCategoryRequest;
import com.dolap.product.strings.ValidationMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2(topic = "product_service")
@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	public Product createAndSaveProduct(CreateProductRequest createProductRequest) {
		if (createProductRequest == null || createProductRequest.getPrice() == null || createProductRequest.getCategory() == null || createProductRequest
				.getImageLink() == null || createProductRequest.getName() == null) {
			log.warn(ValidationMessages.NULL_PRODUCT_ATTRIBUTE_EXCEPTION);
			throw new NullProductAttributeException();
		}

		if (createProductRequest.getName().isEmpty()) {
			log.warn(ValidationMessages.BLANK_NAME_VALIDATION_MESSAGE);
			throw new BlankNameException();
		}
		if (createProductRequest.getPrice() < 0) {
			log.warn(ValidationMessages.NEGATIVE_PRICE_VALIDATION_MESSAGE);
			throw new NegativePriceException();
		}

		Product product = Product.builder().name(createProductRequest.getName()).price(createProductRequest.getPrice())
				.imageLink(createProductRequest.getImageLink()).category(createProductRequest.getCategory()).build();
		log.info("creating and saving a new product : {}", product);
		return productRepository.save(product);
	}

	public Product updateProduct(Product product) {
		if (product == null || product.getPrice() == null || product.getCategory() == null || product
				.getImageLink() == null || product.getName() == null || product.getId() == null) {
			log.warn(ValidationMessages.NULL_PRODUCT_ATTRIBUTE_EXCEPTION);
			throw new NullProductAttributeException();
		}

		if (product.getName().isEmpty()) {
			log.warn(ValidationMessages.BLANK_NAME_VALIDATION_MESSAGE);
			throw new BlankNameException();
		}
		if (product.getPrice() < 0) {
			log.warn(ValidationMessages.NEGATIVE_PRICE_VALIDATION_MESSAGE);
			throw new NegativePriceException();
		}

		Optional<Product> updatingProduct = productRepository.findById(product.getId());
		if (!updatingProduct.isPresent()) {
			throw new UpdatingProductNotExistsException();
		}

		log.info("updating the product : {}", product);
		return productRepository.save(product);
	}

	public List<Product> getProductOfCategoryWithPageNumber(GetProductOfCategoryRequest productRequest) {
		if (productRequest == null || productRequest.getProductCategory() == null) {
			throw new NullProductRequestAttributeException();
		}
		if (productRequest.getPage() < 0) {
			throw new NegativePageIndexException();
		}
		if (productRequest.getCount() < 1) {
			throw new InvalidProductCountException();
		}
		Pageable pageable = PageRequest.of(productRequest.getPage(), productRequest.getCount());
		return productRepository.findAllByCategory(productRequest.getProductCategory(), pageable);
	}

	public void deleteProduct(long productId) {
		log.info("deleting product with id:  {}", productId);
		try {
			productRepository.deleteById(productId);
		} catch (EmptyResultDataAccessException e) {
			log.warn("The product with id : {} doesn't exist", productId);
			throw new DeletingProductNotExistsException();
		}
	}
}
