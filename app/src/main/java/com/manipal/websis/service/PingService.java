package com.manipal.websis.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.android.volley.RequestQueue;

public class PingService extends IntentService {

    private Context context;
    private RequestQueue queue;

    public PingService(String name, Context context) {
        super(name);
        this.context = context;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //TODO Implement Notifications when websis is updated
    }
}
