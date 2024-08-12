package com.sms.Models;

public class InvoiceItem {
    private Integer invoiceId;
    private String itemName;
    private String itemType;
    private Integer itemId;
    private int quantity;
    private double itemPrice;

    public InvoiceItem(Integer invoiceId, String itemName, String itemType, Integer itemId, int quantity, double itemPrice) {
        this.invoiceId = invoiceId;
        this.itemName = itemName;
        this.itemType = itemType;
        this.itemId = itemId;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String toString() {
        return "InvoiceItem{" +
                "invoiceId=" + invoiceId +
                ", itemName='" + itemName + '\'' +
                ", itemType='" + itemType + '\'' +
                ", itemId=" + itemId +
                ", quantity=" + quantity +
                ", itemPrice=" + itemPrice +
                '}';
    }
}
