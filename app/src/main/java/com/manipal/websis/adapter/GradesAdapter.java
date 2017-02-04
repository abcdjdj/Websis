package com.manipal.websis.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.manipal.websis.R;
import com.manipal.websis.model.Grade;
import com.manipal.websis.model.Semester;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

public class GradesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Semester> list;

    public GradesAdapter(Context context, ArrayList<Semester> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (list.size() == 0)
            return new EmptyViewHolder(LayoutInflater.from(context).inflate(R.layout.empty_list, parent, false));
        else {
            switch (viewType) {
                case 1:
                    return new SemesterViewHolder(LayoutInflater.from(context).inflate(R.layout.semester_card, parent, false));
                case 0:
                    return new StatsViewHolder(LayoutInflater.from(context).inflate(R.layout.stats_card, parent, false));
                default:
                    return null;
            }
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SemesterViewHolder) {
            ((SemesterViewHolder) holder).semesterNumber.setText("Semester " + (list.get(list.size() - position).getSemester()));
            String gr = String.valueOf(list.get(list.size() - position).getGpa());
            if (gr.length() == 3)
                gr += 0;
            ((SemesterViewHolder) holder).gradePoint.setText(gr);
            ((SemesterViewHolder) holder).layout.removeAllViewsInLayout();
            for (Grade e : list.get(list.size() - position).getGrades()) {
                View newSubject = LayoutInflater.from(context).inflate(R.layout.subject_grade, null, false);
                TextView subject, grade, credits;
                subject = (TextView) newSubject.findViewById(R.id.gradeSubjectName);
                grade = (TextView) newSubject.findViewById(R.id.gradeSubject);
                credits = (TextView) newSubject.findViewById(R.id.subjectCredits);
                String s = getName(e.getSubject(), 23);
                subject.setText(s);
                grade.setText(getProperGrade(e.getGrade()));
                credits.setText(e.getCredits() + " credit(s)");
                ((SemesterViewHolder) holder).layout.addView(newSubject);
            }
            ((SemesterViewHolder) holder).totalCredits.setText(list.get(list.size() - position).getCredits() + " credit(s)");
        } else if (holder instanceof StatsViewHolder) {
            BigDecimal d = new BigDecimal((double) getCgpa());
            d = d.round(new MathContext(3, RoundingMode.UP));
            ((StatsViewHolder) holder).cgpa.setText(d.toString());
            ((StatsViewHolder) holder).totalCreditsEarned.setText("" + totCred());
            ((StatsViewHolder) holder).highest.setText(getProperGpa("" + getHighestGpa()));
            ((StatsViewHolder) holder).lowest.setText(getProperGpa("" + getLowestGpa()));
        } else if (holder instanceof EmptyViewHolder) {
            ((EmptyViewHolder) holder).emptyText.setText("No data available for grades!");
        }
    }

    private String getProperGpa(String gr) {
        if (gr.length() == 3)
            gr += 0;
        return gr;
    }

    private float getLowestGpa() {
        float min = 11;
        for (Semester s : list) {
            if (s.getGpa() < min)
                min = s.getGpa();
        }
        return min;
    }

    private float getHighestGpa() {
        float max = -1;
        for (Semester s : list) {
            if (s.getGpa() > max)
                max = s.getGpa();
        }
        return max;
    }

    private int totCred() {
        int cred = 0;
        for (Semester s : list) {
            cred += s.getCredits();
        }
        return cred;
    }

    private float getCgpa() {
        float totG = 0, totC = 0;
        for (Semester s : list) {
            float tmp = 0;
            for (Grade g : s.getGrades()) {
                tmp += g.getCredits();
            }
            totG += s.getGpa() * tmp;
            totC += tmp;
        }
        return totG / totC;
    }

    private String getProperGrade(String grade) {
        if (grade.length() == 2)
            return grade;
        else
            return grade + "\u00A0";
    }

    @Override
    public int getItemCount() {
        if (list.size() == 0)
            return 1;
        else
            return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        else
            return 1;
    }

    private String getName(String subject, int value) {
        //21
        if (subject.length() < value) {
            int extra = value - subject.length();
            for (int i = 0; i < extra; i++)
                subject += "\u00A0";
            return subject;
        } else if (subject.length() > value) {
            if (subject.contains("Lab")) {
                return subject.substring(0, value - 4) + "..." + " Lab";
            } else {
                return subject.substring(0, value - 3) + "...";
            }
        } else {
            return subject;
        }
    }

    private class SemesterViewHolder extends RecyclerView.ViewHolder {

        TextView gradePoint, semesterNumber, totalCredits;
        LinearLayout layout;

        SemesterViewHolder(final View itemView) {
            super(itemView);
            gradePoint = (TextView) itemView.findViewById(R.id.gradePoint);
            semesterNumber = (TextView) itemView.findViewById(R.id.semesterNumber);
            totalCredits = (TextView) itemView.findViewById(R.id.totalCredits);
            layout = (LinearLayout) itemView.findViewById(R.id.semesterLayout);
        }
    }

    private class StatsViewHolder extends RecyclerView.ViewHolder {

        TextView totalCreditsEarned, cgpa, highest, lowest;

        StatsViewHolder(View itemView) {
            super(itemView);
            totalCreditsEarned = (TextView) itemView.findViewById(R.id.totalCreditsValue);
            cgpa = (TextView) itemView.findViewById(R.id.cgpaValue);
            highest = (TextView) itemView.findViewById(R.id.highestGpaValue);
            lowest = (TextView) itemView.findViewById(R.id.lowestGpaValue);
        }
    }

}
