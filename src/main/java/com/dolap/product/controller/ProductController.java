package com.dolap.product.controller;

import com.dolap.product.dto.ProductDTO;
import com.dolap.product.dto.ProductRequestDTO;
import com.dolap.product.model.Product;
import com.dolap.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Log4j2(topic = "product_controller")
@RestController
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@PostMapping("product/create")
	private ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO productDTO) {
		log.info("received post request to create product, name: {}, price: {}, category: {}, imageLink: {}",
				 productDTO.getName(), productDTO.getPrice(), productDTO.getCategory(), productDTO.getImageLink());
		Product product = productService.createAndSaveProduct(productDTO);
		return new ResponseEntity<>(product, HttpStatus.CREATED);
	}

	@PostMapping("product/update")
	private ResponseEntity<Product> updateProduct(@Valid @RequestBody Product product) {
		log.info("received post request to update product, id: {}, name: {}, price: {}, category: {}, imageLink: {}",
				 product.getId(), product.getName(), product.getPrice(), product.getCategory(),
				 product.getImageLink());
		Product updatedProduct = productService.updateProduct(product);
		return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
	}

	@GetMapping("product")
	private ResponseEntity<List<Product>> getProductOfCategoryWithPageNumber(@Valid ProductRequestDTO productRequest) {
		log.info("products of category : {} requested with page : {}, count {}", productRequest.getProductCategory(),
				 productRequest.getPage(), productRequest.getCount());
		List<Product> products = productService.getProductOfCategoryWithPageNumber(productRequest);
		return new ResponseEntity<>(products, HttpStatus.OK);
	}

}
