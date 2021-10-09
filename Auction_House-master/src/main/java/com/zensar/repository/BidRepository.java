package com.zensar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zensar.model.Bid;

public interface BidRepository extends JpaRepository<Bid, Integer> {

	@Query("select B from Bid B where B.product.productId=:productId")
	Iterable<Bid> getBidsByProductId(@Param("productId") int productId);

	@Query("select B from Bid B where B.user.userId=:userId")
	Iterable<Bid> getBidsByBidderId(@Param("userId") int userId);

	@Query("FROM Bid B WHERE B.product.productId=:productId ORDER BY B.bidAmount DESC ")
	Iterable<Bid> getBidsByProductIdAndBidAmount(@Param("productId") int productId);
}
