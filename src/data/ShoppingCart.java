package data;

import annotations.FieldLabel;
import core.SalesCalculator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * This class contains methods for a shopping cart.
 */
public class ShoppingCart {

    private ArrayList<ShoppingItem> items = new ArrayList<ShoppingItem>();

    @FieldLabel("Tax Rate")
    private double taxRate = 0.0;

    @FieldLabel("Shipping Cost")
    private double shippingCost = 0.0;

    @FieldLabel("Flat Shipping")
    private boolean flatShipping = false;

    @FieldLabel("Total Tax")
    private double totalTax = 0.0;
    @FieldLabel("Subtotal")
    private double subTotal = 0.0;

    @FieldLabel("Total")
    private double total = 0.0;

    @FieldLabel("Amount Off")
    private double amountOff = 0.0;

    public ArrayList<ShoppingItem> getItems() {
        return items;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public double getShippingCost() {
        return shippingCost;
    }

    public boolean getFlatShipping() {
        return flatShipping;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public double getTotal() {
        return total;
    }

    public double getAmountOff() {
        return amountOff;
    }


    public void setItems(ArrayList<ShoppingItem> items) {
        this.items = items;
    }


    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public void setAmountOff(double amountOff) {
        this.amountOff = amountOff;
    }

    public void setShippingCost(double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public void setFlatShipping(boolean flatShipping) {
        this.flatShipping = flatShipping;
    }

    public void setTotalTax(double totalTax) {
        this.totalTax = totalTax;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void addAmountOff(double amountOff) {
        this.amountOff += amountOff;
    }

    public void subtractAmountOff(double amountOff) {
        this.amountOff -= amountOff;
    }

    /**
     * This method adds an item to the cart.
     * @param item
     */
    public void addItem(ShoppingItem item) {
        items.add(item);
    }

    /**
     * This method edits an item in the cart.
     *
     * @param item
     * @param name
     */
    public void editItem(ShoppingItem item, String name) {
        ShoppingItem itemToEdit = null;
        for (ShoppingItem i : items) {
            if (i.getName().equals(name)) {
                itemToEdit = i;
            }
        }
        if (itemToEdit != null) {
            itemToEdit.setName(item.getName());
            itemToEdit.setQuantity(item.getQuantity());
            itemToEdit.setPrice(item.getPrice());
            itemToEdit.setSaleType(item.getSaleType());
            itemToEdit.setPercentOff(item.getPercentOff());
            itemToEdit.setAmountOff(item.getAmountOff());
            itemToEdit.setAmountX(item.getAmountX());
            itemToEdit.setAmountY(item.getAmountY());
            itemToEdit.setTaxCost(getTaxRate());
            itemToEdit.setHasShipping(item.getHasShipping());
            itemToEdit.setShippingCost(item.getShippingCost());
        }
    }

    /**
     * This method removes an item from the cart.
     *
     * @param item
     */
    public void removeItem(ShoppingItem item) {
        items.remove(item);
    }

    /**
     * This method clears the cart.
     */
    public void clearCart() {
        items.clear();
    }

    /**
     * This method calculates the total tax of the cart.
     * @return
     */
    public double calculateTotalTax() {
        totalTax = 0.0;
        totalTax += calculateSubTotal() * taxRate;
        return totalTax;
    }

    /**
     * This method calculates the subtotal of the cart.
     */
    public double calculateSubTotal() {
        subTotal = 0.0;
        amountOff = 0.0;
        for (ShoppingItem item : items) {
            subTotal += item.getTotalPrice(false);
        }
        for (ShoppingItem item : items) {

            switch (item.getSaleType()) {
                case BuyXGetPercentOffTotal -> {
                    addAmountOff(SalesCalculator.buyXgetPercentOffTotal(subTotal, item, item.getAmountX(), item.getPercentOff()));
                }
                case AmountOffTotal -> addAmountOff(item.getAmountOff());
                default -> {}
            }
        }
        subTotal -= amountOff;
        return subTotal;
    }

    /**
     * This method calculates the total of the cart.
     */
    public double calculateTotal() {
        total = 0.0;
        amountOff = 0.0;

        for (ShoppingItem item : items) {
            total += item.getTotalPrice(true);
        }
        for (ShoppingItem item : items) {
            switch (item.getSaleType()) {
                case BuyXGetPercentOffTotal -> {
                    addAmountOff(SalesCalculator.buyXgetPercentOffTotal(total, item, item.getAmountX(), item.getPercentOff()));
                }
                case AmountOffTotal ->{
                    addAmountOff(item.getAmountOff());
                }
                default -> {}
            }
        }

        total -= amountOff;
        total += calculateShippingCost();
        return total;
    }

    /**
     * This method calculates the shipping cost of the cart.
     */
    public double calculateShippingCost() {
        if (flatShipping) {
            return shippingCost;
        }
        shippingCost = 0.0;
        for (ShoppingItem item : items) {
            if(item.getHasShipping()){
                shippingCost += item.getShippingCost();
            }
        }
        return shippingCost;
    }

    /**
     * This a default constructor for the ShoppingCart class.
     */
    public ShoppingCart() {
        this.items = new ArrayList<ShoppingItem>();
        this.subTotal = 0.0;
        this.total = 0.0;
        this.taxRate = 0.0;
        this.shippingCost = 0.0;
    }

    /**
     * This method saves a cart to a file.
     * @param path
     */
    public void saveCart(String path) {
        var file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        var GSON = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
        var json = GSON.toJson(this);
        try {
            var writer = new java.io.FileWriter(file);
            writer.write(json);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method loads a cart from a file.
     * @param path
     */
    public void loadCart(String path) {
        var file = new File(path);
        if (!file.exists()) {
            return;
        }
        try {
            var reader = new java.io.FileReader(file);
            var GSON = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
            var json = GSON.fromJson(reader, ShoppingCart.class);
            this.items = json.items;
            this.subTotal = json.subTotal;
            this.total = json.total;
            this.taxRate = json.taxRate;
            this.shippingCost = json.shippingCost;
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method saves the receipt of the cart in either a text or csv file.
     *
     * @param path
     * @param extension
     */
    public void saveReceipt(String path, String extension) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        // Find the maximum length of item names and other fields
        int maxNameLength = items.stream().mapToInt(item -> item.getName().length()).max().orElse("Name".length());
        int maxPriceLength = Math.max(items.stream().mapToInt(item -> String.format("%.2f", item.getPrice()).length()).max().orElse("Price".length()), "Price".length());
        int maxQuantityLength = Math.max(items.stream().mapToInt(item -> String.format("%d", item.getQuantity()).length()).max().orElse("Quantity".length()), "Quantity".length());
        int maxTaxLength = Math.max(items.stream().mapToInt(item -> String.format("%.2f", item.getTaxCost()).length()).max().orElse("Tax Cost".length()), "Tax Cost".length());
        int maxShippingLength = Math.max(items.stream().mapToInt(item -> String.format("%.2f", item.getShippingCost()).length()).max().orElse("Shipping Cost".length()), "Shipping Cost".length());
        int maxTotalLength = Math.max(items.stream().mapToInt(item -> String.format("%.2f", item.getTotalPrice(false)).length()).max().orElse("Total Price".length()), "Total Price".length());;

        try (FileWriter writer = new FileWriter(file)) {
            Locale locale = Locale.US;
            String nameFormat = "%-" + maxNameLength + "s";
            String priceFormat = "%-" + maxPriceLength + "s";
            String quantityFormat = "%-" + maxQuantityLength + "s";
            String taxFormat = "%-" + maxTaxLength + "s";
            String shippingFormat = "%-" + maxShippingLength + "s";
            String totalFormat = "%-" + maxTotalLength + "s";

            if (extension.equals("txt")) {
                writer.write(String.format(locale, nameFormat + " " + priceFormat + " " + quantityFormat + " " + taxFormat + " " + shippingFormat + " " + totalFormat + "\n",
                        "Name", "Price", "Quantity", "Tax Cost", "Shipping Cost", "Total Price"));
                writer.write(String.format(locale, nameFormat + " " + priceFormat + " " + quantityFormat + " " + taxFormat + " " + shippingFormat + " " + totalFormat + "\n",
                        String.join("", Collections.nCopies(maxNameLength, "-")),
                        String.join("", Collections.nCopies(maxPriceLength, "-")),
                        String.join("", Collections.nCopies(maxQuantityLength, "-")),
                        String.join("", Collections.nCopies(maxTaxLength, "-")),
                        String.join("", Collections.nCopies(maxShippingLength, "-")),
                        String.join("", Collections.nCopies(maxTotalLength, "-"))));

                for (ShoppingItem item : items) {
                    writer.write(String.format(locale, nameFormat + " " + priceFormat + " " + quantityFormat + " " + taxFormat + " " + shippingFormat + " " + totalFormat + "\n",
                            item.getName(),
                            String.format(locale, "%.2f", item.getPrice()),
                            String.format(locale, "%d", item.getQuantity()),
                            String.format(locale, "%.2f", item.getTaxCost()),
                            String.format(locale, "%.2f", item.getShippingCost()),
                            String.format(locale, "%.2f", item.getTotalPrice(false))));
                }

                writer.write("--------------------------------------------------------" +
                        "----------------------\n");
                writer.write(String.format(locale, nameFormat + " " + priceFormat + "\n", "Total Tax:", String.format("%.2f", calculateTotalTax())));
                writer.write(String.format(locale, nameFormat + " " + priceFormat + "\n", "Shipping Cost:", String.format("%.2f", calculateShippingCost())));
                writer.write(String.format(locale, nameFormat + " " + priceFormat + "\n", "Subtotal:", String.format("%.2f", calculateSubTotal())));
                writer.write(String.format(locale, nameFormat + " " + priceFormat + "\n", "Total:", String.format("%.2f", calculateTotal())));

            } else if (extension.equals("csv")) {
                writer.write(String.join(",", "Name", "Price", "Quantity", "Tax Cost", "Shipping Cost", "Total Price") + "\n");

                for (ShoppingItem item : items) {
                    writer.write(String.format(locale, "\"%s\",%.2f,%d,%.2f,%.2f,%.2f\n",
                            item.getName(), item.getPrice(), item.getQuantity(), item.getTaxCost(),
                            item.getShippingCost(), item.getTotalPrice(false)));
                }

                writer.write("\n");
                writer.write(String.format(locale, "Total Tax,%s\n", String.format(locale, "%.2f", calculateTotalTax())));
                writer.write(String.format(locale, "Shipping Cost,%s\n", String.format(locale, "%.2f", calculateShippingCost())));
                writer.write(String.format(locale, "Subtotal,%s\n", String.format(locale, "%.2f", calculateSubTotal())));
                writer.write(String.format(locale, "Total,%s\n", String.format(locale, "%.2f", calculateTotal())));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
