package com.sms.DAO;

import com.sms.BackEnd.Service;

import java.sql.SQLException;

public interface ServiceDAO extends DAO<Service>{
    public Service getServiceByName(String serviceName) throws SQLException;
}
