package com.vishnu.my_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vishnu.my_shop.dto.Customer;

public interface CustomerRepository extends JpaRepository<Customer,Integer>{

	boolean existsByMobile(long mobile);

	boolean existsByEmail(String email);

	Customer findByEmail(String email);

}
