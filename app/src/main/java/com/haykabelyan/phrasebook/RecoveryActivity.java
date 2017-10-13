package com.haykabelyan.phrasebook;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RecoveryActivity extends AppCompatActivity {

    EditText editTextDR, editTextMR, editTextYR, editTextLER;
    Button button;
    ProgressDialog PD;
    Drawable sendMail;
    String loginOrEmail, email, username, birthdate, password, fname, surname, changePassword, changeDate;

    String url = "http://" + StateHost.URL + "/read_allusers.php";
    String url2 = "http://" + StateHost.URL + "/recovery_request.php";

    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String BIRTHDATE = "birthdate";
    public static final String FNAME = "fname";
    public static final String SURNAME = "surname";

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);
        editTextDR = (EditText) findViewById(R.id.editTextDR);
        editTextMR = (EditText) findViewById(R.id.editTextMR);
        editTextYR = (EditText) findViewById(R.id.editTextYR);
        editTextLER = (EditText) findViewById(R.id.editTextLER);
        button = (Button) findViewById(R.id.buttonRR);
        button.setBackground(getResources().getDrawable(R.drawable.longshape));
        button.setTextColor(Color.WHITE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextLER.getText().length() <= 3 || editTextDR.length() != 2
                        || editTextMR.length() != 2 || editTextYR.length() != 4)
                    Toast.makeText(getApplicationContext(), "Սխալ տվյալներ:", Toast.LENGTH_SHORT).show();
                else
                    checkData();
            }
        });

        PD = new ProgressDialog(this);
        PD.setMessage("Բեռնում...");
        PD.setCancelable(false);
    }

    private void checkData() {
        loginOrEmail = editTextLER.getText().toString();
        PD.show();
        sendMail = getResources().getDrawable(R.drawable.sendmail);

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
                                    email = jobj.getString(EMAIL);
                                    username = jobj.getString(USERNAME);
                                    birthdate = jobj.getString(BIRTHDATE);
                                    fname = jobj.getString(FNAME);
                                    surname = jobj.getString(SURNAME);
                                    if ((email.equalsIgnoreCase(editTextLER.getText().toString()) ||
                                            username.equalsIgnoreCase(editTextLER.getText().toString()))
                                            && birthdate.equals(editTextYR.getText().toString() + "." +
                                            editTextMR.getText().toString() + "." + editTextDR.getText().toString())) {
                                        count = 1;
                                        i = ja.length();
                                        char[] parol = new char[8];
                                        for (int j = 0; j < 8; j++) {
                                            Double def = Math.random();
                                            if (def < 0.333333)
                                                parol[j] = (char) (48 + (int) (Math.random() * 10));
                                            if (def >= 0.333333 && def <= 0.666666)
                                                parol[j] = (char) (97 + (int) (Math.random() * 26));
                                            if (def > 0.666666)
                                                parol[j] = (char) (65 + (int) (Math.random() * 26));
                                        }
                                        password = new String(parol);
                                        changePassword();
                                    }
                                } // for loop ends
                                if (count == 0) {
                                    PD.dismiss();
                                    if (loginOrEmail.contains("@"))
                                        Toast.makeText(getApplicationContext(), "Սխալ էլ. հասցե կամ ծննդյան ամսաթիվ:", Toast.LENGTH_LONG).show();
                                    else
                                        Toast.makeText(getApplicationContext(), "Սխալ մուտքանուն կամ ծննդյան ամսաթիվ:", Toast.LENGTH_LONG).show();
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

    private void changePassword() {
        changePassword = "";
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
        if (Integer.parseInt(day) < 10)
            day = "0" + day;
        if (Integer.parseInt(month) < 10)
            month = "0" + month;
        if (Integer.parseInt(second) < 10)
            second = "0" + second;
        changeDate = year + "." + month + "." + day + ", " + hour + ":" + minute + ":" + second;
        changePassword += "Password recovered on " + changeDate + ". New password " + password + "\n";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        sendEmail();
                        changePassword += "Password recovered on " + changeDate + ". New password " + password + "\n";
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(getApplicationContext(),
                        "Սերվերի կամ կապի խնդիր:", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                params.put("changepassword", changePassword);

                return params;
            }
        };

        MyApplication.getInstance().addToReqQueue(postRequest);
    }

    public void sendEmail() {
        new MailSendTask().execute();
    }

    class MailSendTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PD.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            MailSender sender = new MailSender("myEmail", "myPassword");
            try {
                sender.sendMail("Նոր գաղտնաբառ", "Ողջույն, " + fname + " " + surname +
                                "\nՁեր մուտքանունն է՝ " + username + "\nՁեր նոր գաղտնաբառն է՝ " + password +
                                "\nԽնդրում ենք Ձեր իսկ ապահովության համար այն փոխել «Իմ հաշիվը» բաժնում, երբ մուտք գործեք: " +
                                "\n  Շնորհակալություն:",
                        "phrasebook_mail@mail.ru", email);
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Սերվերի կամ կապի խնդիր:", Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AlertDialog.Builder builder = new AlertDialog.Builder(RecoveryActivity.this);
            builder.setTitle("Վերջ")
                    .setMessage(" Ձեր էլ. հասցեին ուղարկվել է Ձեր մուտքանունն ու նոր գաղտնաբառը:")
                    .setIcon(sendMail)
                    .setCancelable(false)
                    .setNegativeButton("Լավ, հոգաչափ շնորհակալ եմ...",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}