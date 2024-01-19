package com.vishnu.my_shop.helper;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.vishnu.my_shop.dto.Customer;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailSendingHelper {

	@Autowired
	JavaMailSender mailSender;
	public void resendOtp(Customer customer) {
		MimeMessage message=mailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		
		    try {
			helper.setFrom("alurivishnupriya@gmail.com","vishnumyshop");
			helper.setTo(customer.getEmail());
			helper.setSubject("One Time Password(OTP) for verifying your email from VishnuMyShop");
			String gen="";
			if(customer.getGender().equalsIgnoreCase("female"))
				gen="Ms. ";
			else
				gen="Mr. ";
			String body="<html><body><h1>Hello "+gen+customer.getName()+" ,</h1><h2>Your OTP is : "+customer.getOtp()+"</h2><h3>Thanks and Regards </h3></body></html>";
		    helper.setText(body,true);
		    mailSender.send(message);
		    }
		    catch(UnsupportedEncodingException | MessagingException e) {
		    	e.printStackTrace();
		    }
		
		
	}

	public void sendOtp(Customer customer) {
		MimeMessage message=mailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		
		    try {
			helper.setFrom("alurivishnupriya@gmail.com","vishnumyshop");
			helper.setTo(customer.getEmail());
			helper.setSubject("Resend : One Time Password(OTP) for verifying your email from VishnuMyShop");
			String gen="";
			if(customer.getGender().equalsIgnoreCase("female"))
				gen="Ms. ";
			else
				gen="Mr. ";
			String body="<html><body><h1>Hello "+gen+customer.getName()+" ,</h1><h2>Your OTP is : "+customer.getOtp()+"</h2><h3>Thanks and Regards </h3></body></html>";
		    helper.setText(body,true);
		    mailSender.send(message);
		    }
		    catch(UnsupportedEncodingException | MessagingException e) {
		    	e.printStackTrace();
		    }	
	}

}
