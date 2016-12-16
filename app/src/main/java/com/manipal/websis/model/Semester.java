package com.manipal.websis.model;

import java.util.ArrayList;

/**
 * Created by Sudarshan Sunder on 11/20/2016.
 */

public class Semester {

    private int semester;
    private ArrayList<Grade> grades;
    private float gpa;
    private int credits;

    public Semester(int semester, float gpa, ArrayList<Grade> grades) {
        this.semester = semester;
        this.grades = grades;
        this.gpa = gpa;
        this.credits = 0;
    }

    public int getSemester() {
        return semester;
    }

    public ArrayList<Grade> getGrades() {
        return grades;
    }

    public float getGpa() {
        return gpa;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}
