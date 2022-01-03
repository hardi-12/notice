package com.kjsieit.noticeboard.models;

public class event {
    String id, event_name, event_title, end_time, start_time, duration, venue, classC, description, speaker_name, conducted_by, organised_by, eventcoord, eventcoorddesg, amount, series_name, series_title, registration_link, speaker_contact, department, participants, speaker_desg;
    public event() {} // empty constructor required

    public event(String id, String event_name, String event_title, String end_time, String start_time, String duration, String venue, String classC, String description, String speaker_name, String conducted_by, String organised_by, String eventcoord, String eventcoorddesg, String amount, String series_name, String series_title, String registration_link, String speaker_contact, String department, String participants, String speaker_desg) {
        this.id = id;
        this.event_name = event_name;
        this.event_title = event_title;
        this.end_time = end_time;
        this.start_time = start_time;
        this.duration = duration;
        this.venue = venue;
        this.classC = classC;
        this.description = description;
        this.speaker_name = speaker_name;
        this.conducted_by = conducted_by;
        this.organised_by = organised_by;
        this.eventcoord = eventcoord;
        this.eventcoorddesg = eventcoorddesg;
        this.amount = amount;
        this.series_name = series_name;
        this.series_title = series_title;
        this.registration_link = registration_link;
        this.speaker_contact = speaker_contact;
        this.department = department;
        this.participants = participants;
        this.speaker_desg = speaker_desg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
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

    public String getClassC() {
        return classC;
    }

    public void setClassC(String classC) {
        this.classC = classC;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpeaker_name() {
        return speaker_name;
    }

    public void setSpeaker_name(String speaker_name) {
        this.speaker_name = speaker_name;
    }

    public String getConducted_by() {
        return conducted_by;
    }

    public void setConducted_by(String conducted_by) {
        this.conducted_by = conducted_by;
    }

    public String getOrganised_by() {
        return organised_by;
    }

    public void setOrganised_by(String organised_by) {
        this.organised_by = organised_by;
    }

    public String getEventcoord() {
        return eventcoord;
    }

    public void setEventcoord(String eventcoord) {
        this.eventcoord = eventcoord;
    }

    public String getEventcoorddesg() {
        return eventcoorddesg;
    }

    public void setEventcoorddesg(String eventcoorddesg) {
        this.eventcoorddesg = eventcoorddesg;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSeries_name() {
        return series_name;
    }

    public void setSeries_name(String series_name) {
        this.series_name = series_name;
    }

    public String getSeries_title() {
        return series_title;
    }

    public void setSeries_title(String series_title) {
        this.series_title = series_title;
    }

    public String getRegistration_link() {
        return registration_link;
    }

    public void setRegistration_link(String registration_link) {
        this.registration_link = registration_link;
    }

    public String getSpeaker_contact() {
        return speaker_contact;
    }

    public void setSpeaker_contact(String speaker_contact) {
        this.speaker_contact = speaker_contact;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public String getSpeaker_desg() {
        return speaker_desg;
    }

    public void setSpeaker_desg(String speaker_desg) {
        this.speaker_desg = speaker_desg;
    }
}
