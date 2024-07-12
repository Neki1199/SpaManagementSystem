package com.sms.DAO;

import com.sms.BackEnd.Employee;
import com.sms.BackEnd.Service;
import com.sms.DataModels.ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
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
        return 0;
    }

    @Override
    public int update(Service service) throws SQLException {
        return 0;
    }

    @Override
    public int save(Service service) throws SQLException {
        return 0;
    }

    @Override
    public int delete(int id) throws SQLException {
        return 0;
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
}
