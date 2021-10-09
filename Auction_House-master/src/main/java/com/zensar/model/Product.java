package com.zensar.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "product")
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "product_id")
	private int productId;

	@Column(name = "product_name")
	private String productName;

	@Column(name = "product_details")
	private String productDetails;

	@Column(name = "bid_amount")
	private int bidAmount;

	@Column(name = "image_url", unique = true, nullable = false)
	private String image;

	@Column(name = "closing_date")
	private Timestamp closingDate;

	@Column(name = "status")
	private String status = "On";

	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "seller_id")
	private User user;

	@ManyToOne(optional = false, cascade = CascadeType.MERGE)
	@JoinColumn(name = "product_category_id", nullable = false)
	@JsonBackReference
	private ProductCategory productCategory;

}
