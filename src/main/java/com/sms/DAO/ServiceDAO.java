package com.sms.DAO;

import com.sms.Models.Service;

import java.sql.SQLException;
import java.util.List;

public interface ServiceDAO extends DAO<Service>{
    public Service getServiceByName(String serviceName) throws SQLException;
    public List<Service> getServiceByType(String serviceType) throws SQLException;
}
