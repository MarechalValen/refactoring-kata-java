package com.sipios.refactoring.service;

import com.sipios.refactoring.controller.ShoppingController;
import com.sipios.refactoring.exception.CustomerException;
import com.sipios.refactoring.exception.ExceptionErrorCode;
import com.sipios.refactoring.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ShoppingServiceImpl implements ShoppingServiceInterface {

    private Logger logger = LoggerFactory.getLogger(ShoppingController.class);

    /**
     * Compute discount for a given customer type
     *
     * @param customerType customer type
     * @return discount value
     *
     * @throws CustomerException launched if the customer type is unknown
     */
    @Override
    public double computeDiscount(String customerType) throws CustomerException {
        switch (customerType) {
            case "STANDARD_CUSTOMER":
                return 1;
            case "PREMIUM_CUSTOMER":
                return 0.9;
            case "PLATINUM_CUSTOMER":
                return 0.5;
            default:
                throw new CustomerException(ExceptionErrorCode.UNKNOWN_CUSTOMER_TYPE);
        }
    }

    /**
     * compute final price for a given list of items and discount
     * @param items list of items
     * @param discount discount value
     * @return final price value
     *
     * @throws CustomerException launched if at least one article is unknown
     */
    @Override
    public double computeAmount(List<Item> items, double discount) throws CustomerException {

        AtomicBoolean isUnknownItem = new AtomicBoolean(false);
        AtomicReference<Double> price = new AtomicReference<>((double) 0);
        items.forEach(item -> {
            switch (item.getType()) {
                case "TSHIRT" :
                    price.updateAndGet(v -> (v + 30 * item.getQuantity() * discount));
                    break;
                case "DRESS" :
                    price.updateAndGet(v -> (v + 50 * item.getQuantity() * (isSummer() ? 0.8 * discount : discount)));
                    break;
                case "JACKET" :
                    price.updateAndGet(v -> (v + 100 * item.getQuantity() * (isSummer() ? 0.9 * discount : discount)));
                    break;
                case "SWEATSHIRT" :
                    price.updateAndGet(v -> (v + 80 * item.getQuantity()));
                    break;
                default:
                    isUnknownItem.set(true);
                    logger.info(String.format("SHOPPING CART : (%s) item is unknown", item.getType()));
                    break;
            }
        });

        if (!isUnknownItem.get()) {
            return price.get();
        } else{
            throw new CustomerException(ExceptionErrorCode.UNKNOWN_ITEM);
        }
    }

    /**
     * Check autorised Amount for a given customer type
     *
     * @param price        total price
     * @param customerType customer type
     */
    @Override
    public void checkAmount(double price, String customerType) throws CustomerException {

        switch (customerType) {
            case "STANDARD_CUSTOMER" :
                if (price > 200) {
                    throw new CustomerException(ExceptionErrorCode.FORBIDDEN_AMOUNT);
                }
                break;

            case  "PREMIUM_CUSTOMER" :
                if (price > 800) {
                    throw new CustomerException(ExceptionErrorCode.FORBIDDEN_AMOUNT);
                }
                break;
            case  "PLATINUM_CUSTOMER" :
                if (price > 2000) {
                    throw new CustomerException(ExceptionErrorCode.FORBIDDEN_AMOUNT);
                }
                break;
            default:
                break;
        }

    }

    /**
     * Check if the current date is in summer or winter
     *
     * @return <code>true</code> if it's in summer, otherwise <code>false</code>
     */
    private boolean isSummer() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);
        return
            !(
                cal.get(Calendar.DAY_OF_MONTH) < 15 &&
                    cal.get(Calendar.DAY_OF_MONTH) > 5 &&
                    cal.get(Calendar.MONTH) == 5
            ) &&
                !(
                    cal.get(Calendar.DAY_OF_MONTH) < 15 &&
                        cal.get(Calendar.DAY_OF_MONTH) > 5 &&
                        cal.get(Calendar.MONTH) == 0
                );
    }
}
