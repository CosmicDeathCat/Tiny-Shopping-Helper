package UI;

import Core.*;

import java.util.ArrayList;
import java.util.Scanner;

public class ShoppingMainConsole {
    public static ArrayList<ShoppingItem> Cart = new ArrayList<ShoppingItem>();
    public static double Subtotal = 0.0;
    public static double Total = 0.0;

    public static void main(String[] args) {
        Cart.clear();
        Scanner scnr = new Scanner(System.in);
        while (true) {
            shoppingCartInput(scnr);
        }
    }
    public static double calculateTotal (ArrayList<ShoppingItem> items) {
        double total = 0.0;
        for (ShoppingItem item : items) {
            total += item.getTotalPrice(true);
        }
        return totalAfterSalesCalc(items, total);
    }

    public static double calculateSubtotal (ArrayList<ShoppingItem> items) {
        double subtotal = 0.0;
        for (ShoppingItem item : items) {
            subtotal += item.getTotalPrice(false);
        }
        return totalAfterSalesCalc(items, subtotal);
    }

    public static double totalAfterSalesCalc(ArrayList<ShoppingItem> items, double subtotal) {
        for (ShoppingItem item : items) {
            if (item.getSaleType() == SaleType.BuyXGetPercentOffTotal) {
                subtotal = SalesCalculator.buyXgetPercentOffTotal(item, item.getAmountX(), subtotal, item.getPercentOff());
            }
            if (item.getSaleType() == SaleType.AmountOffTotal) {
                subtotal = SalesCalculator.amountOffTotal(subtotal, item.getAmountOff());
            }
        }
        return Math.round(subtotal * 100.0) / 100.0;
    }

    public static void printReceipt(ArrayList<ShoppingItem> items) {
        // Define a format string for the header and items.
        // For example, allocate 20 spaces for the item name, 10 spaces for quantity, etc.
        String headerFormat = "%-20s%-10s%-10s%-10s%-10s%n"; // %n is used for newline
        String itemFormat = "%-20s%-10d%-10.2f%-10.2f%-10.2f%n"; // %.2f will format the number with 2 decimal places

        // Print header
        System.out.printf(headerFormat, "Item", "Quantity", "Price", "Tax Rate", "Total");

        // Print each item
        for (ShoppingItem item : items) {
            System.out.printf(itemFormat,
                    item.getName(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getTaxRate(),
                    item.getTotalPrice(false)
            );
        }

        // Print total
        System.out.println("Total: " + String.format("%.2f", calculateTotal(items)));
    }

    public static void shoppingCartInput (Scanner scnr) {
        int input = -1;
        System.out.println("What would you like to do?");
        System.out.println("1. Add an item to your cart");
        System.out.println("2. Remove an item from your cart");
        System.out.println("3. View your cart");
        System.out.println("4. Checkout");
        System.out.println("5. Exit");
        try {
            input = scnr.nextInt();
            if (input < 1 || input > 5) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                shoppingCartInput(scnr);
                return;
            }
            switch (input) {
                case 1:
                    scnr.nextLine();
                    Cart.add(createShoppingItem(scnr));
                    break;
                case 2:
                    scnr.nextLine();
                    System.out.println("Enter the name of the item you would like to remove: ");
                    String name = scnr.nextLine();
                    for (ShoppingItem item : Cart) {
                        if (item.getName().equals(name)) {
                            Cart.remove(item);
                        }
                    }
                    break;
                case 3:
                    printReceipt(Cart);
                    break;
                case 4:
                    printReceipt(Cart);
                case 5:
                    System.exit(0);
                default:
                    scnr.nextLine();
                    System.out.println("Invalid input. Please enter a number between 1 and 5.");
                    shoppingCartInput(scnr);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a number between 1 and 5.");
            shoppingCartInput(scnr);
        }
    }
    public static ShoppingItem createShoppingItem (Scanner scnr) {
        ShoppingItem item = new ShoppingItem();
        System.out.print("Enter the name of the item: ");
        item.setName(scnr.nextLine());
        try {
            System.out.print("Enter the quantity of the item: ");
            item.setQuantity(scnr.nextInt());
            scnr.nextLine();
        } catch (Exception e) {
            System.out.print("Invalid quantity. Please enter a number.");
            item.setQuantity(scnr.nextInt());
            scnr.nextLine();
        }
        try {
            System.out.print("Enter the price of the item: ");
            item.setPrice(scnr.nextDouble());
            scnr.nextLine();
        } catch (Exception e) {
            System.out.print("Invalid price. Please enter a number.");
            item.setPrice(scnr.nextDouble());
            scnr.nextLine();
        }
        try {
            System.out.print("Enter the sale type of the item: ");
            item.setSaleType(SaleType.valueOf(scnr.nextLine()));
        } catch (Exception e) {
            System.out.print("Invalid sale type. Please enter a valid sale type.");
            item.setSaleType(SaleType.valueOf(scnr.nextLine()));
        }
        try {
            System.out.print("Enter the tax rate of the item: ");
            item.setTaxRate(scnr.nextDouble());
            scnr.nextLine();
        } catch (Exception e) {
            System.out.print("Invalid tax rate. Please enter a number.");
            item.setTaxRate(scnr.nextDouble());
            scnr.nextLine();
        }
        return item;

    }
}
