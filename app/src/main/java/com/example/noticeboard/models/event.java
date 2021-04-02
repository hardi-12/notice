package com.example.noticeboard.models;

public class event {
    String title, description, time_left, speaker, contact, duration, venue, time, conducted_by, reg_link, date, year;

    public event() {} // empty constructor required

    public event(String title, String description, String time_left, String speaker, String contact, String duration, String venue,
                 String time, String conducted_by, String reg_link, String date, String year) {
        this.title = title;
        this.description = description;
        this.time_left = time_left;
        this.speaker = speaker;
        this.contact = contact;
        this.duration = duration;
        this.venue = venue;
        this.time = time;
        this.conducted_by = conducted_by;
        this.reg_link = reg_link;
        this.date = date;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime_left() {
        return time_left;
    }

    public void setTime_left(String time_left) {
        this.time_left = time_left;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getConducted_by() {
        return conducted_by;
    }

    public void setConducted_by(String conducted_by) {
        this.conducted_by = conducted_by;
    }

    public String getReg_link() {
        return reg_link;
    }

    public void setReg_link(String reg_link) {
        this.reg_link = reg_link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
