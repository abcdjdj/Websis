package com.manipal.websis.model;

public class Mark {

    private String mark1, mark2, mark3;
    private String subject, subjectCode;

    public Mark(String subject, String subjectCode, String mark1, String mark2, String mark3) {
        this.subject = subject;
        this.subjectCode = subjectCode;
        this.mark1 = mark1;
        this.mark2 = mark2;
        this.mark3 = mark3;
    }

    public String getSubject() {
        return subject;
    }

    public String getMark1() {
        return mark1;
    }

    public String getMark2() {
        return mark2;
    }

    public String getMark3() {
        return mark3;
    }

    public String getSubjectCode() {
        return subjectCode;
    }
}
