package com.csia.BookingAPI;

//import com.google.gson.Gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

// The class to initialize and test the database (Creates the database and table)
public class InitializeDB {
    // The method to create the database
    public static void createNewDatabase() {
        // The url to the location of the database
        String url = "jdbc:sqlite:src/main/resources/sqlite/Bookings.db";

        try {
            // Checking if the database already exists
            File dbFile = new File("src/main/resources/sqlite/Bookings.db");
            if (!dbFile.exists()) {
                // Creating the database and simultaneously connecting to it
                Connection conn = DriverManager.getConnection(url);
                // If the connection is successful (not null), it retrieves the metadata of the database and prints the driver name
                if (conn != null) {
                    DatabaseMetaData meta = conn.getMetaData();
                    System.out.println("The driver name is " + meta.getDriverName());
                    System.out.println("A new database has been created.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        File settingsFile = new File("src/main/resources/JSON/settings.json");
        if (!settingsFile.exists()) {
            try {
                if (!settingsFile.createNewFile()) {
                    System.out.println("File already exists");
                } else {
                    System.out.println("A new JSON file has been created.");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        String jsonString = gson.toJson(new Settings());

        try {
            FileWriter file = new FileWriter("src/main/resources/JSON/settings.json");
            file.write(jsonString);
            file.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // The method to create the bookings table in the database
    public static void createNewTable() {
        // The url to the location of the database
        String url = "jdbc:sqlite:src/main/resources/sqlite/Bookings.db";

        // The sql command to create the bookings table if it doesn't already exist
        String sql = "CREATE TABLE IF NOT EXISTS bookings (\n"
                + " court varchar(255),\n"
                + " date varchar(255),\n"
                + " time varchar(255),\n"
                + " fname varchar(255),\n"
                + " lname varchar(255),\n"
                + " hnum varchar(255),\n"
                + " pnum bigint,\n"
                + " email varchar(255),\n"
                + " gnum int,\n"
                + " paid bit\n"
                + ");";

        // Connects to the database, creates the sql statement, and executes the sql command
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // A method to test the database management
    public static void testRecord() {
        Connection conn = DBconnect.connect(); // Declaring and initializing the connection to the db
        BookingDAO bookingDAO = new BookingDAO(conn); // Creating a bookingDAO object while passing the connection object to the BookingDAO constructor

        // The court, date, and time variables
        String court = "Tennis";
        String date = "28/12/2022";
        String time = "1:00 AM";

        // Creating the booking object
        Booking booking = new Booking(court, date, time, "John", "Doe", "A-000", 1234567890, "test@mail.com", 5, true);

        // Creating the booking in the database
        try {
            bookingDAO.createBooking(booking);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Getting and printing the record from the database
        try {
            System.out.println(bookingDAO.getBooking(court,date, time).toString());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Updating the record in the database
        booking.setHnum("A-001");
        try {
            bookingDAO.updateBooking(booking, court, date, time);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Getting and printing the updated record from the database
        try {
            System.out.println(bookingDAO.getBooking(court,date, time).toString());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Deleting the record from the database
        try {
            bookingDAO.deleteBooking(court, date, time);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Creating 10 booking records (each different) in the database
        for (int i = 0; i < 10; i++) {
            booking.setGnum(i);
            booking.setPaid(!booking.isPaid());
            try {
                bookingDAO.createBooking(booking);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        // Getting all the bookings from the database at once
        ArrayList<Booking> bookings = null;
        try {
            bookings = bookingDAO.getAllBookings();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Looping through and printing each booking record from the database
        if (bookings != null) {
            bookings.forEach(System.out::println);
        }

        // Deleting all the bookings from the database at once
        try {
            bookingDAO.deleteAllBookings();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Closing the connection to the database
        DBconnect.closeConnection(conn);
    }

    public static void main(String[] args) {
        createNewDatabase();
        createNewTable();
        testRecord();

        Connection conn = DBconnect.connect(); // Declaring and initializing the connection to the db
        System.out.println(conn);
        if (conn != null) {
            BookingDAO bookingDAO = new BookingDAO(conn); // Creating a bookingDAO object while passing the connection object to the BookingDAO constructor
            Booking booking1 = new Booking("Tennis", "1/1/2023", "12:00 AM", "John", "Doe", "A-000", 1234567890, "test@mail.com", 5, true);
            Booking booking2 = new Booking("Squash", "1/1/2023", "12:00 AM", "John", "Doe", "A-000", 1234567890, "test@mail.com", 5, true);
            Booking booking3 = new Booking("Tennis", "1/2/2023", "12:00 AM", "John", "Doe", "A-000", 1234567890, "test@mail.com", 5, true);
            Booking booking4 = new Booking("Tennis", "1/1/2023", "12:01 AM", "John", "Doe", "A-000", 1234567890, "test@mail.com", 5, true);

            try {
                bookingDAO.deleteAllBookings();
                bookingDAO.createBooking(booking1);
                bookingDAO.createBooking(booking2);
                bookingDAO.createBooking(booking3);
                bookingDAO.createBooking(booking4);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            DBconnect.closeConnection(conn);
        }
    }
}