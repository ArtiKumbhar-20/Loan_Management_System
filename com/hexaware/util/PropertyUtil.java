package com.hexaware.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {
    private static final String PROPERTY_FILE = "src/database.properties"; 
    private static Properties properties;

    static {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(PROPERTY_FILE)) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getConnectionString() {
        String hostname = properties.getProperty("hostname");
        String port = properties.getProperty("port");
        String dbname = properties.getProperty("dbname");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        return "jdbc:mysql://" + hostname + ":" + port + "/" + dbname + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&user=" + username + "&password=" + password;
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
