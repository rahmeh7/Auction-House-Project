package com.zensar.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zensar.model.Bid;
import com.zensar.repository.BidRepository;
import com.zensar.repository.ProductRepository;
import com.zensar.service.BidService;

@Service
public class BidServiceImpl implements BidService {

	@Autowired
	private BidRepository bidRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public Iterable<Bid> bidHistory(int productId) {
		return bidRepository.getBidsByProductId(productId);
	}

	@Override
	public int placeBid(Bid bid) {
		int currentBidProductId = bid.getProduct().getProductId();
		int currentBidAmount = bid.getBidAmount();
		int initialAmount = productRepository.getOne(bid.getProduct().getProductId()).getBidAmount();
		boolean hasPreviousBids = bidRepository.getBidsByProductIdAndBidAmount(currentBidProductId).iterator()
				.hasNext();
		if (hasPreviousBids == false) {
			if (initialAmount < currentBidAmount) {
				bidRepository.save(bid);
				return 100;
			}
		} else {
			int lastBidAmount = bidRepository.getBidsByProductIdAndBidAmount(currentBidProductId).iterator().next()
					.getBidAmount();
			if (initialAmount < currentBidAmount && lastBidAmount < currentBidAmount) {
				bidRepository.save(bid);
				return 100;
			} else
				return 200;
		}
		return 0;
	}

	@Override
	public Iterable<Bid> getBidsByBidderId(int bidderId) {
		return bidRepository.getBidsByBidderId(bidderId);
	}

	@Override
	public Iterable<Bid> getAllBids() {
		return bidRepository.findAll();
	}

}
