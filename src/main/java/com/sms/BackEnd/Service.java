package com.sms.BackEnd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Service {
    private final int id;
    private final String name;
    private final String description;
    private final double price;
    private final String serviceType;
    private final int duration;
    private final String staffType;

    public Service(int id, String name, String description, double price, String serviceType, int duration, String staffType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.serviceType = serviceType;
        this.duration = duration;
        this.staffType = staffType;
    }

    public static List<Service> getServicesFromDatabase() {
        List<Service> services = new ArrayList<>();
        String url = "jdbc:sqlite:src/main/resources/SQLiteSMS/sms.db";
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM services")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("serviceId");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                String serviceType = resultSet.getString("serviceType");
                int duration = resultSet.getInt("duration");
                String staffType = resultSet.getString("staffType");

                services.add(new Service(id, name, description, price, serviceType, duration, staffType));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return services;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getServiceType() {
        return serviceType;
    }

    public int getDuration() {
        return duration;
    }

    public String getStaffType() {
        return staffType;
    }
}


