package Core;

public class SalesCalculator {
    public static double percentOff (ShoppingItem item, double percentOff) {
        return item.getPrice() - (item.getPrice() * percentOff / 100);
    }
    public static double buyXgetPercentOffTotal (ShoppingItem item, int x, double subtotal, double percentOff) {
        if (item.getQuantity() >= x) {
            return subtotal - (subtotal * percentOff / 100);
        }
        return item.getPrice() * item.getQuantity();
    }
    public static double buyXgetYPercentOff (ShoppingItem item, int x, double percentOff) {
        if (item.getQuantity() >= x) {
            return item.getPrice() - (item.getPrice() * percentOff / 100);
        }
        return item.getPrice() * item.getQuantity();
    }
    public static double buyXgetYFree (ShoppingItem item, int x, int y) {
        if (item.getQuantity() >= x) {
            return item.getPrice() * (item.getQuantity() - y);
        }
        return item.getPrice() * item.getQuantity();
    }
    public static double amountOff (ShoppingItem item, double amountOff) {
        return item.getPrice() - amountOff;
    }
    public static double amountOffEach (ShoppingItem item, double amountOff) {
        double subtotal = 0;
        for (int i = 0; i < item.getQuantity(); i++) {
            subtotal += item.getPrice() - amountOff;
        }
        return subtotal;
    }
    public static double amountOffTotal (double subTotal, double amountOff) {
        return subTotal - amountOff;
    }
}
