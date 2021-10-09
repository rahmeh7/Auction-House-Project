package com.zensar.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zensar.model.Bid;
import com.zensar.model.User;
import com.zensar.service.BidService;
import com.zensar.util.CustomResponse;

//@CrossOrigin is used to make spring allow the request coming from angular application
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class BidResource {

	@Autowired
	private BidService bidService;

	// Request Handler method to get history of all the bids placed till now
	@GetMapping(value = "/bid/history")
	public CustomResponse bidHistory() {
		boolean hasBids = bidService.getAllBids().iterator().hasNext();
		if (hasBids)
			return new CustomResponse(HttpStatus.OK.value(), "Here is the List of Bid History",
					bidService.getAllBids());
		else
			return new CustomResponse(HttpStatus.NOT_FOUND.value(), "No Bids Avialable");
	}

	// Request Handler method to place the bid on particular product
	@PostMapping(value = "/place/bid")
	public CustomResponse placeBid(@RequestBody Bid bid) {
		bid.setUser(User.session);
		if (User.session != null && User.session.getUserProfile().equalsIgnoreCase("Bidder")) {
			int result = bidService.placeBid(bid);
			if (result == 100)
				return new CustomResponse(HttpStatus.ACCEPTED.value(), " Bid Placed Successfully");
			else
				return new CustomResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Please Increase the Bid Amount");
		} else {
			return new CustomResponse(HttpStatus.UNAUTHORIZED.value(), " You are Not a Registered Bidder");
		}
	}

	// Request Handler method to get the all the bids placed by particular bidder
	@RequestMapping(value = "/get/bids/bidder/{bidderId}")
	public CustomResponse getBidsByBidderId(@PathVariable("bidderId") int bidderId) {
		if (User.session != null && User.session.getUserProfile().equalsIgnoreCase("Bidder")) {
			boolean hasBids = bidService.getBidsByBidderId(bidderId).iterator().hasNext();
			if (hasBids)
				return new CustomResponse(HttpStatus.OK.value(), "Here is the List of Bids Placed By You");
			else
				return new CustomResponse(HttpStatus.NOT_FOUND.value(), "No Bids Placed By You");
		} else {
			return new CustomResponse(HttpStatus.UNAUTHORIZED.value(), "You are Not a Registered Bidder");
		}
	}
}
