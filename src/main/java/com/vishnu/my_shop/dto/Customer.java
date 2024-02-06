package com.vishnu.my_shop.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Component
@Entity
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "* It Is Compulsory Field ")
    @Size(min=3,max=20,message = "* Should be between 3 to 20 characters")
    private String name;
    @NotEmpty(message = "* It Is Compulsory Field")
    @Email(message = "* Enter Proper Email Format")
    private String email;
    @NotEmpty(message="* It Is Compulsory Field")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "* Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character")
    private String password;
    @NotNull(message="* It Is Compulsory Field")
    @DecimalMax(value="9999999999",inclusive=true,message="* Enter Proper Mobile Number")
    @DecimalMin(value="6000000000",inclusive=true,message="* Enter Proper Mobile Number")
    private long mobile;
    @NotEmpty(message="* It Is Compulsory Field")
    private String gender;
    @NotNull(message=" * It Is Compulsory Field")
    @Past(message=" * It Is Proper DOB")
    private LocalDate dob;
    private String role;
    private int otp;
    private boolean verified;
    
    @OneToOne(cascade = CascadeType.ALL)
    Cart cart=new Cart();
    
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    List<ShoppingOrder> orders=new ArrayList<ShoppingOrder>();
    
}
