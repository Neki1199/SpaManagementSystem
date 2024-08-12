package com.sms.DAO;

import com.sms.Models.Client;
import com.sms.Models.Invoice;

import java.sql.SQLException;
import java.util.List;

public interface InvoiceDAO extends DAO<Invoice>{
    List<Invoice> getByClient(Integer clientID) throws SQLException;
}
