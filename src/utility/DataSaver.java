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
import java.util.List;

public class DataSaver {

    public static <T> void saveWithDialog(T data, JFrame frame) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Data");

        FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT & CSV Files", "txt", "csv");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();
            if (!filePath.endsWith(".txt") && !filePath.endsWith(".csv")) {
                filePath += ".csv"; // Default to CSV
            }
            saveToFile(data, filePath);
        }
    }

    public static <T> void saveToFile(T data, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            boolean isCsv = filePath.endsWith(".csv");

            if (data instanceof ShoppingCart) {
                saveShoppingCart((ShoppingCart) data, writer, isCsv);
            } else {
                if (isCsv && data != null) {
                    String header = createCsvHeader(data.getClass());
                    writer.write(header);
                    writer.newLine();
                }
                String formattedData = isCsv ? formatForCsv(data) : formatForReceipt(data);
                writer.write(formattedData);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveShoppingCart(ShoppingCart cart, BufferedWriter writer, boolean isCsv) throws IOException {
        if (isCsv) {
            String header = createCsvHeader(ShoppingItem.class);
            writer.write(header);
            writer.newLine();
        }

        for (ShoppingItem item : cart.getItems()) {
            String formattedData = isCsv ? formatForCsv(item) : formatForReceipt(item);
            writer.write(formattedData);
            writer.newLine();
        }

        if (isCsv) {
            double shippingCost = cart.getItems().stream().mapToDouble(ShoppingItem::getShippingCost).sum();
            writer.write("Subtotal,," + cart.getSubTotal() + "\n");
            writer.write("Shipping Cost,," + shippingCost + "\n");
            writer.write("Total,," + cart.getTotal() + "\n");
        } else {
            writer.write("\nSubtotal: " + cart.getSubTotal() + "\n");
            double shippingCost = cart.getItems().stream().mapToDouble(ShoppingItem::getShippingCost).sum();
            writer.write("Shipping Cost: " + shippingCost + "\n");
            writer.write("Grand Total: " + cart.getTotal() + "\n");
        }
    }

    public static <T> String createCsvHeader(Class<T> clazz) {
        StringBuilder sb = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(FieldLabel.class)) {
                sb.append(field.getAnnotation(FieldLabel.class).value()).append(",");
            }
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1); // Remove trailing comma
        }

        return sb.toString();
    }

    public static <T> String formatForReceipt(T item) {
        StringBuilder sb = new StringBuilder();
        Field[] fields = item.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(FieldLabel.class)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(item);
                    sb.append(field.getAnnotation(FieldLabel.class).value())
                            .append(": ")
                            .append(value)
                            .append("\n");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static <T> String formatForCsv(T item) {
        StringBuilder sb = new StringBuilder();
        Field[] fields = item.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(FieldLabel.class)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(item);
                    sb.append(value).append(",");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1); // Remove trailing comma
        }

        return sb.toString();
    }
}
