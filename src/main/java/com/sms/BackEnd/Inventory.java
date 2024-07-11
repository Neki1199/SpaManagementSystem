package com.sms.BackEnd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private final int id;
    private final String name;
    private final int qty;
    private final double cost;
    private final String description;

    public Inventory(int id, String name, int qty, double cost, String description) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.cost = cost;
        this.description = description;
    }


    public static List<Inventory> getInventoryFromDatabase() {
        List<Inventory> inventory = new ArrayList<>();
        String url = "jdbc:sqlite:src/main/resources/SQLiteSMS/sms.db";
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM inventory")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("productId");
                String name = resultSet.getString("productName");
                int qty = resultSet.getInt("quantity");
                double cost = resultSet.getDouble("cost");
                String description = resultSet.getString("description");
                inventory.add(new Inventory(id, name, qty, cost, description));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inventory;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQty() {
        return qty;
    }

    public double getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }
}

