package com.sms.DAO;

import com.sms.Models.Appointment;

import java.sql.SQLException;
import java.util.List;

// Add more methods for Appointment (implement in AptDAOImplement)

public interface AppointmentDAO extends DAO<Appointment> {
    int deleteFromClientID(int clientID) throws SQLException;

    List<Appointment> getFromDate(String formattedDate) throws SQLException;
    List<Appointment> getFromClient(Integer clientId) throws SQLException;
}
