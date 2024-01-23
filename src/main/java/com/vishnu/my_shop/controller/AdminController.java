package com.vishnu.my_shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vishnu.my_shop.dto.Product;
import com.vishnu.my_shop.service.AdminService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	Product product;
   @GetMapping
   public String loadAdmin(HttpSession session) {
	   return adminService.loadDashBoard(session);
   }
   
   @GetMapping("/add-product")
   public String  loadaddProduct(ModelMap map, HttpSession session) {
	   map.put("product", product);
	   return adminService.loadAddProduct(session);
   }
   
   @PostMapping("/add-product")
   public String addProduct(@Valid Product product, BindingResult result,HttpSession session) {
	  
	   if (result.hasErrors()) {
		  
		  System.out.println("hello errors is there check first");
		  return "AddProduct";
	   }
	   	else
	   return adminService.addProduct(product,session);
   }
}
