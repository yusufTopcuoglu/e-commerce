package com.dolap.product.controller;

import com.dolap.product.model.Product;
import com.dolap.product.reponse.DeleteResponse;
import com.dolap.product.request.CreateProductRequest;
import com.dolap.product.request.DeleteProductRequest;
import com.dolap.product.request.GetProductOfCategoryRequest;
import com.dolap.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.dolap.product.strings.Constants.PRODUCT_DELETED_STATEMENT;

@Log4j2(topic = "product_controller")
@RestController
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@PostMapping("product/create")
	private ResponseEntity<Product> createProduct(@Valid @RequestBody CreateProductRequest createProductRequest) {
		log.info("received post request to create product, name: {}, price: {}, category: {}, imageLink: {}",
				 createProductRequest.getName(), createProductRequest.getPrice(), createProductRequest.getCategory(), createProductRequest
						 .getImageLink());
		Product product = productService.createAndSaveProduct(createProductRequest);
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

	@DeleteMapping("product/delete")
	private ResponseEntity<DeleteResponse> deleteProduct(@Valid @RequestBody DeleteProductRequest deleteProductRequest) {
		log.info("Delete request received for product with id : {}", deleteProductRequest.getProductId());
		productService.deleteProduct(deleteProductRequest.getProductId());
		return new ResponseEntity<>(new DeleteResponse(deleteProductRequest.getProductId(), PRODUCT_DELETED_STATEMENT)
				, HttpStatus.OK);
	}

	@GetMapping("product")
	private ResponseEntity<List<Product>> getProductOfCategoryWithPageNumber(@Valid GetProductOfCategoryRequest getProductOfCategoryRequest) {
		log.info("products of category : {} requested with page : {}, count {}", getProductOfCategoryRequest.getProductCategory(),
				 getProductOfCategoryRequest.getPage(), getProductOfCategoryRequest.getCount());
		List<Product> products = productService.getProductOfCategoryWithPageNumber(getProductOfCategoryRequest);
		return new ResponseEntity<>(products, HttpStatus.OK);
	}

}
