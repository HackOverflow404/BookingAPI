package com.csia.BookingAPI;

import java.util.Arrays;

public class Settings {
    private int price = 200;
    private int maxDateInterval = 5;
    private String timeRangeBegin = "06:00 AM";
    private String timeRangeEnd = "09:00 PM";
    private String[] courts = {"Tennis", "Table Tennis", "Squash", "Badminton", "Basketball"};

    public Settings() {}
    public Settings(int price, int maxDateInterval, String timeRangeBegin, String timeRangeEnd, String[] courts) {
        this.price = price;
        this.maxDateInterval = maxDateInterval;
        this.timeRangeBegin = timeRangeBegin;
        this.timeRangeEnd = timeRangeEnd;
        this.courts = courts.clone();
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMaxDateInterval() {
        return maxDateInterval;
    }

    public void setMaxDateInterval(int maxDateInterval) {
        this.maxDateInterval = maxDateInterval;
    }

    public String getTimeRangeBegin() {
        return timeRangeBegin;
    }

    public void setTimeRangeBegin(String timeRangeBegin) {
        this.timeRangeBegin = timeRangeBegin;
    }

    public String getTimeRangeEnd() {
        return timeRangeEnd;
    }

    public void setTimeRangeEnd(String timeRangeEnd) {
        this.timeRangeEnd = timeRangeEnd;
    }

    public String[] getCourts() {
        return courts;
    }

    public void setCourts(String[] courts) {
        this.courts = courts;
    }

    @Override
    public String toString() {
        return "Price: " + price + "\nMax Date Interval: " + maxDateInterval + "\nTime Range Begin: " + timeRangeBegin + "\nTime Range End: " + timeRangeEnd + "\nCourts: " + Arrays.toString(courts);
    }
}
