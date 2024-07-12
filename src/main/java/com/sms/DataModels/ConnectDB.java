package com.sms.DataModels;
import java.sql.*;

public class ConnectDB {


    private static final String url = "jdbc:sqlite:src/main/resources/SQLiteSMS/sms.db";

    private ConnectDB() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    public static void closeConnection(Connection con) throws SQLException {
        if (con != null) {
            con.close();
        }
    }
    public static void closePreparedStatement(Statement stmt) throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
    }

    public static void closeResultSet(ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
    }

    public static void closeStatement(Statement stmt) throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
    }
}