package com.manipal.websis.model;

public class Grade {

    private String subject;
    private GradeValue grade;

    public Grade(String subject, GradeValue grade) {
        this.subject = subject;
        this.grade = grade;
    }

    public String getSubject() {
        return subject;
    }

    public GradeValue getGrade() {
        return grade;
    }

    public enum GradeValue {
        A1, A, B, C, D, E, F, DT
    }
}