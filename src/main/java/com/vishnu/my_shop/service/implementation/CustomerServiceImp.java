package com.vishnu.my_shop.service.implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.vishnu.my_shop.dao.CustomerDao;
import com.vishnu.my_shop.dao.ItemDao;
import com.vishnu.my_shop.dao.ProductDao;
import com.vishnu.my_shop.dao.ShoppingOrderDao;
import com.vishnu.my_shop.dto.Cart;
import com.vishnu.my_shop.dto.Customer;
import com.vishnu.my_shop.dto.Item;
import com.vishnu.my_shop.dto.Product;
import com.vishnu.my_shop.dto.ShoppingOrder;
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
	
	
	@Autowired
	ShoppingOrderDao orderDao;

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
	//mailHelper.resendOtp(customer);
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
		  return "redirect:/";
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
			
				List<Item> items = cart.getItems();
				if(items.isEmpty()) {
					session.setAttribute("failMessage","No Items in Cart");
					return "redirect:/";
				}else {
					//System.err.println(items);
					//session.setAttribute("successMessage","Items Found");
					map.put("items", items);
					
					return "ViewCart";
				}
		
		}
	}


	@Override
	public String removeFromCart(int id, HttpSession session) {
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


	@Override
	public String paymentPage(HttpSession session, ModelMap map) {
		Customer customer=(Customer) session.getAttribute("customer");
		if(customer==null) {
			session.setAttribute("failMessage","Invalid Session");
			return "redirect:/signin";
		}else {
			List<Item> items = customer.getCart().getItems();
			if(items.isEmpty()) {
				session.setAttribute("failMessage","Nothing To Buy");
				return "redirect:/";
			}else {
				//to calculate total price
				double price=items.stream().mapToDouble(x->x.getPrice()).sum();
				try {
					RazorpayClient razorPay=new RazorpayClient("rzp_test_qN732L3UQRx9RT","0ErxOc72qdY7xjriCgZN6Y8P");
					JSONObject orderRequest = new JSONObject();
					orderRequest.put("amount",price*100);
					orderRequest.put("currency","INR");
					Order order = razorPay.orders.create(orderRequest);
					
					ShoppingOrder myOrder=new ShoppingOrder();
					myOrder.setDateTime(LocalDateTime.now());
					myOrder.setItems(items);
					myOrder.setOrderId(order.get("id"));
					myOrder.setStatus(order.get("status"));
					myOrder.setTotalPrice(price);
					
					orderDao.saveOrder(myOrder);
					
					
					
					map.put("key","rzp_test_qN732L3UQRx9RT");
					map.put("myOrder", myOrder);
					map.put("customer",customer);
					
					customer.getOrders().add(myOrder);
					customerDao.save(customer);
					session.setAttribute("customer",customerDao.findById(customer.getId()));
					return "PaymentPage";
					
				} catch (RazorpayException e) {
					e.printStackTrace();
					return "redirect:/";
				}
			}
		}
	}


	@Override
	public String confirmOrder(HttpSession session, int id, String razorpay_payment_id) {
		Customer customer=(Customer) session.getAttribute("customer");
		if(customer==null) {
			session.setAttribute("failMessage","Invalid Session");
			return "redirect:signin";
		}else {
			for (Item item : customer.getCart().getItems()) {
				Product product = productDao.findByName(item.getName());
				product.setStock(product.getStock() - item.getQuantity());
				productDao.save(product);
			}
	         ShoppingOrder order=orderDao.findOrderById(id);
	         order.setPaymentId(razorpay_payment_id);
	         order.setStatus("success");
	         orderDao.saveOrder(order);
//	         customer.setCart(new Cart());
	         customer.getCart().setItems(new ArrayList<Item>());
//	         customer.setCart(new Cart());
	         customerDao.save(customer);
	         session.setAttribute("customer",customerDao.findById(customer.getId()));
	         session.setAttribute("successMessage","Order Placed Success");
	         return "redirect:/";
		}
	}


	@Override
	public String viewOrders(HttpSession session, ModelMap map) {
		  Customer customer= (Customer) session.getAttribute("customer");
		  if(customer==null) {
				session.setAttribute("failMessage","Invalid Session");
				return "redirect:signin";
			}else {
				List<ShoppingOrder> orders = customer.getOrders();
				if(orders==null || orders.isEmpty()) {
					session.setAttribute("failMessage","No Orders Yet");
					return "redirect:/";
				}else {
					map.put("orders", orders);
					return "ViewOrders";
				}
			}
	}

}
