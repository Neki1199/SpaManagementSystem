package com.sms.BackEnd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Invoice {
    private final int invoiceId;
    private final int clientId;
    private final int services;
    private final int product;
    private final int amount;

    public Invoice(int id, int clientId, int services, int product, int amount) {
        this.invoiceId = id;
        this.clientId = clientId;
        this.services = services;
        this.product = product;
        this.amount = amount;
    }

    public static List<Invoice> getInvoicesFromDatabase() {
        List<Invoice> invoices = new ArrayList<>();
        String url = "jdbc:sqlite:src/main/resources/SQLiteSMS/sms.db";
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM invoices")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("invId");
                int clientId = resultSet.getInt("clientId");
                int services = resultSet.getInt("services");
                int product = resultSet.getInt("product");
                int amount = resultSet.getInt("amount");
                invoices.add(new Invoice(id, clientId, services, product, amount));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoices;
    }

    public int getClientId() {
        return clientId;
    }

    public int getServices() {
        return services;
    }

    public int getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

    public int getInvoiceId() {
        return invoiceId;
    }
}

