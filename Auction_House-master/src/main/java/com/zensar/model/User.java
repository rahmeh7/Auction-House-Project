package com.zensar.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "user")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private int userId;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "email")
	private String email;

	@Column(name = "mobile_no")
	private String mobileNo;

	@Column(name = "password")
	private String password;

	@Column(name = "user_profile")
	private String userProfile;

	@Transient
	public static User session;

}
