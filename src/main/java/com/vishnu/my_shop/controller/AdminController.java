package com.vishnu.my_shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
   public String addProduct(@Valid Product product, BindingResult result,@RequestParam MultipartFile picture, HttpSession session,ModelMap map) {
	  
	   if (result.hasErrors()) {
		  
		  //System.out.println("hello errors is there check first");
		  return "AddProduct";
	   }
	   	else
	   return adminService.addProduct(product,result,picture,session,map);
   }
   
   @GetMapping("/manage-product")
   public String managePRoducts(HttpSession session,ModelMap map) {
	   return adminService.manageProducts(session,map);
   }
   
   @GetMapping("/delete-product/{id}")
   public String deleteProduct(@PathVariable int id,HttpSession session) {
	   return adminService.deleteProduct(id,session);
   }
   
  
   @GetMapping("/edit-product/{id}")
   public String loadEditProduct(@PathVariable int id,HttpSession session,ModelMap map) {
	   return adminService.loadEditProduct(id,session,map);
   }
   
   @PostMapping("/update-product")
    public String updateProduct(@Valid Product product, BindingResult result,@RequestParam MultipartFile picture, HttpSession session,ModelMap map) {
	  
	   if (result.hasErrors()) {
		  return "EditProduct";
	   }
	   	else
	   return adminService.updateProduct(product,result,picture,session,map);
   }
   
   @GetMapping("/create-admin/{email}/{password}")
   public String createAdmin(@PathVariable String email,@PathVariable String password,HttpSession session) {
	   return adminService.createAdmin(email,password,session);
   }
   
   
}
