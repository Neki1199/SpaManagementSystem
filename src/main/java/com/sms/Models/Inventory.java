package com.sms.Models;

public class Inventory {
    private Integer id;
    private String name;
    private int quantity;
    private double cost;
    private String description;

    public Inventory(Integer id, String name, int quantity, double cost, String description) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.cost = cost;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", qty=" + quantity +
                ", cost=" + cost +
                ", description='" + description + '\'' +
                '}';
    }
}



