package com.zensar.service;

import com.zensar.model.Product;

public interface ProductService {

	void updateProduct(Product updatedProduct, int extendHours);

	void createProduct(Product product);

	Product getProductDetailsById(int productId);

	Iterable<Product> getLiveProducts();

	Iterable<Product> getProductsByRange(int priceLowerLimit, int priceUpperLimit);

	Iterable<Product> getProductsByCategory(int categoryId);

	Iterable<Product> getLatestProducts();

}
