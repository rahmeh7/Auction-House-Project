package com.zensar.service;

import com.zensar.model.Bid;

public interface BidService {

	Iterable<Bid> bidHistory(int productId);

	int placeBid(Bid bid);

	Iterable<Bid> getBidsByBidderId(int bidderId);

	Iterable<Bid> getAllBids();

}
