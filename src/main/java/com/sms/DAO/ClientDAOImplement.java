package com.sms.DAO;

import com.sms.Models.Client;
import com.sms.ConnectDB;

import javafx.scene.control.ListView;
import java.sql.*;
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
            String phoneNo = rs.getString("phoneNo");
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
            String phoneNo = rs.getString("phoneNo");
            String notes = rs.getString("notes");
            String email = rs.getString("email");
            Client client = new Client(clientId, fullName, phoneNo, notes, email);
            clients.add(client);
        }

        return clients;
    }

    @Override
    public int insert(Client client) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "INSERT INTO clients(fullName, phoneNo, notes, email) VALUES(?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, client.getName());
        ps.setString(2, client.getPhone());
        ps.setString(3, client.getNotes());
        ps.setString(4, client.getEmail());

        int result = ps.executeUpdate();
        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            client.setId(generatedKeys.getInt(1));
        }
        return result;
    }

    @Override
    public void update(Client client) throws SQLException {
        try(Connection con = ConnectDB.getConnection()) {
            String sql = "UPDATE clients SET fullName = ?, phoneNo = ?, notes = ?, email = ? WHERE clientId = ?";
            try(PreparedStatement ps = con.prepareStatement(sql)){
                ps.setString(1, client.getName());
                ps.setString(2, client.getPhone());
                ps.setString(3, client.getNotes());
                ps.setString(4, client.getEmail());
                ps.setInt(5, client.getId());
                ps.executeUpdate();
                ConnectDB.closePreparedStatement(ps);
                ConnectDB.closeConnection(con);
            } catch (SQLException e){
                throw new SQLException(e.getMessage());
            }
        }
    }

    @Override
    public int save(Client obj) {
        return 0;
    }

    @Override
    public int delete(int id) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "DELETE FROM clients WHERE clientId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        int result = ps.executeUpdate();
        ConnectDB.closePreparedStatement(ps);
        ConnectDB.closeConnection(con);
        return result;
    }

    @Override
    public void search(ListView<String> listView, String toSearch) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT fullName, phoneNo FROM clients WHERE fullName LIKE ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" + toSearch + "%");
        ResultSet rs = ps.executeQuery();

        listView.getItems().clear(); // clear listview

        while (rs.next()) {
            String name = rs.getString("fullName");
            String phone = rs.getString("phoneNo");
            listView.getItems().add(name + "- " + phone);
        }
    }

    @Override
    public Client getClientByName(String name) throws SQLException {
        Connection con = ConnectDB.getConnection();
        Client client = null;

        String sql = "SELECT clientId, fullName, phoneNo, notes, email FROM clients WHERE fullName = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int clientId = rs.getInt("clientId");
            String fullName = rs.getString("fullName");
            String phoneNo = rs.getString("phoneNo");
            String notes = rs.getString("notes");
            String email = rs.getString("email");
            client = new Client(clientId, fullName, phoneNo, notes, email);
        }
        return client;
    }
}
