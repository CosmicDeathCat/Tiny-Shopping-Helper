package utility;

import annotations.FieldLabel;
import data.ShoppingCart;
import data.ShoppingItem;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * This class contains methods for saving data
 */
public class DataSaver {
    /**
     * This method saves data to a file
     * @param data
     * @param frame
     * @param <T>
     */
    public static <T> void saveWithDialog(T data, JFrame frame) {
        // Create file chooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Data");

        FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT & CSV Files", "txt", "csv");
        fileChooser.setFileFilter(filter);
        // Show dialog
        int userSelection = fileChooser.showSaveDialog(frame);
        // If user selects a file
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();
            // If user does not enter a file extension
            if (!filePath.endsWith(".txt") && !filePath.endsWith(".csv")) {
                filePath += ".csv"; // Default to CSV
            }
            // Save to file
            saveToFile(data, filePath);
        }
    }

    /**
     * This method saves data to a file
     * @param data
     * @param filePath
     * @param <T>
     */
    public static <T> void saveToFile(T data, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Check if file is CSV
            boolean isCsv = filePath.endsWith(".csv");
            // If data is a list
            if (data instanceof ShoppingCart) {
                saveShoppingCart((ShoppingCart) data, writer, isCsv);
            } else {
                // If data is not a list
                if (isCsv && data != null) {
                    // Write header
                    String header = createCsvHeader(data.getClass());
                    writer.write(header);
                    writer.newLine();
                }
                // Write data
                String formattedData = isCsv ? formatForCsv(data) : formatForReceipt(data);
                writer.write(formattedData);
                writer.newLine();
            }
        } catch (IOException e) {
            // this error message is displayed if there is an error saving the file
            e.printStackTrace();
        }
    }

    /**
     * This method saves a shopping cart to a file
     * @param cart
     * @param writer
     * @param isCsv
     * @throws IOException
     */
    public static void saveShoppingCart(ShoppingCart cart, BufferedWriter writer, boolean isCsv) throws IOException {
        // this if statement checks if the file is a CSV file, and if it is, it writes the header
        if (isCsv) {
            String header = createCsvHeader(ShoppingItem.class);
            writer.write(header);
            writer.newLine();
        }
        // this for loop iterates through the items in the cart and writes them to the file
        for (ShoppingItem item : cart.getItems()) {
            String formattedData = isCsv ? formatForCsv(item) : formatForReceipt(item);
            writer.write(formattedData);
            writer.newLine();
        }
        // this if statement checks if the file is a CSV file, and if it is, it writes the subtotal, shipping cost, and total
        if (isCsv) {
            double shippingCost = cart.getItems().stream().mapToDouble(ShoppingItem::getShippingCost).sum();
            writer.write("Subtotal,," + cart.getSubTotal() + "\n");
            writer.write("Shipping Cost,," + shippingCost + "\n");
            writer.write("Total,," + cart.getTotal() + "\n");
        } else {
            // this else statement writes the subtotal, shipping cost, and total to the file
            writer.write("\nSubtotal: " + cart.getSubTotal() + "\n");
            double shippingCost = cart.getItems().stream().mapToDouble(ShoppingItem::getShippingCost).sum();
            writer.write("Shipping Cost: " + shippingCost + "\n");
            writer.write("Grand Total: " + cart.getTotal() + "\n");
        }
    }

    /**
     * This method creates a CSV header from a class
     * @param clazz
     * @return
     * @param <T>
     */
    public static <T> String createCsvHeader(Class<T> clazz) {
        //this creates a string builder
        StringBuilder sb = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();
        //this for loop iterates through the fields and adds them to the string builder
        for (Field field : fields) {
            //this if statement checks if the field has a field label annotation, and if it does, it adds it to the string builder
            if (field.isAnnotationPresent(FieldLabel.class)) {
                sb.append(field.getAnnotation(FieldLabel.class).value()).append(",");
            }
        }
        //this if statement checks if the string builder has a length greater than 0, and if it does, it removes the last comma
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1); // Remove trailing comma
        }
        //this returns the string builder as a string
        return sb.toString();
    }

    /**
     * This method formats an object for a receipt
     * @param item
     * @return
     * @param <T>
     */
    public static <T> String formatForReceipt(T item) {
        //this creates a string builder
        StringBuilder sb = new StringBuilder();
        Field[] fields = item.getClass().getDeclaredFields();
        //this for loop iterates through the fields and adds them to the string builder
        for (Field field : fields) {
            //this if statement checks if the field has a field label annotation, and if it does, it adds it to the string builder
            if (field.isAnnotationPresent(FieldLabel.class)) {
                field.setAccessible(true);
                try {
                    //this gets the value of the field
                    Object value = field.get(item);
                    sb.append(field.getAnnotation(FieldLabel.class).value())
                            .append(": ")
                            .append(value)
                            .append("\n");
                } catch (IllegalAccessException e) {
                    // this error message is displayed if there is an error formatting the object
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * This method formats an object for a CSV file
     * @param item
     * @return
     * @param <T>
     */
    public static <T> String formatForCsv(T item) {
        //this creates a string builder
        StringBuilder sb = new StringBuilder();
        Field[] fields = item.getClass().getDeclaredFields();
        //this for loop iterates through the fields and adds them to the string builder
        for (Field field : fields) {
            //this if statement checks if the field has a field label annotation, and if it does, it adds it to the string builder
            if (field.isAnnotationPresent(FieldLabel.class)) {
                field.setAccessible(true);
                try {
                    //this gets the value of the field
                    Object value = field.get(item);
                    sb.append(value).append(",");
                } catch (IllegalAccessException e) {
                    // this error message is displayed if there is an error formatting the object
                    e.printStackTrace();
                }
            }
        }
        //this if statement checks if the string builder has a length greater than 0, and if it does, it removes the last comma
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1); // Remove trailing comma
        }
        //this returns the string builder as a string
        return sb.toString();
    }
}
