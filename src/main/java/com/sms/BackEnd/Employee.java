package com.sms.BackEnd;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Employee {
    private final int id;
    private final String name;
    private final String role;
    private final int phone;
    private final int admin;
    private final String password;
    private final String username;


    public Employee(int id, String name, String role, int phone, int admin, String password, String username) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.admin = admin;
        this.password = password;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public int getPhone() {
        return phone;
    }

    public int getAdmin() {
        return admin;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public static ObservableList<Employee> getEmployeesFromDatabase() {
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        String url = "jdbc:sqlite:src/main/resources/SQLiteSMS/sms.db";
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM employees")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("empId");
                String name = resultSet.getString("fullName");
                String role = resultSet.getString("role");
                int phone = resultSet.getInt("phoneNo");
                int admin = resultSet.getInt("isAdmin");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");

                employees.add(new Employee(id, name, role, phone, admin, username, password));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employees;
    }

    public static Employee getEmployeeFromID(int theId) {
        Employee employee = null;
        String url = "jdbc:sqlite:src/main/resources/SQLiteSMS/sms.db";
        String query = "SELECT * FROM employees WHERE empId = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(query)){

            statement.setInt(1, theId);  // Set the value for the placeholder '?'
            ResultSet resultSet = statement.executeQuery();

            if (resultSet != null) {
                int id = resultSet.getInt("empId");
                String name = resultSet.getString("fullName");
                String role = resultSet.getString("role");
                int phone = resultSet.getInt("phoneNo");
                int admin = resultSet.getInt("isAdmin");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");

                employee = new Employee(id, name, role, phone, admin, username, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employee;
    }

}