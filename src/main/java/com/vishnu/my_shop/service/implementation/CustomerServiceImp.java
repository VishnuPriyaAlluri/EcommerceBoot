package com.vishnu.my_shop.service.implementation;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.vishnu.my_shop.dao.CustomerDao;
import com.vishnu.my_shop.dto.Customer;
import com.vishnu.my_shop.helper.AES;
import com.vishnu.my_shop.helper.MailSendingHelper;
import com.vishnu.my_shop.service.CustomerService;

import jakarta.validation.Valid;


@Service
public class CustomerServiceImp implements CustomerService {
	
	@Autowired
	CustomerDao customerDao;
	
	@Autowired
	MailSendingHelper mailHelper;

	@Override
	public String save(@Valid Customer customer, BindingResult result) {
		
		if(customerDao.checkEmailDuplicate(customer.getEmail()))
			result.rejectValue("email","error.email","Account Already Exists With this Email");
		
		if(customerDao.checkMobileDuplicate(customer.getMobile()))
			result.rejectValue("mobile","error.mobile","Account Already Exists With this mobile");
		
		if(result.hasErrors())
			return "Signup";
		else {
		customer.setPassword(AES.encrypt(customer.getPassword(),"123"));
		customer.setRole("USER");
		customerDao.save(customer);
		return "redirect:/send-otp/"+customer.getId();
		}
	}

	
   @Override
	public String sendOtp(int id, ModelMap map) {
		Customer customer=customerDao.findById(id);
		customer.setOtp(new Random().nextInt(100000,999999));
		mailHelper.sendOtp(customer);
		customerDao.save(customer);
		map.put("id",id);
		map.put("successMEssage","Otp Sent Seuccess");
		return "VerifyOtp";
	}
   
   @Override
	public String verifyOtp(int id,int otp,ModelMap map) {
		Customer customer = customerDao.findById(id);
		if(customer.getOtp()==otp) {
		customer.setVerified(true);
			map.put("successMessage","Otp Verification Success");
			return "SignIn";
		}
		else {
			map.put("failMessage", "Incorrect Otp Try again");
			map.put("id",id);
			return "VerifyOtp";
		}
	}


@Override
public String resendOtp(int id, ModelMap map) {
	Customer customer=customerDao.findById(id);
	
	customer.setOtp(new Random().nextInt(100000,999999));
	mailHelper.resendOtp(customer);
	customerDao.save(customer);
	map.put("id",id);
	map.put("successMEssage","Otp ReSent Seuccess");
	return "VerifyOtp";
}



	
    
}
