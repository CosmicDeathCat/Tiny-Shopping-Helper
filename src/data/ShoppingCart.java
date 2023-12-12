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

    @FieldLabel("Amount Saved")
    private double amountSaved = 0.0;
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

    public double getAmountSaved() { return amountSaved; }

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

    public void setAmountSaved(double amountSaved) { this.amountSaved = amountSaved; }

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
        //this loop finds the item to edit
        ShoppingItem itemToEdit = null;
        for (ShoppingItem i : items) {
            if (i.getName().equals(name)) {
                itemToEdit = i;
            }
        }
        //this if statement checks if the item to edit is not null, and if it is not, it edits the item
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
        //this is the total tax for the cart
        totalTax = 0.0;
        totalTax += calculateSubTotal() * taxRate;
        //this returns the total tax for the cart
        return totalTax;
    }

    /**
     * This method calculates the subtotal of the cart.
     */
    public double calculateSubTotal() {
        //this is the subtotal for the cart
        subTotal = 0.0;
        amountOff = 0.0;
        amountSaved = 0.0;
        //this loop calculates the subtotal for the cart
        for (ShoppingItem item : items) {
            subTotal += item.getTotalPrice(false);
            amountSaved += item.getAmountSaved();
        }
        //this loop calculates the amount off for the cart
        for (ShoppingItem item : items) {
            //this switch statement calculates the amount off for the cart based on the sale type
            switch (item.getSaleType()) {
                case BuyXGetPercentOffTotal -> {
                    addAmountOff(SalesCalculator.buyXgetPercentOffTotal(subTotal, item, item.getAmountX(), item.getPercentOff()));
                }
                case AmountOffTotal ->{
                    addAmountOff(item.getAmountOff());
                }
                default -> {}
            }
        }
        //this subtracts the amount off from the subtotal
        subTotal -= amountOff;
        //this returns the subtotal for the cart
        return subTotal;
    }

    /**
     * This method calculates the total of the cart.
     */
    public double calculateTotal() {
        //this is the total for the cart
        total = 0.0;
        amountOff = 0.0;
        amountSaved = 0.0;
        //this loop calculates the total for the cart
        for (ShoppingItem item : items) {
            total += item.getTotalPrice(true);
            amountSaved += item.getAmountSaved();
        }
        //this loop calculates the amount off for the cart
        for (ShoppingItem item : items) {
            //this switch statement calculates the amount off for the cart based on the sale type
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
        //this subtracts the amount off from the total
        total -= amountOff;
        //this adds the shipping cost to the total
        total += calculateShippingCost();
        //this returns the total for the cart
        return total;
    }

    /**
     * This method calculates the shipping cost of the cart.
     */
    public double calculateShippingCost() {
        //this is the shipping cost for the cart, also this if statement checks if the shipping cost is flat
        if (flatShipping) {
            return shippingCost;
        }
        shippingCost = 0.0;
        //this loop calculates the shipping cost for the cart
        for (ShoppingItem item : items) {
            //this if statement checks if the item has shipping
            if(item.getHasShipping()){
                shippingCost += item.getShippingCost();
            }
        }
        //this returns the shipping cost for the cart
        return shippingCost;
    }

    /**
     * This a default constructor for the ShoppingCart class.
     */
    public ShoppingCart() {
        //this is a default constructor
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
        //this method saves the cart to a file
        var file = new File(path);
        //this if statement checks if the file exists
        if (file.exists()) {
            file.delete();
        }
        //these variables use Gson to format the json
        var GSON = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
        var json = GSON.toJson(this);
        try {
            //this writes the json to the file
            var writer = new java.io.FileWriter(file);
            writer.write(json);
            writer.close();
        } catch (Exception e) {
            //this exception is thrown if the file is not found
            e.printStackTrace();
        }
    }

    /**
     * This method loads a cart from a file.
     * @param path
     */
    public void loadCart(String path) {
        //this method loads the cart from a file
        var file = new File(path);
        //this if statement checks if the file exists
        if (!file.exists()) {
            return;
        }
        try {
            //this reads the json from the file
            var reader = new java.io.FileReader(file);
            var GSON = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
            var json = GSON.fromJson(reader, ShoppingCart.class);
            this.items = json.items;
            this.subTotal = json.subTotal;
            this.total = json.total;
            this.taxRate = json.taxRate;
            this.amountSaved = json.amountSaved;
            this.shippingCost = json.shippingCost;
            reader.close();

        } catch (Exception e) {
            //this exception is thrown if the file is not found
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
        //this if statement checks if the file exists and deletes it if it does
        if (file.exists()) {
            file.delete();
        }
        //these variables are used to format the receipt
        int maxNameLength = items.stream().mapToInt(item -> item.getName().length()).max().orElse("Name".length());
        int maxPriceLength = Math.max(items.stream().mapToInt(item -> String.format("%.2f", item.getPrice()).length()).max().orElse("Price".length()), "Price".length());
        int maxQuantityLength = Math.max(items.stream().mapToInt(item -> String.format("%d", item.getQuantity()).length()).max().orElse("Quantity".length()), "Quantity".length());
        int maxTaxLength = Math.max(items.stream().mapToInt(item -> String.format("%.2f", item.getTaxCost()).length()).max().orElse("Tax Cost".length()), "Tax Cost".length());
        int maxShippingLength = Math.max(items.stream().mapToInt(item -> String.format("%.2f", item.getShippingCost()).length()).max().orElse("Shipping Cost".length()), "Shipping Cost".length());
        int maxTotalSavedLength = Math.max(items.stream().mapToInt(item -> String.format("%.2f", item.getAmountSaved()).length()).max().orElse("Amount Saved".length()), "Amount Saved".length());
        int maxTotalLength = Math.max(items.stream().mapToInt(item -> String.format("%.2f", item.getTotalPrice(false)).length()).max().orElse("Total Price".length()), "Total Price".length());;

        try (FileWriter writer = new FileWriter(file)) {
            //this writes the receipt to the file
            Locale locale = Locale.US;
            String nameFormat = "%-" + maxNameLength + "s";
            String priceFormat = "%-" + maxPriceLength + "s";
            String quantityFormat = "%-" + maxQuantityLength + "s";
            String taxFormat = "%-" + maxTaxLength + "s";
            String shippingFormat = "%-" + maxShippingLength + "s";
            String totalSavedFormat = "%-" + maxTotalSavedLength + "s";
            String totalFormat = "%-" + maxTotalLength + "s";
            //this if statement checks if the extension is txt
            if (extension.equals("txt")) {
                writer.write(String.format(locale, nameFormat + " " + priceFormat + " " + quantityFormat + " " + taxFormat + " " + shippingFormat + " " + totalSavedFormat + " " + totalFormat + "\n",
                        "Name", "Price", "Quantity", "Tax Cost", "Shipping Cost", "Amount Saved", "Total Price"));
                writer.write(String.format(locale, nameFormat + " " + priceFormat + " " + quantityFormat + " " + taxFormat + " " + shippingFormat + " " + totalSavedFormat + " " + totalFormat + "\n",
                        String.join("", Collections.nCopies(maxNameLength, "-")),
                        String.join("", Collections.nCopies(maxPriceLength, "-")),
                        String.join("", Collections.nCopies(maxQuantityLength, "-")),
                        String.join("", Collections.nCopies(maxTaxLength, "-")),
                        String.join("", Collections.nCopies(maxShippingLength, "-")),
                        String.join("", Collections.nCopies(maxTotalSavedLength, "-")),
                        String.join("", Collections.nCopies(maxTotalLength, "-"))));
                //this loop writes the items to the file
                for (ShoppingItem item : items) {
                    writer.write(String.format(locale, nameFormat + " " + priceFormat + " " + quantityFormat + " " + taxFormat + " " + shippingFormat + " " + totalSavedFormat + " " + totalFormat + "\n",
                            item.getName(),
                            String.format(locale, "%.2f", item.getPrice()),
                            String.format(locale, "%d", item.getQuantity()),
                            String.format(locale, "%.2f", item.getTaxCost()),
                            String.format(locale, "%.2f", item.getShippingCost()),
                            String.format(locale, "%.2f", item.getAmountSaved()),
                            String.format(locale, "%.2f", item.getTotalPrice(false))));
                }
                //this writes the total tax, shipping cost, amount saved, subtotal, and total to the file
                writer.write("--------------------------------------------------------" +
                        "----------------------\n");
                writer.write(String.format(locale, nameFormat + " " + priceFormat + "\n", "Total Tax:", String.format("%.2f", calculateTotalTax())));
                writer.write(String.format(locale, nameFormat + " " + priceFormat + "\n", "Shipping Cost:", String.format("%.2f", calculateShippingCost())));
                writer.write(String.format(locale, nameFormat + " " + priceFormat + "\n", "Amount Saved:", String.format("%.2f", amountSaved)));
                writer.write(String.format(locale, nameFormat + " " + priceFormat + "\n", "Subtotal:", String.format("%.2f", calculateSubTotal())));
                writer.write(String.format(locale, nameFormat + " " + priceFormat + "\n", "Total:", String.format("%.2f", calculateTotal())));

            } else if (extension.equals("csv")) {
                //this else if statement checks if the extension is csv
                writer.write(String.join(",", "Name", "Price", "Quantity", "Tax Cost", "Shipping Cost", "Amount Saved", "Total Price") + "\n");
                //this loop writes the items to the file
                for (ShoppingItem item : items) {
                    writer.write(String.format(locale, "\"%s\",%.2f,%d,%.2f,%.2f,%.2f,%.2f\n",
                            item.getName(), item.getPrice(), item.getQuantity(), item.getTaxCost(),
                            item.getShippingCost(), item.getAmountSaved(), item.getTotalPrice(false)));
                }
                //this writes the total tax, shipping cost, amount saved, subtotal, and total to the file
                writer.write("\n");
                writer.write(String.format(locale, "Total Tax,%s\n", String.format(locale, "%.2f", calculateTotalTax())));
                writer.write(String.format(locale, "Shipping Cost,%s\n", String.format(locale, "%.2f", calculateShippingCost())));
                writer.write(String.format(locale, "Amount Saved,%s\n", String.format(locale, "%.2f", amountSaved)));
                writer.write(String.format(locale, "Subtotal,%s\n", String.format(locale, "%.2f", calculateSubTotal())));
                writer.write(String.format(locale, "Total,%s\n", String.format(locale, "%.2f", calculateTotal())));
            }
        } catch (IOException e) {
            //this exception is thrown if the file is not found
            e.printStackTrace();
        }
    }
}
