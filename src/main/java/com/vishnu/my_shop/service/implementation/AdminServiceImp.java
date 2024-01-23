package com.vishnu.my_shop.service.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.vishnu.my_shop.dto.Customer;
import com.vishnu.my_shop.dto.Product;
import com.vishnu.my_shop.service.AdminService;

import jakarta.servlet.http.HttpSession;


@Service
public class AdminServiceImp implements AdminService{

	@Override
	public String loadDashBoard(HttpSession session) {
		Customer customer=(Customer) session.getAttribute("customer");
		if(customer==null) {
			session.setAttribute("failMessage","Invalid Session");
			return "redirect:/signin";
		}
		else {
			if(customer.getRole().equals("ADMIN"))
			{
				return "AdminDashBoard";
			}
			else {
				session.setAttribute("failMessage","You Are Unauthorized to Access This URL");
				return "redirect:/";
			}
		}
	}

	@Override
	public String loadAddProduct(HttpSession session) {
		Customer customer=(Customer) session.getAttribute("customer");
		if(customer==null) {
			session.setAttribute("failMessage","Invalid Session");
			return "redirect:/signin";
		}
		else {
			if(customer.getRole().equals("ADMIN"))
			{
				return "AddProduct";
			}
			else {
				session.setAttribute("failMessage","You Are Unauthorized to Access This URL");
				return "redirect:/";
			}
		}
	}

	@Override
	public String addProduct(Product product,HttpSession session) {
		Customer customer=(Customer) session.getAttribute("customer");
		if(customer==null) {
			session.setAttribute("failMessage","Invalid Session");
			return "redirect:/signin";
		}
		else {
			if(customer.getRole().equals("ADMIN"))
			{
				product.setDate(LocalDateTime.now());
				System.out.println(product);
				session.setAttribute("successMessage","Product Added Successfully");
				return "redirect:/";
			}
			else {
				session.setAttribute("failMessage","You Are Unauthorized to Access This URL");
				return "redirect:/";
			}
		}
	}

}
