package com.zensar.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.zensar.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {

	@Query("FROM User U WHERE U.email = :email")
	User getByEmail(@Param("email") String email);

}
