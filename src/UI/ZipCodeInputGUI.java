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

/**
 * This class contains methods for the ZIP Code Input GUI
 */
public class ZipCodeInputGUI extends JDialog {
    private JTextField userInputZipCode;
    private JButton enterZipCodeButton;
    private JLabel inputQuestionLabel;
    private JPanel mainPanel;
    private JPanel mainFormPanel;

    /**
     * This is a constructor for the ZIP Code Input GUI
     */
    public ZipCodeInputGUI(ShoppingMainGUI mainForm) {
        super(mainForm, true);
        enterZipCodeButton.addActionListener(new ActionListener() {
            /**
             * This method is called when the user clicks the enter zip code button
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String zipCode = userInputZipCode.getText();

                if (isValidUSZipCode(zipCode)) {
                    double taxRate = fetchTaxRate(zipCode);
                    mainForm.updateTaxRate(taxRate);
                    mainForm.taxRateInput.setText(String.valueOf(taxRate));
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid US ZIP Code. Please enter a valid ZIP Code.");
                }
            }
        });
        add(mainPanel);
        setTitle("ZIP Code Input");
        setSize(200, 125);
        setLocationRelativeTo(null);
    }

    /**
     * This method checks if the ZIP Code is valid
     * @param zipCode
     * @return
     */
    private boolean isValidUSZipCode(String zipCode) {
        String regex = "^\\d{5}$|^\\d{5}-\\d{4}$";
        return Pattern.matches(regex, zipCode);
    }

    /**
     * This method fetches the tax rate from the ZIP Code
     * @param zipCode
     * @return
     */
    private double fetchTaxRate(String zipCode) {
        TaxRateClient taxRateClient = new TaxRateClient();
        TaxRateResponse taxRateResponse = taxRateClient.getTaxRateFromZipCityState(zipCode, null, null);
        return taxRateResponse.getTotalRate();
    }
}
