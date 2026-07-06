package com.foodapp.FoodDeliveryApp_2.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "past_order_items")
public class PastOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;
    private int quantity;
    private double price;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private PastOrder pastOrder;

    public PastOrderItem() {}

    public PastOrderItem(String itemName, int quantity, double price, PastOrder pastOrder) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.pastOrder = pastOrder;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public PastOrder getPastOrder() {
		return pastOrder;
	}

	public void setPastOrder(PastOrder pastOrder) {
		this.pastOrder = pastOrder;
	}

    // Getters and Setters
    
}