package com.example.demowithswiggy.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demowithswiggy.model.User;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}