package com.zensar.util;

import java.security.Security;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.smtp.SMTPTransport;

public class EmailService {

	public static void sendEmail(String recipientEmail, String subject, String message)
			throws AddressException, MessagingException {

		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		String senderMailId = "gouravdrive1@gmail.com";
		String senderMailPassword = "Gourav12345678910";
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

		// Get a Properties object
		Properties props = System.getProperties();
		props.setProperty("mail.smtps.host", "smtp.gmail.com");
		props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		props.setProperty("mail.smtps.auth", "true");
		props.put("mail.smtps.quitwait", "false");

		Session session = Session.getInstance(props, null);

		// Creating a new message
		final MimeMessage msg = new MimeMessage(session);

		// Setting the FROM and TO fields
		msg.setFrom(new InternetAddress());
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));

		msg.setSubject(subject);
		msg.setText(message, "utf-8");
		msg.setSentDate(new Date());

		SMTPTransport t = (SMTPTransport) session.getTransport("smtps");

		// sends email
		t.connect("smtp.gmail.com", senderMailId, senderMailPassword);
		t.sendMessage(msg, msg.getAllRecipients());
		t.close();
	}

}
