package com.sms.DAO;

import com.sms.ConnectDB;
import com.sms.Models.Invoice;
import javafx.scene.control.ListView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAOImplement implements InvoiceDAO {

    @Override
    public Invoice get(int id) throws SQLException {
        Connection con = ConnectDB.getConnection();
        Invoice invoice = null;

        String sql = "select * from invoices where invoiceId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Integer invoiceId = rs.getInt("invoiceId");
            Integer clientId = rs.getInt("clientId");
            String orderDate = rs.getString("orderDate");
            double total = rs.getDouble("totalCost");
            String paymentMethod = rs.getString("paymentMethod");

            invoice = new Invoice(invoiceId, clientId, orderDate, total, paymentMethod);
        }
        return invoice;
    }

    @Override
    public List<Invoice> getAll() throws SQLException {
        Connection con = ConnectDB.getConnection();
        List<Invoice> invoices = new ArrayList<Invoice>();
        String sql = "SELECT * FROM invoices";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Integer invoiceId = rs.getInt("invoiceId");
            Integer clientId = rs.getInt("clientId");
            String orderDate = rs.getString("orderDate");
            double total = rs.getDouble("totalCost");
            String paymentMethod = rs.getString("paymentMethod");
            Invoice invoice = new Invoice(invoiceId, clientId, orderDate, total, paymentMethod);
            invoices.add(invoice);
        }
        return invoices;
    }

    @Override
    public int insert(Invoice invoice) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "insert into invoices(clientId,orderDate,totalCost,paymentMethod) values(?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, invoice.getClientId());
        ps.setString(2, invoice.getOrderDate());
        ps.setDouble(3, invoice.getTotalCost());
        ps.setString(4, invoice.getPaymentMethod());

        int result = ps.executeUpdate();
        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            invoice.setInvoiceId(generatedKeys.getInt(1));
        }
        return result;
    }

    @Override
    public void update(Invoice invoice) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "update invoices set clientId = ?,orderDate = ?,totalCost = ?,paymentMethod = ? where invoiceId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, invoice.getClientId());
        ps.setString(2, invoice.getOrderDate());
        ps.setDouble(3, invoice.getTotalCost());
        ps.setString(4, invoice.getPaymentMethod());
        ps.setInt(5, invoice.getInvoiceId());
        ps.executeUpdate();
        ConnectDB.closePreparedStatement(ps);
        ConnectDB.closeConnection(con);
    }

    @Override
    public int save(Invoice obj) {return 0;}

    @Override
    public int delete(int id) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "delete from invoices where invoiceId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeUpdate();
    }

    @Override
    public void search(ListView<String> list, String toSearch) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT i.invoiceId FROM invoices i JOIN clients c ON i.clientId = c.clientId WHERE c.fullName LIKE ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" + toSearch + "%");
        ResultSet rs = ps.executeQuery();

        list.getItems().clear();

        while (rs.next()) {
            String invoiceId = String.valueOf(rs.getInt("invoiceId"));
            list.getItems().add(invoiceId);
        }
    }
}
