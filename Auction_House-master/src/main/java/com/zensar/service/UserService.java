package com.zensar.service;

import com.zensar.model.Mail;
import com.zensar.model.User;

public interface UserService {

	int sendMail(Mail mail);

	int login(String email, String password, String userProfile);

	int register(User user);

	User getUserByEmail(String email);

}
