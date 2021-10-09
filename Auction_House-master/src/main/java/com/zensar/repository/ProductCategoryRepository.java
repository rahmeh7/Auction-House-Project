package com.zensar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zensar.model.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

	@Query("FROM ProductCategory P WHERE P.productCategoryName = :productCategoryName")
	ProductCategory getProductCategoryByName(@Param("productCategoryName") String productCategoryName);

}
