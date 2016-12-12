package com.manipal.websis.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.manipal.websis.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.manipal.websis.Constants.ERROR;
import static com.manipal.websis.Constants.SHOULD_GET;
import static com.manipal.websis.Constants.URL;
import static com.manipal.websis.Constants.USER_DATA;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout registrationNo, dateOfBirth;
    private FloatingActionButton loginButton;
    private SharedPreferences prefs;
    private Toolbar toolbar;
    private RequestQueue queue;
    private ProgressDialog dialog;

    private boolean isValidDateOfBirth(String dateOfBirth) {
        return (dateOfBirth.length() == 10);
    }

    private boolean isValidLoginInfo() {

        if (registrationNo.getEditText().getText().toString().isEmpty() || dateOfBirth.getEditText().getText().toString().isEmpty())
            return false;
        boolean regError = (registrationNo.getError() != null);
        boolean dobError = (dateOfBirth.getError() != null);
        return (!regError && !dobError);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
        if (prefs.getBoolean("auth", false)) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra(SHOULD_GET, true);
            intent.putExtra(ERROR, false);
            startActivity(intent);
            finish();
        }
        queue = Volley.newRequestQueue(this);
        toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        registrationNo = (TextInputLayout) findViewById(R.id.registration_number);
        dateOfBirth = (TextInputLayout) findViewById(R.id.date_of_birth);
        registrationNo.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 9 || charSequence.toString().trim().equals("harambe")) {
                    registrationNo.setError(null);
                } else {
                    registrationNo.setError("Invalid registration number format!");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dateOfBirth.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isValidDateOfBirth(charSequence.toString().trim())) {
                    dateOfBirth.setError(null);
                } else {
                    dateOfBirth.setError("Invalid date of birth!");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        loginButton = (FloatingActionButton) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidLoginInfo()) {
                    dialog = new ProgressDialog(LoginActivity.this);
                    dialog.setMessage("Logging in...");
                    dialog.show();
                    if (registrationNo.getEditText().getText().toString().equals("harambe")) {
                        startActivity(new Intent(getApplicationContext(), HarambeActivity.class));
                        finish();
                    } else {
                        getInformation(view);
                    }
                } else {
                    Snackbar.make(view, "Enter valid information", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getInformation(final View view) {

        queue.getCache().clear();

        final String regno = prefs.getString("registration_number", "");
        Log.d("Registration number", regno);
        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Volley response", response);
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.has("status") && !json.getBoolean("status")) {
                        dialog.dismiss();
                        Snackbar.make(view, "Invalid username or password", Snackbar.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        prefs.edit().putBoolean("auth", true)
                                .putString("registration_number", registrationNo.getEditText().getText().toString())
                                .putString("date_of_birth", dateOfBirth.getEditText().getText().toString())
                                .apply();
                        intent.putExtra(ERROR, true);
                        intent.putExtra(SHOULD_GET, false);
                        intent.putExtra(USER_DATA, response);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley error", error.toString());
                dialog.dismiss();
                Snackbar.make(view, "There was an error. Please try again", Snackbar.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("regno", registrationNo.getEditText().getText().toString());
                params.put("bdate", dateOfBirth.getEditText().getText().toString());
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        queue.add(request);
    }
}
