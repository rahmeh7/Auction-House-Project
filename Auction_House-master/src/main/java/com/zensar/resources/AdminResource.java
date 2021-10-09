package com.zensar.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zensar.model.Mail;
import com.zensar.model.Product;
import com.zensar.model.ProductCategory;
import com.zensar.model.User;
import com.zensar.service.ProductCategoryService;
import com.zensar.service.ProductService;
import com.zensar.service.UserService;
import com.zensar.util.CustomResponse;

// @CrossOrigin is used to make spring allow the request coming from angular application 
// MediaType is used to make application produce only JSON form data
@RestController
@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200")
public class AdminResource {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductCategoryService productCategoryService;

	@Autowired
	private UserService userService;

	// Request Handler method to send mail to seller or bidder
	@PostMapping(value = "/send/mail")
	public CustomResponse sendMail(@RequestBody Mail mail) {
		if (User.session != null && User.session.getUserProfile().equalsIgnoreCase("Admin")) {
			int result = userService.sendMail(mail);
			if (result == 100)
				return new CustomResponse(HttpStatus.OK.value(), "Mail Sent Successfully");
			else
				return new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Mail Could Not be Sent");
		} else {
			return new CustomResponse(HttpStatus.UNAUTHORIZED.value(), "UnAuthorized Access");
		}

	}

	// Request Handler method to update the details of the product
	@PutMapping(value = "/update/product/{extendHours}")
	public CustomResponse updateProduct(@RequestBody Product updatedProduct,
			@PathVariable("extendHours") int extendHours) {
		if (User.session != null && User.session.getUserProfile().equalsIgnoreCase("Admin")) {
			productService.updateProduct(updatedProduct, extendHours);
			return new CustomResponse(HttpStatus.ACCEPTED.value(), "Product Details Updated");
		} else {
			return new CustomResponse(HttpStatus.UNAUTHORIZED.value(), "UnAuthorized Access");
		}
	}

	// Request Handler method to add new product category
	@PostMapping(value = "/add/product/category")
	public CustomResponse addProductCategory(@RequestBody ProductCategory productCategory) {
		if (User.session != null && User.session.getUserProfile().equalsIgnoreCase("Admin")) {
			int result = productCategoryService.addProductCategory(productCategory);
			if (result == 200)
				return new CustomResponse(HttpStatus.CREATED.value(), "Product Category Added");
			else
				return new CustomResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Product Category Already Exists");
		} else {
			return new CustomResponse(HttpStatus.UNAUTHORIZED.value(), "UnAuthorized Access");
		}
	}

}
