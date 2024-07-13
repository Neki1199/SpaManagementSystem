package com.sms.DAO;

import com.sms.BackEnd.Employee;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeDAO extends DAO<Employee>{

    public List<Employee> getByType(String type) throws SQLException;
    public Employee getEmployeeByName(String name) throws SQLException;
}
