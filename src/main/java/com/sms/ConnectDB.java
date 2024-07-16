package com.sms;
import java.sql.*;

public class ConnectDB {
    private static Connection con;


    private static final String url = "jdbc:sqlite:src/main/resources/SQLiteSMS/sms.db";

    private ConnectDB() {}

    public static synchronized Connection getConnection() throws SQLException {
        if(con == null || con.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
                con = DriverManager.getConnection(url);
                Statement stmt = con.createStatement();
                stmt.execute("PRAGMA journal_mode=WAL;");
                stmt.close();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return con;
    }

    public static void closeConnection(Connection con) throws SQLException {
        if (con != null && !con.isClosed()) {
            con.close();
        }
    }
    public static void closePreparedStatement(Statement stmt) throws SQLException {
        if (stmt != null && !stmt.isClosed()) {
            stmt.close();
        }
    }

    public static void closeResultSet(ResultSet rs) throws SQLException {
        if (rs != null && !rs.isClosed()) {
            rs.close();
        }
    }

    public static void closeStatement(Statement stmt) throws SQLException {
        if (stmt != null && !stmt.isClosed()) {
            stmt.close();
        }
    }
}