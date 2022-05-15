package com.sipios.refactoring.model;

public class Price {

    private String price;
    private String customerInformation;

    /**
     * Default constructor
     */
    public Price() {
    }

    public Price(String price, String customerInformation) {
        this.price = price;
        this.customerInformation = customerInformation;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCustomerInformation() {
        return customerInformation;
    }

    public void setCustomerInformation(String customerInformation) {
        this.customerInformation = customerInformation;
    }
}
