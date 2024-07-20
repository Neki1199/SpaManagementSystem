package com.sms.DAO;

import com.sms.Models.Employee;
import com.sms.ConnectDB;
import javafx.scene.control.ListView;

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
            String phoneNo = rs.getString("phoneNo");
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
        ps.setString(3, employee.getPhone());
        ps.setInt(4, employee.getAdmin());
        ps.setString(5, employee.getUsername());
        ps.setString(6, employee.getPassword());
        int result = ps.executeUpdate();
        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            employee.setId(generatedKeys.getInt(1));
        }
        return result;

    }

    @Override
    public void update(Employee employee) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "UPDATE employees set fullName = ?, role = ?, phoneNo = ?, isAdmin = ?, username = ?, password = ? where empID = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, employee.getName());
        ps.setString(2, employee.getRole());
        ps.setString(3, employee.getPhone());
        ps.setInt(4, employee.getAdmin());
        ps.setString(5, employee.getUsername());
        ps.setString(6, employee.getPassword());
        ps.setInt(7, employee.getId());
        ps.executeUpdate();

    }

    @Override
    public int save(Employee employee) {
        return 0;
    }

    @Override
    public int delete(int id) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "DELETE FROM employees WHERE empID = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeUpdate();
    }

    @Override
    public void search(ListView<String> list, String toSearch) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT fullName, phoneNo FROM employees WHERE fullName LIKE ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" + toSearch + "%");
        ResultSet rs = ps.executeQuery();

        list.getItems().clear(); // clear listview

        while (rs.next()) {
            String name = rs.getString("fullName");
            String phone = rs.getString("phoneNo");
            list.getItems().add(name + "- " + phone);
        }
    }

    @Override
    public List<Employee> getByType(String theRole) throws SQLException {
        Connection con = ConnectDB.getConnection();
        List<Employee> employeeList = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE role = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, theRole);
        ResultSet rs = ps.executeQuery();
        // Do not add close
        return getEmployees(employeeList, rs);
    }

    @Override
    public Employee getEmployeeByName(String name) throws SQLException {
        Connection con = ConnectDB.getConnection();
        Employee employee = null;
        String sql = "SELECT empID, fullName, role, phoneNo, isAdmin, username, password FROM employees WHERE fullName = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int eID = rs.getInt("empID");
            String fullName = rs.getString("fullName");
            String role = rs.getString("role");
            String phoneNo = rs.getString("phoneNo");
            int isAdmin = rs.getInt("isAdmin");
            String username = rs.getString("username");
            String password = rs.getString("password");
            employee = new Employee(eID, fullName, role, phoneNo, isAdmin, username, password);
        }
        return employee;
    }

    @Override
    public boolean usersExist() throws SQLException {
        String query = "SELECT COUNT(*) FROM employees";
        try (Connection connection = ConnectDB.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;
    }

    private List<Employee> getEmployees(List<Employee> employeeList, ResultSet rs) throws SQLException {
        while (rs.next()) {
            int eID = rs.getInt("empID");
            String fullName = rs.getString("fullName");
            String role = rs.getString("role");
            String phoneNo = rs.getString("phoneNo");
            int isAdmin = rs.getInt("isAdmin");
            String username = rs.getString("username");
            String password = rs.getString("password");
            Employee employee = new Employee(eID, fullName, role, phoneNo, isAdmin, username, password);
            employeeList.add(employee);
        }
        return employeeList;
    }

}

