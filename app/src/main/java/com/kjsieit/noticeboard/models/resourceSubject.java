package com.kjsieit.noticeboard.models;

public class resourceSubject {
    private String subjectTitle, subjectCode, subjectSem, subjectDept;

    public resourceSubject() {}

    public resourceSubject(String subjectTitle, String subjectCode, String subjectSem, String subjectDept) {
        this.subjectTitle = subjectTitle;
        this.subjectCode = subjectCode;
        this.subjectSem = subjectSem;
        this.subjectDept = subjectDept;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectSem() {
        return subjectSem;
    }

    public void setSubjectSem(String subjectSem) {
        this.subjectSem = subjectSem;
    }

    public String getSubjectDept() {
        return subjectDept;
    }

    public void setSubjectDept(String subjectDept) {
        this.subjectDept = subjectDept;
    }
}
