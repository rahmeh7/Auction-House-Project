package com.zensar.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "product_category")
public class ProductCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "product_category_id")
	private int productCategoryId;

	@Column(name = "product_category_name", unique = true, nullable = false)
	private String productCategoryName;

	@ToString.Exclude
	@JsonManagedReference
	@OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Product> products;
}
