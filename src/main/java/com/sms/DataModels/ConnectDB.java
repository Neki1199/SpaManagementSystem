package com.sms.DataModels;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectDB {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            // Connect to the database
            String url = "jdbc:sqlite:src/main/resources/SQLiteSMS/sms.db";
            connection = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite established.");

            // Create a statement
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}


//              Example of creating a table
//            String createTableSQL = "CREATE TABLE IF NOT EXISTS example (id INTEGER PRIMARY KEY, name TEXT)";
//            statement.executeUpdate(createTableSQL);

//              Example of inserting data
//            String insertSQL = "INSERT INTO example (name) VALUES ('Sample Name')";
//            statement.executeUpdate(insertSQL);

//               Example of querying data
//          String querySQL = "SELECT * FROM employees";
//          ResultSet rs = statement.executeQuery(querySQL);
//          while (rs.next()) {
//        // Read the result set
//              System.out.println("ID: " + rs.getInt("empId") + ", Name: " + rs.getString("fullName"));
//          }