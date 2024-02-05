package com.vishnu.my_shop.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vishnu.my_shop.dto.Item;
import com.vishnu.my_shop.repository.ItemRepository;

@Repository
public class ItemDao {
	@Autowired
    ItemRepository itemRepository;

	public Item findById(int id) {
		return itemRepository.findById(id).orElse(null);
		
	}

	

	public void delete(Item item) {
		itemRepository.delete(item);		
	}



	public void save(Item item) {
		itemRepository.save(item);
	}
}
