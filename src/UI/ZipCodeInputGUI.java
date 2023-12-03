package ui;

import api.TaxRateClient;
import data.TaxRateResponse;
import ui.ShoppingMainGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Pattern;

public class ZipCodeInputGUI extends JFrame {
    private JTextField userInputZipCode;
    private JButton enterZipCodeButton;
    private JLabel inputQuestionLabel;

    public ZipCodeInputGUI() {
        setTitle("ZIP Code Input");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        panel.add(inputQuestionLabel, BorderLayout.NORTH);
        panel.add(userInputZipCode, BorderLayout.CENTER);
        panel.add(enterZipCodeButton, BorderLayout.SOUTH);

        enterZipCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String zipCode = userInputZipCode.getText();

                if (isValidUSZipCode(zipCode)) {
                    double taxRate = fetchTaxRate(zipCode);

                    ShoppingMainGUI mainForm = new ShoppingMainGUI(taxRate);
                    mainForm.setVisible(true);

                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid US ZIP Code. Please enter a valid ZIP Code.");
                }
            }
        });

        add(panel);
    }

    private boolean isValidUSZipCode(String zipCode) {
        String regex = "^\\d{5}$|^\\d{5}-\\d{4}$";
        return Pattern.matches(regex, zipCode);
    }

    private double fetchTaxRate(String zipCode) {
        TaxRateClient taxRateClient = new TaxRateClient();
        TaxRateResponse taxRateResponse = taxRateClient.getTaxRateFromZipCityState(zipCode, null, null);
        return taxRateResponse.getTotalRate();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ZipCodeInputGUI zipCodeInputGUI = new ZipCodeInputGUI();
            zipCodeInputGUI.setVisible(true);
        });
    }
}
