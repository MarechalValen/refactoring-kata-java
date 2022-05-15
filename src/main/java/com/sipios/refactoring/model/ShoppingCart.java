package com.sipios.refactoring.model;

import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    @NonNull
    private List<Item> items;

    @NonNull
    private String customerType;

    /**
     * Default Constructor
     */
    public ShoppingCart() {}

    public ShoppingCart(List<Item> items, String customerType) {
        this.items = items;
        this.customerType = customerType;
    }

    public List<Item> getItems() {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }
}
