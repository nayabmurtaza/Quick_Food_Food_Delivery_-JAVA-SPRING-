package com.foodapp.FoodDeliveryApp_2.repository;

import com.foodapp.FoodDeliveryApp_2.entity.PastOrder;
import com.foodapp.FoodDeliveryApp_2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PastOrderRepository extends JpaRepository<PastOrder, Long> {
    List<PastOrder> findByUserOrderByOrderDateDesc(User user);
}