package com.sms;

import com.sms.BackEnd.Employee;
import com.sms.DAO.EmpDAOImplement;

import java.sql.SQLException;

public class Tester {
    public static void main(String[] args) throws SQLException {
        EmpDAOImplement empDAOImplement = new EmpDAOImplement();


        Employee employee = empDAOImplement.get(1);
        System.out.println(employee);



    }
}
