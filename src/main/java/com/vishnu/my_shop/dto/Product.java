package com.vishnu.my_shop.dto;




import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Component
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
   private int id;
	 @NotEmpty(message = "* It Is Compulsory Field ")
	 @Size(min=5,max=50,message = "* Should be between 5 to 50 characters")
   private String name;
	 @NotNull(message="* It Is Compulsory Field")
	 @DecimalMax(value="10000",inclusive=true,message="* Enter Proper Price")
	 @DecimalMin(value="100",inclusive=true,message="* Enter Proper Price")	
   private double price;
   @NotEmpty(message=" It Is Compulsory Field")
   @Size(min=10,max=50,message = "* Should be between 10 to 50 characters")
   private String category;
   @NotNull
   @DecimalMin(value="1",inclusive=true,message="Enter Stock minimum 1")
   private int stock;
   @NotEmpty
   @Size(min=20,max=50,message = "* Should be between 20 to 50 characters")
   private String description;
//   @Lob
//   @Column(columnDefinition = "MEDIUMBLOB")
//   byte[] image;
   private String imagePath;
}
