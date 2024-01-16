package com.vishnu.my_shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;




@Controller
public class GeneralController {
   
	@GetMapping("/")
   public String loadHome() {
		
		return "Home";
	}
	
	@GetMapping("/signup")
	public String loadSignup() {
		return "Signup";
	}
	
	@GetMapping("/signin")
	public String loadSignin() {
		return "SignIn";
	}
}
