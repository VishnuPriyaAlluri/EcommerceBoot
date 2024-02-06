package com.vishnu.my_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vishnu.my_shop.dto.ShoppingOrder;

public interface ShoppingOrderRepository extends JpaRepository<ShoppingOrder, Integer> {

}
