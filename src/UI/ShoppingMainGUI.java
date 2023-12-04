package ui;

import annotations.FieldLabel;
import data.ShoppingCart;
import data.ShoppingItem;
import utility.DataSaver;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;

public class ShoppingMainGUI extends JFrame{
    private JPanel TSHPanel;
    private JPanel btnBar;
    private JLabel applicationNameLabel;
    private JPanel innerContentPanel;
    private JPanel topButtonBar;
    private JButton addItemButton;
    private JButton calculateTotalButton;
    private JTable shoppingCartTable;
    private JButton saveReceiptButton;
    private JButton saveProgressButton;
    private JScrollPane shoppingCartScroll;
    private JButton deleteButton;
    private JLabel cartTotalLabel;
    private JButton loadButton;

    private double taxRate;

    private ShoppingCart shoppingCart = new ShoppingCart();

    /**
     * this is a constructor for the main GUI
     * @param taxRate
     */
    public ShoppingMainGUI(Double taxRate) {
        setTitle("Tiny Shopping Helper");
        this.taxRate = taxRate;
        shoppingCart.setTaxRate(taxRate);

        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(TSHPanel, BorderLayout.CENTER);

        DefaultTableModel tableModel = new DefaultTableModel(
                new String[]{
                        "Item Name", "Item Price", "Item Quantity", "Item Tax","Shipping Cost", "Item Total"
                },
                0
        );

        shoppingCartTable.setModel(tableModel);
        addItemButton.addActionListener(new ActionListener() {
            /**
             * this is a method to add an item to the cart
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                AddItemMenu addItemMenu = new AddItemMenu(ShoppingMainGUI.this);

                addItemMenu.setTitle("Add Item");
                addItemMenu.setSize(400, 400);
                addItemMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                addItemMenu.setLocationRelativeTo(null);
                addItemMenu.setVisible(true);
            }
        });
        calculateTotalButton.addActionListener(new ActionListener() {
            /**
             * this is a method to calculate the total of the cart
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                getCartTotal();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            /**
             * this is a method to delete an item in the cart
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = shoppingCartTable.getSelectedRow();
                deleteItem(index);
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
             JFileChooser fileChooser = new JFileChooser();
             fileChooser.setDialogTitle("Save Progress");
             fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
             var fileFilter = new FileNameExtensionFilter("json", "json");
             fileChooser.setFileFilter(fileFilter);
             int result = fileChooser.showSaveDialog(ShoppingMainGUI.this);
             if (result == JFileChooser.APPROVE_OPTION) {
                 String path = fileChooser.getSelectedFile().getAbsolutePath();
                 path = path.substring(0, path.lastIndexOf('.'));
                 path += ".json";
                 shoppingCart.saveCart(path);
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
             JFileChooser fileChooser = new JFileChooser();
             fileChooser.setDialogTitle("Load Progress");
             fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
             var fileFilter = new FileNameExtensionFilter("json", "json");
             fileChooser.setFileFilter(fileFilter);
             int result = fileChooser.showOpenDialog(ShoppingMainGUI.this);
             if (result == JFileChooser.APPROVE_OPTION) {
                 String path = fileChooser.getSelectedFile().getAbsolutePath();
                 shoppingCart.loadCart(path);
                 DefaultTableModel tableModel = (DefaultTableModel) shoppingCartTable.getModel();
                 tableModel.setRowCount(0);
                 for (ShoppingItem item : shoppingCart.getItems()) {
                     Object[] rowData = {item.getName(), item.getPrice(), item.getQuantity(), shoppingCart.getTaxRate(), item.getShippingCost(), item.getTotalPrice(false, true)};
                     tableModel.addRow(rowData);
                 }
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
                path = path.substring(0, path.lastIndexOf('.'));
                if (fileChooser.getFileFilter().getDescription().equals("txt")) {
                    shoppingCart.saveReceipt(path + ".txt", "txt");
                } else {
                    shoppingCart.saveReceipt(path + ".csv", "csv");
                }
            }
        }
    });


        setSize(800, 600);
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

        Object[] rowData = {item.getName(), item.getPrice(), item.getQuantity(), shoppingCart.getTaxRate(), item.getShippingCost(),item.getTotalPrice(false, true)};
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
    }

    /**
     * this gets the total of the cart
     */
    public void getCartTotal() {
        double total = shoppingCart.calculateTotal();
        cartTotalLabel.setText("Total: " + total);
    }

    /**
     * this is the main method
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ShoppingMainGUI mainForm = new ShoppingMainGUI(0.0);
            mainForm.setVisible(true);
        });
    }

}
