package com.zensar.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zensar.model.ProductCategory;
import com.zensar.repository.ProductCategoryRepository;
import com.zensar.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@Override
	public Iterable<ProductCategory> getProductCategories() {
		return productCategoryRepository.findAll();
	}

	@Override
	public int addProductCategory(ProductCategory productCategory) {
		try {
			ProductCategory checkedProductcategory = productCategoryRepository
					.getProductCategoryByName(productCategory.getProductCategoryName());
			if (checkedProductcategory != null && checkedProductcategory.getProductCategoryName()
					.equalsIgnoreCase(productCategory.getProductCategoryName())) {
				productCategoryRepository.save(productCategory);
				return 150;
			} else {
				productCategoryRepository.save(productCategory);
				return 200;
			}
		} catch (Exception e) {
			return 400;
		}
	}

	@Override
	public ProductCategory getProductCategoryByName(String productCategoryName) {
		return productCategoryRepository.getProductCategoryByName(productCategoryName);
	}

}
