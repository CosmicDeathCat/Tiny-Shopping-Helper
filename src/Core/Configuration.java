package core;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class Configuration {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = Configuration.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException("config.properties file not found in the classpath");
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error reading from config.properties", ex);
        }
    }

    public static String getApiUrl() {
        return properties.getProperty("API_URL");
    }

    public static String getApiKey() {
        return properties.getProperty("API_KEY");
    }

    public static String getApiHost() {
        return properties.getProperty("API_HOST");
    }
}

