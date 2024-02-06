package com.vishnu.my_shop.dao;

import java.util.List;

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
	
	public List<Product> findAll(){
		return productRepository.findAll();
	}

	public void deleteById(int id) {
		productRepository.deleteById(id);
		
	}

	public Product findById(int id) {
		return productRepository.findById(id).orElse(null);
	}

	public Product findByName(String name) {
	    return productRepository.findByName(name);
	}
}
