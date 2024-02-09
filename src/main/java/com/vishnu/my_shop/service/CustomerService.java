package com.vishnu.my_shop.service;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.vishnu.my_shop.dto.Customer;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

public interface CustomerService {

	String save(@Valid Customer customer, BindingResult result);

	

	String verifyOtp(int id, int otp, ModelMap map, HttpSession session);



	String sendOtp(int id, ModelMap map);



	String resendOtp(int id, ModelMap map);



	String login(String email, String password, ModelMap map, HttpSession session);



	String viewProduct(HttpSession session, ModelMap map);



	String addToCart(int id, HttpSession session);



	String viewCart( HttpSession session, ModelMap map);



	String removeFromCart(int id, HttpSession session);



	String paymentPage(HttpSession session, ModelMap map);



	String confirmOrder(HttpSession session, int id, String razorpay_payment_id);



	String viewOrders(HttpSession session, ModelMap map);

}
