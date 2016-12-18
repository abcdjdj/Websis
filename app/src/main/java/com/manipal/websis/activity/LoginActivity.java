package com.manipal.websis.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.manipal.websis.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.manipal.websis.Constants.AUTH;
import static com.manipal.websis.Constants.CACHE;
import static com.manipal.websis.Constants.DATE_OF_BIRTH;
import static com.manipal.websis.Constants.ERROR;
import static com.manipal.websis.Constants.LOGIN_PREFS;
import static com.manipal.websis.Constants.REG_NO;
import static com.manipal.websis.Constants.SHOULD_GET;
import static com.manipal.websis.Constants.URL;
import static com.manipal.websis.Constants.USER_DATA;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout registrationNo, dateOfBirth;
    private FloatingActionButton loginButton;
    private SharedPreferences prefs;
    private RequestQueue queue;
    private ProgressDialog dialog;
    private View mainView;

    private boolean isValidDateOfBirth(String dateOfBirth) {
        return (dateOfBirth.length() == 10);
    }

    private boolean isValidLoginInfo() {

        if (registrationNo.getEditText().getText().toString().toLowerCase().equals("harambe"))
            return true;
        if (registrationNo.getEditText().getText().toString().isEmpty() || dateOfBirth.getEditText().getText().toString().isEmpty())
            return false;
        boolean regError = (registrationNo.getError() != null);
        boolean dobError = (dateOfBirth.getError() != null);
        return (!regError && !dobError);
    }

    private void showPermissionDialog(final String[] permissions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Storage permission")
                .setMessage("We need this permission so that you can view your attendance and shit offline!")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(LoginActivity.this, permissions, 69);
                    }
                });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 69) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(mainView, "Permissions granted, yayy!", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(mainView, "Lol, too bad. Reload every time on ION :)).", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void checkForPermission() {
        int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        ArrayList<String> permissions = new ArrayList<>();
        if (read == PackageManager.PERMISSION_DENIED)
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (write == PackageManager.PERMISSION_DENIED)
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissions.size() > 0) {
            String[] p = new String[permissions.size()];
            int i = 0;
            for (String string : permissions) {
                p[i++] = string;
            }
            showPermissionDialog(p);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        checkForPermission();
        prefs = getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
        if (prefs.getBoolean(AUTH, false)) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra(SHOULD_GET, true);
            intent.putExtra(ERROR, false);
            startActivity(intent);
            finish();
        }
        queue = Volley.newRequestQueue(this);
        mainView = findViewById(R.id.activity_login);
        registrationNo = (TextInputLayout) findViewById(R.id.registration_number);
        dateOfBirth = (TextInputLayout) findViewById(R.id.date_of_birth);
        registrationNo.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 9 || charSequence.toString().toLowerCase().trim().equals("harambe")) {
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
                    dialog.setCancelable(false);
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
        final String regno = prefs.getString(REG_NO, "");
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
                        prefs.edit().putBoolean(AUTH, true)
                                .putString(REG_NO, registrationNo.getEditText().getText().toString())
                                .putString(DATE_OF_BIRTH, dateOfBirth.getEditText().getText().toString())
                                .apply();
                        intent.putExtra(ERROR, false);
                        intent.putExtra(SHOULD_GET, false);
                        intent.putExtra(USER_DATA, response);
                        intent.putExtra(CACHE, true);
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
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Snackbar.make(view, "Please check your connection", Snackbar.LENGTH_SHORT).show();
                } else
                    Snackbar.make(view, "An error occurred. Please try again", Snackbar.LENGTH_SHORT).show();
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
        request.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 0f));
        request.setShouldCache(false);
        queue.add(request);
    }
}
