package core;

import data.ShoppingItem;

/**
 * This class contains methods for calculating sales
 */
public class SalesCalculator {
    /**
     * Calculates the price of an item with a percent off sale
     * @param item
     * @param percentOff
     * @return
     */
    public static double percentOff (ShoppingItem item, double percentOff) {
        //this gets the percent off of the item by multiplying the price by the quantity and then multiplying that by the percent off
        return item.getPrice() * item.getQuantity() - ((item.getPrice() * item.getQuantity()) * percentOff / 100);
    }

    /**
     * Calculates the price of an item with a buy x get percent off total sale
     * @param item
     * @param x
     * @param percentOff
     * @return
     */
    public static double buyXgetPercentOffTotal (double subtotal, ShoppingItem item, int x, double percentOff) {
        //this if statement checks if the quantity is greater than or equal to x, and if it is, it calculates the price
        if (item.getQuantity() >= x) {
            return (subtotal * percentOff) / 100;
        }
        return subtotal;
    }

    /**
     * Calculates the price of an item with a buy x get y percent off sale
     * @param item
     * @param x
     * @param percentOff
     * @return
     */
    public static double buyXgetYPercentOff (ShoppingItem item, int x, double percentOff) {
        //this if statement checks if the quantity is greater than or equal to x, and if it is, it calculates the price
        if (item.getQuantity() >= x) {
            return item.getPrice() * item.getQuantity() - ((item.getPrice() * item.getQuantity()) * percentOff / 100);
        }
        return item.getPrice() * item.getQuantity();
    }
    /**
     * Calculates the price of an item with a buy x get y free sale
     * @param item
     * @param x
     * @param y
     * @return
     */
    public static double buyXgetYFree (ShoppingItem item, int x, int y) {
        //this if statement checks if the quantity is greater than or equal to x, and if it is, it calculates the price
        if (item.getQuantity() >= x) {
            return item.getPrice() * (item.getQuantity() - y);
        }
        return item.getPrice() * item.getQuantity();
    }

    /**
     * Calculates the price of an item with an amount off sale
     * @param item
     * @param amountOff
     * @return
     */
    public static double amountOff (ShoppingItem item, double amountOff) {
        //this gets the amount off of the item by multiplying the price by the quantity and then subtracting the amount off
        return item.getPrice() * item.getQuantity() - amountOff;
    }
    /**
     * Calculates the price of an item with an amount off each sale
     * @param item
     * @param amountOff
     * @return
     */
    public static double amountOffEach (ShoppingItem item, double amountOff) {
        //this is the total price for the item
        double subtotal = 0;
        //this for loop calculates the subtotal for each item
        for (int i = 0; i < item.getQuantity(); i++) {
            subtotal += item.getPrice() - amountOff;
        }
        return subtotal;
    }
}
