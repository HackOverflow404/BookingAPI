package com.csia.BookingAPI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RestAPIController {
    // Get courts
    @GetMapping("/settings/courts")
    public String[] getCourts() {
        Settings currentSettings = new Settings();

        try {
            currentSettings = BookingDAO.getSettings();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return currentSettings.getCourts();
    }

    // Get price
    @GetMapping("/settings/price")
    public int getPrice() {
        Settings currentSettings = new Settings();

        try {
            currentSettings = BookingDAO.getSettings();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return currentSettings.getPrice();
    }

    // Get time range
    @GetMapping("/settings/timeRange")
    public String[] getTimeRange() {
        Settings currentSettings = new Settings();

        try {
            currentSettings = BookingDAO.getSettings();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return new String[]{currentSettings.getTimeRangeBegin(), currentSettings.getTimeRangeEnd()};
    }

    // Get max date interval
    @GetMapping("/settings/maxDateInterval")
    public int getMaxDateInterval() {
        Settings currentSettings = new Settings();

        try {
            currentSettings = BookingDAO.getSettings();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return currentSettings.getMaxDateInterval();
    }

    // Get settings
    @GetMapping("/settings")
    public Settings getSettings() {
        Settings currentSettings = null;

        try {
            currentSettings = BookingDAO.getSettings();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return currentSettings;
    }

    // Set settings
    @PostMapping("/settings")
    public ResponseEntity<Object> setSettings(@RequestBody Settings newSettings) {
        try {
            BookingDAO.setSettings(newSettings);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    // Create a new booking
    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestBody Booking booking) {
        Connection conn = DBconnect.connect();
        BookingDAO bookingDAO = new BookingDAO(conn);

        try {
            bookingDAO.createBooking(booking);

            // Recipient's email ID needs to be mentioned.
            String recipient = booking.getEmail();
            // Sender's email ID needs to be mentioned
            String sender = "courtbookingofficial1@gmail.com";
            String host = "smtp.gmail.com";

            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.starttls.enable", "true");

            // Get the default Session object.
            Session session = Session.getDefaultInstance(properties,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(sender, "iokcshxnpjdhfuwy");
                        }
                    });

            try {
                // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(sender));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
                message.setSubject("Booking created for " + booking.getDate());
                String emailHTML =
                        null;
                try {
                    emailHTML = "<html>" +
                                    "<head>" +
                                        "<meta charset=\"utf-8\" />" +
                                    "</head>" +
                                    "<body>" +
                                        "<h1>Your booking has been made with the following information:</h1>" +
                                        "<tr>" +
                                            "<th style=\"text-align: right;\">Court: </th>" +
                                            "<td>" + booking.getCourt() + "</td>" +
                                        "</tr>" +
                                        "<tr>" +
                                            "<th style=\"text-align: right;\">Date: </th>" +
                                            "<td>" + booking.getDate() + "</td>" +
                                        "</tr>" +
                                        "<tr>" +
                                            "<th style=\"text-align: right;\">Time: </th>" +
                                            "<td>" + booking.getTime() + "</td>" +
                                        "</tr>" +
                                        "<tr>" +
                                            "<th style=\"text-align: right;\">Name: </th>" +
                                            "<td>" + booking.getFname() + " " + booking.getLname() + "</td>" +
                                        "</tr>" +
                                        "<tr>" +
                                            "<th style=\"text-align: right;\">House No.: </th>" +
                                            "<td>" + booking.getHnum() + "</td>" +
                                        "</tr>" +
                                        "<tr>" +
                                            "<th style=\"text-align: right;\">Phone No.: </th>" +
                                            "<td>" + booking.getPnum() + "</td>" +
                                        "</tr>" +
                                        "<tr>" +
                                            "<th style=\"text-align: right;\">Email: </th>" +
                                            "<td>" + booking.getEmail() + "</td>" +
                                        "</tr>" +
                                        "<tr>" +
                                            "<th style=\"text-align: right;\">Guest: </th>" +
                                            "<td>" + booking.getGnum() + "</td>" +
                                        "</tr>" +
                                        "<tr>" +
                                            "<th style=\"text-align: right;\">Price: </th>" +
                                            "<td>&#8377;" + (booking.getGnum() * BookingDAO.getSettings().getPrice()) + "</td>" +
                                        "</tr>" +
                                        "<tr>" +
                                            "<th style=\"text-align: right;\">Paid: </th>" +
                                            "<td>" + (booking.isPaid() ? "Paid" : "Not yet") + "</td>" +
                                        "</tr>" +
                                    "</body>" +
                                "</html>";
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                message.setContent(emailHTML, "text/html");

                // Send message
                Transport.send(message);
                System.out.println("Sent message successfully....");
            } catch (MessagingException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        DBconnect.closeConnection(conn);

        return ResponseEntity.ok().build();
    }

    // Get all bookings
    @GetMapping
    public List<Booking> getAllBookings() {
        Connection conn = DBconnect.connect();
        BookingDAO bookingDAO = new BookingDAO(conn);
        List<Booking> bookings = null;

        try {
            bookings = bookingDAO.getAllBookings(); // Receives an ArrayList of bookings which is stored into the bookings variable using polymorphism and inheritance
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        DBconnect.closeConnection(conn);

        return bookings;
    }

    // Get a single booking
    @GetMapping("/{court}/{date}/{time}")
    public Booking getBooking(@PathVariable String court, @PathVariable String date, @PathVariable String time) {
        Connection conn = DBconnect.connect();
        BookingDAO bookingDAO = new BookingDAO(conn);
        Booking booking = null;
        date = date.replaceAll("%2F", "/");

        try {
            booking = bookingDAO.getBooking(court, date, time); // Receives an ArrayList of bookings which is stored into the bookings variable using polymorphism and inheritance
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        DBconnect.closeConnection(conn);

        return booking;
    }

    @GetMapping("/{court}/{date}")
    public List<String> getTimings(@PathVariable String court, @PathVariable String date) {
        Connection conn = DBconnect.connect();
        BookingDAO bookingDAO = new BookingDAO(conn);
        List<String> timings = null;
        date = date.replaceAll("%2F", "/");

        try {
            timings = bookingDAO.getBookingTimes(court, date); // Receives an ArrayList of bookings which is stored into the bookings variable using polymorphism and inheritance
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        DBconnect.closeConnection(conn);

        return timings;
    }

    @PutMapping("/{courtOld}/{dateOld}/{timeOld}")
    public ResponseEntity<Object> updateBooking(@PathVariable String courtOld, @PathVariable String dateOld, @PathVariable String timeOld, @RequestBody Booking bookingNew) {
        Connection conn = DBconnect.connect();
        BookingDAO bookingDAO = new BookingDAO(conn);
        dateOld = dateOld.replaceAll("%2F", "/");

        try {
            bookingDAO.updateBooking(bookingNew, courtOld, dateOld, timeOld);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{court}/{date}/{time}")
    public ResponseEntity<Object> deleteBooking(@PathVariable String court, @PathVariable String date, @PathVariable String time) {
        Connection conn = DBconnect.connect();
        BookingDAO bookingDAO = new BookingDAO(conn);
        Booking booking = null;
        date = date.replaceAll("%2F", "/");

        try {
            booking = bookingDAO.getBooking(court, date, time);
            bookingDAO.deleteBooking(court, date, time);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteAllBookings() {
        Connection conn = DBconnect.connect();
        BookingDAO bookingDAO = new BookingDAO(conn);

        try {
            bookingDAO.deleteAllBookings();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }
}
