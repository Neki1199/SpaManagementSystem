package com.sms.BackEnd;

public class Inventory {
    private int id;
    private String name;
    private int qty;
    private double cost;
    private String description;

    public Inventory(int id, String name, int qty, double cost, String description) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.cost = cost;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
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
                ", qty=" + qty +
                ", cost=" + cost +
                ", description='" + description + '\'' +
                '}';
    }
}



