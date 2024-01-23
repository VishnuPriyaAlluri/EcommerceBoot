package com.vishnu.my_shop.service;

import com.vishnu.my_shop.dto.Product;

import jakarta.servlet.http.HttpSession;

public interface AdminService {

	String loadDashBoard(HttpSession session);

	String loadAddProduct(HttpSession session);

	String addProduct(Product product, HttpSession session);

}
