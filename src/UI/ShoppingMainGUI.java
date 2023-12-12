package ui;

import data.ShoppingCart;
import data.ShoppingItem;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This class contains methods for the main GUI
 */
public class ShoppingMainGUI extends JFrame{

    public JTextField taxRateInput;
    public double taxRate;

    public static ShoppingCart shoppingCart = new ShoppingCart();
    public JTable shoppingCartTable;
    private JPanel TSHPanel;
    private JPanel btnBar;
    private JLabel applicationNameLabel;
    private JPanel innerContentPanel;
    private JPanel topButtonBar;
    private JButton addItemButton;
    private JButton calculateTotalButton;
    private JButton saveReceiptButton;
    private JButton saveProgressButton;
    private JScrollPane shoppingCartScroll;
    private JButton deleteButton;
    private JLabel cartGrandTotalLabel;
    private JButton loadButton;
    private JButton editButton;
    private JLabel cartTaxRateLabel;
    private JLabel cartSubtotalLabel;
    private JLabel cartTotalTaxLabel;
    private JLabel cartTotalShippingLabel;
    private JPanel secondButtonBar;
    private JCheckBox useFlatShippingCheckBox;
    private JLabel useFlatShippingLabel;
    private JTextField useFlatShippingInput;
    private JButton zipCodeEntryButton;
    private JLabel amountSavedLabel;
    private JButton clearAllButton;


    /**
     * this is a constructor for the main GUI
     */
    public ShoppingMainGUI() {
        //these are the labels for the GUI
        setTitle("Tiny Shopping Helper");
        updateTaxRate(0.0);
        getCartTotalShipping();
        getCartTotalTax();
        getCartSubtotal();
        getCartGrandTotal();
        getAmountSaved();
        //shows the flat shipping input if the checkbox is selected
        useFlatShippingInput.setVisible(false);
        useFlatShippingLabel.setVisible(false);
        useFlatShippingInput.setPreferredSize(new Dimension(100, 25));
        //this jpnael is for the table
        JPanel contentPane = (JPanel) getContentPane();
        //this content pane is for the border layout
        contentPane.setLayout(new BorderLayout());
        //this content pane is for the table
        contentPane.add(TSHPanel, BorderLayout.CENTER);
        //creates the table
        DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{
                    "Item Name", "Item Price", "Item Quantity", "Item Tax Cost","Shipping Cost", "Amount Saved", "Item Total"
            },
            0){
        /**
         * this is a method to make the table editable
         * @param row             the row whose value is to be queried
         * @param column          the column whose value is to be queried
         * @return
         */
                @Override
                public boolean isCellEditable(int row, int column) {
                    //Only the first 3 columns are editable the rest are not
                    return column != 3 && column != 5 && column != 6;
        }};
        //sets the table model
        shoppingCartTable.setModel(tableModel);
        //sets the table to be single selection
        shoppingCartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //sets the table to be centered
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        //this for loop sets the table to be centered
        for (int columnIndex = 0; columnIndex < shoppingCartTable.getColumnCount(); columnIndex++) {
            shoppingCartTable.getColumnModel().getColumn(columnIndex).setCellRenderer(centerRenderer);
        }


        tableModel.addTableModelListener(new TableModelListener() {

            private boolean isUpdating = false;

            /**
             * This method is called when the data in the table is changed
             * @param e a {@code TableModelEvent} to notify listener that a table model
             *          has changed
             */
            @Override
            public void tableChanged(TableModelEvent e) {
                //Prevent infinite recursion
                if (isUpdating) {
                    return;
                }

                int row = e.getFirstRow();
                int column = e.getColumn();
                //If the row or column is less than 0 then the table is empty
                if (row >= 0 && column >= 0) {
                    DefaultTableModel model = (DefaultTableModel) e.getSource();
                    ShoppingItem item = shoppingCart.getItems().get(row);

                    try {

                        isUpdating = true;
                        double num = 0;
                        switch (column) {
                            case 0:
                                try {
                                    //Set the name of the item
                                    item.setName((String) model.getValueAt(row, column));
                                } catch (IllegalArgumentException ex) {
                                    JOptionPane.showMessageDialog(null, "Invalid name", "Error", JOptionPane.ERROR_MESSAGE);
                                    model.setValueAt(item.getName(), row, column);
                                }
                                    break;
                            case 1:
                                try {
                                    //Set the price of the item
                                    double price = Double.parseDouble((String) model.getValueAt(row, column));
                                    if (price < 0) {
                                        JOptionPane.showMessageDialog(null, "Price cannot be negative", "Error", JOptionPane.ERROR_MESSAGE);
                                        model.setValueAt(String.format("%.2f", item.getPrice()), row, column);
                                    } else {
                                        item.setPrice(price);
                                        model.setValueAt(String.format("%.2f", item.getPrice()), row, column);
                                    }
                                    model.setValueAt(String.format("%.2f", item.getTaxCost()), row, 3);
                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(null, "Invalid price format", "Error", JOptionPane.ERROR_MESSAGE);
                                    model.setValueAt(String.format("%.2f", item.getPrice()), row, column);
                                }
                                break;
                            case 2:
                                try {
                                    //Set the quantity of the item
                                    int quantity = Integer.parseInt((String) model.getValueAt(row, column));
                                    if (quantity < 0) {
                                        JOptionPane.showMessageDialog(null, "Quantity cannot be negative", "Error", JOptionPane.ERROR_MESSAGE);
                                        model.setValueAt(item.getQuantity(), row, column);
                                    } else {
                                        item.setQuantity(quantity);
                                        model.setValueAt(item.getQuantity(), row, column);
                                    }
                                }
                                catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(null, "Invalid quantity format", "Error", JOptionPane.ERROR_MESSAGE);
                                    model.setValueAt(item.getQuantity(), row, column);
                                }
                                break;
                            case 3:
                                try {
                                    //Set the tax cost of the item
                                    double tax = Double.parseDouble((String) model.getValueAt(row, column));
                                    if (tax < 0) {
                                        JOptionPane.showMessageDialog(null, "Tax cannot be negative", "Error", JOptionPane.ERROR_MESSAGE);
                                        model.setValueAt(String.format("%.2f", item.getTaxCost()), row, column);
                                    } else {
                                        item.setTaxCost(tax);
                                        model.setValueAt(String.format("%.2f", item.getTaxCost()), row, column);
                                    }
                                }
                                catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(null, "Invalid tax cost format", "Error", JOptionPane.ERROR_MESSAGE);
                                    model.setValueAt(String.format("%.2f", item.getTaxCost()), row, column);
                                }
                                break;
                            case 4:
                                try {
                                    //Set the shipping cost of the item
                                    double shipping = Double.parseDouble((String) model.getValueAt(row, column));
                                    if (shipping < 0) {
                                        JOptionPane.showMessageDialog(null, "Shipping cannot be negative", "Error", JOptionPane.ERROR_MESSAGE);
                                        model.setValueAt(String.format("%.2f", item.getShippingCost()), row, column);
                                    }
                                    else {
                                        item.setHasShipping(shipping != 0);
                                        item.setShippingCost(shipping);
                                        model.setValueAt(String.format("%.2f", item.getShippingCost()), row, column);
                                    }
                                }
                                catch (NumberFormatException ex) {
                                    //if there is an error it will show an error message
                                    JOptionPane.showMessageDialog(null, "Invalid shipping cost format", "Error", JOptionPane.ERROR_MESSAGE);
                                    model.setValueAt(String.format("%.2f", item.getShippingCost()), row, column);
                                }
                                break;
                        }
                        //updates the table
                        SwingUtilities.invokeLater(() -> {
                            try{
                                updateShoppingCartTableItems();
                        }
                            catch (Exception ex){
                                //if there is an error updating the table it will show an error message
                                JOptionPane.showMessageDialog(null, "Error updating cart", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        });

                    } catch (NumberFormatException ex) {
                        //this is an error message if there is an invalid number format such as letters or symbols
                        JOptionPane.showMessageDialog(null, "Invalid number format", "Error", JOptionPane.ERROR_MESSAGE);
                        if(column == 1) model.setValueAt(String.format("%.2f", item.getPrice()), row, column);
                        if(column == 2) model.setValueAt(item.getQuantity(), row, column);
                        if(column == 3) model.setValueAt(String.format("%.2f", item.getTaxCost()), row, column);
                        if(column == 4) model.setValueAt(String.format("%.2f", item.getShippingCost()), row, column);
                    } finally {
                        isUpdating = false;
                        updateTaxRate(taxRate);
                        getCartTotalShipping();
                        getCartTotalTax();
                        getCartSubtotal();
                        getAmountSaved();
                    }
                }
            }
        });


        addItemButton.addActionListener(new ActionListener() {
            /**
             * this is a method to add an item to the cart
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    //creates a new add item menu
                AddItemMenu addItemMenu = new AddItemMenu(ShoppingMainGUI.this, taxRate);
            }
                catch (Exception ex){
                    //this error message will show if there is an error adding an item
                    JOptionPane.showMessageDialog(null, "Error adding item", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        calculateTotalButton.addActionListener(new ActionListener() {
            /**
             * this is a method to calculate the total of the cart
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    //calculates the total of the cart
                updateTaxRate(taxRate);
                getCartTotalShipping();
                getCartTotalTax();
                getCartSubtotal();
                getCartGrandTotal();
                getAmountSaved();
            }
                catch (Exception ex){
                    //this error message will show if there is an error calculating the total
                    JOptionPane.showMessageDialog(null, "Error calculating total", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        editButton.addActionListener(new ActionListener() {
            /**
             * this is a method to edit an item in the cart
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    //creates a new edit item menu
                int index = shoppingCartTable.getSelectedRow();
                ShoppingItem item = shoppingCart.getItems().get(index);
                EditItemMenu editItemMenu = new EditItemMenu(ShoppingMainGUI.this, item, index);
            }
                catch (Exception ex){
                    //this error message will show if there is an error editing an item
                    JOptionPane.showMessageDialog(null, "Error editing item. Please select an item to edit.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            /**
             * this is a method to delete an item in the cart
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    //deletes an item in the cart
                int index = shoppingCartTable.getSelectedRow();
                deleteItem(index);
                updateTaxRate(taxRate);
                getCartTotalShipping();
                getCartTotalTax();
                getCartSubtotal();
                getAmountSaved();
            }
                catch (Exception ex){
                    //this error message will show if there is an error deleting an item
                    JOptionPane.showMessageDialog(null, "Error deleting item", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        saveProgressButton.addActionListener(new ActionListener() {
         /**
          * this method will save the cart to a Json file using GSON will prompt the user with a file dialog choosing where to save the file
          *
          * @param e
          */
         @Override
         public void actionPerformed(ActionEvent e) {
             try{
                 //saves the cart to a Json file
             JFileChooser fileChooser = new JFileChooser();
             fileChooser.setDialogTitle("Save Progress");
             fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
             var fileFilter = new FileNameExtensionFilter("json", "json");
             fileChooser.setFileFilter(fileFilter);
             int result = fileChooser.showSaveDialog(ShoppingMainGUI.this);
             //if the user selects a file it will save the cart
             if (result == JFileChooser.APPROVE_OPTION) {
                 String path = fileChooser.getSelectedFile().getAbsolutePath();
                 //if the file does not have an extension it will add the extension
                 if(path.lastIndexOf('.') == -1){
                     path += ".json";
                 }
                 //else if the file does not have a json extension it will add the extension
                 else if(!path.substring(path.lastIndexOf('.')).equals(".json")){
                     path = path.substring(0, path.lastIndexOf('.'));
                     path += ".json";
                 }
                //saves the cart
                 shoppingCart.saveCart(path);
             }
         }
                catch (Exception ex){
                     //this error message will show if there is an error saving the cart
                    JOptionPane.showMessageDialog(null, "Error saving cart", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
     });
        loadButton.addActionListener(new ActionListener() {
         /**
          * this method will load the cart from a Json file using GSON will prompt the user with a file dialog choosing where to load the file from
          *
          * @param e
          */
         @Override
         public void actionPerformed(ActionEvent e) {
             try{
                 //loads the cart from a Json file
             JFileChooser fileChooser = new JFileChooser();
             fileChooser.setDialogTitle("Load Progress");
             fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
             var fileFilter = new FileNameExtensionFilter("json", "json");
             fileChooser.setFileFilter(fileFilter);
             int result = fileChooser.showOpenDialog(ShoppingMainGUI.this);
             //if the user selects a file it will load the cart
             if (result == JFileChooser.APPROVE_OPTION) {
                 String path = fileChooser.getSelectedFile().getAbsolutePath();
                 shoppingCart.loadCart(path);
                 updateTaxRate(shoppingCart.getTaxRate());
                 taxRateInput.setText(String.valueOf(shoppingCart.getTaxRate()));
                 updateShoppingCartTableItems();
             }
             //these methods update the cart
             getCartTotalShipping();
             getCartTotalTax();
             getCartSubtotal();
             getCartGrandTotal();
             getAmountSaved();
         }
                catch (Exception ex){
                 //this error message will show if there is an error loading the cart
                    JOptionPane.showMessageDialog(null, "Error loading cart", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
     });

        useFlatShippingCheckBox.addActionListener(new ActionListener() {
            /**
             * this is a method to change the tax rate
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //if the checkbox is selected it will show the flat shipping input
                    if(useFlatShippingCheckBox.isSelected()){
                        useFlatShippingInput.setVisible(true);
                        useFlatShippingLabel.setVisible(true);
                        shoppingCart.setFlatShipping(true);
                    }
                    else{
                        //if the checkbox is not selected it will hide the flat shipping input
                        useFlatShippingInput.setVisible(false);
                        useFlatShippingLabel.setVisible(false);
                        shoppingCart.setFlatShipping(false);
                    }
                    if(!useFlatShippingInput.getText().isEmpty()){
                        //if the flat shipping input is not empty it will set the shipping cost to the input
                        double shipping = Double.parseDouble(useFlatShippingInput.getText());
                        if (shipping < 0) {
                            JOptionPane.showMessageDialog(null, "Shipping cannot be negative", "Error", JOptionPane.ERROR_MESSAGE);
                            useFlatShippingInput.setText(String.valueOf(shoppingCart.getShippingCost()));
                        } else {
                            //if the flat shipping input is not empty it will set the shipping cost to the input
                            shoppingCart.setShippingCost(shipping);
                            useFlatShippingInput.setText(String.format("%.2f",shipping));
                        }
                    }
                    else {
                        //if the flat shipping input is empty it will set the shipping cost to the default
                        useFlatShippingInput.setText(String.valueOf(shoppingCart.getShippingCost()));
                    }

                } catch (NumberFormatException ex) {
                    //this error message will show if there is an error changing the tax rate
                    JOptionPane.showMessageDialog(null, "Invalid number format", "Error", JOptionPane.ERROR_MESSAGE);
                    useFlatShippingInput.setText(String.format("%.2f", shoppingCart.getShippingCost()));
                }
                finally {
                    //updates the tax rate
                    updateTaxRate(taxRate);
                    getCartTotalShipping();
                    getCartTotalTax();
                    getCartSubtotal();
                    getCartGrandTotal();
                    getAmountSaved();
                }
            }
        });

        zipCodeEntryButton.addActionListener(new ActionListener() {
            /**
             * This method is called when the user clicks the enter zip code button
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    //creates a new zip code input GUI
                ZipCodeInputGUI zipCodeInputGUI = new ZipCodeInputGUI(ShoppingMainGUI.this);
                zipCodeInputGUI.setModal(true);
                zipCodeInputGUI.setVisible(true);
            }
                catch (Exception ex){
                    //this error message will show if there is an error loading the tax rate
                    JOptionPane.showMessageDialog(null, "Error loading tax rate", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        useFlatShippingInput.addActionListener(new ActionListener() {
            /**
             * this is a method to change the tax rate
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //if the flat shipping input is not empty it will set the shipping cost to the input
                    double shipping = Double.parseDouble(useFlatShippingInput.getText());
                    //if the flat shipping input is not empty it will set the shipping cost to the input
                    if (shipping < 0) {
                        JOptionPane.showMessageDialog(null, "Shipping cannot be negative", "Error", JOptionPane.ERROR_MESSAGE);
                        useFlatShippingInput.setText(String.valueOf(shoppingCart.getShippingCost()));
                    } else {
                        //if the flat shipping input is not empty it will set the shipping cost to the input
                        shoppingCart.setShippingCost(shipping);
                        useFlatShippingInput.setText(String.valueOf(shipping));
                        getCartTotalShipping();
                        getCartGrandTotal();
                    }
                } catch (NumberFormatException ex) {
                    //this error message will show if there is an error changing the number format
                    JOptionPane.showMessageDialog(null, "Invalid number format", "Error", JOptionPane.ERROR_MESSAGE);
                    useFlatShippingInput.setText(String.format("%.2f", shoppingCart.getShippingCost()));
                }
            }
        });
        //creates a new receipt
        saveReceiptButton.addActionListener(new ActionListener() {
        /**
         * this method will save the cart to either a txt or csv file will prompt the user with a file dialog choosing where to save the file
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                //saves the receipt to a txt or csv file
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Receipt");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            var fileFilter = new FileNameExtensionFilter("txt", "txt");
            fileChooser.setFileFilter(fileFilter);
            fileFilter = new FileNameExtensionFilter("csv", "csv");
            fileChooser.setFileFilter(fileFilter);
            int result = fileChooser.showSaveDialog(ShoppingMainGUI.this);
            //if the user selects a file it will save the receipt
            if (result == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                //if the file does not have an extension it will add the extension
                if(path.lastIndexOf('.') != -1){
                    path = path.substring(0, path.lastIndexOf('.'));
                }
                else if(path.lastIndexOf('.') != -1 && !path.substring(path.lastIndexOf('.')).equals(".txt") && !path.substring(path.lastIndexOf('.')).equals(".csv")){
                    path = path.substring(0, path.lastIndexOf('.'));
                }
                //if the file is a txt file it will save the receipt as a txt file
                if (fileChooser.getFileFilter().getDescription().equals("txt")) {
                    shoppingCart.saveReceipt(path + ".txt", "txt");
                } else {
                    //if the file is a csv file it will save the receipt as a csv file
                    shoppingCart.saveReceipt(path + ".csv", "csv");
                }
            }
        }
            catch (Exception ex){
                //this error message will show if there is an error saving the receipt
                JOptionPane.showMessageDialog(null, "Error saving receipt", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

        taxRateInput.addActionListener(new ActionListener() {
            /**
             * this is a method to change the tax rate
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                double tax = 0.0;
                try {
                    //if the tax rate input is not empty it will set the tax rate to the input
                    tax = Double.parseDouble(taxRateInput.getText());
                    //if the tax rate input is not empty it will set the tax rate to the input
                    if (tax < 0) {
                        JOptionPane.showMessageDialog(null, "Tax rate cannot be negative", "Error", JOptionPane.ERROR_MESSAGE);
                        taxRateInput.setText(String.valueOf(taxRate));
                    } else {
                        //updates the tax rate
                        taxRate = tax;
                        shoppingCart.setTaxRate(tax);
                        //this for loop sets the tax rate for each item in the cart
                        for (ShoppingItem item : shoppingCart.getItems()) {
                            item.setTaxRate(tax);
                        }
                        //this label shows the tax rate
                        cartTaxRateLabel.setText("Tax Rate: " + tax * 100 + "%");
                        //these get the total shipping, tax, subtotal, grand total, and amount saved
                        updateShoppingCartTableItems();
                        getCartTotalShipping();
                        getCartTotalTax();
                        getCartSubtotal();
                        getAmountSaved();
                    }
                } catch (NumberFormatException ex) {
                    //this error message will show if there is an error changing the number format
                    JOptionPane.showMessageDialog(null, "Invalid number format", "Error", JOptionPane.ERROR_MESSAGE);
                    taxRateInput.setText(String.valueOf(tax));
                }
            }
        });

        clearAllButton.addActionListener(new ActionListener() {
            /**
             * this is a method to clear the cart
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    //clears the cart
                    int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear the cart?", "Clear Cart", JOptionPane.YES_NO_OPTION);
                    //if the user selects yes it will clear the cart
                    if(result == JOptionPane.YES_OPTION) {
                        shoppingCart.clearCart();
                        updateShoppingCartTableItems();
                        getCartTotalShipping();
                        getCartTotalTax();
                        getCartSubtotal();
                        getCartGrandTotal();
                        getAmountSaved();
                    }

            }
                catch (Exception ex){
                    //this error message will show if there is an error clearing the cart
                    JOptionPane.showMessageDialog(null, "Error clearing cart", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        this.addWindowListener(new WindowAdapter() {
            /**
             * This method is called when the window is opened
             * @param e
             */
            @Override
            public void windowOpened(WindowEvent e) {
                try{
                    //creates a new zip code input GUI
                super.windowOpened(e);
                ZipCodeInputGUI zipCodeInputGUI = new ZipCodeInputGUI(ShoppingMainGUI.this);
                zipCodeInputGUI.setModal(true);
                zipCodeInputGUI.setVisible(true);
            }
                catch (Exception ex){
                    //this error message will show if there is an error loading the tax rate
                    JOptionPane.showMessageDialog(null, "Error loading tax rate", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //sets the size of the GUI
        setSize(900, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * this is a method to add an item to the cart
     * @param item
     */
    public void addItemToCart(ShoppingItem item) {
        try{
            //adds an item to the cart
        shoppingCart.addItem(item);
        DefaultTableModel tableModel = (DefaultTableModel) shoppingCartTable.getModel();
        //adds the item to the table
        Object[] rowData = {
                item.getName(),
                String.format("%.2f", item.getPrice()),
                item.getQuantity(),
                String.format("%.2f", item.getTaxCost()),
                String.format("%.2f", item.getShippingCost()),
                String.format("%.2f", item.getTotalPrice(false))
        };
        //this adds the row to the table
        tableModel.addRow(rowData);
    }
        catch (Exception ex){
            //this error message will show if there is an error adding an item
            JOptionPane.showMessageDialog(null, "Error adding item", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * this is a method to delete an item in the cart
     * @param index
     */
    public void deleteItem(int index) {
        try{
            //deletes an item in the cart
        shoppingCart.getItems().remove(index);
        DefaultTableModel tableModel = (DefaultTableModel) shoppingCartTable.getModel();
        tableModel.removeRow(index);
        updateShoppingCartTableItems();
    }
        catch (Exception ex){
            //this error message will show if there is an error deleting an item
            JOptionPane.showMessageDialog(null, "Error deleting item", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * this gets the shipping of the cart
     */
    public void getCartTotalShipping() {
        //gets the shipping of the cart
        double totalShipping = shoppingCart.calculateShippingCost();
        cartTotalShippingLabel.setText(String.format("Total Shipping: %.2f", totalShipping));
    }
    /**
     * this gets the tax of the cart
     */
    public void getCartTotalTax() {
        //gets the tax of the cart
        double totalTax = shoppingCart.calculateTotalTax();
        cartTotalTaxLabel.setText(String.format("Total Tax: %.2f", totalTax));
    }

    /**
     * this gets the amount saved of the cart
     */
    public void getAmountSaved(){
        //gets the amount saved of the cart
        double amountSaved = shoppingCart.getAmountSaved();
        amountSavedLabel.setText(String.format("Amount Saved: %.2f", amountSaved));
    }

    /**
     * this gets the subtotal of the cart
     */
    public void getCartSubtotal() {
        //gets the subtotal of the cart
        double subtotal = shoppingCart.calculateSubTotal();
        cartSubtotalLabel.setText(String.format("Subtotal: %.2f", subtotal));
    }

    /**
     * this gets the total of the cart
     */
    public void getCartGrandTotal() {
        //gets the total of the cart
        double grandTotal = shoppingCart.calculateTotal();
        cartGrandTotalLabel.setText(String.format("Grand Total: %.2f", grandTotal));
    }

    public void updateShoppingCartTableItems() {
        try{
            //updates the cart table
        DefaultTableModel tableModel = (DefaultTableModel) shoppingCartTable.getModel();
        tableModel.setRowCount(0);
        //adds the items to the table
        for (ShoppingItem item : shoppingCart.getItems()) {
            Object[] rowData = {
                    item.getName(),
                    String.format("%.2f", item.getPrice()),
                    item.getQuantity(),
                    String.format("%.2f", item.getTaxCost()),
                    String.format("%.2f", item.getShippingCost()),
                    String.format("%.2f", item.getAmountSaved()),
                    String.format("%.2f", item.getTotalPrice(false))
            };
            tableModel.addRow(rowData);
        }
    }
        catch (Exception ex){
            //this error message will show if there is an error updating the cart
            JOptionPane.showMessageDialog(null, "Error updating cart", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * this is a method to update the cart table
     * @param updatedItem
     * @param rowIndex
     */
    public void updateShoppingCartTableItem(ShoppingItem updatedItem, int rowIndex) {
        try {
            //updates the cart table
        DefaultTableModel tableModel = (DefaultTableModel) shoppingCartTable.getModel();
        //these set the values of the table
        tableModel.setValueAt(updatedItem.getName(), rowIndex, 0);
        tableModel.setValueAt(String.format("%.2f", updatedItem.getPrice()), rowIndex, 1);
        tableModel.setValueAt(String.valueOf(updatedItem.getQuantity()), rowIndex, 2);
        //this gets the tax cost of the item
        double taxCost = updatedItem.getTaxCost();
        tableModel.setValueAt(String.format("%.2f", taxCost), rowIndex, 3);
        //this sets value of the shipping cost of the item
        tableModel.setValueAt(String.format("%.2f", updatedItem.getShippingCost()), rowIndex, 4);
        //this gets the amount saved of the item
        double amountSaved = updatedItem.getAmountSaved();
        tableModel.setValueAt(String.format("%.2f", amountSaved), rowIndex, 5);
        //this gets the total price of the item
        double total = updatedItem.getTotalPrice(false);
        tableModel.setValueAt(String.format("%.2f", total), rowIndex, 6);
        //this updates the table
        tableModel.fireTableRowsUpdated(rowIndex, rowIndex);
    }
        catch (Exception ex){
            //this error message will show if there is an error updating the cart
            JOptionPane.showMessageDialog(null, "Error updating cart", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * this is a method to update the tax rate
     * @param taxRate
     */
    public void updateTaxRate(double taxRate) {
        //updates the tax rate
        this.taxRate = taxRate;
        shoppingCart.setTaxRate(taxRate);
        //this for loop sets the tax rate for each item in the cart
        for (ShoppingItem item : shoppingCart.getItems()) {
            item.setTaxRate(taxRate);
        }
        //this label shows the tax rate
        cartTaxRateLabel.setText("Tax Rate: " + taxRate * 100 + "%");
    }

    /**
     * this is the main method
     * @param args
     */
    public static void main(String[] args) {
        //this creates the main GUI
        SwingUtilities.invokeLater(() -> {
            ShoppingMainGUI mainForm = new ShoppingMainGUI();
            mainForm.setVisible(true);
        });
    }

}
