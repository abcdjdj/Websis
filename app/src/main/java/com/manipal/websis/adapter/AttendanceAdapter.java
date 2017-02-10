package com.manipal.websis.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.manipal.websis.R;
import com.manipal.websis.RandomUtils;
import com.manipal.websis.model.Attendance;

import java.util.ArrayList;

import at.grabner.circleprogress.CircleProgressView;


public class AttendanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Attendance> list;
    private int num;

    public AttendanceAdapter(Context context, ArrayList<Attendance> list, int num) {
        this.context = context;
        this.list = list;
        this.num = num;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (list.size() == 0)
            return new EmptyViewHolder(LayoutInflater.from(context).inflate(R.layout.empty_list, parent, false));
        else {
            if (viewType == 1)
                return new AttendanceViewHolder(LayoutInflater.from(context).inflate(R.layout.attendance_card, parent, false));
            else
                return new StatsViewHolder(LayoutInflater.from(context).inflate(R.layout.attendance_stats_card, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (num > 0)
            position--;
        if (holder instanceof AttendanceViewHolder) {
            ((AttendanceViewHolder) holder).classesBunked.setText(getAttendance(list.get(position).getClassesAbsent()));
            ((AttendanceViewHolder) holder).classesAttended.setText(getAttendance(list.get(position).getClassesAttended()));
            ((AttendanceViewHolder) holder).classesTaken.setText(getAttendance(list.get(position).getClassesTaken()));
            String subjectName = getProperSubjectName(list.get(position).getSubject());
            ((AttendanceViewHolder) holder).subjectName.setText(Html.fromHtml(subjectName));
            ((AttendanceViewHolder) holder).lastUpdated.setText("(" + RandomUtils.getAttendanceDate(list.get(position).getLastUpdated()) + ")");
            ((AttendanceViewHolder) holder).progressView.setSeekModeEnabled(false);
            int c = list.get(position).getCredits();
            String credits = c + " credit";
            if (c > 1)
                credits += 's';
            ((AttendanceViewHolder) holder).credits.setText(credits);
            if (list.get(position).getPercentage() >= 75 || list.get(position).getPercentage() == 0) {
                ((AttendanceViewHolder) holder).progressView.setBarColor(Color.parseColor("#3d4a97"));
                ((AttendanceViewHolder) holder).progressView.setFillCircleColor(Color.parseColor("#fcfcfc"));
                ((AttendanceViewHolder) holder).progressView.setInnerContourColor(context.getResources().getColor(R.color.primary));
                ((AttendanceViewHolder) holder).progressView.setOuterContourColor(context.getResources().getColor(R.color.primary));
                ((AttendanceViewHolder) holder).progressView.setRimColor(Color.parseColor("#2c6776a4"));
            } else {
                ((AttendanceViewHolder) holder).progressView.setBarColor(Color.parseColor("#973d40"));
                ((AttendanceViewHolder) holder).progressView.setFillCircleColor(Color.parseColor("#fcfcfc"));
                ((AttendanceViewHolder) holder).progressView.setInnerContourColor(Color.parseColor("#b71c1c"));
                ((AttendanceViewHolder) holder).progressView.setOuterContourColor(Color.parseColor("#b71c1c"));
                ((AttendanceViewHolder) holder).progressView.setRimColor(Color.parseColor("#2c6776a4"));
            }
            ((AttendanceViewHolder) holder).progressView.setValueAnimated((float) list.get(position).getPercentage());
        } else if (holder instanceof EmptyViewHolder) {
            ((EmptyViewHolder) holder).emptyText.setText("No data available for attendance!");
        } else if (holder instanceof StatsViewHolder) {

            String lowSubjects = "You have less than 75% attendance in ";
            int i = 0;
            Log.d("Num is ", "" + num);
            for (Attendance e : list) {
                if (e.getPercentage() < 75 && e.getPercentage() != 0) {
                    if (i == 0)
                        lowSubjects += "<b>" + getShortSubjectName(getProperSubjectName(e.getSubject())) + "</b>";
                    else if (i == num - 1)
                        lowSubjects += ", and " + "<b>" + getShortSubjectName(getProperSubjectName(e.getSubject())) + "</b>";
                    else
                        lowSubjects += ", " + "<b>" + getShortSubjectName(getProperSubjectName(e.getSubject())) + "</b>";
                    i++;
                }
            }
            lowSubjects += ".";
            ((StatsViewHolder) holder).attendanceAlert.setText(Html.fromHtml(lowSubjects));
        }
    }

    private boolean stopWord(String s) {
        return s.toLowerCase().contains("of") || s.toLowerCase().contains("and");
    }

    private String getShortSubjectName(String properSubjectName) {
        String words[] = properSubjectName.split(" ");
        String shortName = "";
        for (String s : words) {
            if (Character.isLetter(s.charAt(0)) && !stopWord(s))
                shortName += s.charAt(0);
        }
        return shortName;
    }

    private String getProperSubjectName(String subject) {

        if (subject.toLowerCase().contains("lab"))
            return subject;
        int flag = 0;
        String brackets = "";
        for (int i = 0; i < subject.length(); i++) {
            if (subject.charAt(i) == '(')
                flag = 1;
            if (flag != 1)
                brackets += subject.charAt(i);
            if (subject.charAt(i) == ')')
                flag = 0;
        }
        String numbers = "";
        for (int i = 0; i < brackets.length(); i++) {
            if (!Character.isDigit(brackets.charAt(i)))
                numbers += brackets.charAt(i);
        }
        String res = numbers.replace("Elective", "").replace("-", "").trim();
        return res;
    }

    private String getAttendance(int classes) {
        String tmp = String.valueOf(classes);
        if (tmp.length() == 1)
            return "0" + tmp;
        else
            return tmp;
    }

    @Override
    public int getItemViewType(int position) {
        if (num == 0)
            return 1;
        if (position == 0)
            return 0;
        else return 1;
    }

    @Override
    public int getItemCount() {

        if (list.size() == 0)
            return 1;
        else if (num > 0)
            return list.size() + 1;
        else
            return list.size();
    }

    private class AttendanceViewHolder extends RecyclerView.ViewHolder {

        TextView classesAttended, classesTaken, classesBunked, lastUpdated, subjectName, credits;
        CircleProgressView progressView;

        AttendanceViewHolder(View itemView) {
            super(itemView);
            classesAttended = (TextView) itemView.findViewById(R.id.classesAttendedValue);
            classesTaken = (TextView) itemView.findViewById(R.id.classesTakenValue);
            classesBunked = (TextView) itemView.findViewById(R.id.classesBunkedValue);
            lastUpdated = (TextView) itemView.findViewById(R.id.attendanceUpdatedAt);
            subjectName = (TextView) itemView.findViewById(R.id.attendanceSubjectName);
            progressView = (CircleProgressView) itemView.findViewById(R.id.subjectPercentage);
            credits = (TextView) itemView.findViewById(R.id.attendanceCredits);
            progressView.setTextTypeface(Typeface.DEFAULT);
        }
    }

    private class StatsViewHolder extends RecyclerView.ViewHolder {

        TextView attendanceAlert;

        StatsViewHolder(View view) {
            super(view);
            attendanceAlert = (TextView) view.findViewById(R.id.attendanceAlertText);
        }
    }
}
