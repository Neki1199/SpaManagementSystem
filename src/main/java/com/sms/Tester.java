package com.sms;

import com.sms.BackEnd.Employee;
import com.sms.BackEnd.Service;
import com.sms.DAO.EmpDAOImplement;
import com.sms.DAO.ServiceDAO;
import com.sms.DAO.ServiceDAOImplement;

import java.sql.SQLException;

public class Tester {
    public static void main(String[] args) throws SQLException {
        ServiceDAO serviceDAO = new ServiceDAOImplement();
        Service theService = serviceDAO.getServiceByName("Facial Hygiene");
        String serviceType = theService.getServiceType();
        System.out.println(serviceType);



    }
}
