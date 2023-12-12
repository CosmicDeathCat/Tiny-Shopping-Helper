package core;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/**
 * This class contains methods for reading from the config.properties file
 */
public class Configuration {
    //this creates a new Properties object
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = Configuration.class.getClassLoader().getResourceAsStream("config.properties")) {
            // load properties from file
            if (input == null) {
                throw new IllegalStateException("config.properties file not found in the classpath");
            }
            properties.load(input);
        } catch (IOException ex) {
            //this would be a runtime exception because the program cannot run without the config.properties file
            ex.printStackTrace();
            throw new RuntimeException("Error reading from config.properties", ex);
        }
    }
    //this gets the API URL from the config.properties file
    public static String getApiUrl() {
        return properties.getProperty("API_URL");
    }
    //this gets the API key from the config.properties file
    public static String getApiKey() {
        return properties.getProperty("API_KEY");
    }
    //this gets the API host from the config.properties file
    public static String getApiHost() {
        return properties.getProperty("API_HOST");
    }
}

