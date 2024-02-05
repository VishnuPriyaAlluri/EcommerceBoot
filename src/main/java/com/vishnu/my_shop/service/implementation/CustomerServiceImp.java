package com.vishnu.my_shop.service.implementation;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;


import com.vishnu.my_shop.dao.CustomerDao;
import com.vishnu.my_shop.dao.ItemDao;
import com.vishnu.my_shop.dao.ProductDao;
import com.vishnu.my_shop.dto.Cart;
import com.vishnu.my_shop.dto.Customer;
import com.vishnu.my_shop.dto.Item;
import com.vishnu.my_shop.dto.Product;
import com.vishnu.my_shop.helper.AES;
import com.vishnu.my_shop.helper.MailSendingHelper;
import com.vishnu.my_shop.service.CustomerService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Service
public class CustomerServiceImp implements CustomerService {
	
	@Autowired
	CustomerDao customerDao;
	
	@Autowired
	ProductDao productDao;
	
	@Autowired
	MailSendingHelper mailHelper;
	
	
	
	@Autowired
	ItemDao itemDao;

	@Override
	public String save(@Valid Customer customer, BindingResult result) {
		
		if(customerDao.checkEmailDuplicate(customer.getEmail()))
			result.rejectValue("email","error.email","Account Already Exists With this Email");
		
		if(customerDao.checkMobileDuplicate(customer.getMobile()))
			result.rejectValue("mobile","error.mobile","Account Already Exists With this mobile");
		
		if(result.hasErrors())
			return "Signup";
		else {
		customer.setPassword(AES.encrypt(customer.getPassword(),"123"));
		customer.setRole("USER");
		customerDao.save(customer);
		return "redirect:/send-otp/"+customer.getId();
		}
	}

	
   @Override
	public String sendOtp(int id, ModelMap map) {
		Customer customer=customerDao.findById(id);
		customer.setOtp(new Random().nextInt(100000,999999));
		//mailHelper.sendOtp(customer);
		customerDao.save(customer);
		map.put("id",id);
		map.put("successMessage","Otp Sent Seuccess");
		return "VerifyOtp";
	}
   
   @Override
	public String verifyOtp(int id,int otp,ModelMap map,HttpSession session) {
		Customer customer = customerDao.findById(id);
		if(customer.getOtp()==otp) {
		customer.setVerified(true);
		customerDao.save(customer);
		session.setAttribute("successMessage","Account Crated Success");
			return "redirect:/signin";
		}
		else {
			map.put("failMessage", "Invalid Otp, Try again");
			map.put("id",id);
			return "VerifyOtp";
		}
	}


@Override
public String resendOtp(int id, ModelMap map) {
	Customer customer=customerDao.findById(id);
	
	customer.setOtp(new Random().nextInt(100000,999999));
	mailHelper.resendOtp(customer);
	customerDao.save(customer);
	map.put("id",id);
	map.put("successMEssage","Otp ReSent Seuccess");
	return "VerifyOtp";
}


@Override
public String login(String email, String password, ModelMap map, HttpSession session) {
	    Customer customer= customerDao.findByEmail(email);
	    if(customer==null)
	    {
	    	session.setAttribute("failMessage","Invalid Email");
	    }
	    else {
	    	if(AES.decrypt(customer.getPassword(), "123").equals(password)) {
	    		
	    		if(customer.isVerified()) {
	    			session.setAttribute("customer", customer);
	    			session.setAttribute("successMessage","Login Success");
	    			return "redirect:/";
	    		}
	    		else {
	    			return resendOtp(customer.getId(),map);
	    		}
	    	}
	    	else {
	    		session.setAttribute("failMessage","Incorrect Password");
	    	}
	    }
	    return "signin";
}


   @Override
   public String viewProduct(HttpSession session, ModelMap map) {
	List<Product> products = productDao.findAll();
	if (products.isEmpty()) {
		session.setAttribute("failMessage", "No Products Present");
		return "redirect:/admin";
	}else {
	map.put("products", products);
	return "ViewProducts";
	}
}


    @Override
    public String addToCart(int id, HttpSession session) {
	    Customer customer = (Customer) session.getAttribute("customer");
	    if(customer==null) {
	    	session.setAttribute("failMessage","Invalid Session");
	    	return "redirect:/";
	    }else {
	    	Product product = productDao.findById(id);
	    	if(product.getStock()>0) {
	    		Cart cart=customer.getCart();
	    		List<Item> items = cart.getItems();
	    		if(items.isEmpty()) {
	    			Item item=new Item();
	    			item.setCategory(product.getCategory());
	    			item.setDescription(product.getDescription());
	    			item.setImagePath(product.getImagePath());
	    			item.setName(product.getName());
	    			item.setPrice(product.getPrice());
	    			item.setQuantity(1);
	    			items.add(item);
	    			session.setAttribute("successMessage","Item Added to Cart Success");
	    		}else {
	    			boolean flag=true;
	    			for(Item item:items) {
	    				if(item.getName().equals(product.getName())) {
	    					flag=false;
	    					if(item.getQuantity()<product.getStock()) {
	    						item.setQuantity(item.getQuantity()+1);
	    						item.setPrice(item.getPrice()+product.getPrice());
	    						session.setAttribute("successMessage","Item Added To Cart Success");
	    					}else {
	    						session.setAttribute("failMessage","Out Of Stock");
	    					}
	    					break;
	    				}
	    				
	    			}
	    			if(flag) {
	    				Item item=new Item();
		    			item.setCategory(product.getCategory());
		    			item.setDescription(product.getDescription());
		    			item.setImagePath(product.getImagePath());
		    			item.setName(product.getName());
		    			item.setPrice(product.getPrice());
		    			item.setQuantity(1);
		    			items.add(item);
		    			session.setAttribute("successMessage","Item Added to Cart Success");
		    		}
	    		}
	    		 customerDao.save(customer);
	    		 session.setAttribute("customer",customerDao.findById(customer.getId()));
	    		    return "redirect:/products";
	    		}
	    	else {
	    		session.setAttribute("failMessage","Out Of Stock");
	    		return "redirect:/";
	    	}
	    }
	   
    }


	@Override
	public String viewCart(HttpSession session,ModelMap map) {
		Customer customer=(Customer) session.getAttribute("customer");
		if(customer==null) {
			session.setAttribute("failMessage","Invalid Session");
			return "redirect:/signin";
		}else {
			Cart cart=customer.getCart();
			if(cart==null) {
				session.setAttribute("failMessage","No Cart Found");
				return "redirect:/";
			}else {
				List<Item> items = cart.getItems();
				if(items.isEmpty()) {
					session.setAttribute("failMessage","No Items Present");
					return "redirect:/";
				}else {
					//System.err.println(items);
					map.put("items", items);
					session.setAttribute("successMessage","Items Found");
					return "ViewCart";
				}
			}
		}
	}


	@Override
	public String removeCart(int id, HttpSession session) {
		Customer customer = (Customer) session.getAttribute("customer");
		if(customer==null) {
			session.setAttribute("failMessage","Invalid Session");
			return "redirect:/signin";
		}else {
		     Item item=itemDao.findById(id);
		     if(item.getQuantity()==1) {
		    	 //remove mapping
		    	 customer.getCart().getItems().remove(item);
		    	 customerDao.save(customer);
		    	 session.setAttribute("customer",customerDao.findById(customer.getId()));
		    	 itemDao.delete(item);
		    	 session.setAttribute("successMessage","Item Removed From Cart Success");
		     }else {
		    	 item.setPrice(item.getPrice()-(item.getPrice()/item.getQuantity()));
		    	 item.setQuantity(item.getQuantity()-1);
		    	 itemDao.save(item);
		    	 session.setAttribute("successMessage","Item Quantity Reduced By 1");
		     }
		}
		session.setAttribute("customer",customerDao.findById(customer.getId()));
		return "redirect:/cart";
		
	}

}
