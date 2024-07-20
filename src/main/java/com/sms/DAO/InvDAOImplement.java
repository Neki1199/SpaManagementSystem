package com.sms.DAO;

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
        String sql = "SELECT productId, productName, quantity, cost, description FROM inventory WHERE productId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            int productId = rs.getInt("productId");
            String productName = rs.getString("productName");
            int quantity = rs.getInt("quantity");
            double cost = rs.getDouble("cost");
            String description = rs.getString("description");

            inventory = new Inventory(productId, productName, quantity, cost, description);
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
            inventoryList.add(new Inventory(productId, productName, quantity, cost, description));
        }
        ConnectDB.closePreparedStatement(ps);
        ConnectDB.closeConnection(con);
        return inventoryList;
    }

    @Override
    public int insert(Inventory inventory) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "INSERT INTO inventory(productName, quantity, cost, description) VALUES(?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, inventory.getName());
        ps.setInt(2, inventory.getQty());
        ps.setDouble(3, inventory.getCost());
        ps.setString(4, inventory.getDescription());

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
        String sql = "UPDATE inventory set productName = ?, quantity = ?, cost = ?, description = ? WHERE productId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, inventory.getName());
        ps.setInt(2, inventory.getQty());
        ps.setDouble(3, inventory.getCost());
        ps.setString(4, inventory.getDescription());
        ps.setInt(5, inventory.getId());
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
}
