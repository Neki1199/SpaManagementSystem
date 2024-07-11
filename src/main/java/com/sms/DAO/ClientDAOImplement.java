package com.sms.DAO;

import com.sms.BackEnd.Client;
import com.sms.BackEnd.Service;
import com.sms.DataModels.ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAOImplement implements ClientDAO{
    @Override
    public Client get(int id) throws SQLException {
        Connection con = ConnectDB.getConnection();
        Client client = null;

        String sql = "SELECT clientId, fullName, phoneNo, notes, email FROM clients WHERE clientId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int clientId = rs.getInt("clientId");
            String fullName = rs.getString("fullName");
            int phoneNo = rs.getInt("phoneNo");
            String notes = rs.getString("notes");
            String email = rs.getString("email");

            client = new Client(clientId, fullName, phoneNo, notes, email);
        }
        return client;
    }

    @Override
    public List<Client> getAll() throws SQLException {
        Connection con = ConnectDB.getConnection();
        List<Client> clients = new ArrayList<Client>();
        String sql = "SELECT * FROM clients";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int clientId = rs.getInt("clientId");
            String fullName = rs.getString("fullName");
            int phoneNo = rs.getInt("phoneNo");
            String notes = rs.getString("notes");
            String email = rs.getString("email");
            Client client = new Client(clientId, fullName, phoneNo, notes, email);
            clients.add(client);
        }
        return clients;
    }

    @Override
    public int insert(Client obj) throws SQLException {
        return 0;
    }

    @Override
    public int update(Client obj) throws SQLException {
        return 0;
    }

    @Override
    public int save(Client obj) throws SQLException {
        return 0;
    }

    @Override
    public int delete(int id) throws SQLException {
        return 0;
    }
}
