package com.manipal.websis.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.manipal.websis.R;
import com.manipal.websis.model.Attendance;

import java.util.ArrayList;

import at.grabner.circleprogress.CircleProgressView;


public class AttendanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Attendance> list;

    public AttendanceAdapter(Context context, ArrayList<Attendance> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new AttendanceViewHolder(LayoutInflater.from(context).inflate(R.layout.attendance_card, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((AttendanceViewHolder) holder).classesBunked.setText(getAttendance(list.get(position).getClassesAbsent()));
        ((AttendanceViewHolder) holder).classesAttended.setText(getAttendance(list.get(position).getClassesAttended()));
        ((AttendanceViewHolder) holder).classesTaken.setText(getAttendance(list.get(position).getClassesTaken()));
        ((AttendanceViewHolder) holder).subjectName.setText(getShortName(list.get(position).getSubject()));
        ((AttendanceViewHolder) holder).subjectCode.setText("(" + list.get(position).getSubjectCode() + ")");
        ((AttendanceViewHolder) holder).lastUpdated.setText("" + list.get(position).getLastUpdated());
        ((AttendanceViewHolder) holder).progressView.setSeekModeEnabled(false);
        //TODO Change color for less than 75%
        ((AttendanceViewHolder) holder).progressView.setValueAnimated((float) list.get(position).getPercentage());
    }

    private String getAttendance(int classes) {
        String tmp = String.valueOf(classes);
        if (tmp.length() == 1)
            return "0" + tmp;
        else
            return tmp;
    }

    private String getShortName(String subject) {
        if (subject.length() < 30)
            return subject;
        else {
            return subject.substring(0, 30) + "...";
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class AttendanceViewHolder extends RecyclerView.ViewHolder {

        TextView classesAttended, classesTaken, classesBunked, lastUpdated, subjectName, subjectCode;
        CircleProgressView progressView;

        AttendanceViewHolder(View itemView) {
            super(itemView);
            classesAttended = (TextView) itemView.findViewById(R.id.classesAttendedValue);
            classesTaken = (TextView) itemView.findViewById(R.id.classesTakenValue);
            classesBunked = (TextView) itemView.findViewById(R.id.classesBunkedValue);
            lastUpdated = (TextView) itemView.findViewById(R.id.attendanceUpdatedAt);
            subjectName = (TextView) itemView.findViewById(R.id.attendanceSubjectName);
            subjectCode = (TextView) itemView.findViewById(R.id.attendanceSubjectCode);
            progressView = (CircleProgressView) itemView.findViewById(R.id.subjectPercentage);
            progressView.setTextTypeface(Typeface.DEFAULT);
        }
    }
}
