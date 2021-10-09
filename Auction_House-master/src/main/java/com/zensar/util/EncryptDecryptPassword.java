package com.zensar.util;

import org.jasypt.util.password.StrongPasswordEncryptor;

public class EncryptDecryptPassword {

	// To encrypt the password
	public static String encryptPassword(String inputPassword) {
		StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
		return encryptor.encryptPassword(inputPassword);
	}

	// To decrypt password and check that password matches or not
	public static boolean checkPassword(String inputPassword, String encryptedStoredPassword) {
		StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
		return encryptor.checkPassword(inputPassword, encryptedStoredPassword);
	}

}
