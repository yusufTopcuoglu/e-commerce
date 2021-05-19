package com.dolap.product.controller;

import com.dolap.product.dto.ProductDTO;
import com.dolap.product.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Log4j2(topic = "product_controller")
@RestController
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping("product/create")
	private ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
		log.info("received post request to create product, name: {}, price: {}, category: {}, imageLink: {}",
				 productDTO.getName(), productDTO.getPrice(), productDTO.getCategory(), productDTO.getImageLink());
		ProductDTO product = productService.createAndSaveProduct(productDTO);
		return new ResponseEntity<>(product, HttpStatus.CREATED);
	}
}
