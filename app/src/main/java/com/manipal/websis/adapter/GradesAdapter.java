package com.manipal.websis.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.manipal.websis.R;
import com.manipal.websis.model.Grade;
import com.manipal.websis.model.Semester;

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
        switch (viewType) {
            case 1:
                return new SemesterViewHolder(LayoutInflater.from(context).inflate(R.layout.semester_card, parent, false));
            case 0:
                return new StatsViewHolder(LayoutInflater.from(context).inflate(R.layout.stats_card, parent, false));
        }
        return null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SemesterViewHolder) {
            ((SemesterViewHolder) holder).semesterNumber.setText("Semester " + list.get(list.size() - position).getSemester());
            ((SemesterViewHolder) holder).gradePoint.setText("" + list.get(list.size() - position).getGpa());
            ((SemesterViewHolder) holder).layout.removeAllViewsInLayout();
            Log.d("Num mark for semester " + (list.size() - position), "" + list.get(list.size() - position).getGrades().size());
            for (Grade e : list.get(list.size() - position).getGrades()) {
                View newSubject = LayoutInflater.from(context).inflate(R.layout.subject_grade, null, false);
                TextView subject, grade, credits;
                subject = (TextView) newSubject.findViewById(R.id.gradeSubjectName);
                grade = (TextView) newSubject.findViewById(R.id.gradeSubject);
                credits = (TextView) newSubject.findViewById(R.id.subjectCredits);
                String s = getName(e.getSubject());
                subject.setText(s);
                grade.setText(getProperGrade(e.getGrade()));
                credits.setText(e.getCredits() + " credit(s)");
                ((SemesterViewHolder) holder).layout.addView(newSubject);
            }
        } else if (holder instanceof StatsViewHolder) {
            //Do nothing for now
        }
    }

    private String getProperGrade(String grade) {
        if (grade.length() == 2)
            return grade;
        else
            return grade + "\u00A0";
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        else
            return 1;
    }

    private String getName(String subject) {
        if (subject.length() < 21) {
            int extra = 21 - subject.length();
            for (int i = 0; i < extra; i++)
                subject += "\u00A0";
            return subject;
        } else if (subject.length() > 21) {
            if (subject.contains("Lab")) {
                return subject.substring(0, 17) + "..." + " Lab";
            } else {
                return subject.substring(0, 18) + "...";
            }
        } else {
            return subject;
        }
    }

    private class SemesterViewHolder extends RecyclerView.ViewHolder {

        TextView gradePoint, semesterNumber;
        LinearLayout layout;

        SemesterViewHolder(final View itemView) {
            super(itemView);
            gradePoint = (TextView) itemView.findViewById(R.id.gradePoint);
            semesterNumber = (TextView) itemView.findViewById(R.id.semesterNumber);
            layout = (LinearLayout) itemView.findViewById(R.id.semesterLayout);
        }
    }

    private class StatsViewHolder extends RecyclerView.ViewHolder {

        StatsViewHolder(View itemView) {
            super(itemView);
        }
    }

}
