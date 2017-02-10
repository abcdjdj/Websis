package com.manipal.websis.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.NotificationCompat;
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
import com.manipal.websis.R;
import com.manipal.websis.activity.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.manipal.websis.Constants.CACHE_FILE;
import static com.manipal.websis.Constants.DATE_OF_BIRTH;
import static com.manipal.websis.Constants.LOGIN_PREFS;
import static com.manipal.websis.Constants.REG_NO;
import static com.manipal.websis.Constants.URL;

public class RefreshDataBroadcast extends BroadcastReceiver {

    private RequestQueue queue;
    private SharedPreferences prefs;
    static int i = 0;

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
                Log.d("Volley response service", response);
                try {
                    checkForUpdate(response, context);
                    writeFileToCache(response, context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley error service", error.toString());
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
        request.setRetryPolicy(new DefaultRetryPolicy(25000, 1, 0f));
        queue.add(request);
    }

    private void checkForUpdate(String response, Context context) {
        try {
            String current = readDataFromCache(context);
            String msg = "";
            if (!current.equals(response)) {
                Log.d("75%", "Attendance has been updated");
                msg = "An update was found!";
            } else {
                Log.d("75%", "Attendance not updated");
                msg = "No updates found!";
            }
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);
            Notification n = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_group_black_24dp)
                    .setContentTitle("Websis data has been refreshed")
                    .setContentText(msg)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pIntent)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(msg)).build();
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(i++, n);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readDataFromCache(Context context) throws IOException {
        File file = new File(context.getExternalCacheDir() + CACHE_FILE);
        FileInputStream fin = new FileInputStream(file);
        byte[] bytes = new byte[fin.available()];
        int n = fin.read(bytes);
        if (n <= 0)
            return null;
        fin.close();
        return new String(bytes);
    }

}
