package com.sms.DAO;

import com.sms.BackEnd.Appointment;
import com.sms.BackEnd.Service;
import com.sms.DataModels.ConnectDB;

import java.sql.*;
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
        ConnectDB.closePreparedStatement(ps);
        ConnectDB.closeConnection(con);

        return result;
    }

    @Override
    public int update(Appointment appointment) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "UPDATE appointments set appointmentId = ?, clientId = ?, serviceId = ?, staffId = ?, date = ?, hour = ?, status = ? where appointmentId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, appointment.getAppointmentId());
        ps.setInt(2, appointment.getClientId());
        ps.setInt(3, appointment.getServiceId());
        ps.setInt(4, appointment.getStaffId());
        ps.setString(5, appointment.getDate());
        ps.setString(6, appointment.getHour());
        ps.setString(7, appointment.getStatus());
        ps.setInt(8, appointment.getAppointmentId());

        int result = ps.executeUpdate();
        ConnectDB.closePreparedStatement(ps);
        ConnectDB.closeConnection(con);

        return result;
    }

    @Override
    public int save(Appointment appointment) throws SQLException {
        return 0;
    }

    @Override
    public int delete(int id) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "DELETE FROM appointments WHERE appointmentId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);

        int result = ps.executeUpdate();
        ConnectDB.closePreparedStatement(ps);
        ConnectDB.closeConnection(con);

        return result;
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
