package com.foodapp.FoodDeliveryApp_2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodapp.FoodDeliveryApp_2.entity.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    // Basic CRUD methods like findAll() or findById() are generated automatically!
}
