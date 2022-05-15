package com.sipios.refactoring.service;

import com.sipios.refactoring.exception.CustomerException;
import com.sipios.refactoring.model.Item;

import java.util.List;

public interface ShoppingServiceInterface {

    /**
     * Compute discount for a given customer type
     *
     * @param customerType customer type
     * @return discount value
     *
     * @throws CustomerException launched if the customer type is unknown
     */
    double computeDiscount(String customerType) throws CustomerException;

    /**
     * compute final price for a given list of items and discount
     * @param items list of items
     * @param discount discount value
     * @return final price value
     *
     * @throws CustomerException launched if at least one article is unknown
     */
    double computeAmount(List<Item> items, double discount) throws CustomerException;

    /**
     * Check autorised Amount for a given customer type
     * @param price total price
     * @param customerType customer type
     */
    void checkAmount(double price, String customerType) throws CustomerException;
}
