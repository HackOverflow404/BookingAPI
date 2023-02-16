package com.csia.BookingAPI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// The class to connect to the database
public class DBconnect {

    // The method to handle connecting to the database (returns the database connection object)
    public static Connection connect() {
        // Declares the connection object and initializes it to null
        Connection conn = null;

        try {
            // The url to location of the database
            String url = "jdbc:sqlite:src/main/resources/sqlite/Bookings.db";
            // Connects to the database at the provided url
            conn = DriverManager.getConnection(url);

//            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // Returns the connection object
        return conn;
    }

    // The method to handle terminating the connection to the database (takes the database connection object as a parameter)
    public static void closeConnection(Connection conn) {
        try {
            // If the connection exists (is not null), it closes the connection
            if (conn != null) {
                conn.close();
//                System.out.println("Connection to SQLite has been terminated.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}