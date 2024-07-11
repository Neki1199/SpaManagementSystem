package com.sms.BackEnd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Service {
    private int id;
    private String name;
    private String description;
    private double price;
    private String serviceType;
    private int duration;
    private String staffType;

    public Service(int id, String name, String description, double price, String serviceType, int duration, String staffType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.serviceType = serviceType;
        this.duration = duration;
        this.staffType = staffType;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getStaffType() {
        return staffType;
    }

    public void setStaffType(String staffType) {
        this.staffType = staffType;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", serviceType='" + serviceType + '\'' +
                ", duration=" + duration +
                ", staffType='" + staffType + '\'' +
                '}';
    }
}


