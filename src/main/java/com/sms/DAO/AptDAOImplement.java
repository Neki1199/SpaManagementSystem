package com.sms.DAO;

import com.sms.Models.Appointment;
import com.sms.ConnectDB;
import com.sms.Models.Client;
import com.sms.Models.Employee;
import com.sms.Models.Service;
import javafx.scene.control.ListView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AptDAOImplement implements AppointmentDAO{

    ClientDAO clientDAO = new ClientDAOImplement();
    ServiceDAO serviceDAO = new ServiceDAOImplement();
    EmployeeDAO employeeDAO = new EmpDAOImplement();

    @Override
    public Appointment get(int id) throws SQLException {
        Connection con = ConnectDB.getConnection();
        Appointment appointment = null;

        String sql = "SELECT appointmentId, clientId, serviceId, staffId, date, hour, status FROM appointments WHERE appointmentId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int appointmentId = rs.getInt("appointmentId");
            int clientId = rs.getInt("clientId");
            int serviceId = rs.getInt("serviceId");
            int staffId = rs.getInt("staffId");
            String date = rs.getString("date");
            String hour = rs.getString("hour");
            String status = rs.getString("status");

            appointment = new Appointment(appointmentId, clientId, serviceId, staffId, date, hour, status);
        }
        return appointment;
    }

    @Override
    public List<Appointment> getAll() throws SQLException {
        Connection con = ConnectDB.getConnection();
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int appointmentId = rs.getInt("appointmentId");
            int clientId = rs.getInt("clientId");
            int serviceId = rs.getInt("serviceId");
            int staffId = rs.getInt("staffId");
            String date = rs.getString("date");
            String hour = rs.getString("hour");
            String status = rs.getString("status");

            appointments.add(new Appointment(appointmentId, clientId, serviceId, staffId, date, hour, status));
        }
        return appointments;
    }

    @Override
    public int insert(Appointment appointment) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "INSERT INTO appointments(clientId, serviceId, staffId, date, hour, status) VALUES(?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, appointment.getClientId());
        ps.setInt(2, appointment.getServiceId());
        ps.setInt(3, appointment.getStaffId());
        ps.setString(4, appointment.getDate());
        ps.setString(5, appointment.getHour());
        ps.setString(6, appointment.getStatus());
        int result = ps.executeUpdate();


        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            appointment.setAppointmentId(generatedKeys.getInt(1));
        }
        return result;
    }

    @Override
    public void update(Appointment appointment) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "UPDATE appointments SET clientId = ?, serviceId = ?, staffId = ?, date = ?, hour = ?, status = ? where appointmentId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, appointment.getClientId());
        ps.setInt(2, appointment.getServiceId());
        ps.setInt(3, appointment.getStaffId());
        ps.setString(4, appointment.getDate());
        ps.setString(5, appointment.getHour());
        ps.setString(6, appointment.getStatus());
        ps.setInt(7, appointment.getAppointmentId());
        ps.executeUpdate();
        ConnectDB.closePreparedStatement(ps);
        ConnectDB.closeConnection(con);
    }

    @Override
    public int save(Appointment appointment) {
        return 0;
    }

    @Override
    public int delete(int id) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "DELETE FROM appointments WHERE appointmentId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeUpdate();
    }

    @Override
    public int deleteFromClientID(int clientID) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "DELETE FROM appointments WHERE clientId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, clientID);
        return ps.executeUpdate();
    }

    @Override
    public void search(ListView<String> list, String toSearch) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT a.appointmentId, c.fullName, s.name AS serviceName, e.fullName AS staffName, a.date, a.hour " +
                "FROM appointments a " +
                "JOIN clients c ON a.clientId = c.clientId " +
                "JOIN services s ON a.serviceId = s.serviceId " +
                "JOIN employees e ON a.staffId = e.empId " +
                "WHERE c.fullName LIKE ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" + toSearch + "%");
        ResultSet rs = ps.executeQuery();

        list.getItems().clear(); // clear listview

        while (rs.next()) {
            int appointmentID = rs.getInt("appointmentId");
            String clientName = rs.getString("fullName");
            String serviceName = rs.getString("serviceName");
            String staffName = rs.getString("staffName");
            String date = rs.getString("date");
            String hour = rs.getString("hour");

            list.getItems().add(appointmentID + " - " + clientName + ": " + serviceName + " - " + staffName + " - " + date + " - " + hour);
        }
    }

    /***********/

    public List<Appointment> getFromDate(String date) throws SQLException{
        Connection con = ConnectDB.getConnection();
        List<Appointment> appointmentList = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE date = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, date);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int appointmentId = rs.getInt("appointmentId");
            int clientId = rs.getInt("clientId");
            int serviceId = rs.getInt("serviceId");
            int staffId = rs.getInt("staffId");
            String theDate = rs.getString("date");
            String hour = rs.getString("hour");
            String status = rs.getString("status");

            Appointment appointment = new Appointment(appointmentId, clientId, serviceId, staffId, theDate, hour, status);
            appointmentList.add(appointment);
        }
        return appointmentList;
    }

    @Override
    public List<Appointment> getFromClient(Integer clientId) throws SQLException {
        List<Appointment> clientAppointments = new ArrayList<>();
        List<Appointment> allAppointments = getAll();

        for(Appointment appointment : allAppointments) {
            if(appointment.getClientId() == clientId) {
                clientAppointments.add(appointment);
            }
        }
        return clientAppointments;
    }
}
