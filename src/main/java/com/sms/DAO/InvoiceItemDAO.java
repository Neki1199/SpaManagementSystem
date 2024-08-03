package com.sms.DAO;

import com.sms.Models.InvoiceItem;

import java.sql.SQLException;

public interface InvoiceItemDAO extends DAO<InvoiceItem>{
    public int delete(int itemId, int invoiceId) throws SQLException;
}
