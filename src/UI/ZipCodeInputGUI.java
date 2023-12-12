package ui;

import api.TaxRateClient;
import data.TaxRateResponse;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
                //this if statement checks if the ZIP Code is valid, and if it is, it updates the tax rate
                if (isValidUSZipCode(zipCode)) {
                    double taxRate = fetchTaxRate(zipCode);
                    mainForm.updateTaxRate(taxRate);
                    mainForm.taxRateInput.setText(String.valueOf(taxRate));
                    dispose();
                } else {
                    //this displays an error message if the ZIP Code is invalid
                    JOptionPane.showMessageDialog(null, "Invalid US ZIP Code. Please enter a valid ZIP Code.");
                }
            }
        });
        //this adds the main panel to the form
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
        //this returns true if the ZIP Code matches the regex
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
        //this returns the total rate
        return taxRateResponse.getTotalRate();
    }
}
