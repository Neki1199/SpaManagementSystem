package com.sms.BackEnd;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Appointment {
    private final int aptId;
    private final int clientId;
    private final int serviceId;
    private final String date;
    private final String status;
    private final int staffId;
    private final String hour;

    public Appointment(int aptId, int clientId, int serviceId, String date, String status, int staffId, String hour) {
        this.aptId = aptId;
        this.clientId = clientId;
        this.serviceId = serviceId;
        this.date = date;
        this.status = status;
        this.staffId = staffId;
        this.hour = hour;
    }

    public static ObservableList<Appointment> getAppointmentsFromDatabase() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String url = "jdbc:sqlite:src/main/resources/SQLiteSMS/sms.db";
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM appointments")) {

            while (resultSet.next()) {
                int aptId = resultSet.getInt("appointmentId");
                int clientId = resultSet.getInt("clientId");
                int serviceId = resultSet.getInt("serviceId");
                String date = resultSet.getString("date");
                String status = resultSet.getString("status");
                int staffId = resultSet.getInt("staffId");
                String hour = resultSet.getString("hour");

                Appointment appointment = new Appointment(aptId, clientId, serviceId, date, status, staffId, hour);
                appointments.add(appointment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public static List<Appointment> getAppointmentForDate(String date2) {
        List<Appointment> appointments = new ArrayList<>();

        String query = "SELECT * FROM appointments WHERE date = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/SQLiteSMS/sms.db");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the date parameter in the query
            preparedStatement.setString(1, date2);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int aptId = resultSet.getInt("appointmentId");
                    int clientId = resultSet.getInt("clientId");
                    int serviceId = resultSet.getInt("serviceId");
                    String date = resultSet.getString("date");
                    String status = resultSet.getString("status");
                    int staffId = resultSet.getInt("staffId");
                    String hour = resultSet.getString("hour");

                    Appointment appointment = new Appointment(aptId, clientId, serviceId, date, status, staffId, hour);
                    appointments.add(appointment);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public static Service getServiceFromID(int theId) {
        Service service = null;
        String url = "jdbc:sqlite:src/main/resources/SQLiteSMS/sms.db";
        String query = "SELECT * FROM services WHERE serviceId = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(query)){

            statement.setInt(1, theId);  // Set the value for the placeholder '?'
            ResultSet resultSet = statement.executeQuery();

            if (resultSet != null) {
                int serviceId = resultSet.getInt("serviceId");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                String serviceType = resultSet.getString("serviceType");
                int duration = resultSet.getInt("duration");
                String staffType = resultSet.getString("staffType");

                service = new Service(serviceId, name, description, price, serviceType, duration, staffType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return service;
    }

    public String toString(){
        String info = "";
        info += "Client name: " + Client.getClientFromID(clientId).getName() + " - Date: " + date + " " + hour + "\nService: " + Appointment.getServiceFromID(serviceId).getName()
                + "\nProfessional name: " + Employee.getEmployeeFromID(staffId).getName() + "\nStatus: " + status + "\n";
        return info;
    }

    public int getAptId() {
        return aptId;
    }

    public int getClientId() {
        return clientId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() { return status; }

    public int getStaffId() {
        return staffId;
    }

    public String getHour() { return hour; }
}
