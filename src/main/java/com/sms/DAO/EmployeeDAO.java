package com.sms.DAO;

import com.sms.Models.Employee;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeDAO extends DAO<Employee>{

    public List<Employee> getByType(String type) throws SQLException;
    public Employee getEmployeeByName(String name) throws SQLException;
    public boolean usersExist() throws SQLException;
}
