package com.sms.DAO;

import com.sms.Models.Client;
import com.sms.Models.Inventory;

import java.sql.SQLException;

public interface InventoryDAO extends DAO<Inventory>{
    public Inventory getProductByName(String name) throws SQLException;
}
