package com.manipal.websis.service;

import android.Manifest;
import android.app.job.JobParameters;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.manipal.websis.Constants.CACHE_FILE;
import static com.manipal.websis.Constants.DATE_OF_BIRTH;
import static com.manipal.websis.Constants.LOGIN_PREFS;
import static com.manipal.websis.Constants.REG_NO;
import static com.manipal.websis.Constants.URL;

public class JobService extends android.app.job.JobService {

    static int count = 0;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        Log.d("Job Service", "onStartJob " + count);
        if (count == 0)
            return true;
        final SharedPreferences prefs = getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Job Service", "onResponse");
                try {
                    writeFileToCache(response);
                    jobFinished(jobParameters, true);
                } catch (Exception e) {
                    e.printStackTrace();
                    jobFinished(jobParameters, true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley error job", error.toString());
                jobFinished(jobParameters, true);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("regno", prefs.getString(REG_NO, ""));
                params.put("bdate", prefs.getString(DATE_OF_BIRTH, ""));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(12000, 0, 0f));
        queue.add(request);
        return true;
    }

    private void writeFileToCache(String response) throws IOException {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            return;
        }
        File file = new File(getExternalCacheDir() + CACHE_FILE);
        FileOutputStream fout = new FileOutputStream(file);
        Log.d("Writing to cache job", response);
        fout.write(response.getBytes());
        fout.close();
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d("Job Service", "onStopJob");
        return false;
    }
}
