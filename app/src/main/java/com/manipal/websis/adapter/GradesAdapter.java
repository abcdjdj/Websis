package com.manipal.websis.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.manipal.websis.R;
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
            ((SemesterViewHolder) holder).semesterNumber.setText("Semester " + list.get(position - 1).getSemester());
            ((SemesterViewHolder) holder).gradePoint.setText("" + list.get(position - 1).getGpa());
        } else if (holder instanceof StatsViewHolder) {
            //Do nothing for now
            Log.d("Show stats", "Nothing yet");
        }
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

    private class SemesterViewHolder extends RecyclerView.ViewHolder {

        TextView gradePoint, semesterNumber;
        Button viewGrades;

        SemesterViewHolder(final View itemView) {
            super(itemView);
            gradePoint = (TextView) itemView.findViewById(R.id.gradePoint);
            semesterNumber = (TextView) itemView.findViewById(R.id.semesterNumber);
            viewGrades = (Button) itemView.findViewById(R.id.viewAllGrades);
            viewGrades.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(itemView, "Not implemented yet, sorry.", Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    private class StatsViewHolder extends RecyclerView.ViewHolder {

        StatsViewHolder(View itemView) {
            super(itemView);
        }
    }

}
