package com.manipal.websis.model;

import java.util.ArrayList;

/**
 * Created by Sudarshan Sunder on 11/20/2016.
 */

public class Semester {

    private int semester;
    private ArrayList<Grade> grades;

    public Semester(int semester, ArrayList<Grade> grades) {
        this.semester = semester;
        this.grades = grades;
    }

    public int getSemester() {
        return semester;
    }

    public ArrayList<Grade> getGrades() {
        return grades;
    }
}
