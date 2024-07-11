package com.sms.BackEnd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private final int id;
    private final String name;
    private final int phone;
    private final String notes;
    private final String email;

    public Client(int id, String name, int phone, String notes, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.notes = notes;
        this.email = email;
    }

    public static List<Client> getClientsFromDatabase() {
        List<Client> clients = new ArrayList<>();
        String url = "jdbc:sqlite:src/main/resources/SQLiteSMS/sms.db";
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM clients")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("clientId");
                String name = resultSet.getString("fullName");
                int phone = resultSet.getInt("phoneNo");
                String notes = resultSet.getString("notes");
                String email = resultSet.getString("email");
                Client client = new Client(id, name, phone, notes, email);
                clients.add(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clients;
    }

    public static Client getClientFromID(int theId) {
        Client client = null;
        String url = "jdbc:sqlite:src/main/resources/SQLiteSMS/sms.db";
        String query = "SELECT * FROM clients WHERE clientId = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(query)){

            statement.setInt(1, theId);  // Set the value for the placeholder '?'
            ResultSet resultSet = statement.executeQuery();

                if (resultSet != null) {
                    int id = resultSet.getInt("clientId");
                    String name = resultSet.getString("fullName");
                    int phone = resultSet.getInt("phoneNo");
                    String notes = resultSet.getString("notes");
                    String email = resultSet.getString("email");
                    client = new Client(id, name, phone, notes, email);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return client;
        }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPhone() {
        return phone;
    }

    public String getNotes() {
        return notes;
    }

    public String getEmail() {
        return email;
    }
}
