package com.foodapp.FoodDeliveryApp_2.entity;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<Integer, CartItem> items = new HashMap<>();

    public void addItem(CartItem item) {
        if (items.containsKey(item.getId())) {
            CartItem existing = items.get(item.getId());
            existing.setQuantity(existing.getQuantity() + item.getQuantity());
        } else {
            items.put(item.getId(), item);
        }
    }

    public void updateItem(int itemId, int quantity) {
        if (items.containsKey(itemId)) {
            if (quantity <= 0) items.remove(itemId);
            else items.get(itemId).setQuantity(quantity);
        }
    }

    public void removeItem(int itemId) { items.remove(itemId); }
    public Map<Integer, CartItem> getItems() { return items; }
    public double getTotalPrice() {
        return items.values().stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
    }
}
