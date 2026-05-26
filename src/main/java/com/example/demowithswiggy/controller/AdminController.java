package com.example.demowithswiggy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demowithswiggy.model.*;
import java.util.List;
import com.example.demowithswiggy.dao.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
	RestaurantRepo rr;
    @Autowired
	UserRepo ur;
    @Autowired
	FoodItemRepo fir;
    @Autowired
	FoodOrderRepo foor;
    @Autowired
    private PasswordEncoder passwordEncoder;
	
    
    @PostMapping("/restaurant")
    public Resuturant get1(@RequestBody Resuturant r) {
        return rr.save(r);
    }
    @PostMapping("/food")
    public FoodItem get2(@RequestBody FoodItem f) {
        return fir.save(f);
    }
    
    @PutMapping("/assign/{orderId}/{partnerId}")
    public FoodOrder assignDelivery(
            @PathVariable Long orderId, 
            @PathVariable Long partnerId) { 

    	FoodOrder order = foor.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        
        User partner = ur.findById(partnerId)
            .orElseThrow(() -> new RuntimeException("Delivery Partner not found with ID: " + partnerId));

        order.setDeliveryPartner(partner);
        order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
        
        return foor.save(order);
    }

    @PostMapping("/user")
    public User addUser1(@RequestBody User u) {
    	u.setPassword(passwordEncoder.encode(u.getPassword()));
        return ur.save(u);
    }
    @GetMapping("/orders")
    public List<FoodOrder> getAllOrders() {
        return foor.findAll();
    }

    @PutMapping("/complete/{orderId}")
    public FoodOrder completeOrder(@PathVariable Long orderId) {
        FoodOrder order = foor.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        
        order.setStatus(OrderStatus.DELIVERED);
        
        return foor.save(order);
    }

    @GetMapping("/restaurants")
    public List<Resuturant> getRest() {
        return rr.findAll();
    }

    @GetMapping("/foods")
    public List<FoodItem> getFood() {
        System.out.println("--- Foods fetch request received! ---"); // Console check-ku
        return fir.findAll();
    }
    
    @GetMapping("/partners")
    public List<User> getAllPartners() {
        return ur.findAll().stream()
                .filter(user -> user.getRole() != null && 
                       (user.getRole() == Role.DELIVERY_PARTNER))
                .toList();
    }
    
    @PostMapping("/order")
    public FoodOrder placeOrder(@RequestBody java.util.Map<String, String> request) {
        String email = request.get("customerEmail");
        Long foodId = Long.parseLong(request.get("foodId"));
        // 1. Find Customer by Email
        User customer = ur.findAll().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Email not found: " + email));

        // 2. Find Food and Restaurant
        FoodItem food = fir.findById(foodId).get();
        // 3. Create New Order
        FoodOrder newOrder = new FoodOrder();
        newOrder.setCustomer(customer);
        newOrder.setItems(java.util.List.of(food)); // Single food order
        newOrder.setStatus(OrderStatus.PLACED);
        // Un model-la restaurant field irundha mattum idha vai:
        // newOrder.setRestaurant(rest); 

        return foor.save(newOrder);
    }
}