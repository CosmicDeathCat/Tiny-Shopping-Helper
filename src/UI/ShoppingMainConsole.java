package ui;

import api.TaxRateClient;
import core.*;
import data.TaxRateResponse;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ShoppingMainConsole {
    public static ShoppingCart Cart = new ShoppingCart();

    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        TaxRateResponse taxRateResponse = enterTaxZipCode(scnr);
        Cart.setTaxRate(taxRateResponse.getTaxRate());
        while (true) {
            shoppingCartInput(scnr);
        }
    }
    /**
     * Prints the receipt for the items in the cart.
     * @param items The items in the cart.
     */
    public static void printReceipt(ArrayList<ShoppingItem> items) {
        String headerFormat = "%-20s%-10s%-10s%-10s%-10s%-10s%n";
        String itemFormat = "%-20s%-10d%-10.2f%-10.2f%-10.2f%-10.2f%n";

        // Print header
        System.out.printf(headerFormat, "Item", "Quantity", "Price", "Tax Rate", "SubTotal", "Total");

        // Print each item
        for (ShoppingItem item : items) {
            System.out.printf(itemFormat,
                    item.getName(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getTaxRate(),
                    item.getTotalPrice(false, false),
                    item.getTotalPrice(true, true)
            );
        }
        Cart.calculateSubTotal();
        Cart.calculateTotal();

        // Print SubTotal
        System.out.println("SubTotal: " + String.format("%.2f", Cart.getSubTotal()));
        // Print total
        System.out.println("Total: " + String.format("%.2f", Cart.getTotal()));
    }

    /**
     * Gets the user's input for what they want to do with their shopping cart.
     * @param scnr The scanner to get the user's input.
     */
    public static void shoppingCartInput(Scanner scnr) {
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
                System.out.println("Invalid input. Enter a number between 1 and 5.");
                shoppingCartInput(scnr);
                return;
            }
            switch (input) {
                case 1:
                    scnr.nextLine();
                    Cart.addItem(createShoppingItem(scnr));
                    break;
                case 2:
                    scnr.nextLine();
                    System.out.println("Enter the item's name of the item you would like to remove: ");
                    String name = scnr.nextLine();
                    for (ShoppingItem item : Cart.getItems()) {
                        if (item.getName().equals(name)) {
                            Cart.removeItem(item);
                        }
                    }
                    break;
                case 3:
                    printReceipt(Cart.getItems());
                    break;
                case 4:
                    printReceipt(Cart.getItems());
                case 5:
                    System.exit(0);
                default:
                    scnr.nextLine();
                    System.out.println("Invalid input. Enter a number between 1 and 5.");
                    shoppingCartInput(scnr);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Enter a number between 1 and 5.");
            shoppingCartInput(scnr);
        }
    }

    /**
     * Gets the user's input for the name of the item.
     * @param scnr
     * @return
     */
    public static String enterItemName(Scanner scnr) {
        System.out.print("What is the item called: ");
        String name = scnr.nextLine();
        for (ShoppingItem item : Cart.getItems()) {
            if (item.getName().equals(name)) {
                System.out.println("This item already exists. Enter a different name.");
                enterItemName(scnr);
                return name;
            }
        }
        return name;
    }

    /**
     * Gets the user's input for the quantity of the item.
     * @param scnr
     * @return
     */
    public static int enterItemQuantity(Scanner scnr) {
        try {
            System.out.print("How many are there: ");
            int quantity = scnr.nextInt();
            scnr.nextLine();
            return quantity;
        } catch (Exception e) {
            System.out.print("Invalid quantity. Please enter a number.");
            return enterItemQuantity(scnr);
        }
    }

    /**
     * Gets the user's input for the price of the item.
     * @param scnr
     * @return
     */
    public static double enterItemPrice(Scanner scnr) {
        try {
            System.out.print("What is the price: ");
            double price = scnr.nextDouble();
            scnr.nextLine();
            return price;
        } catch (Exception e) {
            System.out.print("Invalid price. Please enter a number.");
            return enterItemPrice(scnr);
        }
    }

    /**
     * Gets the user's input for the sale type of the item.
     * @param scnr
     * @return
     */
    public static SaleType enterItemSaleType(Scanner scnr) {
        try {
            System.out.println("Enter the sale type of the item:");
            System.out.println("0: None");
            System.out.println("1: Percent Off");
            System.out.println("2: Buy X Get Percent Off Total");
            System.out.println("3: Buy X Get Y Percent Off");
            System.out.println("4: Buy X Get Y Free");
            System.out.println("5: Amount Off");
            System.out.println("6: Amount Off Each");
            System.out.println("7: Amount Off Total");

            int input = scnr.nextInt();
            if (input < 0 || input > 7) {
                throw new Exception("Invalid input. Please enter a number between 0 and 7.");
            } else {
                switch (input) {
                    case 0 -> {
                        return SaleType.None;
                    }
                    case 1 -> {
                        return SaleType.PercentOff;
                    }
                    case 2 -> {
                        return SaleType.BuyXGetPercentOffTotal;
                    }
                    case 3 -> {
                        return SaleType.BuyXGetYPercentOff;
                    }
                    case 4 -> {
                        return SaleType.BuyXGetYFree;
                    }
                    case 5 -> {
                        return SaleType.AmountOff;
                    }
                    case 6 -> {
                        return SaleType.AmountOffEach;
                    }
                    case 7 -> {
                        return SaleType.AmountOffTotal;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Invalid input. Please enter a number between 0 and 7.");
            return enterItemSaleType(scnr);
        }
        return SaleType.None;
    }

    /**
     * Gets the user's input for the percent off of the item.
     * @param scnr
     * @return
     */
    public static double enterItemPercentOff(Scanner scnr) {
        try {
            System.out.print("Enter the percent off of the item: ");
            double percentOff = scnr.nextDouble();
            scnr.nextLine();
            return percentOff;
        } catch (Exception e) {
            System.out.print("Invalid percent off. Enter a number.");
            return enterItemPercentOff(scnr);
        }
    }

    /**
     * Gets the user's input for the amount off of the item.
     * @param scnr
     * @return
     */
    public static double enterItemAmountOff(Scanner scnr) {
        try {
            System.out.print("Enter the amount off of the item: ");
            double amountOff = scnr.nextDouble();
            scnr.nextLine();
            return amountOff;
        } catch (Exception e) {
            System.out.print("Invalid amount off. Enter a number.");
            return enterItemAmountOff(scnr);
        }
    }

    /**
     * Gets the user's input for the amount of items to buy of the item.
     * @param scnr
     * @return
     */
    public static int enterItemAmountX(Scanner scnr) {
        try {
            System.out.print("Enter the amount of items to buy: ");
            int amountX = scnr.nextInt();
            scnr.nextLine();
            return amountX;
        } catch (Exception e) {
            System.out.print("Invalid amount. Enter a number.");
            return enterItemAmountX(scnr);
        }
    }
/**
     * Gets the user's input for the amount of items to get free of the item.
     * @param scnr
     * @return
     */
    public static int enterItemAmountY(Scanner scnr) {
        try {
            System.out.print("Enter the amount of items to get free: ");
            int amountY = scnr.nextInt();
            scnr.nextLine();
            return amountY;
        } catch (Exception e) {
            System.out.print("Invalid amount. Enter a number.");
            return enterItemAmountY(scnr);
        }
    }

    /**
     * Gets the user's input for the tax rate of the item.
     * @param scnr
     * @return
     */
    public static TaxRateResponse enterTaxZipCode(Scanner scnr){
        String zipCodePattern = "^[0-9]{5}(?:-[0-9]{4})?$"; // Regex for validating US zip code

        while (true) {
            try {
                System.out.print("Enter your zip code to get the tax rate: ");
                String zipCode = scnr.nextLine();

                // Check if the zip code matches the pattern
                if (!Pattern.matches(zipCodePattern, zipCode)) {
                    System.out.println("Invalid zip code format. Please enter a 5-digit or 9-digit zip code.");
                    continue;
                }

                TaxRateClient taxRateClient = new TaxRateClient();
                TaxRateResponse taxRateResponse = taxRateClient.getTaxRateByZip(zipCode);

                if (taxRateResponse != null) {
                    System.out.println("Tax rate: " + taxRateResponse.getTaxRate());
                    return taxRateResponse;
                } else {
                    System.out.println("Failed to retrieve tax rate. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }
    }

    /**
     * Gets the user's input for the tax rate of the item.
     * @param scnr
     * @return
     */
//    public static double enterItemTaxRate(Scanner scnr) {
//        try {
//            System.out.print("Enter the tax rate of the item: ");
//            double taxRate = scnr.nextDouble();
//            scnr.nextLine();
//            return taxRate;
//        } catch (Exception e) {
//            System.out.print("Invalid tax rate. Enter a number.");
//            return enterItemTaxRate(scnr);
//        }
//    }

    /**
     * Gets the user's input for if the item has shipping.
     * @param scnr
     * @return
     */
    public static boolean enterItemHasShipping(Scanner scnr) {
        try {
            System.out.print("Does the item have shipping? (true/false): ");
            boolean hasShipping = scnr.nextBoolean();
            scnr.nextLine();
            return hasShipping;
        } catch (Exception e) {
            System.out.print("Invalid input. Enter if true or false.");
            return enterItemHasShipping(scnr);
        }
    }

    /**
     * Gets the user's input for the shipping cost of the item.
     * @param scnr
     * @return
     */
    public static double enterItemShippingCost(Scanner scnr) {
        try {
            System.out.print("Enter the shipping cost of the item: ");
            double shippingCost = scnr.nextDouble();
            scnr.nextLine();
            return shippingCost;
        } catch (Exception e) {
            System.out.print("Invalid shipping cost. Enter a number.");
            return enterItemShippingCost(scnr);
        }
    }

    /**
     * Creates a shopping item with the user's input.
     * @param scnr
     * @return
     */
    public static ShoppingItem createShoppingItem(Scanner scnr) {
        ShoppingItem item = new ShoppingItem();
        item.setName(enterItemName(scnr));
        item.setQuantity(enterItemQuantity(scnr));
        item.setPrice(enterItemPrice(scnr));
        item.setSaleType(enterItemSaleType(scnr));
        switch (item.getSaleType()) {
            case None -> {
            }
            case PercentOff -> {
                item.setPercentOff(enterItemPercentOff(scnr));
            }
            case BuyXGetPercentOffTotal -> {
                item.setAmountX(enterItemAmountX(scnr));
                item.setPercentOff(enterItemPercentOff(scnr));
            }
            case BuyXGetYPercentOff -> {
                item.setAmountX(enterItemAmountX(scnr));
                item.setPercentOff(enterItemPercentOff(scnr));
            }
            case BuyXGetYFree -> {
                item.setAmountX(enterItemAmountX(scnr));
                item.setAmountY(enterItemAmountY(scnr));
            }
            case AmountOff -> {
                item.setAmountOff(enterItemAmountOff(scnr));
            }
            case AmountOffEach -> {
                item.setAmountOff(enterItemAmountOff(scnr));
            }
            case AmountOffTotal -> {
                item.setAmountOff(enterItemAmountOff(scnr));
            }
        }
        item.setTaxRate(Cart.getTaxRate());
//        item.setTaxRate(enterItemTaxRate(scnr));
        item.setHasShipping(enterItemHasShipping(scnr));
        if (item.getHasShipping()) {
            item.setShippingCost(enterItemShippingCost(scnr));
        }
        return item;
    }

}
