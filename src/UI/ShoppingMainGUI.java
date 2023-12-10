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
import java.util.Locale;

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
        setTitle("Tiny Shopping Helper");
        updateTaxRate(0.0);
        getCartTotalShipping();
        getCartTotalTax();
        getCartSubtotal();
        getCartGrandTotal();
        getAmountSaved();

        useFlatShippingInput.setVisible(false);
        useFlatShippingLabel.setVisible(false);

        useFlatShippingInput.setPreferredSize(new Dimension(100, 25));

        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(TSHPanel, BorderLayout.CENTER);

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
                    return column != 3 && column != 5 && column != 6;
        }};

        shoppingCartTable.setModel(tableModel);

        shoppingCartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int columnIndex = 0; columnIndex < shoppingCartTable.getColumnCount(); columnIndex++) {
            shoppingCartTable.getColumnModel().getColumn(columnIndex).setCellRenderer(centerRenderer);
        }


        tableModel.addTableModelListener(new TableModelListener() {
            private boolean isUpdating = false;

            @Override
            public void tableChanged(TableModelEvent e) {
                if (isUpdating) {
                    return;
                }

                int row = e.getFirstRow();
                int column = e.getColumn();
                if (row >= 0 && column >= 0) {
                    DefaultTableModel model = (DefaultTableModel) e.getSource();
                    ShoppingItem item = shoppingCart.getItems().get(row);

                    try {
                        isUpdating = true;
                        double num = 0;
                        switch (column) {
                            case 0:
                                item.setName((String) model.getValueAt(row, column));
                                break;
                            case 1:
                                item.setPrice(Double.parseDouble((String) model.getValueAt(row, column)));
                                 num = Double.parseDouble((String) model.getValueAt(row, column));
                                model.setValueAt(String.format("%.2f", num), row, column);
                                model.setValueAt(String.format("%.2f", item.getTaxCost()), row, 3);
                                break;
                            case 2:
                                item.setQuantity(Integer.parseInt((String) model.getValueAt(row, column)));
                                model.setValueAt(String.format("%.2f", item.getTaxCost()), row, 3);
                                break;
                            case 3:
                                item.setTaxCost(Double.parseDouble((String) model.getValueAt(row, column)));
                                num = Double.parseDouble((String) model.getValueAt(row, column));
                                model.setValueAt(String.format("%.2f", num), row, column);
                                break;
                            case 4:
                                String shippingValue = ((String) model.getValueAt(row, column)).trim().toLowerCase();
                                if(shippingValue.equals("n/a") || shippingValue.isEmpty() || shippingValue.equals("0")) {
                                    item.setHasShipping(false);
                                    item.setShippingCost(0);
                                    model.setValueAt(0.0, row, 4);
                                } else {
                                    try {
                                        num = Double.parseDouble(shippingValue);
                                        item.setHasShipping(true);
                                        item.setShippingCost(num);
                                        model.setValueAt(String.format("%.2f", num), row, column);
                                    } catch (NumberFormatException ex) {
                                        JOptionPane.showMessageDialog(null, "Invalid shipping cost format", "Error", JOptionPane.ERROR_MESSAGE);
                                        model.setValueAt(0.0, row, 4);
                                    }
                                }

                                break;
                        }

                        SwingUtilities.invokeLater(() -> {
                            String formattedTotal = String.format(Locale.US, "%.2f", item.getTotalPrice(false));
                            model.setValueAt(formattedTotal, row, 6);
                        });

                    } catch (NumberFormatException ex) {
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
                AddItemMenu addItemMenu = new AddItemMenu(ShoppingMainGUI.this, taxRate);
            }
                catch (Exception ex){
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
                updateTaxRate(taxRate);
                getCartTotalShipping();
                getCartTotalTax();
                getCartSubtotal();
                getCartGrandTotal();
                getAmountSaved();
            }
                catch (Exception ex){
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
                int index = shoppingCartTable.getSelectedRow();
                ShoppingItem item = shoppingCart.getItems().get(index);
                EditItemMenu editItemMenu = new EditItemMenu(ShoppingMainGUI.this, item, index);
            }
                catch (Exception ex){
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
                int index = shoppingCartTable.getSelectedRow();
                deleteItem(index);
                updateTaxRate(taxRate);
                getCartTotalShipping();
                getCartTotalTax();
                getCartSubtotal();
                getAmountSaved();
            }
                catch (Exception ex){
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
             JFileChooser fileChooser = new JFileChooser();
             fileChooser.setDialogTitle("Save Progress");
             fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
             var fileFilter = new FileNameExtensionFilter("json", "json");
             fileChooser.setFileFilter(fileFilter);
             int result = fileChooser.showSaveDialog(ShoppingMainGUI.this);
             if (result == JFileChooser.APPROVE_OPTION) {
                 String path = fileChooser.getSelectedFile().getAbsolutePath();
                 if(path.lastIndexOf('.') == -1){
                     path += ".json";
                 }
                 else if(!path.substring(path.lastIndexOf('.')).equals(".json")){
                     path = path.substring(0, path.lastIndexOf('.'));
                     path += ".json";
                 }

                 shoppingCart.saveCart(path);
             }
         }
                catch (Exception ex){
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
             JFileChooser fileChooser = new JFileChooser();
             fileChooser.setDialogTitle("Load Progress");
             fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
             var fileFilter = new FileNameExtensionFilter("json", "json");
             fileChooser.setFileFilter(fileFilter);
             int result = fileChooser.showOpenDialog(ShoppingMainGUI.this);
             if (result == JFileChooser.APPROVE_OPTION) {
                 String path = fileChooser.getSelectedFile().getAbsolutePath();
                 shoppingCart.loadCart(path);
                 updateTaxRate(shoppingCart.getTaxRate());
                 taxRateInput.setText(String.valueOf(shoppingCart.getTaxRate()));
                 updateShoppingCartTableItems();
             }
             getCartTotalShipping();
             getCartTotalTax();
             getCartSubtotal();
             getCartGrandTotal();
             getAmountSaved();
         }
                catch (Exception ex){
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
                    if(useFlatShippingCheckBox.isSelected()){
                        useFlatShippingInput.setVisible(true);
                        useFlatShippingLabel.setVisible(true);
                        shoppingCart.setFlatShipping(true);
                    }
                    else{
                        useFlatShippingInput.setVisible(false);
                        useFlatShippingLabel.setVisible(false);
                        shoppingCart.setFlatShipping(false);
                    }
                    if(!useFlatShippingInput.getText().isEmpty()){
                        double shipping = Double.parseDouble(useFlatShippingInput.getText());
                        if (shipping < 0) {
                            JOptionPane.showMessageDialog(null, "Shipping cannot be negative", "Error", JOptionPane.ERROR_MESSAGE);
                            useFlatShippingInput.setText(String.valueOf(shoppingCart.getShippingCost()));
                        } else {
                            shoppingCart.setShippingCost(shipping);
                            useFlatShippingInput.setText(String.format("%.2f",shipping));
                        }
                    }
                    else {
                        useFlatShippingInput.setText(String.valueOf(shoppingCart.getShippingCost()));
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid number format", "Error", JOptionPane.ERROR_MESSAGE);
                    useFlatShippingInput.setText(String.format("%.2f", shoppingCart.getShippingCost()));
                }
                finally {
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
                ZipCodeInputGUI zipCodeInputGUI = new ZipCodeInputGUI(ShoppingMainGUI.this);
                zipCodeInputGUI.setModal(true);
                zipCodeInputGUI.setVisible(true);
            }
                catch (Exception ex){
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
                    double shipping = Double.parseDouble(useFlatShippingInput.getText());
                    if (shipping < 0) {
                        JOptionPane.showMessageDialog(null, "Shipping cannot be negative", "Error", JOptionPane.ERROR_MESSAGE);
                        useFlatShippingInput.setText(String.valueOf(shoppingCart.getShippingCost()));
                    } else {
                        shoppingCart.setShippingCost(shipping);
                        useFlatShippingInput.setText(String.valueOf(shipping));
                        getCartTotalShipping();
                        getCartGrandTotal();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid number format", "Error", JOptionPane.ERROR_MESSAGE);
                    useFlatShippingInput.setText(String.format("%.2f", shoppingCart.getShippingCost()));
                }
            }
        });

        saveReceiptButton.addActionListener(new ActionListener() {
        /**
         * this method will save the cart to either a txt or csv file will prompt the user with a file dialog choosing where to save the file
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Receipt");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            var fileFilter = new FileNameExtensionFilter("txt", "txt");
            fileChooser.setFileFilter(fileFilter);
            fileFilter = new FileNameExtensionFilter("csv", "csv");
            fileChooser.setFileFilter(fileFilter);
            int result = fileChooser.showSaveDialog(ShoppingMainGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                if(path.lastIndexOf('.') != -1){
                    path = path.substring(0, path.lastIndexOf('.'));
                }
                else if(path.lastIndexOf('.') != -1 && !path.substring(path.lastIndexOf('.')).equals(".txt") && !path.substring(path.lastIndexOf('.')).equals(".csv")){
                    path = path.substring(0, path.lastIndexOf('.'));
                }
                if (fileChooser.getFileFilter().getDescription().equals("txt")) {
                    shoppingCart.saveReceipt(path + ".txt", "txt");
                } else {
                    shoppingCart.saveReceipt(path + ".csv", "csv");
                }
            }
        }
            catch (Exception ex){
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
                    tax = Double.parseDouble(taxRateInput.getText());
                    if (tax < 0) {
                        JOptionPane.showMessageDialog(null, "Tax rate cannot be negative", "Error", JOptionPane.ERROR_MESSAGE);
                        taxRateInput.setText(String.valueOf(taxRate));
                    } else {
                        taxRate = tax;
                        shoppingCart.setTaxRate(tax);

                        for (ShoppingItem item : shoppingCart.getItems()) {
                            item.setTaxRate(tax);
                        }

                        cartTaxRateLabel.setText("Tax Rate: " + tax * 100 + "%");

                        updateShoppingCartTableItems();
                        getCartTotalShipping();
                        getCartTotalTax();
                        getCartSubtotal();
                        getAmountSaved();
                    }
                } catch (NumberFormatException ex) {
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
                    int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear the cart?", "Clear Cart", JOptionPane.YES_NO_OPTION);
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
                super.windowOpened(e);
                ZipCodeInputGUI zipCodeInputGUI = new ZipCodeInputGUI(ShoppingMainGUI.this);
                zipCodeInputGUI.setModal(true);
                zipCodeInputGUI.setVisible(true);
            }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(null, "Error loading tax rate", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        setSize(900, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * this is a method to add an item to the cart
     * @param item
     */
    public void addItemToCart(ShoppingItem item) {
        shoppingCart.addItem(item);
        DefaultTableModel tableModel = (DefaultTableModel) shoppingCartTable.getModel();

        Object[] rowData = {
                item.getName(),
                String.format("%.2f", item.getPrice()),
                item.getQuantity(),
                String.format("%.2f", item.getTaxCost()),
                String.format("%.2f", item.getShippingCost()),
                String.format("%.2f", item.getTotalPrice(false))
        };
        tableModel.addRow(rowData);
    }

    /**
     * this is a method to delete an item in the cart
     * @param index
     */
    public void deleteItem(int index) {
        shoppingCart.getItems().remove(index);
        DefaultTableModel tableModel = (DefaultTableModel) shoppingCartTable.getModel();
        tableModel.removeRow(index);
        updateShoppingCartTableItems();
    }

    /**
     * this gets the shipping of the cart
     */
    public void getCartTotalShipping() {
        double totalShipping = shoppingCart.calculateShippingCost();
        cartTotalShippingLabel.setText(String.format("Total Shipping: %.2f", totalShipping));
    }
    /**
     * this gets the tax of the cart
     */
    public void getCartTotalTax() {
        double totalTax = shoppingCart.calculateTotalTax();
        cartTotalTaxLabel.setText(String.format("Total Tax: %.2f", totalTax));
    }

    /**
     * this gets the amount saved of the cart
     */
    public void getAmountSaved(){
        double amountSaved = shoppingCart.getAmountSaved();
        amountSavedLabel.setText(String.format("Amount Saved: %.2f", amountSaved));
    }

    /**
     * this gets the subtotal of the cart
     */
    public void getCartSubtotal() {
        double subtotal = shoppingCart.calculateSubTotal();
        cartSubtotalLabel.setText(String.format("Subtotal: %.2f", subtotal));
    }

    /**
     * this gets the total of the cart
     */
    public void getCartGrandTotal() {
        double grandTotal = shoppingCart.calculateTotal();
        cartGrandTotalLabel.setText(String.format("Grand Total: %.2f", grandTotal));
    }

    public void updateShoppingCartTableItems() {
        DefaultTableModel tableModel = (DefaultTableModel) shoppingCartTable.getModel();
        tableModel.setRowCount(0);
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

    /**
     * this is a method to update the cart table
     * @param updatedItem
     * @param rowIndex
     */
    public void updateShoppingCartTableItem(ShoppingItem updatedItem, int rowIndex) {
        DefaultTableModel tableModel = (DefaultTableModel) shoppingCartTable.getModel();

        tableModel.setValueAt(updatedItem.getName(), rowIndex, 0);
        tableModel.setValueAt(String.format("%.2f", updatedItem.getPrice()), rowIndex, 1);
        tableModel.setValueAt(String.valueOf(updatedItem.getQuantity()), rowIndex, 2);

        double taxCost = updatedItem.getTaxCost();
        tableModel.setValueAt(String.format("%.2f", taxCost), rowIndex, 3);

        tableModel.setValueAt(String.format("%.2f", updatedItem.getShippingCost()), rowIndex, 4);

        double amountSaved = updatedItem.getAmountSaved();
        tableModel.setValueAt(String.format("%.2f", amountSaved), rowIndex, 5);

        double total = updatedItem.getTotalPrice(false);
        tableModel.setValueAt(String.format("%.2f", total), rowIndex, 6);

        tableModel.fireTableRowsUpdated(rowIndex, rowIndex);
    }

    /**
     * this is a method to update the tax rate
     * @param taxRate
     */
    public void updateTaxRate(double taxRate) {
        this.taxRate = taxRate;
        shoppingCart.setTaxRate(taxRate);

        for (ShoppingItem item : shoppingCart.getItems()) {
            item.setTaxRate(taxRate);
        }

        cartTaxRateLabel.setText("Tax Rate: " + taxRate * 100 + "%");
    }

    /**
     * this is the main method
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ShoppingMainGUI mainForm = new ShoppingMainGUI();
            mainForm.setVisible(true);
        });
    }

}
