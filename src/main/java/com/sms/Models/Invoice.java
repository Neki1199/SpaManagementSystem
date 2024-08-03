package com.sms.Models;

public class Invoice {
    private Integer invoiceId;
    private Integer clientId;
    private String orderDate;
    private double totalCost;
    private String paymentMethod;

    public Invoice(Integer invoiceId, Integer clientId, String orderDate, double totalCost, String paymentMethod) {
        this.invoiceId = invoiceId;
        this.clientId = clientId;
        this.orderDate = orderDate;
        this.totalCost = totalCost;
        this.paymentMethod = paymentMethod;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId=" + invoiceId +
                ", clientId=" + clientId +
                ", orderDate=" + orderDate +
                ", totalCost=" + totalCost +
                ", paymentMethod=" + paymentMethod +
                '}';
    }
}


