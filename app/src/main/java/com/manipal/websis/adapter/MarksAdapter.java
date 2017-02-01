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

        if (list.size() == 0)
            return new EmptyViewHolder(LayoutInflater.from(context).inflate(R.layout.empty_list, parent, false));
        else
            return new MarksViewHolder(LayoutInflater.from(context).inflate(R.layout.marks_card, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MarksViewHolder) {
            ((MarksViewHolder) holder).subjectName.setText(list.get(position).getSubject());
            ((MarksViewHolder) holder).subjectCode.setText("(" + list.get(position).getSubjectCode().toUpperCase() + ")");
            ((MarksViewHolder) holder).sessionalOne.setText(getMarks(list.get(position).getMark1()));
            ((MarksViewHolder) holder).sessionalTwo.setText(getMarks(list.get(position).getMark2()));
            ((MarksViewHolder) holder).assignments.setText(getMarks(list.get(position).getMark3()));
            ((MarksViewHolder) holder).total.setText(getTotalMarks(list.get(position)).trim());
            ((MarksViewHolder) holder).totalOutOf.setText("out of " + getTotalOutOf(list.get(position)));
        } else if (holder instanceof EmptyViewHolder) {
            ((EmptyViewHolder) holder).emptyText.setText("No data available for marks!");
        }
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

    private String getMarks(String mark) {
        int len = mark.length(), c = 0;
        for (int i = 0; i < len; i++) {
            if (mark.charAt(i) == '.')
                break;
            c++;
        }
        if (c == 1)
            return "0" + mark;
        else
            return mark;
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
        if (list.size() == 0)
            return 1;
        else
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
