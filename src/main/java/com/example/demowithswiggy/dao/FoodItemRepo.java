
package com.example.demowithswiggy.dao;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demowithswiggy.model.*;
public interface FoodItemRepo extends JpaRepository<FoodItem, Long>{

}