package com.garageims.config;

public final class AppConfig {
    private AppConfig() {}

    // Update these values to match your PostgreSQL setup before running.
    public static final String DB_URL = "jdbc:mysql://localhost:3306/garageims?useSSL=false&allowPublicKeyRetrieval=true";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "om123";

    // Inventory items at or below this quantity are shown as low stock.
    public static final int LOW_STOCK_THRESHOLD = 5;
}
