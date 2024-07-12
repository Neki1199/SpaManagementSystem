package com.sms.DAO;

import com.sms.BackEnd.Employee;
import com.sms.DataModels.ConnectDB;

import java.sql.*;
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
        return getEmployees(employeeList, rs);
    }

    @Override
    public int insert(Employee employee) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "INSERT INTO employees(fullName, role, phoneNo, isAdmin, username, password) VALUES(?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, employee.getName());
        ps.setString(2, employee.getRole());
        ps.setInt(3, employee.getPhone());
        ps.setInt(4, employee.getAdmin());
        ps.setString(5, employee.getUsername());
        ps.setString(6, employee.getPassword());
        int result = ps.executeUpdate();
        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            employee.setId(generatedKeys.getInt(1));
        }
        ConnectDB.closePreparedStatement(ps);
        ConnectDB.closeConnection(con);
        return result;

    }

    @Override
    public int update(Employee employee) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "UPDATE employees set empId = ?, fullName = ?, role = ?, phoneNo = ?, isAdmin = ?, username = ?, password = ? where empID = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, employee.getId());
        ps.setString(2, employee.getName());
        ps.setString(3, employee.getRole());
        ps.setInt(4, employee.getPhone());
        ps.setInt(5, employee.getAdmin());
        ps.setString(6, employee.getUsername());
        ps.setString(7, employee.getPassword());
        int result = ps.executeUpdate();
        ConnectDB.closePreparedStatement(ps);
        ConnectDB.closeConnection(con);
        return result;
    }

    @Override
    public int save(Employee employee) throws SQLException {
        return 0;
    }

    @Override
    public int delete(int id) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "DELETE FROM employees WHERE empID = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        int result = ps.executeUpdate();
        ConnectDB.closePreparedStatement(ps);
        ConnectDB.closeConnection(con);
        return result;
    }

    @Override
    public List<Employee> getByType(String theRole) throws SQLException {
        Connection con = ConnectDB.getConnection();
        List<Employee> employeeList = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE role = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, theRole);
        ResultSet rs = ps.executeQuery();
        return getEmployees(employeeList, rs);
    }

    private List<Employee> getEmployees(List<Employee> employeeList, ResultSet rs) throws SQLException {
        while (rs.next()) {
            int eID = rs.getInt("empID");
            String fullName = rs.getString("fullName");
            String role = rs.getString("role");
            int phoneNo = rs.getInt("phoneNo");
            int isAdmin = rs.getInt("isAdmin");
            String username = rs.getString("username");
            String password = rs.getString("password");
            Employee employee = new Employee(eID, fullName, role, phoneNo, isAdmin, username, password);
            employeeList.add(employee);
        }
        return employeeList;
    }

}

