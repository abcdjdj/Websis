package com.manipal.websis.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.manipal.websis.R;
import com.manipal.websis.model.Mark;

import java.util.ArrayList;


public class MarksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Mark> list;

    public MarksAdapter(Context context, ArrayList<Mark> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MarksViewHolder(LayoutInflater.from(context).inflate(R.layout.marks_card, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MarksViewHolder) holder).subjectName.setText(getShortName(list.get(position).getSubject()));
        ((MarksViewHolder) holder).subjectCode.setText(list.get(position).getSubjectCode().toUpperCase());
        ((MarksViewHolder) holder).sessionalOne.setText("" + list.get(position).getMark1());
        ((MarksViewHolder) holder).sessionalTwo.setText("" + list.get(position).getMark2());
        ((MarksViewHolder) holder).assignments.setText("" + list.get(position).getMark3());
        ((MarksViewHolder) holder).total.setText(getTotalMarks(list.get(position)).trim());
        ((MarksViewHolder) holder).totalOutOf.setText("out of " + getTotalOutOf(list.get(position)));
    }

    private String getShortName(String subject) {
        if (subject.length() < 30)
            return subject;
        else {
            return subject.substring(0, 30) + "...";
        }
    }

    private String getTotalOutOf(Mark mark) {
        int tot = 0;
        if (!mark.getMark1().equals("--"))
            tot += 15;
        if (!mark.getMark2().equals("--"))
            tot += 15;
        if (!mark.getMark3().equals("--"))
            tot += 20;
        return String.valueOf(tot).trim();
    }

    private String getTotalMarks(Mark mark) {
        double tot = 0;
        if (!mark.getMark1().equals("--"))
            tot += Double.valueOf(mark.getMark1());
        if (!mark.getMark2().equals("--"))
            tot += Double.valueOf(mark.getMark2());
        if (!mark.getMark3().equals("--"))
            tot += Double.valueOf(mark.getMark3());
        return String.valueOf(tot).trim();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class MarksViewHolder extends RecyclerView.ViewHolder {

        TextView subjectName, subjectCode, sessionalOne, sessionalTwo, assignments, total, totalOutOf;

        MarksViewHolder(View itemView) {
            super(itemView);
            subjectName = (TextView) itemView.findViewById(R.id.marksSubjectName);
            subjectCode = (TextView) itemView.findViewById(R.id.marksSubjectCode);
            sessionalOne = (TextView) itemView.findViewById(R.id.sessionalOneValue);
            sessionalTwo = (TextView) itemView.findViewById(R.id.sessionalTwoValue);
            assignments = (TextView) itemView.findViewById(R.id.assignmentsValue);
            total = (TextView) itemView.findViewById(R.id.subjectTotal);
            totalOutOf = (TextView) itemView.findViewById(R.id.subjectMarksOutOf);
        }
    }
}
