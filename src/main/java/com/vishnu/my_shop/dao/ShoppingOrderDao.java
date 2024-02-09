package com.vishnu.my_shop.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vishnu.my_shop.dto.ShoppingOrder;
import com.vishnu.my_shop.repository.ShoppingOrderRepository;

@Repository
public class ShoppingOrderDao {
    @Autowired
    ShoppingOrderRepository orderRepository;

	public void saveOrder(ShoppingOrder order) {
		orderRepository.save(order);
		
	}

	public ShoppingOrder findOrderById(int id) {
		return orderRepository.findById(id).orElseThrow();
	}

	
}
