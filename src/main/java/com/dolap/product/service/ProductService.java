package com.dolap.product.service;

import com.dolap.product.dto.ProductDTO;
import com.dolap.product.exception.BlankNameException;
import com.dolap.product.exception.NegativePriceException;
import com.dolap.product.exception.NullProductAttributeException;
import com.dolap.product.model.Product;
import com.dolap.product.repository.ProductRepository;
import com.dolap.product.strings.ValidationMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2(topic = "product_service")
@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	public ProductDTO createAndSaveProduct(ProductDTO productDTO) {
		if (productDTO == null || productDTO.getPrice() == null || productDTO.getCategory() == null || productDTO
				.getImageLink() == null || productDTO.getName() == null) {
			log.warn(ValidationMessages.NULL_PRODUCT_ATTRIBUTE_EXCEPTION);
			throw new NullProductAttributeException();
		}

		if (productDTO.getName().isEmpty()) {
			log.warn(ValidationMessages.BLANK_NAME_VALIDATION_MESSAGE);
			throw new BlankNameException();
		}
		if (productDTO.getPrice() < 0) {
			log.warn(ValidationMessages.NEGATIVE_PRICE_VALIDATION_MESSAGE);
			throw new NegativePriceException();
		}

		Product product = Product.builder().name(productDTO.getName()).price(productDTO.getPrice())
				.imageLink(productDTO.getImageLink()).category(productDTO.getCategory()).build();
		log.info("creating and saving a new product : {}", product);
		productRepository.save(product);
		return productDTO;
	}
}
