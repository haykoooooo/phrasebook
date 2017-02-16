package com.tutorialsbuzz.phrasebook;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText login, password;
    Button button;
    ProgressDialog PD;
    TextView forgot;

    String url = "http://" + StateHost.URL + "/read_allusers.php";
    String url2 = "http://" + StateHost.URL + "/add_session.php";

    Drawable lockedIcon;
    String usernameField, passwordField, fname, surname, loginDate, sex, address, email, birthDate, telephone, sessions, active;
    int id;

    // JSON Node names
    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String FNAME = "fname";
    public static final String SURNAME = "surname";
    public static final String ADDRESS = "address";
    public static final String TELEPHONE = "telephone";
    public static final String EMAIL = "email";
    public static final String SEX = "sex";
    public static final String BIRTHDATE = "birthdate";
    public static final String ACTIVE = "active";
    public static final String SESSIONS = "sessions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        lockedIcon = getResources().getDrawable(R.drawable.locked);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button = (Button) findViewById(R.id.button);
        login = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);
        forgot = (TextView) findViewById(R.id.textViewForgot);
        login.setTextColor(Color.rgb(6, 36, 216));
        password.setTextColor(Color.rgb(6, 36, 216));
        forgot.setTextColor(Color.rgb(6, 36, 216));
        button.setTextColor(Color.WHITE);
        button.setBackground(getResources().getDrawable(R.drawable.longshape));
        button.setOnClickListener(this);
        PD = new ProgressDialog(this);
        PD.setMessage("Բեռնում...");
        PD.setCancelable(false);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RecoveryActivity.class));
            }
        });
    }

    @Override
    public void onClick(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clickanim));
        usernameField = login.getText().toString();
        passwordField = password.getText().toString();
        if ((usernameField.length() == 0) || (passwordField.length() == 0))
            emptyData();
        else
            loginAttempt();
    }

    private void emptyData() {
        if (login.getText().toString().length() == 0)
            Toast.makeText(getApplicationContext(), "Մուտքագրեք Ձեր մուտքանունը:", Toast.LENGTH_SHORT).show();
        else if (password.getText().toString().length() == 0)
            Toast.makeText(getApplicationContext(), "Մուտքագրեք Ձեր գաղտնաբառը:", Toast.LENGTH_SHORT).show();
        login.setText("");
        password.setText("");
    }

    private void loginAttempt() {
        PD.show();

        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int count = 0;
                            int success = response.getInt("success");
                            if (success == 1) {
                                JSONArray ja = response.getJSONArray("aforizmner");
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jobj = ja.getJSONObject(i);
                                    id = jobj.getInt(ID);
                                    fname = jobj.getString(FNAME);
                                    surname = jobj.getString(SURNAME);
                                    address = jobj.getString(ADDRESS);
                                    telephone = jobj.getString(TELEPHONE);
                                    email = jobj.getString(EMAIL);
                                    birthDate = jobj.getString(BIRTHDATE);
                                    active = jobj.getString(ACTIVE);
                                    sessions = jobj.optString(SESSIONS);
                                    sex = jobj.getString(SEX);
                                    if (jobj.getString(USERNAME).equalsIgnoreCase(usernameField) &&
                                            jobj.getString(PASSWORD).equalsIgnoreCase(passwordField)) {
                                        count = 1;
                                        if (active.equalsIgnoreCase("yes"))
                                            logIn();
                                        else {
                                            PD.dismiss();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                            builder.setTitle("Ներող")
                                                    .setMessage("Ցավոք, Դուք արգելափակված եք")
                                                    .setIcon(lockedIcon)
                                                    .setCancelable(false)
                                                    .setNegativeButton("ՕԿ",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    login.setText("");
                                                                    password.setText("");
                                                                }
                                                            });
                                            AlertDialog alert = builder.create();
                                            alert.show();
                                        }
                                    }
                                } // for loop ends
                                if (count == 0) {
                                    PD.dismiss();
                                    notLogIn();
                                }
                            } // if ends
                        } catch (JSONException e) {
                            PD.dismiss();
                            Toast.makeText(getApplicationContext(), "Սերվերի խնդիր:", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(getApplicationContext(), "Ինտերնետ կապի խնդիր:", Toast.LENGTH_SHORT).show();
            }
        });
        // Adding request to request queue
        MyApplication.getInstance().addToReqQueue(jreq);
    }

    private void notLogIn() {
        Toast.makeText(getApplicationContext(), "Սխալ մուտքանուն կամ գաղտնաբառ:", Toast.LENGTH_SHORT).show();
        login.setText("");
        password.setText("");
    }

    private void logIn() {
        GregorianCalendar calendar = new GregorianCalendar();
        String hour = calendar.getTime().getHours() + "";
        String minute = calendar.getTime().getMinutes() + "";
        String second = calendar.getTime().getSeconds() + "";
        String day = calendar.get(calendar.DAY_OF_MONTH) + "";
        String month = calendar.getTime().getMonth() + 1 + "";
        String year = 1900 + calendar.getTime().getYear() + "";
        if (Integer.parseInt(hour) < 10)
            hour = "0" + hour;
        if (Integer.parseInt(minute) < 10)
            minute = "0" + minute;
        if (Integer.parseInt(second) < 10)
            second = "0" + second;
        if (Integer.parseInt(day) < 10)
            day = "0" + day;
        if (Integer.parseInt(month) < 10)
            month = "0" + month;
        loginDate = year + "." + month + "." + day + ", " + hour + ":" + minute + ":" + second;
        usernameField = login.getText().toString();
        passwordField = password.getText().toString();
        sessions = loginDate + "(login) \n";

        final Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("login", usernameField);
        intent.putExtra("password", passwordField);
        intent.putExtra("fname", fname);
        intent.putExtra("surname", surname);
        intent.putExtra("address", address);
        intent.putExtra("sex", sex);
        intent.putExtra("telephone", telephone);
        intent.putExtra("email", email);
        intent.putExtra("birthdate", birthDate);
        intent.putExtra("sessions", sessions);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PD.dismiss();
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(getApplicationContext(),
                        "Սերվերի խնդիր:", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("sessions", sessions);
                params.put("username", usernameField);
                return params;
            }
        };

        // Adding request to request queue
        MyApplication.getInstance().addToReqQueue(postRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}