package com.sms.DAO;

import com.sms.Models.Service;
import com.sms.ConnectDB;
import javafx.scene.control.ListView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAOImplement implements ServiceDAO {
    @Override
    public Service get(int id) throws SQLException {
        Connection con = ConnectDB.getConnection();
        Service service = null;

        String sql = "SELECT serviceId, name, description, price, serviceType, duration, staffType FROM services WHERE serviceId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int serviceId = rs.getInt("serviceId");
            String name = rs.getString("name");
            String description = rs.getString("description");
            double price = rs.getDouble("price");
            String serviceType = rs.getString("serviceType");
            int duration = rs.getInt("duration");
            String staffType = rs.getString("staffType");

            service = new Service(serviceId, name, description, price, serviceType, duration, staffType);
        }
        return service;
    }

    @Override
    public List<Service> getAll() throws SQLException {
        Connection con = ConnectDB.getConnection();
        List<Service> services = new ArrayList<Service>();
        String sql = "SELECT * FROM services";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int serviceId = rs.getInt("serviceId");
            String name = rs.getString("name");
            String description = rs.getString("description");
            double price = rs.getDouble("price");
            String serviceType = rs.getString("serviceType");
            int duration = rs.getInt("duration");
            String staffType = rs.getString("staffType");
            services.add(new Service(serviceId, name, description, price, serviceType, duration, staffType));

        }
        return services;
    }

    @Override
    public int insert(Service service) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "INSERT INTO services(name, description, price, serviceType, duration, staffType) VALUES(?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, service.getName());
        ps.setString(2, service.getDescription());
        ps.setDouble(3, service.getPrice());
        ps.setString(4, service.getServiceType());
        ps.setInt(5, service.getDuration());
        ps.setString(6, service.getStaffType());

        int result = ps.executeUpdate();
        // So can be autoincremented
        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            service.setId(generatedKeys.getInt(1));
        }
        return result;
    }

    @Override
    public void update(Service service) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "UPDATE services set name = ?, description = ?, price = ?, serviceType = ?, duration = ?, staffType = ? where serviceId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, service.getName());
        ps.setString(2, service.getDescription());
        ps.setDouble(3, service.getPrice());
        ps.setString(4, service.getServiceType());
        ps.setInt(5, service.getDuration());
        ps.setString(6, service.getStaffType());
        ps.setInt(7, service.getId());
        ps.executeUpdate();
        ConnectDB.closePreparedStatement(ps);
        ConnectDB.closeConnection(con);

    }

    @Override
    public int save(Service service) {
        return 0;
    }

    @Override
    public int delete(int id) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "DELETE FROM services WHERE serviceId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeUpdate();
    }

    @Override
    public void search(ListView<String> list, String toSearch) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT name FROM services WHERE name LIKE ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" + toSearch + "%");
        ResultSet rs = ps.executeQuery();

        list.getItems().clear(); // clear listview

        while (rs.next()) {
            String name = rs.getString("name");
            list.getItems().add(name);
        }
    }

    @Override
    public Service getServiceByName(String serviceName) throws SQLException {
        Connection con = ConnectDB.getConnection();
        Service service = null;

        String sql = "SELECT serviceId, name, description, price, serviceType, duration, staffType FROM services WHERE name = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, serviceName);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int serviceId = rs.getInt("serviceId");
            String name = rs.getString("name");
            String description = rs.getString("description");
            double price = rs.getDouble("price");
            String serviceType = rs.getString("serviceType");
            int duration = rs.getInt("duration");
            String staffType = rs.getString("staffType");

            service = new Service(serviceId, name, description, price, serviceType, duration, staffType);
        }
        return service;
    }

    @Override
    public List<Service> getServiceByType(String serviceT) throws SQLException {
        Connection con = ConnectDB.getConnection();
        List<Service> servicesList =  new ArrayList<Service>();
        String sql = "SELECT * FROM services WHERE serviceType = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, serviceT);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int serviceId = rs.getInt("serviceId");
            String name = rs.getString("name");
            String description = rs.getString("description");
            double price = rs.getDouble("price");
            String serviceType = rs.getString("serviceType");
            int duration = rs.getInt("duration");
            String staffType = rs.getString("staffType");
            servicesList.add(new Service(serviceId, name, description, price, serviceType, duration, staffType));
        }
        return servicesList;
    }
}
