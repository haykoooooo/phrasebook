package com.tutorialsbuzz.phrasebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HoroscopeActivity extends AppCompatActivity {

    ProgressDialog PD;
    TextView textViewHeaderHoroscope, textViewContentHoroscope, textViewDateHoroscope;
    ImageView imageViewHoroscope;

    String zodiac, zodiacSign, content, date, type;
    String url = "http://" + StateHost.URL + "/read_horoscope.php";
    public static final String DATE = "date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horoscope);
        PD = new ProgressDialog(this);
        PD.setMessage("Բեռնում...");
        PD.setCancelable(false);
        Intent intent = getIntent();
        zodiac = intent.getStringExtra("zodiac");
        type = intent.getStringExtra("type");
        textViewHeaderHoroscope = (TextView) findViewById(R.id.textViewHeaderHoroscope);
        textViewContentHoroscope = (TextView) findViewById(R.id.textViewContentHoroscope);
        textViewDateHoroscope = (TextView) findViewById(R.id.textViewDateHoroscope);
        imageViewHoroscope = (ImageView) findViewById(R.id.imageViewHoroscope);
        textViewHeaderHoroscope.setText(zodiac.toUpperCase());
        switch (zodiac.toLowerCase()) {
            case "խոյ":
                zodiacSign = "xoy";
                imageViewHoroscope.setImageDrawable(getResources().getDrawable(R.drawable.xoy));
                break;
            case "ցուլ":
                zodiacSign = "cul";
                imageViewHoroscope.setImageDrawable(getResources().getDrawable(R.drawable.cul));
                break;
            case "երկվորյակներ":
                zodiacSign = "erkvoryakner";
                imageViewHoroscope.setImageDrawable(getResources().getDrawable(R.drawable.erk));
                break;
            case "խեցգետին":
                zodiacSign = "xecgetin";
                imageViewHoroscope.setImageDrawable(getResources().getDrawable(R.drawable.xec));
                break;
            case "առյուծ":
                zodiacSign = "aryuc";
                imageViewHoroscope.setImageDrawable(getResources().getDrawable(R.drawable.ary));
                break;
            case "կույս":
                zodiacSign = "kuys";
                imageViewHoroscope.setImageDrawable(getResources().getDrawable(R.drawable.kuy));
                break;
            case "կշեռք":
                zodiacSign = "ksherq";
                imageViewHoroscope.setImageDrawable(getResources().getDrawable(R.drawable.ksh));
                break;
            case "կարիճ":
                zodiacSign = "karich";
                imageViewHoroscope.setImageDrawable(getResources().getDrawable(R.drawable.kar));
                break;
            case "աղեղնավոր":
                zodiacSign = "agheghnavor";
                imageViewHoroscope.setImageDrawable(getResources().getDrawable(R.drawable.agh));
                break;
            case "այծեղջյուր":
                zodiacSign = "ayceghjyur";
                imageViewHoroscope.setImageDrawable(getResources().getDrawable(R.drawable.ayc));
                break;
            case "ջրհոս":
                zodiacSign = "jrhos";
                imageViewHoroscope.setImageDrawable(getResources().getDrawable(R.drawable.jrh));
                break;
            case "ձկներ":
                zodiacSign = "dzkner";
                imageViewHoroscope.setImageDrawable(getResources().getDrawable(R.drawable.dzk));
                break;
        }

        PD.show();
        JsonObjectRequest jreq0 = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = response.getInt("success");
                            if (success == 1) {
                                JSONArray ja = response.getJSONArray("aforizmner");
                                for (int i = ja.length() - 1; i >= 0; i--) {
                                    JSONObject jobj = ja.getJSONObject(i);
                                    date = jobj.getString(DATE);
                                    content = jobj.getString(zodiacSign);
                                    if (type.equals("general")) {
                                        if (date.length() > 4) {
                                            i = -1;
                                            PD.dismiss();
                                        }
                                    }
                                    if (type.equals("year")) {
                                        if (date.length() == 4) {
                                            i = -1;
                                            PD.dismiss();
                                        }
                                    }
                                } // for loop ends
                                textViewContentHoroscope.setText(content);
                                textViewDateHoroscope.setText(date);
                            } // if ends
                        } catch (JSONException e) {
                            PD.dismiss();
                            Toast.makeText(getApplicationContext(), "Սերվերի խնդիր է առկա: Խնդրում ենք փորձել մի փոքր ուշ:", Toast.LENGTH_LONG).show();
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
        MyApplication.getInstance().addToReqQueue(jreq0);
    }
}