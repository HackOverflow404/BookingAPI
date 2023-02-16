package com.csia.BookingAPI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

// The Booking Data Access Object class
// Manages creating, retrieving, updating, and deleting booking records from the database
public class BookingDAO {
    // The connection variable
    private final Connection conn;

    // The BookingDAO constructor that takes the database connection object as a parameter and assigns it to the class variable
    public BookingDAO(Connection conn) {
        this.conn = conn;
    }

    // The method to set the settings
    public static void setSettings(Settings newSettings) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        String jsonString = gson.toJson(newSettings);

        new FileWriter("src/main/resources/JSON/settings.json", false).close();
        FileWriter file = new FileWriter("src/main/resources/JSON/settings.json");
        file.write(jsonString);
        file.close();
    }

    // The method to get the settings
    public static Settings getSettings() throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        Reader reader = Files.newBufferedReader(Paths.get("src/main/resources/JSON/settings.json"));
        Settings currentSettings = gson.fromJson(reader,Settings.class);
        reader.close();

        return currentSettings;
    }

    // The method to create a booking record in the database
    public void createBooking(Booking booking) throws SQLException {
        // The SQL statement
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO bookings (court, date, time, fname, lname, hnum, pnum, email, gnum, paid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        // Filling in the "?" arguments in the SQL statement with the corresponding values
        stmt.setString(1, booking.getCourt());
        stmt.setString(2, booking.getDate());
        stmt.setString(3, booking.getTime());
        stmt.setString(4, booking.getFname());
        stmt.setString(5, booking.getLname());
        stmt.setString(6, booking.getHnum());
        stmt.setLong(7, booking.getPnum());
        stmt.setString(8, booking.getEmail());
        stmt.setInt(9, booking.getGnum());
        stmt.setBoolean(10, booking.isPaid());

        // Executing the SQL statement
        stmt.executeUpdate();
        System.out.println("Created booking");

        // Terminating the statement object
        stmt.close();
    }

    // The method to get a booking record from the database
    public Booking getBooking(String court, String date, String time) throws SQLException {
        // The SQL statement
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM bookings WHERE court = ? AND date = ? AND time = ?");

        // Filling in the "?" arguments in the SQL statement with the corresponding values
        stmt.setString(1, court);
        stmt.setString(2, date);
        stmt.setString(3, time);

        // Executing the SQL statement and storing the output in the rs object
        ResultSet rs = stmt.executeQuery();

        // If the rs object is not empty, it will create a booking object and assign it with the corresponding values
        // Ideally, there should only be one record returned because the user panel should not allow for the user to book a booking at the same date, time, and court as a previous booking
        if (rs.next()) {
            Booking booking = new Booking();

            booking.setCourt(rs.getString("court"));
            booking.setDate(rs.getString("date"));
            booking.setTime(rs.getString("time"));
            booking.setFname(rs.getString("fname"));
            booking.setLname(rs.getString("lname"));
            booking.setHnum(rs.getString("hnum"));
            booking.setPnum(rs.getLong("pnum"));
            booking.setEmail(rs.getString("email"));
            booking.setGnum(rs.getInt("gnum"));
            booking.setPaid(rs.getBoolean("paid"));

            System.out.println(booking.toString());

            // Terminating the rs object and the statement object and returning the booking
            rs.close();
            stmt.close();
            return booking;
        }

        // If the rs object is empty (the requested record does not exist) then the method terminates the rs object and statement object and returns null
        rs.close();
        stmt.close();
        return null;
    }

    public ArrayList<String> getBookingTimes(String court, String date) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM bookings WHERE court = ? AND date = ?"); // The SQL statement
        stmt.setString(1, court);
        stmt.setString(2, date);
        ResultSet rs = stmt.executeQuery(); // Executing the SQL statement and storing the output in the rs object
        ArrayList<String> timings = new ArrayList<String>(); // Creating an arraylist of bookings to store all the bookings

        // Looping through rs and storing the values into a booking object which is appended or pushed to the bookings arraylist
        while (rs.next()) {
            timings.add(rs.getString("time"));
        }

        timings.forEach(System.out::println);

        // Terminating the rs object and the statement object and returning the booking
        rs.close();
        stmt.close();
        return timings;
    }

    // The method to get all bookings from the database
    public ArrayList<Booking> getAllBookings() throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM bookings"); // The SQL statement
        ResultSet rs = stmt.executeQuery(); // Executing the SQL statement and storing the output in the rs object
        ArrayList<Booking> bookings = new ArrayList<Booking>(); // Creating an arraylist of bookings to store all the bookings

        // Looping through rs and storing the values into a booking object which is appended or pushed to the bookings arraylist
        while (rs.next()) {
            Booking booking = new Booking();

            booking.setCourt(rs.getString("court"));
            booking.setDate(rs.getString("date"));
            booking.setTime(rs.getString("time"));
            booking.setFname(rs.getString("fname"));
            booking.setLname(rs.getString("lname"));
            booking.setHnum(rs.getString("hnum"));
            booking.setPnum(rs.getLong("pnum"));
            booking.setEmail(rs.getString("email"));
            booking.setGnum(rs.getInt("gnum"));
            booking.setPaid(rs.getBoolean("paid"));

            bookings.add(booking);
        }

        bookings.forEach(System.out::println);

        // Terminating the rs object and the statement object and returning the booking
        rs.close();
        stmt.close();
        return bookings;
    }

    // The method to update a booking record in the database
    public void updateBooking(Booking booking, String oldCourt, String oldDate, String oldTime) throws SQLException {
        // The SQL statement
        PreparedStatement stmt = conn.prepareStatement("UPDATE bookings SET court = ?, date = ?, time = ?, hnum = ?, pnum = ?, email = ?, gnum = ?, fname = ?, lname = ?, paid = ? WHERE court = ? AND date = ? AND time = ?");

        // Filling in the "?" arguments in the SQL statement with the corresponding values
        stmt.setString(1, booking.getCourt());
        stmt.setString(2, booking.getDate());
        stmt.setString(3, booking.getTime());
        stmt.setString(4, booking.getHnum());
        stmt.setLong(5, booking.getPnum());
        stmt.setString(6, booking.getEmail());
        stmt.setInt(7, booking.getGnum());
        stmt.setString(8, booking.getFname());
        stmt.setString(9, booking.getLname());
        stmt.setBoolean(10, booking.isPaid());
        stmt.setString(11, oldCourt);
        stmt.setString(12, oldDate);
        stmt.setString(13, oldTime);

        // Executing the SQL statement
        stmt.executeUpdate();
        System.out.println("Updated booking");

        // Terminating the statement object
        stmt.close();
    }

    // The method to delete a booking record in the database
    public void deleteBooking(String court, String date, String time) throws SQLException {
        // The SQL statement
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM bookings WHERE court = ? AND date = ? AND time = ?");

        // Filling in the "?" arguments in the SQL statement with the corresponding values
        stmt.setString(1, court);
        stmt.setString(2, date);
        stmt.setString(3, time);

        // Executing the SQL statement
        stmt.executeUpdate();
        System.out.println("Deleted booking");

        // Terminating the statement object
        stmt.close();
    }

    // The method to delete all bookings from the database
    public void deleteAllBookings() throws SQLException {
        // The SQL statement
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM bookings");

        // Executing the SQL statement
        stmt.executeUpdate();
        System.out.println("Deleted all bookings");

        // Terminating the statement object
        stmt.close();
    }
}