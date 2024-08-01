package com.sms.DAO;

import com.sms.Models.Client;
import com.sms.Models.Inventory;
import com.sms.ConnectDB;
import javafx.scene.control.ListView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvDAOImplement implements InventoryDAO{
    @Override
    public Inventory get(int id) throws SQLException {
        Inventory inventory = null;
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT productId, productName, quantity, cost, description, url FROM inventory WHERE productId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            int productId = rs.getInt("productId");
            String productName = rs.getString("productName");
            int quantity = rs.getInt("quantity");
            double cost = rs.getDouble("cost");
            String description = rs.getString("description");
            String url = rs.getString("url");

            inventory = new Inventory(productId, productName, quantity, cost, description, url);
        }
        ConnectDB.closePreparedStatement(ps);
        ConnectDB.closeConnection(con);
        return inventory;

    }

    @Override
    public List<Inventory> getAll() throws SQLException {
        List<Inventory> inventoryList = new ArrayList<Inventory>();
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT * FROM inventory";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            int productId = rs.getInt("productId");
            String productName = rs.getString("productName");
            int quantity = rs.getInt("quantity");
            double cost = rs.getDouble("cost");
            String description = rs.getString("description");
            String url = rs.getString("url");
            inventoryList.add(new Inventory(productId, productName, quantity, cost, description, url));
        }
        ConnectDB.closePreparedStatement(ps);
        ConnectDB.closeConnection(con);
        return inventoryList;
    }

    @Override
    public int insert(Inventory inventory) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "INSERT INTO inventory(productName, quantity, cost, description, url) VALUES(?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, inventory.getName());
        ps.setInt(2, inventory.getQuantity());
        ps.setDouble(3, inventory.getCost());
        ps.setString(4, inventory.getDescription());
        ps.setString(5, inventory.getUrl());

        int result = ps.executeUpdate();
        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            inventory.setId(generatedKeys.getInt(1));
        }
        ConnectDB.closePreparedStatement(ps);
        ConnectDB.closeConnection(con);
        return result;
    }

    @Override
    public void update(Inventory inventory) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "UPDATE inventory set productName = ?, quantity = ?, cost = ?, description = ?, url = ? WHERE productId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, inventory.getName());
        ps.setInt(2, inventory.getQuantity());
        ps.setDouble(3, inventory.getCost());
        ps.setString(4, inventory.getDescription());
        ps.setString(5, inventory.getUrl());
        ps.setInt(6, inventory.getId());
        ps.executeUpdate();
        ConnectDB.closePreparedStatement(ps);
        ConnectDB.closeConnection(con);
    }

    @Override
    public int save(Inventory inventory) {
        return 0;
    }

    @Override
    public int delete(int id) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "DELETE FROM inventory WHERE productId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        int result = ps.executeUpdate();
        ConnectDB.closePreparedStatement(ps);
        ConnectDB.closeConnection(con);
        return result;
    }

    @Override
    public void search(ListView<String> list, String toSearch) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT productName FROM inventory WHERE productName LIKE ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" + toSearch + "%");
        ResultSet rs = ps.executeQuery();

        list.getItems().clear(); // clear listview

        while (rs.next()) {
            String name = rs.getString("productName");
            list.getItems().add(name);
        }
    }

    @Override
    public Inventory getProductByName(String name) throws SQLException {
        Connection con = ConnectDB.getConnection();
        Inventory inventory = null;

        String sql = "SELECT productId, productName, quantity, cost, description, url FROM inventory WHERE productName = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int productId = rs.getInt("productId");
            String productName = rs.getString("productName");
            int quantity = rs.getInt("quantity");
            double cost = rs.getDouble("cost");
            String description = rs.getString("description");
            String url = rs.getString("url");
            inventory = new Inventory(productId, productName, quantity, cost, description, url);
        }
        return inventory;
    }
}
