package com.manipal.websis.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.manipal.websis.DateUtils;
import com.manipal.websis.R;
import com.manipal.websis.adapter.AttendanceAdapter;
import com.manipal.websis.adapter.GradesAdapter;
import com.manipal.websis.adapter.MarksAdapter;
import com.manipal.websis.model.Attendance;
import com.manipal.websis.model.Grade;
import com.manipal.websis.model.Mark;
import com.manipal.websis.model.Semester;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.support.design.widget.Snackbar.make;
import static com.manipal.websis.Constants.CACHE;
import static com.manipal.websis.Constants.CACHE_FILE;
import static com.manipal.websis.Constants.DATE_OF_BIRTH;
import static com.manipal.websis.Constants.LOGIN_PREFS;
import static com.manipal.websis.Constants.REG_NO;
import static com.manipal.websis.Constants.SHOULD_GET;
import static com.manipal.websis.Constants.URL;
import static com.manipal.websis.Constants.USER_DATA;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private TextView headerName, headerNumber, headerBranch, headerError;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView attendanceRecyclerView, marksRecyclerView, gradesRecyclerView;
    private View loadingView, errorView, mainView;
    private RequestQueue queue;
    private ArrayList<Attendance> attendanceList;
    private ArrayList<Mark> marksList;
    private ArrayList<Semester> gradeList;
    private ProgressBar headerProgress;
    private Date date;
    private AlertDialog.Builder builder;
    private TextView errorText;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);
        prefs = getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
        loadingView = findViewById(R.id.main_loading_include);
        errorView = findViewById(R.id.main_error_include);
        errorText = (TextView) errorView.findViewById(R.id.loading_error_text);
        mainView = findViewById(R.id.main_normal_include);
        attendanceRecyclerView = (RecyclerView) findViewById(R.id.attendance_recycler_view);
        marksRecyclerView = (RecyclerView) findViewById(R.id.marks_recycler_view);
        gradesRecyclerView = (RecyclerView) findViewById(R.id.grades_recycler_view);
        attendanceRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        marksRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        gradesRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        navView = (NavigationView) findViewById(R.id.main_navigation_view);
        View headerView = navView.getHeaderView(0);
        headerName = (TextView) headerView.findViewById(R.id.header_name);
        headerNumber = (TextView) headerView.findViewById(R.id.header_registration_number);
        headerBranch = (TextView) headerView.findViewById(R.id.header_branch);
        headerProgress = (ProgressBar) headerView.findViewById(R.id.headerProgress);
        headerError = (TextView) headerView.findViewById(R.id.headerLoadingError);
        headerName.setTextColor(Color.WHITE);
        headerNumber.setTextColor(Color.WHITE);
        headerBranch.setTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Attendance");
        navView.setItemIconTintList(null);
        navView.setCheckedItem(R.id.menu_attendance);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_logout:
                        logoutUser();
                        break;
                    default:
                        showRecyclerView(item.getItemId());
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.lel, R.string.lel);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.mainSwipeRefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queue.cancelAll(getApplicationContext());
                getInformation();
                refreshLayout.setRefreshing(false);
            }
        });
        if (getIntent().getBooleanExtra(SHOULD_GET, true))
            getInformation();
        else {
            String json = getIntent().getStringExtra(USER_DATA);
            try {
                parseJsonAndPopulateViews(json, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showRecyclerView(int itemId) {
        switch (itemId) {
            case R.id.menu_attendance:
                attendanceRecyclerView.setVisibility(View.VISIBLE);
                marksRecyclerView.setVisibility(View.GONE);
                gradesRecyclerView.setVisibility(View.GONE);
                getSupportActionBar().setTitle("Attendance");
                break;
            case R.id.menu_marks:
                attendanceRecyclerView.setVisibility(View.GONE);
                marksRecyclerView.setVisibility(View.VISIBLE);
                gradesRecyclerView.setVisibility(View.GONE);
                getSupportActionBar().setTitle("Marks");
                break;
            case R.id.menu_gpa:
                attendanceRecyclerView.setVisibility(View.GONE);
                marksRecyclerView.setVisibility(View.GONE);
                gradesRecyclerView.setVisibility(View.VISIBLE);
                getSupportActionBar().setTitle("Grades");
                break;
        }
    }

    private void getInformation() {

        headerName.setVisibility(View.GONE);
        headerNumber.setVisibility(View.GONE);
        headerBranch.setVisibility(View.GONE);
        headerError.setVisibility(View.GONE);
        headerProgress.setVisibility(View.VISIBLE);
        if (snackbar != null) {
            snackbar.dismiss();
        }
        final String regno = prefs.getString(REG_NO, "");
        Log.d("Registration number", regno);
        mainView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.INVISIBLE);
        loadingView.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Volley response", response);
                try {
                    parseJsonAndPopulateViews(response, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley error", error.toString());
                handleVolleyError();
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
    }

    private void writeFileToCache(String response) throws IOException {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            return;
        }
        File file = new File(getExternalCacheDir() + CACHE_FILE);
        FileOutputStream fout = new FileOutputStream(file);
        Log.d("Writing to cache", response);
        fout.write(response.getBytes());
        fout.close();
    }

    private void handleVolleyError() {
        mainView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.INVISIBLE);
        headerError.setVisibility(View.VISIBLE);
        headerName.setVisibility(View.GONE);
        headerNumber.setVisibility(View.GONE);
        headerBranch.setVisibility(View.GONE);
        headerProgress.setVisibility(View.GONE);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Not reachable")
                    .setMessage("Looks like we're having trouble reaching websis. Would you like us to load a cached copy?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            try {
                                readDataFromCache();
                            } catch (IOException e) {
                                e.printStackTrace();
                                if (e instanceof FileNotFoundException) {
                                    errorText.setText("No cached copy exists. Please swipe down to try again!");
                                } else {
                                    errorText.setText("An error occurred while reading cached data. Please swipe down to try again");
                                }
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            make(errorView, "Cannot reach websis", Snackbar.LENGTH_SHORT).show();
                        }
                    });
            builder.show();
        } else {
            make(errorView, "Cannot reach websis", Snackbar.LENGTH_SHORT).show();
        }

    }

    private void readDataFromCache() throws IOException {
        File file = new File(getExternalCacheDir() + CACHE_FILE);
        long time = file.lastModified();
        date = new Date(time);
        Log.d("Last modified of cache", SimpleDateFormat.getInstance().format(date));
        Log.d("File path of cache", file.getAbsolutePath());
        FileInputStream fin = new FileInputStream(file);
        byte[] bytes = new byte[fin.available()];
        int n = fin.read(bytes);
        if (n > 0)
            try {
                Log.d("Data from cache is ", new String(bytes));
                parseJsonAndPopulateViews(new String(bytes), true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        fin.close();
    }

    private void parseJsonAndPopulateViews(String response, boolean fromCache) throws JSONException, IOException {

        if (!fromCache || getIntent().getBooleanExtra(CACHE, false)) {
            writeFileToCache(response);
        }
        JSONObject json = new JSONObject(response);
        if (json.has("Status") && !json.getBoolean("Status")) {
            mainView.setVisibility(View.INVISIBLE);
            errorView.setVisibility(View.VISIBLE);
            loadingView.setVisibility(View.INVISIBLE);
            return;
        }
        JSONArray attendance = json.getJSONArray("Attendance");
        attendanceList = new ArrayList<>();
        marksList = new ArrayList<>();
        gradeList = new ArrayList<>();
        for (int i = 0; i < attendance.length(); i++) {
            JSONObject sub = attendance.getJSONObject(i);
            if (!sub.getString("Name").contains("Lab"))
                attendanceList.add(new Attendance(sub.getString("Name"), sub.getString("Course Code"), sub.getString("Updated"), Integer.valueOf(sub.getString("Classes")), Integer.valueOf(sub.getString("Attended")), Integer.valueOf(sub.getString("%"))));
        }
        JSONArray marks1 = json.getJSONObject("Scores").getJSONArray("Internal Assesment 1");
        JSONArray marks2 = json.getJSONObject("Scores").getJSONArray("Internal Assesment 2");
        JSONArray marks3 = json.getJSONObject("Scores").getJSONArray("Internal Assesment 3");
        for (int i = 0; i < marks1.length(); i++) {
            marksList.add(new Mark(marks1.getJSONObject(i).getString("Course"), marks1.getJSONObject(i).getString("Course Code"), marks1.getJSONObject(i).getString("Marks"), marks2.getJSONObject(i).getString("Marks"), marks3.getJSONObject(i).getString("Marks")));
        }
        JSONObject user = json.getJSONObject("User Data");
        String branch = user.getString("Branch");
        branch = branch.substring(0, branch.length() - 11);
        JSONObject grades = json.getJSONObject("GPA");
        int k = 1;
        while (grades.has(branch + " Semester " + k)) {
            gradeList.add(new Semester(k, Float.valueOf(grades.getString(branch + " Semester " + k)), getTemporaryGradeList()));
            k++;
        }
        attendanceRecyclerView.setAdapter(new AttendanceAdapter(MainActivity.this, attendanceList));
        marksRecyclerView.setAdapter(new MarksAdapter(MainActivity.this, marksList));
        gradesRecyclerView.setAdapter(new GradesAdapter(MainActivity.this, gradeList));
        headerName.setText(user.getString("Name"));
        headerNumber.setText(user.getString("Registration Number"));
        headerBranch.setText(branch);
        headerName.setVisibility(View.VISIBLE);
        headerNumber.setVisibility(View.VISIBLE);
        headerBranch.setVisibility(View.VISIBLE);
        headerProgress.setVisibility(View.GONE);
        headerError.setVisibility(View.GONE);
        mainView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.INVISIBLE);
        loadingView.setVisibility(View.INVISIBLE);
        if (fromCache) {
            String dateFormat = SimpleDateFormat.getInstance().format(date);
            snackbar = Snackbar.make(mainView, "Last updated on " + DateUtils.getProperDateMessage(dateFormat), 10000);
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            }).setDuration(10000).setActionTextColor(Color.YELLOW).show();
        }
    }

    private ArrayList<Grade> getTemporaryGradeList() {
        ArrayList<Grade> sub = new ArrayList<>();
        sub.add(new Grade("Computer Architecture", "A", 3));
        sub.add(new Grade("Operating Systems", "A+", 4));
        sub.add(new Grade("Computer Networks", "A", 4));
        sub.add(new Grade("Software Engineering", "C", 4));
        sub.add(new Grade("Elective I", "A", 3));
        sub.add(new Grade("Operating Systems Lab", "A", 1));
        sub.add(new Grade("Algorithms Lab", "C", 1));
        sub.add(new Grade("Computer Networks Lab", "A+", 2));
        return sub;
    }

    private void logoutUser() {
        queue.cancelAll(MainActivity.this);
        prefs.edit().clear().apply();
        File file = new File(getExternalCacheDir() + CACHE_FILE);
        boolean del = file.delete();
        Log.d("File delete", "" + del);
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}
