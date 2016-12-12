package com.manipal.websis.model;


public class Attendance {

    private String subject, lastUpdated, subjectCode;
    private int classesTaken, classesAttended, classesAbsent;
    private int percentage;

    public Attendance(String subject, String subjectCode, String lastUpdated, int classesTaken, int classesAttended, int percentage) {
        this.subject = subject;
        this.subjectCode = subjectCode.toUpperCase();
        this.lastUpdated = lastUpdated;
        this.classesTaken = classesTaken;
        this.classesAttended = classesAttended;
        this.classesAbsent = this.classesTaken - this.classesAttended;
        this.percentage = percentage;
    }

    public String getSubject() {
        return subject;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public int getPercentage() {
        return percentage;
    }

    public int getClassesTaken() {
        return classesTaken;
    }

    public int getClassesAttended() {
        return classesAttended;
    }

    public int getClassesAbsent() {
        return classesAbsent;
    }

    public String getSubjectCode() {
        return subjectCode;
    }
}
