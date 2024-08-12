package com.sms.DAO;

import com.sms.Models.InvoiceItem;
import javafx.scene.control.ListView;

import java.sql.SQLException;

public interface InvoiceItemDAO extends DAO<InvoiceItem>{
    public int delete(int itemId, int invoiceId) throws SQLException;
    public void search(ListView<String> list, Integer invoiceID) throws SQLException;
}
