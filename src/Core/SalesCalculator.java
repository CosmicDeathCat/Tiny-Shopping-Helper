package core;

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
        return item.getPrice() * item.getQuantity() - ((item.getPrice() * item.getQuantity()) * percentOff / 100);
    }

    /**
     * Calculates the price of an item with a buy x get percent off total sale
     * @param item
     * @param x
     * @param subtotal
     * @param percentOff
     * @return
     */
    public static double buyXgetPercentOffTotal (ShoppingItem item, int x, double subtotal, double percentOff) {
        if (item.getQuantity() >= x) {
            return subtotal - (subtotal * percentOff / 100);
        }
        return item.getPrice() * item.getQuantity();
    }

    /**
     * Calculates the price of an item with a buy x get y percent off sale
     * @param item
     * @param x
     * @param percentOff
     * @return
     */
    public static double buyXgetYPercentOff (ShoppingItem item, int x, double percentOff) {
        if (item.getQuantity() >= x) {
            return item.getPrice() - (item.getPrice() * percentOff / 100);
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
        return item.getPrice() - amountOff;
    }
    /**
     * Calculates the price of an item with an amount off each sale
     * @param item
     * @param amountOff
     * @return
     */
    public static double amountOffEach (ShoppingItem item, double amountOff) {
        double subtotal = 0;
        for (int i = 0; i < item.getQuantity(); i++) {
            subtotal += item.getPrice() - amountOff;
        }
        return subtotal;
    }
    /**
     * Calculates the price of an item with an amount off total sale
     * @param subTotal
     * @param amountOff
     * @return
     */
    public static double amountOffTotal (double subTotal, double amountOff) {
        return subTotal - amountOff;
    }
}
