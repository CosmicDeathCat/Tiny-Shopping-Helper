package ui;

import data.ShoppingCart;
import data.ShoppingItem;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    private double taxRate;

    private ShoppingCart shoppingCart = new ShoppingCart();


    public ShoppingMainGUI(Double taxRate) {
        setTitle("Tiny Shopping Helper");
        this.taxRate = taxRate;
        shoppingCart.setTaxRate(taxRate);

        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new BorderLayout());

        // Assuming TSHPanel is a panel containing your form components, you need to add it to the content pane
        contentPane.add(TSHPanel, BorderLayout.CENTER);

        DefaultTableModel tableModel = new DefaultTableModel(
                new String[]{
                        "Item Name", "Item Price", "Item Quantity", "Item Tax","Shipping Cost", "Sale Discount", "Item Total"
                },
                0
        );

        shoppingCartTable.setModel(tableModel);
        addItemButton.addActionListener(new ActionListener() {
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

        // Set the size and default close operation
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void addItemToCart(ShoppingItem item) {

        shoppingCart.addItem(item);
        DefaultTableModel tableModel = (DefaultTableModel) shoppingCartTable.getModel();

        Object[] rowData = {item.getName(), item.getPrice(), item.getQuantity(), shoppingCart.getTaxRate(), item.getShippingCost(),0,item.getTotalPrice(false, true)};
        tableModel.addRow(rowData);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ShoppingMainGUI mainForm = new ShoppingMainGUI(0.0); // Replace 0.0 with the actual tax rate
            mainForm.setVisible(true);
        });
    }

}
