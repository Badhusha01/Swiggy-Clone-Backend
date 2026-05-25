package com.example.demowithswiggy.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demowithswiggy.model.*;

public interface FoodOrderRepo extends JpaRepository<FoodOrder, Long> {
    
    List<FoodOrder> findByDeliveryPartnerId(Long partnerId);
}