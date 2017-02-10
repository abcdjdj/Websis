package com.manipal.websis;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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

public class RefreshDataBroadcast extends BroadcastReceiver {

    private RequestQueue queue;
    private SharedPreferences prefs;

    @Override
    public void onReceive(Context context, Intent intent) {
        queue = Volley.newRequestQueue(context);
        prefs = context.getSharedPreferences(LOGIN_PREFS, Context.MODE_PRIVATE);
        Log.d("RefreshDataBroadcast", "onReceive()");
        getInformationRequest(context);
    }

    private void writeFileToCache(String response, Context context) throws IOException {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            return;
        }
        File file = new File(context.getExternalCacheDir() + CACHE_FILE);
        FileOutputStream fout = new FileOutputStream(file);
        Log.d("Writing to cache", response);
        fout.write(response.getBytes());
        fout.close();
    }

    private void getInformationRequest(final Context context) {
        final String regno = prefs.getString(REG_NO, "");
        Log.d("Registration number", regno);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Volley response", response);
                try {
                    writeFileToCache(response, context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley error", error.toString());
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
        request.setRetryPolicy(new DefaultRetryPolicy(25000, 0, 0f));
        queue.add(request);
    }

}
