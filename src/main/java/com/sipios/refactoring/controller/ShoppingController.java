package com.sipios.refactoring.controller;

import com.sipios.refactoring.exception.CustomerException;
import com.sipios.refactoring.model.Price;
import com.sipios.refactoring.model.ShoppingCart;
import com.sipios.refactoring.service.ShoppingServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopping")
public class ShoppingController {

    @Autowired
    private ShoppingServiceInterface shoppingServiceInterface;

    private Logger logger = LoggerFactory.getLogger(ShoppingController.class);

    @PostMapping
    public Price getPrice(@RequestBody @NonNull ShoppingCart shoppingCart) {

        if (shoppingCart.getItems().size() <= 1 ) {
            return new Price("0.0", "Shopping cart must contain at least one item.");
        }

        try {
            double discount = shoppingServiceInterface.computeDiscount(shoppingCart.getCustomerType());
            logger.info(String.format("SHOPPING CART : Customer with type (%s) will beneficiates a discount of (%s)", shoppingCart.getCustomerType(), discount));

            double price = shoppingServiceInterface.computeAmount(shoppingCart.getItems(), discount);
            logger.info(String.format("SHOPPING CART : Customer has to pay (%s€)", price));

            shoppingServiceInterface.checkAmount(price, shoppingCart.getCustomerType());
            logger.info("SHOPPING CART :total amount checked");

            return new Price(String.valueOf(price), "Shopping cart computed with success");
            } catch (CustomerException ce) {
                switch (ce.getErrorCode()) {
                    case UNKNOWN_CUSTOMER_TYPE:
                        logger.error(String.format("SHOPPING CART : Customer type (%s) is unknwon", shoppingCart.getCustomerType()));
                        return new Price("N/A", "The customer has an unknown type, transaction cancelled");
                    case UNKNOWN_ITEM:
                        logger.error("SHOPPING CART : At leat one item is unknwon");
                        return new Price("N/A", "The customer has at least one unknown item in the shopping card, transaction cancelled");
                    case FORBIDDEN_AMOUNT:
                        logger.error(String.format("SHOPPING CART : total amount is too high for a customer with type (%s)", shoppingCart.getCustomerType()));
                        return new Price("N/A", "The customer has a shopping cart amount too high for his type, transaction cancelled");
                    default:
                        logger.error(String.format("SHOPPING CART : an error occured : %s", ce.getMessage()));
                        return new Price("N/A", "An error occured during shopping cart computing, please try later");
                }
            } catch (Exception e) {
            logger.error(String.format("SHOPPING CART : an error occured : %s", e.getMessage()));
            return new Price("N/A", "An error occured during shopping cart computing, please try later");
            }
        }
}
