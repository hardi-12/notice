package com.example.noticeboard.models;

public class resource {
    String title, author, publication, subject, description, upload, link, semDept;

    resource() {}

    public resource(String title, String author, String publication, String subject, String description, String upload, String link, String semDept) {
        this.title = title;
        this.author = author;
        this.publication = publication;
        this.subject = subject;
        this.description = description;
        this.upload = upload;
        this.link = link;
        this.semDept = semDept;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUpload() {
        return upload;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSemDept() {
        return semDept;
    }

    public void setSemDept(String semDept) {
        this.semDept = semDept;
    }
}
