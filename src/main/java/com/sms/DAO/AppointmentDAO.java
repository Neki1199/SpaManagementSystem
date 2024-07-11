package com.sms.DAO;

import com.sms.BackEnd.Appointment;

import java.sql.SQLException;
import java.util.List;

// Add more methods for Appointment (implement in AptDAOImplement)

public interface AppointmentDAO extends DAO<Appointment> {
    List<Appointment> getFromDate(String formattedDate) throws SQLException;
}
