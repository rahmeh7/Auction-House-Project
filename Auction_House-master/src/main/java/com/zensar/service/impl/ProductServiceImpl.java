package com.zensar.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zensar.model.Product;
import com.zensar.model.ProductCategory;
import com.zensar.repository.ProductCategoryRepository;
import com.zensar.repository.ProductRepository;
import com.zensar.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@Override
	public void updateProduct(Product updatedProduct, int extendHours) {
		String updatedProductCatergoryName = updatedProduct.getProductCategory().getProductCategoryName();
		ProductCategory updatedProductCategory = productCategoryRepository
				.getProductCategoryByName(updatedProductCatergoryName);
		productRepository.updateProduct(updatedProduct.getProductId(), updatedProduct.getProductName(),
				updatedProduct.getProductDetails(), updatedProduct.getBidAmount(), setClosingDate(extendHours), "On",
				updatedProductCategory.getProductCategoryId());
	}

	@Override
	public void createProduct(Product product) {
		product.setClosingDate(setClosingDate(24));
		productRepository.save(product);
	}

	static Timestamp setClosingDate(long hours) {
		LocalDateTime dateTime = LocalDateTime.now();
		dateTime = dateTime.plusHours(hours);
		Timestamp closingDateTime = Timestamp.valueOf(dateTime);
		return closingDateTime;
	}

	@Override
	public Product getProductDetailsById(int productId) {
		Optional<Product> product = productRepository.findById(productId);
		boolean isPresent = product.isPresent();
		if (isPresent)
			return product.get();
		else
			return null;
	}

	@Override
	public Iterable<Product> getLiveProducts() {
		return productRepository.getLiveProducts();
	}

	@Override
	public Iterable<Product> getProductsByRange(int priceLowerLimit, int priceUpperLimit) {
		return productRepository.getProductsByRange(priceLowerLimit, priceUpperLimit);
	}

	@Override
	public Iterable<Product> getProductsByCategory(int categoryId) {
		return productRepository.getProductsByCategory(categoryId);
	}

	@Override
	public Iterable<Product> getLatestProducts() {
		return productRepository.getLatestProducts();
	}

}
