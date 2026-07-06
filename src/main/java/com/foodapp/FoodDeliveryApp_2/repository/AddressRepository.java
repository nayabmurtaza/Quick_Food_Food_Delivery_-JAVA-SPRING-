package com.foodapp.FoodDeliveryApp_2.repository;

import com.foodapp.FoodDeliveryApp_2.entity.Address;
import com.foodapp.FoodDeliveryApp_2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
}