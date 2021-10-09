package com.zensar.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "bids")
public class Bid implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "bid_id")
	private int bidId;

	@Column(name = "bid_amount", unique = true, nullable = false)
	private int bidAmount;

	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "product_id")
	private Product product;

	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "bidder_id")
	private User user;

}
