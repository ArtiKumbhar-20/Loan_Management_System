package com.hexaware.util;

public class TestPropertyUtil {
    public static void main(String[] args) {
        String connectionString = PropertyUtil.getConnectionString();

        System.out.println("Database Properties:");
        System.out.println("Hostname: " + PropertyUtil.getProperty("hostname"));
        System.out.println("Port: " + PropertyUtil.getProperty("port"));
        System.out.println("Database Name: " + PropertyUtil.getProperty("dbname"));
        System.out.println("Username: " + PropertyUtil.getProperty("username"));
        System.out.println("Password: " + PropertyUtil.getProperty("password"));

        System.out.println("\nConnection String: " + connectionString);
    }
}
