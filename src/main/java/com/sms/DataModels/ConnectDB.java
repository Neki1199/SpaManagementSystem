package com.sms.DataModels;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {


    private static final String url = "jdbc:sqlite:src/main/resources/SQLiteSMS/sms.db";

    private ConnectDB() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }
}