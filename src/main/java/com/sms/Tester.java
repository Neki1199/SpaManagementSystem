package com.sms;

import com.sms.Models.*;
import com.sms.DAO.*;

import java.sql.SQLException;

public class Tester {
    public static void main(String[] args) throws SQLException {
        ServiceDAO appointmentDAO = new ServiceDAOImplement();

        Service appointment = new Service(null, "Reflexology", "Foot massage", 25.00, "Massage", 45, "Beautician");
        System.out.println(appointmentDAO.insert(appointment));
    }
}
