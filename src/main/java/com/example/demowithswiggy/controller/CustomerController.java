package com.example.demowithswiggy.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demowithswiggy.dao.*;
import com.example.demowithswiggy.model.*;


@RestController
@RequestMapping("/customer")
@CrossOrigin(origins = "*")
public class CustomerController {
    
    @Autowired
	UserRepo ur;
    @Autowired
	FoodItemRepo fir;
    @Autowired
	FoodOrderRepo foor;
    @GetMapping("/foods")
    public List<FoodItem> viewFoods() {
        return fir.findAll();
    }
    
    @PostMapping("/order/{customerId}")
    public FoodOrder placeOrder(
            @PathVariable Long customerId,
            @RequestBody List<Long> foodIds) {

        FoodOrder order = new FoodOrder();
        
        User customer = ur.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
            
        order.setCustomer(customer);

        // Iterable-ah List-ah maathi kudukka casting pannanum mapla
        List<FoodItem> items = (List<FoodItem>) fir.findAllById(foodIds);
        order.setItems(items); 
        
        order.setStatus(OrderStatus.PLACED);

        return foor.save(order);
    }

    
	
}