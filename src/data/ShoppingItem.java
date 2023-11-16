package data;

import annotations.FieldLabel;
import core.SalesCalculator;

/**
 * This class contains methods for a shopping item.
 */
public class ShoppingItem {
    @FieldLabel("Name")
    private String name;
    @FieldLabel("Quantity")
    private int quantity = 1;
    @FieldLabel("Price")
    private double price = 0.0;
    @FieldLabel("Sale Type")
    private SaleType saleType = SaleType.None;
    @FieldLabel("Percent Off")
    private double percentOff = 0.0;
    @FieldLabel("Amount Off")
    private double amountOff = 0.0;

    @FieldLabel("Amount X")
    private int amountX = 0;

    @FieldLabel("Amount Y")
    private int amountY = 0;

    @FieldLabel("Tax Rate")
    private double taxRate = 0.0;

    @FieldLabel("Has Shipping")
    private boolean hasShipping = false;

    @FieldLabel("Shipping Cost")
    private double shippingCost = 0.0;

    public String getName () {
        return name;
    }

    public int getQuantity () {
        return quantity;
    }

    public double getPrice () {
        return price;
    }

    public SaleType getSaleType () {
        return saleType;
    }

    public double getPercentOff () { return percentOff; }

    public double getAmountOff () { return amountOff; }

    public int getAmountX () { return amountX; }

    public int getAmountY () { return amountY; }

    public double getTaxRate () {
        return taxRate;
    }

    public boolean getHasShipping () { return hasShipping; }

    public double getShippingCost () { return shippingCost; }

    public void setName (String name) { this.name = name; }

    public void setQuantity (int quantity) { this.quantity = quantity; }

    public void setPrice (double price) { this.price = price; }

    public void setSaleType (SaleType saleType) { this.saleType = saleType; }

    public void setPercentOff (double percentOff) { this.percentOff = percentOff; }

    public void setAmountOff (double amountOff) { this.amountOff = amountOff; }

    public void setAmountX (int amountX) { this.amountX = amountX; }

    public void setAmountY (int amountY) { this.amountY = amountY; }

    public void setTaxRate (double taxRate) { this.taxRate = taxRate; }

    public void setHasShipping (boolean hasShipping) { this.hasShipping = hasShipping; }

    public void setShippingCost (double shippingCost) { this.shippingCost = shippingCost; }

    /**
     * Calculates the total price of an item
     * @param includeTax
     * @return
     */
    public double getTotalPrice (boolean includeTax, boolean includeShipping) {
        double subtotal = 0;

        switch (saleType){

            case None -> {
                subtotal = this.price * this.quantity;
            }
            case PercentOff -> {
                subtotal = SalesCalculator.percentOff(this, this.percentOff);
            }
            case BuyXGetYPercentOff -> {
                subtotal = SalesCalculator.buyXgetYPercentOff(this, this.amountX, this.percentOff);
            }
            case BuyXGetPercentOffTotal -> {
                subtotal = SalesCalculator.buyXgetPercentOffTotal(this, this.amountX, this.price * this.quantity, this.percentOff);
            }
            case BuyXGetYFree -> {
                subtotal = SalesCalculator.buyXgetYFree(this, this.amountX, this.amountY);
            }
            case AmountOff -> {
                subtotal = SalesCalculator.amountOff(this, this.amountOff);
            }
            case AmountOffEach -> {
                subtotal = SalesCalculator.amountOffEach(this, this.amountOff);
            }
            case AmountOffTotal -> {
                subtotal = SalesCalculator.amountOffTotal(this.price * this.quantity, this.amountOff);
            }
        }
        if (includeShipping && this.hasShipping) {
            subtotal += this.shippingCost;
        }
        if (includeTax) {
            return Math.round(subtotal * (1 + this.taxRate) * 100.0) / 100.0;
        }
        return subtotal;
    }

    /**
     * Default constructor for a shopping item
     */
    public ShoppingItem() {
        this.name = "Item";
        this.quantity = 1;
        this.price = 1.0;
        this.saleType = SaleType.None;
        this.taxRate = 1.0;
    }

    /**
     * Constructor for a shopping item
     * @param name
     * @param quantity
     * @param price
     * @param saleType
     * @param taxRate
     */
    public ShoppingItem(String name, int quantity, double price, SaleType saleType, double taxRate) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.saleType = saleType;
        this.taxRate = taxRate;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nQuantity: " + quantity + "\nPrice: " + price + "\nSale Type: " + saleType + "\nTax Rate: " + taxRate + "\nSubtotal: " + getTotalPrice(false, false) + "\nTotal: " + getTotalPrice(true, true);
    }

    
}
