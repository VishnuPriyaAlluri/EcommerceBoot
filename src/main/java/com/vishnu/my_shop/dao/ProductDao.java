package com.vishnu.my_shop.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vishnu.my_shop.dto.Product;
import com.vishnu.my_shop.repository.ProductRepository;

@Repository
public class ProductDao {
	@Autowired
    ProductRepository productRepository;
	
	public boolean checkByName(String name) {
		return productRepository.existsByName(name);
	}

	public void save(Product product) {
		productRepository.save(product);
		
	}
}
