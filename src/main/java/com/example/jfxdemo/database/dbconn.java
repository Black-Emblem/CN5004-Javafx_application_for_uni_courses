package com.example.jfxdemo.database;


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;


public class dbconn {
     static Connection conn = null;
    public static Connection dbconn() throws SQLException {


        try {
            //Creating a connection to the database
            conn = DriverManager.getConnection("jdbc:mariadb://localhost:3307/uni db", "root", "");

        } catch (SQLException e) {
            System.out.println("Connection with data base failed");
        }
        return conn;
    }
}