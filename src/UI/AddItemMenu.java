package ui;

import data.SaleType;
import data.ShoppingItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddItemMenu extends JFrame {
    private JPanel mainPanel;
    private JPanel addItemTitlePanel;
    private JLabel addItemMenuTitle;
    private JPanel topBarPanel;
    private JTextField itemNameInput;
    private JTextField itemQuantityInput;
    private JComboBox saleTypeDropDown;
    private JComboBox itemShippingDropDown;
    private JTextField itemShippingInput;
    private JLabel itemNameLabel;
    private JLabel itemQuantityLabel;
    private JLabel itemSaleTypeLabel;
    private JLabel shippingYesNoLabel;
    private JLabel itemShippingCostLabel;
    private JLabel saleTypeCondition1;
    private JTextField condition1Input;
    private JLabel saleTypeCondition2;
    private JTextField condition2Input;
    private JButton addItemButton;
    private JTextField itemPriceInput;
    private JLabel itemPriceLabel;
    private ShoppingMainGUI mainForm;

    private SaleType selectedSaleType = SaleType.None;

    private double taxRate;

    /**
     * This is a contructor that creates the add item menu
     * @param mainForm
     */
    public AddItemMenu(ShoppingMainGUI mainForm, double taxRate) {
        this.mainForm = mainForm;
        this.taxRate = taxRate;

        itemNameInput.setPreferredSize(new Dimension(200, 24));

        itemQuantityInput.setPreferredSize(new Dimension(200, 24));

        itemPriceInput.setPreferredSize(new Dimension(200, 24));

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

        for (SaleType saleType : SaleType.values()) {
            saleTypeDropDown.addItem(saleType);
        }

        condition2Input.setPreferredSize(new Dimension(200, 24));

        saleTypeCondition1.setVisible(false);
        saleTypeCondition2.setVisible(false);
        condition1Input.setVisible(false);
        condition2Input.setVisible(false);

        saleTypeDropDown.addActionListener(new ActionListener() {
            /**
             * This method shows the condition inputs based on the sale type selected
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedSaleType = (SaleType) saleTypeDropDown.getSelectedItem();
                updateConditionVisibility();
            }
        });


        addItemButton.addActionListener(new ActionListener() {
            /**
             * This method adds an item to the cart
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemName = itemNameInput.getText();

                double itemPrice = Double.parseDouble(itemPriceInput.getText());
                int itemQuantity = Integer.parseInt(itemQuantityInput.getText());
                double itemShippingCost = 0.0;
                if(itemShippingDropDown.getSelectedItem().equals("Yes")) {
                    itemShippingCost = Double.parseDouble(itemShippingInput.getText());
                }

                ShoppingItem item = new ShoppingItem();
                item.setName(itemName);
                item.setPrice(itemPrice);
                item.setQuantity(itemQuantity);
                item.setTaxRate(taxRate);
                item.setHasShipping("Yes".equals(itemShippingDropDown.getSelectedItem()));
                item.setShippingCost(itemShippingCost);
                item.setSaleType(selectedSaleType);
                switch (selectedSaleType) {
                    case None -> {
                        item.setPercentOff(0.0);
                        item.setAmountOff(0.0);
                        item.setAmountX(0);
                        item.setAmountY(0);
                    }
                    case PercentOff -> {
                        item.setPercentOff(Double.parseDouble(condition1Input.getText()));
                        item.setAmountOff(0.0);
                        item.setAmountX(0);
                        item.setAmountY(0);
                    }

                    case BuyXGetPercentOffTotal -> {
                        item.setPercentOff(Double.parseDouble(condition2Input.getText()));
                        item.setAmountOff(0.0);
                        item.setAmountX(Integer.parseInt(condition1Input.getText()));
                        item.setAmountY(0);
                    }
                    case BuyXGetYPercentOff -> {
                        item.setPercentOff(Double.parseDouble(condition2Input.getText()));
                        item.setAmountOff(0.0);
                        item.setAmountX(Integer.parseInt(condition1Input.getText()));
                        item.setAmountY(0);
                    }
                    case BuyXGetYFree -> {
                        item.setPercentOff(0.0);
                        item.setAmountOff(0.0);
                        item.setAmountX(Integer.parseInt(condition1Input.getText()));
                        item.setAmountY(Integer.parseInt(condition2Input.getText()));
                    }
                    case AmountOff -> {
                        item.setPercentOff(0.0);
                        item.setAmountOff(Double.parseDouble(condition1Input.getText()));
                        item.setAmountX(0);
                        item.setAmountY(0);
                    }
                    case AmountOffEach -> {
                        item.setPercentOff(0.0);
                        item.setAmountOff(Double.parseDouble(condition1Input.getText()));
                        item.setAmountX(0);
                        item.setAmountY(0);
                    }
                    case AmountOffTotal -> {
                        item.setPercentOff(0.0);
                        item.setAmountOff(Double.parseDouble(condition1Input.getText()));
                        item.setAmountX(0);
                        item.setAmountY(0);
                    }
                }

                item.setTaxCost(item.getTaxCost());
                mainForm.addItemToCart(item);
                mainForm.getCartTotalTax();
                mainForm.getCartSubtotal();

                dispose();
            }
        });

        add(mainPanel);
        setTitle("Add Item");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * This method sets the visibility of the condition inputs based on the sale type selected
     */
    private void updateConditionVisibility() {

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
                saleTypeCondition1.setVisible(true);
                saleTypeCondition1.setText("Amount Off Total");
                saleTypeCondition2.setVisible(false);
                condition1Input.setVisible(true);
                condition2Input.setVisible(false);
            }
        }
    }
}
