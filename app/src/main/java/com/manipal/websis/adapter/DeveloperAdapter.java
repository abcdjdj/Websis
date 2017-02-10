package com.manipal.websis.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.manipal.websis.R;

/**
 * Created by Sudarshan Sunder on 2/10/2017.
 */

public class DeveloperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    public DeveloperAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PersonViewHolder(LayoutInflater.from(context).inflate(R.layout.dev_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    private class PersonViewHolder extends RecyclerView.ViewHolder {


        public PersonViewHolder(View itemView) {
            super(itemView);
        }
    }

}
