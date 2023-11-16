package data;

import annotations.FieldLabel;

import java.util.ArrayList;

/**
 * This class contains methods for a shopping cart.
 */
public class ShoppingCart {

    @FieldLabel("Items")
    private ArrayList<ShoppingItem> items = new ArrayList<ShoppingItem>();

    @FieldLabel("Subtotal")
    private double subTotal = 0.0;

    @FieldLabel("Total")
    private double total = 0.0;

    @FieldLabel("Tax Rate")
    private double taxRate = 0.0;

    @FieldLabel("Shipping Cost")
    private double shippingCost = 0.0;

    @FieldLabel("Flat Shipping")
    private boolean flatShipping = false;

    public ArrayList<ShoppingItem> getItems() {
        return items;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public double getTotal() {
        return total;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public double getShippingCost() {
        return shippingCost;
    }


    public void setItems(ArrayList<ShoppingItem> items) {
        this.items = items;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public void setShippingCost(double shippingCost) {
        this.shippingCost = shippingCost;
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
}
