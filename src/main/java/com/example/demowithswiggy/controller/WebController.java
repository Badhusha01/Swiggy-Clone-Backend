package com.example.demowithswiggy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/Home")
    public String showHome() {
        return "forward:/index.html"; 
    }

    @GetMapping("/Admin")
    public String showAdmin() {
        return "forward:/admin.html";
    }

    @GetMapping("/Customer")
    public String showCustomer() {
        return "forward:/customer.html"; 
    }
    
    @GetMapping("/Delivery")
    public String showDelivery() {
        return "forward:/delivery.html";
    }

}