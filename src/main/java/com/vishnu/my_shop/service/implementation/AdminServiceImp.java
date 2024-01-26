package com.vishnu.my_shop.service.implementation;




import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.vishnu.my_shop.dao.ProductDao;
import com.vishnu.my_shop.dto.Customer;
import com.vishnu.my_shop.dto.Product;
import com.vishnu.my_shop.service.AdminService;

import jakarta.servlet.http.HttpSession;


@Service
public class AdminServiceImp implements AdminService{
	
	@Autowired
	ProductDao productDao;

	@Override
	public String loadDashBoard(HttpSession session) {
		Customer customer=(Customer) session.getAttribute("customer");
		if(customer==null) {
			session.setAttribute("failMessage","Invalid Session");
			return "redirect:/signin";
		}
		else {
			if(customer.getRole().equals("ADMIN"))
			{
				return "AdminDashBoard";
			}
			else {
				session.setAttribute("failMessage","You Are Unauthorized to Access This URL");
				return "redirect:/";
			}
		}
	}

	@Override
	public String loadAddProduct(HttpSession session) {
		Customer customer=(Customer) session.getAttribute("customer");
		if(customer==null) {
			session.setAttribute("failMessage","Invalid Session");
			return "redirect:/signin";
		}
		else {
			if(customer.getRole().equals("ADMIN"))
			{
				return "AddProduct";
			}
			else {
				session.setAttribute("failMessage","You Are Unauthorized to Access This URL");
				return "redirect:/";
			}
		}
	}

	@Override
	public String addProduct(Product product,BindingResult result,MultipartFile picture,HttpSession session,ModelMap map) {
		Customer customer=(Customer) session.getAttribute("customer");
		if(customer==null) {
			session.setAttribute("failMessage","Invalid Session");
			return "redirect:/signin";
		}
		else {
			if(customer.getRole().equals("ADMIN"))
			{
				if(productDao.checkByName(product.getName()))
					result.rejectValue("name","error.name","Product with same name already exists");

				if(result.hasErrors()) {
					return "AddProduct";
				}
				else {
//				System.out.println(product);
			
//					try {
//						byte[]image=new byte[picture.getInputStream().available()];
//						picture.getInputStream().read(image);
//						product.setImage(image);
//						productDao.save(product);
//						session.setAttribute("successMessage","Product Added Successfully");
//						return "redirect:/admin";
//					} catch (IOException e) {
//						session.setAttribute("failMessage","Something went wrong");
//						return "redirect:/";
//					}
					product.setImagePath("/images/"+product.getName()+".jpg");
					productDao.save(product);
					
					File file=new File("src/main/resources/static/images");
					if(!file.isDirectory())
						file.mkdir();
					try {
						Files.write(Paths.get("src/main/resources/static/images",product.getName()+".jpg"),picture.getBytes());
					}
					catch(IOException e) {
						session.setAttribute("failMessage", "You are unauthorized to access this url");
						return "redirect:/";
					}
					session.setAttribute("successMessage","Product Added Successfully");
					return "redirect:/admin";
				
				}
			}
			else {
				session.setAttribute("failMessage","You Are Unauthorized to Access This URL");
				return "redirect:/";
			}
		}
	}

}
