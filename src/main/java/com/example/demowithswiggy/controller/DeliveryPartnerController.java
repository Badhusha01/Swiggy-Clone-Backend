package com.example.demowithswiggy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import com.example.demowithswiggy.dao.*;
import com.example.demowithswiggy.model.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/delivery")
public class DeliveryPartnerController {

    @Autowired
    FoodOrderRepo foor;
    
    @GetMapping("/orders/{partnerId}")
    public List<FoodOrder> myOrders(@PathVariable Long partnerId) { // int to Long
        return foor.findByDeliveryPartnerId(partnerId);
    }

    @PutMapping("/status/{orderId}")
    public FoodOrder updateStatus(@PathVariable Long orderId) { 
        FoodOrder order = foor.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
            
        order.setStatus(OrderStatus.DELIVERED);
        return foor.save(order);
    }
}