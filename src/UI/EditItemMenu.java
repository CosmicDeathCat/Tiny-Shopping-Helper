package ui;

import core.SalesCalculator;
import data.SaleType;
import data.ShoppingItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class contains methods for the Edit Item Menu
 */
public class EditItemMenu extends JDialog{
    private JPanel mainPanel;
    private JPanel editItemPanel;
    private JPanel editTopBarPanel;
    private JPanel editItemMainPanel;
    private JTextField ItemNameInput;
    private JLabel itemNameLabel;
    private JTextField ItemQuantityInput;
    private JLabel ItemQuantityLabel;
    private JLabel itemSaleTypeLabel;
    private JComboBox saleTypeDropDown;
    private JTextField condition1Input;
    private JLabel saleTypeCondition1;
    private JTextField condition2Input;
    private JLabel saleTypeCondition2;
    private JLabel shippingYesNoLabel;
    private JComboBox itemShippingDropDown;
    private JTextField itemShippingInput;
    private JLabel itemShippingCostLabel;
    private JTextField itemPriceInput;
    private JLabel itemPriceLabel;
    private JButton saveItemButton;
    private SaleType selectedSaleType = SaleType.None;
    private ShoppingMainGUI mainForm;

    private int rowIndex;

    /**
     * This is a constructor for the Edit Item Menu
     * @param main
     * @param item
     * @param rowIndex
     */
    public EditItemMenu(ShoppingMainGUI main, ShoppingItem item, int rowIndex) {
        //this is a constructor for the Edit Item Menu
        super(main, true);
        this.mainForm = main;
        this.rowIndex = rowIndex;
        this.ItemNameInput.setText(item.getName());
        this.ItemQuantityInput.setText(String.valueOf(item.getQuantity()));
        this.itemPriceInput.setText(String.valueOf(item.getPrice()));
        //this for loop adds the sale types to the dropdown menu
        for (SaleType saleType : SaleType.values()) {
            saleTypeDropDown.addItem(saleType);
        }
        //this line of code sets the sale type dropdown to the item's sale type
        saleTypeDropDown.setSelectedItem(item.getSaleType());
        //this line of code sets the selected sale type to the item's sale type
        selectedSaleType = item.getSaleType();
        //this line of code updates the condition visibility
        updateConditionVisibility();
        //this switch statement sets the condition inputs based on the sale type
        switch (item.getSaleType()) {
            case None -> {
            }
            case PercentOff -> {
                condition1Input.setText(String.valueOf(item.getPercentOff()));
            }
            case BuyXGetPercentOffTotal -> {
                condition1Input.setText(String.valueOf(item.getAmountX()));
                condition2Input.setText(String.valueOf(item.getPercentOff()));
            }
            case BuyXGetYPercentOff -> {
                condition1Input.setText(String.valueOf(item.getAmountX()));
                condition2Input.setText(String.valueOf(item.getPercentOff()));
            }
            case BuyXGetYFree -> {
                condition1Input.setText(String.valueOf(item.getAmountX()));
                condition2Input.setText(String.valueOf(item.getAmountY()));
            }
            case AmountOff -> {
                condition1Input.setText(String.valueOf(item.getAmountOff()));
            }
            case AmountOffEach -> {
                condition1Input.setText(String.valueOf(item.getAmountOff()));
            }
            case AmountOffTotal -> {
                condition1Input.setText(String.valueOf(item.getAmountOff()));
            }
        }
        //these lines of code set the visibility of the shipping inputs
        itemShippingCostLabel.setVisible(false);
        itemShippingInput.setVisible(false);
        itemShippingDropDown.addItem("No");
        itemShippingDropDown.addItem("Yes");
        itemShippingDropDown.addActionListener(new ActionListener() {
            /**
             * This method shows the shipping cost input if the user selects yes
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                //this method shows the shipping cost input if the user selects yes
                String selectedOption = (String) itemShippingDropDown.getSelectedItem();
                if ("Yes".equals(selectedOption)) {
                    itemShippingCostLabel.setVisible(true);
                    itemShippingInput.setVisible(true);
                } else {
                    itemShippingCostLabel.setVisible(false);
                    itemShippingInput.setVisible(false);
                }
            }
        });
        //this line of code sets the shipping inputs based on the item
        int shippingIndex = item.getHasShipping() ? 1 : 0;
        //this line of code sets the shipping inputs based on the item
        itemShippingDropDown.setSelectedIndex(shippingIndex);
        //this line of code sets the shipping inputs based on the item
        itemShippingInput.setText(String.valueOf(item.getShippingCost()));
        //this action listener shows the condition inputs based on the sale type selected
        saleTypeDropDown.addActionListener(new ActionListener() {
            /**
             * This method shows the condition inputs based on the sale type selected
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                //this method shows the condition inputs based on the sale type selected
                selectedSaleType = (SaleType) saleTypeDropDown.getSelectedItem();
                updateConditionVisibility();
            }
        });

        saveItemButton.addActionListener(new ActionListener() {
            /**
             * This method saves the item to the cart
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    //this method saves the item to the cart
                item.setName(ItemNameInput.getText());
                item.setQuantity(Integer.parseInt(ItemQuantityInput.getText()));
                //this if statement checks if the quantity is less than 1
                if (item.getQuantity() < 1) {
                    throw new NumberFormatException();
                }
                item.setPrice(Double.parseDouble(itemPriceInput.getText()));
                if (item.getPrice() < 0) {
                    throw new NumberFormatException();
                }
                item.setSaleType(selectedSaleType);
                //this switch statement sets the condition inputs based on the sale type
                switch (item.getSaleType()){

                    case None -> {
                    }
                    case PercentOff -> {
                        item.setPercentOff(Double.parseDouble(condition1Input.getText()));
                    }
                    case BuyXGetPercentOffTotal -> {
                        item.setAmountX(Integer.parseInt(condition1Input.getText()));
                        item.setPercentOff(Double.parseDouble(condition2Input.getText()));
                        item.setAmountSaved(SalesCalculator.buyXgetPercentOffTotal(ShoppingMainGUI.shoppingCart.calculateSubTotal(), item, item.getAmountX(), item.getPercentOff()));
                    }
                    case BuyXGetYPercentOff -> {
                        item.setAmountX(Integer.parseInt(condition1Input.getText()));
                        item.setPercentOff(Double.parseDouble(condition2Input.getText()));
                    }
                    case BuyXGetYFree -> {
                        item.setAmountX(Integer.parseInt(condition1Input.getText()));
                        item.setAmountY(Integer.parseInt(condition2Input.getText()));
                    }
                    case AmountOff -> {
                        item.setAmountOff(Double.parseDouble(condition1Input.getText()));
                    }
                    case AmountOffEach -> {
                        item.setAmountOff(Double.parseDouble(condition1Input.getText()));
                    }
                    case AmountOffTotal -> {
                        item.setAmountOff(Double.parseDouble(condition1Input.getText()));
                        item.setAmountSaved(item.getAmountOff());
                    }
                }
                item.setHasShipping(itemShippingDropDown.getSelectedIndex() == 1);
                item.setShippingCost(Double.parseDouble(itemShippingInput.getText()));
                if (item.getShippingCost() < 0) {
                    throw new NumberFormatException();
                }
                mainForm.updateShoppingCartTableItem(item, rowIndex);
                mainForm.getCartTotalShipping();
                mainForm.getCartTotalTax();
                mainForm.getCartSubtotal();
                mainForm.getAmountSaved();
                dispose();
            }
                catch (NumberFormatException exception) {
                    //this shows an error message if the user enters an invalid number for the quantity, price, or shipping cost
                    JOptionPane.showMessageDialog(null, "Please enter a valid positive number.");
                }
            }
        });
        add(mainPanel);
        setTitle("Edit Item");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * This method shows the condition inputs based on the sale type selected
     */
    private void updateConditionVisibility() {
        //this switch statement shows the condition inputs based on the sale type selected
        switch (selectedSaleType) {
            case None -> {
                saleTypeCondition1.setVisible(false);
                saleTypeCondition2.setVisible(false);
                condition1Input.setVisible(false);
                condition2Input.setVisible(false);
            }
            case PercentOff -> {
                saleTypeCondition1.setVisible(true);
                saleTypeCondition1.setText("Percent Off");
                saleTypeCondition2.setVisible(false);
                condition1Input.setVisible(true);
                condition2Input.setVisible(false);
            }
            case BuyXGetPercentOffTotal -> {
                saleTypeCondition1.setVisible(true);
                saleTypeCondition1.setText("Buy How Many?");
                saleTypeCondition2.setVisible(true);
                saleTypeCondition2.setText("Percent Off");
                condition1Input.setVisible(true);
                condition2Input.setVisible(true);
            }
            case BuyXGetYPercentOff -> {
                saleTypeCondition1.setVisible(true);
                saleTypeCondition1.setText("Buy How Many?");
                saleTypeCondition2.setVisible(true);
                saleTypeCondition2.setText("Percent Off");
                condition1Input.setVisible(true);
                condition2Input.setVisible(true);
            }
            case BuyXGetYFree -> {
                saleTypeCondition1.setVisible(true);
                saleTypeCondition1.setText("Buy How Many?");
                saleTypeCondition2.setVisible(true);
                saleTypeCondition2.setText("Get How Many Free?");
                condition1Input.setVisible(true);
                condition2Input.setVisible(true);
            }
            case AmountOff -> {
                saleTypeCondition1.setVisible(true);
                saleTypeCondition1.setText("Amount Off");
                saleTypeCondition2.setVisible(false);
                condition1Input.setVisible(true);
                condition2Input.setVisible(false);
            }
            case AmountOffEach -> {
                saleTypeCondition1.setVisible(true);
                saleTypeCondition1.setText("Amount Off Each");
                saleTypeCondition2.setVisible(false);
                condition1Input.setVisible(true);
                condition2Input.setVisible(false);
            }
            case AmountOffTotal -> {
                condition1Input.setVisible(true);
                saleTypeCondition1.setText("Amount Off Total");
                saleTypeCondition2.setVisible(false);
                condition1Input.setVisible(true);
                condition2Input.setVisible(false);
            }
        }
    }





}
