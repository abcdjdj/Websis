package com.manipal.websis.model;

public class Grade {

    private String subject;
    private String grade;
    private int credits;

    public Grade(String subject, String grade, int credits) {
        this.subject = subject;
        this.grade = grade;
        this.credits = credits;
    }

    public String getSubject() {
        return subject;
    }

    public String getGrade() {
        return grade;
    }

    public int getCredits() {
        return credits;
    }
}