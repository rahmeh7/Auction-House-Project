package com.zensar.service;

import com.zensar.model.ProductCategory;

public interface ProductCategoryService {

	Iterable<ProductCategory> getProductCategories();

	int addProductCategory(ProductCategory productCategory);

	ProductCategory getProductCategoryByName(String productCategoryName);

}
