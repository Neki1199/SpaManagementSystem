package com.sms.DAO;

import com.sms.Models.Client;

import java.sql.SQLException;

public interface ClientDAO extends DAO<Client>{
    public Client getClientByName(String name) throws SQLException;
}
