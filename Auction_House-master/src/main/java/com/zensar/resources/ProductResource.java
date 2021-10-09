package com.zensar.resources;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zensar.model.Product;
import com.zensar.model.ProductCategory;
import com.zensar.model.User;
import com.zensar.service.ProductCategoryService;
import com.zensar.service.ProductService;
import com.zensar.util.CustomResponse;
import com.zensar.util.S3BucketUtility;

//@CrossOrigin is used to make spring allow the request coming from angular application 
//MediaType is used to make application produce only JSON form data
@RestController
@RequestMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200")
public class ProductResource {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductCategoryService productCategoryService;
	private S3BucketUtility s3BucketUtility;

	// Request Handler method to sell the product
	@PostMapping(value = "/sell/product")
	public CustomResponse sellProduct(@RequestBody Product product) {
		if (User.session != null && User.session.getUserProfile().equalsIgnoreCase("Seller")) {
			product.setUser(User.session);
			product.setProductCategory(productCategoryService
					.getProductCategoryByName(product.getProductCategory().getProductCategoryName()));
			if (product.getImage() != null && product.getImage().length() > 10 && product != null) {
				productService.createProduct(product);
				return new CustomResponse(HttpStatus.CREATED.value(), "Product Registered For Bid");
			} else {
				return new CustomResponse(HttpStatus.NOT_ACCEPTABLE.value(),
						"Please Upload Appropriate Image and Enter Appropriate Details ");
			}
		} else {
			return new CustomResponse(HttpStatus.UNAUTHORIZED.value(), "UnAuthorized Access");
		}

	}

	// Request Handler method to get all product categories
	@GetMapping(value = "/categories")
	public CustomResponse getProductCategories() {
		boolean hasProductCategories = productCategoryService.getProductCategories().iterator().hasNext();
		if (hasProductCategories)
			return new CustomResponse(HttpStatus.OK.value(), "Here is the List of All Product Categories",
					productCategoryService.getProductCategories());
		else
			return new CustomResponse(HttpStatus.NOT_FOUND.value(), "No Product Categories Available",
					productCategoryService.getProductCategories());
	}

	// Request Handler method to get all the latest products whose status is ON (means available to place bid)
	@GetMapping(value = "/latest")
	public CustomResponse getLatestProducts() {
		boolean hasLatestProducts = productService.getLatestProducts().iterator().hasNext();
		if (hasLatestProducts)
			return new CustomResponse(HttpStatus.OK.value(), "Here is the List of All Latest Products",
					productService.getLatestProducts());
		else
			return new CustomResponse(HttpStatus.NOT_FOUND.value(), "No Latest Products Found",
					productService.getLatestProducts());
	}

	// Request Handler method to get details of particular product
	@GetMapping(value = "/details/{productId}")
	public CustomResponse getProductDetailsById(@PathVariable("productId") int productId) {
		Product product = productService.getProductDetailsById(productId);
		if (product != null) {
			return new CustomResponse(HttpStatus.FOUND.value(), product.getProductCategory().getProductCategoryName(),
					product);
		} else
			return new CustomResponse(HttpStatus.NOT_FOUND.value(), "Product Not Found  ", product);
	}

	// Request Handler method to get latest 5 products whose status is ON
	@GetMapping(value = "/live/products")
	public CustomResponse getLiveProducts() {
		boolean hasLiveProducts = productService.getLiveProducts().iterator().hasNext();
		if (User.session != null) {
			if (hasLiveProducts)
				return new CustomResponse(HttpStatus.OK.value(), User.session.getUserProfile(),
						productService.getLiveProducts());
			else
				return new CustomResponse(HttpStatus.NOT_FOUND.value(), User.session.getUserProfile(),
						productService.getLiveProducts());
		} else {
			if (hasLiveProducts)
				return new CustomResponse(HttpStatus.OK.value(), " ", productService.getLiveProducts());
			else
				return new CustomResponse(HttpStatus.NOT_FOUND.value(), " ", productService.getLiveProducts());
		}
	}

	// Request Handler method to get all the products whose price falls between specific range
	@GetMapping(value = "/{priceLowerLimit}/{priceUpperLimit}")
	public CustomResponse getProductsByRange(@PathVariable("priceLowerLimit") int priceLowerLimit,
			@PathVariable("priceUpperLimit") int priceUpperLimit) {
		boolean hasProducts = productService.getProductsByRange(priceLowerLimit, priceUpperLimit).iterator().hasNext();
		if (hasProducts)
			return new CustomResponse(HttpStatus.OK.value(), "Here is the List of Products in Given Price Range ",
					productService.getProductsByRange(priceLowerLimit, priceUpperLimit));
		else
			return new CustomResponse(HttpStatus.NOT_FOUND.value(), "No Products Found in Given Price Range ",
					productService.getProductsByRange(priceLowerLimit, priceUpperLimit));
	}

	// Request Handler method to get particular product category details as per product category id
	@GetMapping(value = "/category/categoryId/{categoryId}")
	public CustomResponse getProductsByCategory(@PathVariable("categoryId") int categoryId) {
		boolean hasProducts = productService.getProductsByCategory(categoryId).iterator().hasNext();
		if (hasProducts) {
			return new CustomResponse(HttpStatus.OK.value(), "Here is the List of Products By Category ",
					productService.getProductsByCategory(categoryId));
		} else {
			return new CustomResponse(HttpStatus.NOT_FOUND.value(), "Products Not Found By ID : ",
					productService.getProductsByCategory(categoryId));
		}
	}

	// Request Handler method to get particular product category details as per product category name
	@GetMapping(value = "/category/categoryName/{categoryName}")
	public CustomResponse getProductCategoryByName(@PathVariable("categoryName") String categoryName) {
		ProductCategory productCategory = productCategoryService.getProductCategoryByName(categoryName);
		if (productCategory != null)
			return new CustomResponse(HttpStatus.OK.value(), "This is the Category", productCategory);
		else
			return new CustomResponse(HttpStatus.NOT_FOUND.value(), "Product category Does Not Exist ");
	}

	// Request Handler method to upload the product image on AWS S3 bucket
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public CustomResponse uploadFile(@RequestPart(value = "image") MultipartFile image) throws IOException {
		try {
			s3BucketUtility = new S3BucketUtility();
			if (User.session != null && User.session.getUserProfile().equalsIgnoreCase("Seller")) {
				String imageLocationPath = s3BucketUtility.uploadFile(image);
				if (imageLocationPath.equals("Extension of image is not correct")) {
					return new CustomResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Extension of image is not correct");
				} else {
					return new CustomResponse(HttpStatus.OK.value(), imageLocationPath);
				}
			} else {
				return new CustomResponse(HttpStatus.UNAUTHORIZED.value(), "UnAuthorized Access");
			}
		} catch (Exception e) {
			return new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error");
		}

	}

}
