package com.dolap.product.repository;

import com.dolap.product.enums.ProductCategory;
import com.dolap.product.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findAllByCategory(ProductCategory productCategory, Pageable pageable);
}
