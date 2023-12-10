package ui;

import data.ShoppingCart;
import data.ShoppingItem;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

public class ShoppingMainGUI extends JFrame{

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
    private JLabel cartTotalLabel;
    private JButton loadButton;
    private JButton editButton;

    private double taxRate;


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
            0){
        /**
         * this is a method to make the table editable
         * @param row             the row whose value is to be queried
         * @param column          the column whose value is to be queried
         * @return
         */
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column < 5;
        }};

        shoppingCartTable.setModel(tableModel);

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
                                break;
                            case 2:
                                item.setQuantity(Integer.parseInt((String) model.getValueAt(row, column)));
                                break;
                            case 3:
                                item.setTaxRate(Double.parseDouble((String) model.getValueAt(row, column)));
                                num = Double.parseDouble((String) model.getValueAt(row, column));
                                model.setValueAt(String.format("%.2f", num), row, column);
                                break;
                            case 4:
                                item.setShippingCost(Double.parseDouble((String) model.getValueAt(row, column)));
                                num = Double.parseDouble((String) model.getValueAt(row, column));
                                model.setValueAt(String.format("%.2f", num), row, column);
                                break;
                        }

                        SwingUtilities.invokeLater(() -> {
                            String formattedTotal = String.format(Locale.US, "%.2f", item.getTotalPrice(false, true));
                            model.setValueAt(formattedTotal, row, 5);
                            getCartTotal();
                        });

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid number format", "Error", JOptionPane.ERROR_MESSAGE);
                        if(column == 1) model.setValueAt(String.format("%.2f", item.getPrice()), row, column);
                        if(column == 2) model.setValueAt(item.getQuantity(), row, column);
                        if(column == 4) model.setValueAt(String.format("%.2f", item.getShippingCost()), row, column);
                    } finally {
                        isUpdating = false;
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
        editButton.addActionListener(new ActionListener() {
            /**
             * this is a method to edit an item in the cart
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = shoppingCartTable.getSelectedRow();
                ShoppingItem item = shoppingCart.getItems().get(index);
                EditItemMenu editItemMenu = new EditItemMenu(ShoppingMainGUI.this, item, index);
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
                     // Format the numeric values to have two decimal places
                     Object[] rowData = {
                             item.getName(),
                             String.format("%.2f", item.getPrice()),
                             item.getQuantity(),
                             String.format("%.2f", shoppingCart.getTaxRate()),
                             String.format("%.2f", item.getShippingCost()),
                             String.format("%.2f", item.getTotalPrice(false, true))
                     };
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

        // Format the numeric values to have two decimal places
        Object[] rowData = {
                item.getName(),
                String.format("%.2f", item.getPrice()),
                item.getQuantity(),
                String.format("%.2f", shoppingCart.getTaxRate()),
                String.format("%.2f", item.getShippingCost()),
                String.format("%.2f", item.getTotalPrice(false, true))
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
