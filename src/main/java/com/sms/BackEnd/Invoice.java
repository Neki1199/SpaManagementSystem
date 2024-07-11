package com.sms.BackEnd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Invoice {
    private int invoiceId;
    private int clientId;
    private int services;
    private int product;
    private int amount;

    public Invoice(int invoiceId, int clientId, int services, int product, int amount) {
        this.invoiceId = invoiceId;
        this.clientId = clientId;
        this.services = services;
        this.product = product;
        this.amount = amount;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getServices() {
        return services;
    }

    public void setServices(int services) {
        this.services = services;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId=" + invoiceId +
                ", clientId=" + clientId +
                ", services=" + services +
                ", product=" + product +
                ", amount=" + amount +
                '}';
    }
}

