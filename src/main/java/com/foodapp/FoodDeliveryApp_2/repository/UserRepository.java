package com.foodapp.FoodDeliveryApp_2.repository;

import com.foodapp.FoodDeliveryApp_2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}