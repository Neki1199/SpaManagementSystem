package com.sms.DAO;

import com.sms.BackEnd.Inventory;
import com.sms.DataModels.ConnectDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        return inventoryList;
    }

    @Override
    public int insert(Inventory inventory) throws SQLException {
        return 0;
    }

    @Override
    public int update(Inventory inventory) throws SQLException {
        return 0;
    }

    @Override
    public int save(Inventory inventory) throws SQLException {
        return 0;
    }

    @Override
    public int delete(int id) throws SQLException {
        return 0;
    }
}
