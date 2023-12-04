package ui;

import annotations.FieldLabel;
import api.TaxRateClient;
import data.SaleType;
import data.ShoppingCart;
import data.ShoppingItem;
import data.TaxRateResponse;
import utility.DataSaver;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ShoppingMainConsole {
    public static ShoppingCart Cart = new ShoppingCart();

    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Welcome to the Tiny Shopping Helper program!");
        System.out.println("Do you want to manually enter the tax rate or get it from your zip code?");
        System.out.println("1. Get tax rate from zip code");
        System.out.println("2. Manually enter tax rate");
        int input = scnr.nextInt();
        if (input == 1) {
            scnr.nextLine();
            TaxRateResponse taxRateResponse = enterTaxZipCode(scnr);
            Cart.setTaxRate(taxRateResponse.getTotalRate());
        } else if (input == 2) {
            scnr.nextLine();
            System.out.print("Enter the tax rate: ");
            double taxRate = scnr.nextDouble();
            scnr.nextLine();
            Cart.setTaxRate(taxRate);
        } else {
            System.out.println("Invalid input. Enter a number between 1 and 2.");
            main(args);
            return;
        }
        while (true) {
            shoppingCartInput(scnr);
        }
    }

    /**
     * Prints the receipt for the shopping cart.
     * @param cart The shopping cart to print the receipt for.
     */
    public static void printReceipt(ShoppingCart cart) {
        cart.calculateTotalTax();
        cart.calculateSubTotal();
        cart.calculateTotal();

        if (!cart.getItems().isEmpty()) {
            for (ShoppingItem item : cart.getItems()) {
                printObjectFields(item);
                System.out.println();
            }
        }

        // Print separator
        System.out.println("---- Cart Totals ----");

        printObjectFields(cart);

        JFrame frame = new JFrame();
        DataSaver.saveWithDialog(cart, frame);
    }

    /**
     * Prints the fields of an object.
     * @param obj The object to print the fields of.
     */
    public static void printObjectFields(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(FieldLabel.class)) {
                try {
                    field.setAccessible(true);
                    System.out.printf("%s: %s%n",
                            field.getAnnotation(FieldLabel.class).value(),
                            field.get(obj));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
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
                    printReceipt(Cart);
                    break;
                case 4:
                    printReceipt(Cart);
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
        String zipCodePattern = "^[0-9]{5}(?:-[0-9]{4})?$";

        while (true) {
            try {
                System.out.print("Enter your zip code to get the tax rate: ");
                String zipCode = scnr.nextLine();

                if (!Pattern.matches(zipCodePattern, zipCode)) {
                    System.out.println("Invalid zip code format. Please enter a 5-digit or 9-digit zip code.");
                    continue;
                }

                TaxRateClient taxRateClient = new TaxRateClient();
                TaxRateResponse taxRateResponse = taxRateClient.getTaxRateFromZipCityState(zipCode, null, null);

                if (taxRateResponse != null) {
                    System.out.println("Tax rate: " + taxRateResponse.getTotalRate());
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
