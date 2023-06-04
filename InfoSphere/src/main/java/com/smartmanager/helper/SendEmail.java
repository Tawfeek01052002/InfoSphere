package com.smartmanager.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendEmail {

	@Autowired
	private JavaMailSender javaMailSender;

	public boolean SendingEmail(String msg, String toEmail, String subject) {

		SimpleMailMessage m = new SimpleMailMessage();
		m.setSubject(subject);
		m.setText(msg);
		m.setTo(toEmail);

		javaMailSender.send(m);
		return true;
	}
}
