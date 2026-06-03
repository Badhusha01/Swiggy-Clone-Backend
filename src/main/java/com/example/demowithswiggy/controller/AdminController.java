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

// 🔥 '*' தூக்கிட்டு, செக்யூரிட்டி ஃபைலுக்கு ஏத்த மாதிரி வெர்சல் மற்றும் லோக்கல்ஹோஸ்ட்டை மட்டும் அலோவ் பண்றோம்!
@CrossOrigin(origins = {"http://localhost:5173", "https://swiggy-clone-frontend-six.vercel.app"}, allowedHeaders = "*", allowCredentials = "true")
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
    
    // 🔥 இதுதான் மெயின் என்கோடர் Bean!
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
        System.out.println("--- Foods fetch request received! ---"); 
        return fir.findAll();
    }
    
    @GetMapping("/partners")
    public List<User> getAllPartners() {
        return ur.findAll().stream()
                .filter(user -> user.getRole() != null && 
                       (user.getRole() == Role.DELIVERY_PARTNER))
                .toList();
    }
    
    @PostMapping(value = "/order", consumes = "application/json", produces = "application/json")
    public FoodOrder placeOrder(@RequestBody java.util.Map<String, Object> request) {
        System.out.println("--- Inside placeOrder backend endpoint! ---");
        
        String email = (String) request.get("customerEmail");
        Object foodIdObj = request.get("foodId");
        
        if (email == null || foodIdObj == null) {
            throw new RuntimeException("Required fields missing in payload!");
        }

        Long foodId = Long.parseLong(foodIdObj.toString());

        User customer = ur.findAll().stream()
                .filter(u -> u.getEmail() != null && u.getEmail().equalsIgnoreCase(email.trim()))
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("User not found! Creating temporary customer for: " + email);
                    User newUser = new User();
                    
                    String nameFromEmail = email.split("@")[0];
                    String capitalizedName = nameFromEmail.substring(0, 1).toUpperCase() + nameFromEmail.substring(1);
                    
                    newUser.setName(capitalizedName);
                    newUser.setEmail(email.trim());
                    
                    // 🔥 இப்போ மேல இருக்குற ஸ்பிரிங் 'passwordEncoder' பீனை கரெக்ட்டா கூப்பிடும், 'null' எரர் வராது!
                    newUser.setPassword(passwordEncoder.encode("1234")); 
                    
                    newUser.setRole(com.example.demowithswiggy.model.Role.CUSTOMER); 
                    
                    return ur.save(newUser);
                });

        // 2. Find Food Item safely
        FoodItem food = fir.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food Item not found with ID: " + foodId));
                
        // 3. Create New Order
        FoodOrder newOrder = new FoodOrder();
        newOrder.setCustomer(customer);
        newOrder.setItems(java.util.List.of(food));
        
        newOrder.setStatus(com.example.demowithswiggy.model.OrderStatus.PLACED);

        System.out.println("--- Saving Order to Database! ---");
        return foor.save(newOrder);
    }
    
    // ❌ அந்த பழைய 'private PasswordEncoder passwordEncoder()' மெத்தடை அப்படியே தூக்கியாச்சு மாப்ள!
}