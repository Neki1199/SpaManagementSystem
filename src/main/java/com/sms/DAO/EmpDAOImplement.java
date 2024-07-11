package com.sms.DAO;

import com.sms.BackEnd.Employee;
import com.sms.DataModels.ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpDAOImplement implements EmployeeDAO {

    @Override
    public Employee get(int id) throws SQLException {
        Connection con = ConnectDB.getConnection();
        Employee employee = null;

        String sql = "SELECT empID, fullName, role, phoneNo, isAdmin, username, password FROM employees WHERE empID = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int eID = rs.getInt("empID");
            String fullName = rs.getString("fullName");
            String role = rs.getString("role");
            int phoneNo = rs.getInt("phoneNo");
            int isAdmin = rs.getInt("isAdmin");
            String username = rs.getString("username");
            String password = rs.getString("password");

            employee = new Employee(eID, fullName, role, phoneNo, isAdmin, username, password);
        }
        return employee;
    }

    @Override
    public List<Employee> getAll() throws SQLException {
        Connection con = ConnectDB.getConnection();
        List<Employee> employeeList = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int eID = rs.getInt("empID");
            String fullName = rs.getString("fullName");
            String role = rs.getString("role");
            int phoneNo = rs.getInt("phoneNo");
            int isAdmin = rs.getInt("isAdmin");
            String username = rs.getString("username");
            String password = rs.getString("password");
            employeeList.add(new Employee(eID, fullName, role, phoneNo, isAdmin, username, password));
        }
        return employeeList;
    }

    @Override
    public int insert(Employee employee) throws SQLException {
        return 0;
    }

    @Override
    public int update(Employee employee) throws SQLException {
        return 0;
    }

    @Override
    public int save(Employee employee) throws SQLException {
        return 0;
    }

    @Override
    public int delete(int id) throws SQLException {
        return 0;
    }
}

