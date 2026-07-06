package com.foodapp.FoodDeliveryApp_2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodapp.FoodDeliveryApp_2.entity.Menu;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    // Spring extracts parameters and queries custom properties safely based on method signatures
    List<Menu> findByRestaurantId(int restaurantId);
}
