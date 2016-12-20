package com.manipal.websis.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.manipal.websis.R;

/**
 * Created by Sudarshan Sunder on 12/20/2016.
 */

public class EmptyViewHolder extends RecyclerView.ViewHolder {

    TextView emptyText;

    EmptyViewHolder(View itemView) {
        super(itemView);
        emptyText = (TextView) itemView.findViewById(R.id.empty_text);
    }
}
