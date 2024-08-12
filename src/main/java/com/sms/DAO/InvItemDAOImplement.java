package com.sms.DAO;

import com.sms.ConnectDB;
import com.sms.Models.InvoiceItem;
import javafx.scene.control.ListView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class InvItemDAOImplement implements InvoiceItemDAO {

    @Override
    public InvoiceItem get(int id) throws SQLException {
        Connection con = ConnectDB.getConnection();
        InvoiceItem invoiceItem = null;

        String sql = "SELECT invoiceId, itemName, itemType, itemId, quantity, itemPrice FROM invoiceItems WHERE itemId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Integer invoiceId = rs.getInt("invoiceId");
            String itemName = rs.getString("itemName");
            String itemType = rs.getString("itemType");
            Integer itemId = rs.getInt("itemId");
            int quantity = rs.getInt("quantity");
            double itemPrice = rs.getDouble("itemPrice");

            invoiceItem = new InvoiceItem(invoiceId, itemName, itemType, itemId, quantity, itemPrice);
        }
        return invoiceItem;
    }

    @Override
    public List<InvoiceItem> getAll() throws SQLException {
        Connection con = ConnectDB.getConnection();
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        String sql = "SELECT * FROM invoiceItems";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Integer invoiceId = rs.getInt("invoiceId");
            String itemName = rs.getString("itemName");
            String itemType = rs.getString("itemType");
            Integer itemId = rs.getInt("itemId");
            int quantity = rs.getInt("quantity");
            double itemPrice = rs.getDouble("itemPrice");

            InvoiceItem invoiceItem = new InvoiceItem(invoiceId, itemName, itemType, itemId, quantity, itemPrice);
            invoiceItems.add(invoiceItem);
        }
        return invoiceItems;
    }

    @Override
    public int insert(InvoiceItem invoiceItem) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "INSERT INTO invoiceItems (invoiceId, itemName, itemType, itemId, quantity, itemPrice) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, invoiceItem.getInvoiceId());
        ps.setString(2, invoiceItem.getItemName());
        ps.setString(3, invoiceItem.getItemType());
        ps.setInt(4, invoiceItem.getItemId());
        ps.setInt(5, invoiceItem.getQuantity());
        ps.setDouble(6, invoiceItem.getItemPrice());

        return ps.executeUpdate();
    }

    @Override
    public void update(InvoiceItem invoiceItem) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "update invoiceItems set itemName = ?, itemType = ?, quantity = ?, itemPrice = ? where invoiceId = ? and itemId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, invoiceItem.getItemName());
        ps.setString(2, invoiceItem.getItemType());
        ps.setInt(3, invoiceItem.getQuantity());
        ps.setDouble(4, invoiceItem.getItemPrice());
        ps.setInt(5, invoiceItem.getInvoiceId());
        ps.executeUpdate();
    }

    @Override
    public int save(InvoiceItem obj) {
        return 0;
    }

    @Override
    public int delete(int id) throws SQLException {
        return 0;
    }

    @Override
    public void search(ListView<String> list, String toSearch) throws SQLException {

    }

    @Override
    public int delete(int itemId, int invoiceId) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "DELETE FROM invoiceItems WHERE itemId = ? AND invoiceId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, itemId);
        ps.setInt(2, invoiceId);
        return ps.executeUpdate();
    }

    @Override
    public void search(ListView<String> list, Integer invoiceID) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT itemName, itemPrice FROM invoiceItems WHERE invoiceId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, invoiceID);
        ResultSet rs = ps.executeQuery();
        list.getItems().clear();
        while (rs.next()) {
            String itemName = rs.getString("itemName");
            int price = rs.getInt("itemPrice");
            list.getItems().add(itemName + " - Cost: " + price);
        }
    }
}
