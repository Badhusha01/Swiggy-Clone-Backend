
package com.example.demowithswiggy.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demowithswiggy.dao.UserRepo;
import com.example.demowithswiggy.model.Role;
import com.example.demowithswiggy.model.User;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {
	
	
	@Autowired
	UserRepo r;
	@Autowired
	PasswordEncoder e;
	
	@PostMapping("/register")
	public User register(@RequestBody User user) {
	    user.setPassword(e.encode(user.getPassword()));
	    
	    if (user.getRole() == null) {
	        user.setRole(Role.ROLE_CUSTOMER); 
	    }
	    
	    return r.save(user);
	}
	
	
	}

	
