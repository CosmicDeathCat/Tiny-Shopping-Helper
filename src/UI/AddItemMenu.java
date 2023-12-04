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
    private JLabel saleTypeConditon1;
    private JTextField condition1Input;
    private JLabel saleTypeConditon2;
    private JTextField condition2Input;
    private JButton addItemButton;
    private JTextField itemPriceInput;
    private JLabel itemPriceLabel;
    private ShoppingMainGUI mainForm;

    private SaleType selectedSaleType = SaleType.None;

    /**
     * This is a contructor that creates the add item menu
     * @param mainForm
     */
    public AddItemMenu(ShoppingMainGUI mainForm) {
        this.mainForm = mainForm;
        getContentPane().setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(itemNameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        itemNameInput.setPreferredSize(new Dimension(200, 24));
        contentPanel.add(itemNameInput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(itemQuantityLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        itemQuantityInput.setPreferredSize(new Dimension(200, 24));
        contentPanel.add(itemQuantityInput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(itemPriceLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        itemPriceInput.setPreferredSize(new Dimension(200, 24));
        contentPanel.add(itemPriceInput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(shippingYesNoLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        itemShippingDropDown = new JComboBox<>(new String[]{"No", "Yes"});
        contentPanel.add(itemShippingDropDown, gbc);

        itemShippingCostLabel.setVisible(false);
        itemShippingInput.setVisible(false);

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

        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPanel.add(itemShippingCostLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        itemShippingInput.setPreferredSize(new Dimension(200, 24));
        contentPanel.add(itemShippingInput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        contentPanel.add(itemSaleTypeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        saleTypeDropDown = new JComboBox<>(SaleType.values());
        contentPanel.add(saleTypeDropDown, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        contentPanel.add(saleTypeConditon1, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        condition1Input.setPreferredSize(new Dimension(200, 24));
        contentPanel.add(condition1Input, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        contentPanel.add(saleTypeConditon2, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        condition2Input.setPreferredSize(new Dimension(200, 24));
        contentPanel.add(condition2Input, gbc);


        saleTypeConditon1.setVisible(false);
        saleTypeConditon2.setVisible(false);
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

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        contentPanel.add(addItemButton, gbc);

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
                        item.setPercentOff(Double.parseDouble(condition1Input.getText()));
                        item.setAmountOff(0.0);
                        item.setAmountX(Integer.parseInt(condition2Input.getText()));
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

                mainForm.addItemToCart(item);

                dispose();
            }
        });

        getContentPane().add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * This method sets the visibility of the condition inputs based on the sale type selected
     */
    private void updateConditionVisibility() {

        switch (selectedSaleType) {
            case None -> {
                saleTypeConditon1.setVisible(false);
                saleTypeConditon2.setVisible(false);
                condition1Input.setVisible(false);
                condition2Input.setVisible(false);
            }
            case PercentOff -> {
                saleTypeConditon1.setVisible(true);
                saleTypeConditon1.setText("Percent Off");
                saleTypeConditon2.setVisible(false);
                condition1Input.setVisible(true);
                condition2Input.setVisible(false);
            }
            case BuyXGetPercentOffTotal -> {
                saleTypeConditon1.setVisible(true);
                saleTypeConditon1.setText("Buy How Many?");
                saleTypeConditon2.setVisible(true);
                saleTypeConditon2.setText("Percent Off");
                condition1Input.setVisible(true);
                condition2Input.setVisible(true);
            }
            case BuyXGetYPercentOff -> {
                saleTypeConditon1.setVisible(true);
                saleTypeConditon1.setText("Buy How Many?");
                saleTypeConditon2.setVisible(true);
                saleTypeConditon2.setText("Percent Off");
                condition1Input.setVisible(true);
                condition2Input.setVisible(true);
            }
            case BuyXGetYFree -> {
                saleTypeConditon1.setVisible(true);
                saleTypeConditon1.setText("Buy How Many?");
                saleTypeConditon2.setVisible(true);
                saleTypeConditon2.setText("Get How Many Free?");
                condition1Input.setVisible(true);
                condition2Input.setVisible(true);
            }
            case AmountOff -> {
                saleTypeConditon1.setVisible(true);
                saleTypeConditon1.setText("Amount Off");
                saleTypeConditon2.setVisible(false);
                condition1Input.setVisible(true);
                condition2Input.setVisible(false);
            }
            case AmountOffEach -> {
                saleTypeConditon1.setVisible(true);
                saleTypeConditon1.setText("Amount Off Each");
                saleTypeConditon2.setVisible(false);
                condition1Input.setVisible(true);
                condition2Input.setVisible(false);
            }
            case AmountOffTotal -> {
                saleTypeConditon1.setVisible(true);
                saleTypeConditon1.setText("Amount Off Total");
                saleTypeConditon2.setVisible(false);
                condition1Input.setVisible(true);
                condition2Input.setVisible(false);
            }
        }
    }
}
