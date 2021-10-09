package com.zensar.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zensar.model.User;
import com.zensar.service.UserService;
import com.zensar.util.CustomResponse;

//@CrossOrigin is used to make spring allow the request coming from angular application 
//MediaType is used to make application produce only JSON form data and also accept JSON form data
@RestController
@RequestMapping(value = "/user", consumes = { MediaType.APPLICATION_JSON_VALUE,
		MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
@CrossOrigin(origins = "http://localhost:4200")
public class UserResource {

	@Autowired
	private UserService userService;

	// Request Handler method to register user
	@PostMapping(value = "/register")
	public CustomResponse register(@RequestBody User user) {
		CustomResponse response = new CustomResponse();
		try {
			int result = userService.register(user);
			if (result == 400) {
				response.setCode(HttpStatus.NOT_ACCEPTABLE.value());
				response.setMessage(" Not a Valid Email Id");
				return new CustomResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Not a Valid Email Id");
			}
			if (result == 200) {
				return new CustomResponse(HttpStatus.CREATED.value(), "Successfully Registered");
			}
			if (result == 401) {
				return new CustomResponse(HttpStatus.BAD_REQUEST.value(), "Email Id is Already Registered");
			}
		} catch (Exception e) {
			return new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error");
		}
		return null;
	}

	// Request Handler method to login user
	@PostMapping(value = "/login")
	public CustomResponse login(@RequestBody User user) {
		int result = userService.login(user.getEmail(), user.getPassword(), user.getUserProfile());
		if (result == 400) {
			return new CustomResponse(HttpStatus.UNAUTHORIZED.value(), "Not a Registered Email Id");
		}
		if (result == 200) {
			User user2 = userService.getUserByEmail(user.getEmail());
			User.session = user2;
			return new CustomResponse(HttpStatus.ACCEPTED.value(), "Logged in Successfully");
		}
		if (result == 401) {
			return new CustomResponse(HttpStatus.UNAUTHORIZED.value(), "Wrong Password");
		}
		if (result == 150) {
			return new CustomResponse(HttpStatus.UNAUTHORIZED.value(),
					"Not an Authorized " + user.getUserProfile() + " Profile ");
		}
		return null;
	}

	// Request Handler method to logout user
	@GetMapping(value = "/logout")
	public CustomResponse logout() {
		if (User.session != null) {
			User.session = null;
			return new CustomResponse(HttpStatus.OK.value(), "Logged Out Successfully");
		} else {
			return new CustomResponse(HttpStatus.BAD_REQUEST.value(), "Already Logged Out");
		}
	}

}
