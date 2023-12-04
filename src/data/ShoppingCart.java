package data;

import annotations.FieldLabel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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

    private boolean flatShipping = false;

    @FieldLabel("Total Tax")
    private double totalTax = 0.0;
    @FieldLabel("Subtotal")
    private double subTotal = 0.0;

    @FieldLabel("Total")
    private double total = 0.0;

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


    public void setItems(ArrayList<ShoppingItem> items) {
        this.items = items;
    }


    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
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

    /**
     * This method adds an item to the cart.
     *
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
            itemToEdit.setTaxRate(getTaxRate());
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

    public double calculateTotalTax() {
        totalTax = 0.0;
        totalTax += calculateSubTotal() * (taxRate / 100);
        return totalTax;
    }

    /**
     * This method calculates the subtotal of the cart.
     */
    public double calculateSubTotal() {
        subTotal = 0.0;
        for (ShoppingItem item : items) {
            subTotal += item.getTotalPrice(false, false);
        }
        return subTotal;
    }

    /**
     * This method calculates the total of the cart.
     */
    public double calculateTotal() {
        total = 0.0;
        for (ShoppingItem item : items) {
            total += item.getTotalPrice(true, true);
        }
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
        shippingCost = items.stream().mapToDouble(ShoppingItem::getShippingCost).sum();
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
        try (FileWriter writer = new FileWriter(file)) {
            Locale locale = Locale.US;

            if (extension.equals("txt")) {
                writer.write(String.format(locale, "%-20s %-9s %-9s %-11s %-15s %-12s\n",
                        "Name", "Price", "Quantity", "Tax Rate", "Shipping Cost", "Total Price"));
                writer.write(String.format(locale, "%-20s %-9s %-9s %-11s %-15s %-12s\n",
                        "--------------------", "---------", "---------", "-----------", "---------------", "------------"));

                for (ShoppingItem item : items) {
                    writer.write(String.format(locale, "%-20s $%-8.2f %-9d $%-10.2f $%-14.2f $%-11.2f\n",
                            item.getName(), item.getPrice(), item.getQuantity(), item.getTaxRate(),
                            item.getShippingCost(), item.getTotalPrice(false, true)));
                }

                writer.write("--------------------------------------------------------" +
                        "----------------------\n");

                writer.write(String.format(locale, "%-20s $%-8.2f\n", "Subtotal:", calculateSubTotal()));
                writer.write(String.format(locale, "%-20s $%-8.2f\n", "Total Tax:", calculateTotalTax()));
                writer.write(String.format(locale, "%-20s $%-8.2f\n", "Shipping Cost:", calculateShippingCost()));
                writer.write(String.format(locale, "%-20s $%-8.2f\n", "Total:", calculateTotal()));

            } else if (extension.equals("csv")) {
                writer.write(String.join(",", "Name", "Price", "Quantity", "Tax Rate", "Shipping Cost", "Total Price") + "\n");

                for (ShoppingItem item : items) {
                    writer.write(String.format(locale, "\"%s\",%.2f,%d,%.2f,%.2f,%.2f\n",
                            item.getName(), item.getPrice(), item.getQuantity(), item.getTaxRate(),
                            item.getShippingCost(), item.getTotalPrice(false, true)));
                }

                writer.write("\n");
                writer.write(String.format(locale, "Subtotal,%s\n", String.format(locale, "%.2f", calculateSubTotal())));
                writer.write(String.format(locale, "Total Tax,%s\n", String.format(locale, "%.2f", calculateTotalTax())));
                writer.write(String.format(locale, "Shipping Cost,%s\n", String.format(locale, "%.2f", calculateShippingCost())));
                writer.write(String.format(locale, "Total,%s\n", String.format(locale, "%.2f", calculateTotal())));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
