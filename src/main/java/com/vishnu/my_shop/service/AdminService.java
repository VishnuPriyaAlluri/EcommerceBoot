package com.vishnu.my_shop.service;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.vishnu.my_shop.dto.Product;

import jakarta.servlet.http.HttpSession;

public interface AdminService {

	String loadDashBoard(HttpSession session);

	String loadAddProduct(HttpSession session);

	String addProduct(Product product, BindingResult result, MultipartFile picture, HttpSession session, ModelMap map);

}
