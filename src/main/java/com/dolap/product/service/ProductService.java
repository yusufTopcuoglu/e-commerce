package com.dolap.product.service;

import com.dolap.product.dto.ProductDTO;
import com.dolap.product.model.Product;
import com.dolap.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2(topic = "product_service")
@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	public ProductDTO createAndSaveProduct(ProductDTO productDTO) {
		Product product = Product.builder().name(productDTO.getName()).price(productDTO.getPrice())
				.imageLink(productDTO.getImageLink()).category(productDTO.getCategory()).build();
		log.info("creating and saving product : {}", product);
		productRepository.save(product);
		return productDTO;
	}
}
