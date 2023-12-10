package ui;

import data.SaleType;
import data.ShoppingItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditItemMenu extends JFrame{
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

    public EditItemMenu(ShoppingMainGUI main, ShoppingItem item, int rowIndex) {

        this.mainForm = main;
        this.rowIndex = rowIndex;
        this.ItemNameInput.setText(item.getName());
        this.ItemQuantityInput.setText(String.valueOf(item.getQuantity()));
        this.itemPriceInput.setText(String.valueOf(item.getPrice()));

        for (SaleType saleType : SaleType.values()) {
            saleTypeDropDown.addItem(saleType);
        }
        saleTypeDropDown.setSelectedItem(item.getSaleType());
        selectedSaleType = item.getSaleType();
        updateConditionVisibility();

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

        int shippingIndex = item.getHasShipping() ? 1 : 0;
        itemShippingDropDown.setSelectedIndex(shippingIndex);
        itemShippingInput.setText(String.valueOf(item.getShippingCost()));

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

        saveItemButton.addActionListener(new ActionListener() {
            /**
             * This method saves the item to the cart
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                item.setName(ItemNameInput.getText());
                item.setQuantity(Integer.parseInt(ItemQuantityInput.getText()));
                item.setPrice(Double.parseDouble(itemPriceInput.getText()));
                item.setSaleType(selectedSaleType);

                switch (item.getSaleType()){

                    case None -> {
                    }
                    case PercentOff -> {
                        item.setPercentOff(Double.parseDouble(condition1Input.getText()));
                    }
                    case BuyXGetPercentOffTotal -> {
                        item.setAmountX(Integer.parseInt(condition1Input.getText()));
                        item.setPercentOff(Double.parseDouble(condition2Input.getText()));
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
                    }
                }
                item.setHasShipping(itemShippingDropDown.getSelectedIndex() == 1);
                item.setShippingCost(Double.parseDouble(itemShippingInput.getText()));
                updateShoppingCartTable(item);
                dispose();
            }
        });
        add(mainPanel);
        setTitle("Edit Item");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

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
                condition1Input.setVisible(true);
                saleTypeCondition1.setText("Amount Off Total");
                saleTypeCondition2.setVisible(false);
                condition1Input.setVisible(true);
                condition2Input.setVisible(false);
            }
        }
    }

    private void updateShoppingCartTable(ShoppingItem updatedItem) {
        DefaultTableModel tableModel = (DefaultTableModel) mainForm.shoppingCartTable.getModel();

        // Update the table model with the new details of the edited item
        tableModel.setValueAt(updatedItem.getName(), rowIndex, 0);
        tableModel.setValueAt(String.format("%.2f", updatedItem.getPrice()), rowIndex, 1);
        tableModel.setValueAt(String.valueOf(updatedItem.getQuantity()), rowIndex, 2);

        // Calculate the tax amount and convert to string
        double taxRate = updatedItem.getTaxRate();
        tableModel.setValueAt(String.format("%.2f", taxRate), rowIndex, 3);

        // Convert shipping cost to string
        tableModel.setValueAt(String.format("%.2f", updatedItem.getShippingCost()), rowIndex, 4);

        // Calculate the total and convert to string
        double total = updatedItem.getTotalPrice(false, false);
        tableModel.setValueAt(String.format("%.2f", total), rowIndex, 5);

        // Update the UI
        tableModel.fireTableRowsUpdated(rowIndex, rowIndex);
    }



}
