package com.vishnu.my_shop.service;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.vishnu.my_shop.dto.Customer;

import jakarta.validation.Valid;

public interface CustomerService {

	String save(@Valid Customer customer, BindingResult result);

	

	String verifyOtp(int id, int otp, ModelMap map);



	String sendOtp(int id, ModelMap map);



	String resendOtp(int id, ModelMap map);

}
