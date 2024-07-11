package com.sms.DAO;

import com.sms.BackEnd.Appointment;
import com.sms.BackEnd.Service;
import com.sms.DataModels.ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AptDAOImplement implements AppointmentDAO{

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
        return 0;
    }

    @Override
    public int update(Appointment appointment) throws SQLException {
        return 0;
    }

    @Override
    public int save(Appointment appointment) throws SQLException {
        return 0;
    }

    @Override
    public int delete(int id) throws SQLException {
        return 0;
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
}
