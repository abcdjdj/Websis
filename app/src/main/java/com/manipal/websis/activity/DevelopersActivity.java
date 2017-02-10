package com.manipal.websis.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.manipal.websis.R;

public class DevelopersActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);
        toolbar = (Toolbar) findViewById(R.id.dev_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.dev_scrollview);
        toolbar.setTitle("Developers");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
