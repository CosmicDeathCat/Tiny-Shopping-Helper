package core;

import java.util.ArrayList;

/**
 * This class contains methods for a shopping cart.
 */
public class ShoppingCart {
    private ArrayList<ShoppingItem> items = new ArrayList<ShoppingItem>();
    private double subTotal = 0.0;
    private double total = 0.0;

    public ArrayList<ShoppingItem> getItems() {
        return items;
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

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public void setTotal(double total) {
        this.total = total;
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
            itemToEdit.setTaxRate(item.getTaxRate());
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
    public void calculateSubTotal() {
        subTotal = 0.0;
        for (ShoppingItem item : items) {
            subTotal += item.getTotalPrice(false);
        }
    }

    /**
     * This method calculates the total of the cart.
     */
    public void calculateTotal() {
        total = 0.0;
        for (ShoppingItem item : items) {
            total += item.getTotalPrice(true);
        }
    }

    /**
     * This a default constructor for the ShoppingCart class.
     */
    public ShoppingCart() {
        this.items = new ArrayList<ShoppingItem>();
        this.subTotal = 0.0;
        this.total = 0.0;
    }
}
