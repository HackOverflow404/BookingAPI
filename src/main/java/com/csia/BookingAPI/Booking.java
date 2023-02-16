package com.csia.BookingAPI;

import java.util.Date;

// The booking object class
public class Booking {
    // The booking class variables (each variable has a default value assigned) (The class variables are encapsulated)
    private String court = "";
    private String date = "";
    private String time = "";
    private String fname = "";
    private String lname = "";
    private String hnum = "";
    private long pnum = 0;
    private String email = "";
    private int gnum = 0;
    private boolean paid = false;

    // A default no-args constructor
    public Booking() {}

    // An overloaded args constructor
    public Booking(String court, String date, String time, String fname, String lname, String hnum, long pnum, String email, int gnum, boolean paid) {
        this.court = court;
        this.date = date;
        this.time = time;
        this.fname = fname;
        this.lname = lname;
        this.hnum = hnum;
        this.pnum = pnum;
        this.email = email;
        this.gnum = gnum;
        this.paid = paid;
    }

    // The setters and getters for the class variables
    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getHnum() {
        return hnum;
    }

    public void setHnum(String hnum) {
        this.hnum = hnum;
    }

    public long getPnum() {
        return pnum;
    }

    public void setPnum(long pnum) {
        this.pnum = pnum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGnum() {
        return gnum;
    }

    public void setGnum(int gnum) {
        this.gnum = gnum;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    @Override
    public String toString() {
        return "Court: " + court + "\nDate: " + date + "\nTime: " + time + "\nFirst Name: " + fname + "\nLast Name: " + lname + "\nHouse Number: " + hnum + "\nPhone Number: " + pnum + "\nEmail: " + email + "\nNumber of Guests: " + gnum + "\nPaid: " + paid;
    }
}