package com.tutorialsbuzz.phrasebook;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.util.HashMap;
import java.util.Map;

public class PhrasesActivity extends AppCompatActivity {

    boolean rated;
    String yourRating;
    int ratingsCount;
    int ratingsSum;
    double newRating;
    String author, content, phrases_id, user_id, image, date, views, rating, rating_id;

    Intent selfIntent;
    ProgressDialog PD;
    TextView textViewAuthorPhrases, textViewContentPhrases, textViewDatePhrases, textViewViewsPhrases,
            textViewRatingPhrases, textViewYourRatingPhrases;
    ImageView imageViewPhrases;
    Spinner spinnerRating;

    String url0 = "http://" + StateHost.URL + "/read_allphrases.php";
    String url1 = "http://" + StateHost.URL + "/read_allphrasesratings.php";
    String url2 = "http://" + StateHost.URL + "/add_phrases_views.php";
    String url3 = "http://" + StateHost.URL + "/update_rate_phrases.php";
    String url4 = "http://" + StateHost.URL + "/rate_phrases.php";
    String url5 = "http://" + StateHost.URL + "/unrate_phrases.php";

    // JSON Node names for Phrases_ratings
    public static final String USER_ID = "user_id";
    public static final String PHRASES_ID = "phrases_id";

    // JSON Node names for Phrases
    public static final String ID = "id";
    public static final String AUTHOR = "author";
    public static final String CONTENT = "content";
    public static final String IMAGE = "image";
    public static final String DATE = "date";
    public static final String VIEWS = "views";
    public static final String RATING = "rating";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Drawable[] ratingIcons = new Drawable[5];
        ratingIcons[0] = getResources().getDrawable(R.drawable.rating1);
        ratingIcons[1] = getResources().getDrawable(R.drawable.rating2);
        ratingIcons[2] = getResources().getDrawable(R.drawable.rating3);
        ratingIcons[3] = getResources().getDrawable(R.drawable.rating4);
        ratingIcons[4] = getResources().getDrawable(R.drawable.rating5);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrases);
        PD = new ProgressDialog(this);
        PD.setMessage("Բեռնում...");
        PD.setCancelable(false);
        Intent intent = getIntent();
        phrases_id = intent.getStringExtra("phrases_id");
        user_id = intent.getStringExtra("user_id");

        textViewAuthorPhrases = (TextView) findViewById(R.id.textViewAuthorPhrases);
        textViewContentPhrases = (TextView) findViewById(R.id.textViewContentPhrases);
        textViewDatePhrases = (TextView) findViewById(R.id.textViewDatePhrases);
        textViewViewsPhrases = (TextView) findViewById(R.id.textViewViewsPhrases);
        textViewYourRatingPhrases = (TextView) findViewById(R.id.textViewRatingPhrases);
        textViewRatingPhrases = (TextView) findViewById(R.id.textViewRatingP);
        imageViewPhrases = (ImageView) findViewById(R.id.imageViewPhrases);
        spinnerRating = (Spinner) findViewById(R.id.spinnerRatingPhrases);
        spinnerRating.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.dayspinner,
                new String[]{"...", " 5 ", " 4 ", " 3 ", " 2 ", " 1 "}));
        yourRating = "0";
        ratingsCount = 0;
        ratingsSum = 0;

        PD.show();
        JsonObjectRequest jreq0 = new JsonObjectRequest(Request.Method.GET, url0,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = response.getInt("success");
                            if (success == 1) {
                                JSONArray ja = response.getJSONArray("aforizmner");
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jobj = ja.getJSONObject(i);
                                    if (phrases_id.equals(jobj.getString(ID))) {
                                        author = jobj.getString(AUTHOR);
                                        content = jobj.getString(CONTENT);
                                        date = jobj.getString(DATE);
                                        image = jobj.getString(IMAGE);
                                        views = jobj.getString(VIEWS);
                                        rating = jobj.getString(RATING);
                                        i = ja.length();
                                        PD.dismiss();
                                        new ImageLoadTask(getApplicationContext(), image, imageViewPhrases).execute();
                                        textViewAuthorPhrases.setText("          " + author);
                                        textViewContentPhrases.setText("   " + content);
                                        textViewDatePhrases.setText("ավելացված՝ " + date);
                                        textViewViewsPhrases.setText("Դիտումների թիվը՝ " + views);
                                        textViewRatingPhrases.setText("Վարկանիշ՝ " + rating);
                                        textViewYourRatingPhrases.setText("Ձեր գնահատականը. ");
                                    }
                                } // for loop ends
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

        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.GET, url1,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = response.getInt("success");
                            if (success == 1) {
                                JSONArray ja = response.getJSONArray("aforizmner");
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jobj = ja.getJSONObject(i);
                                    if (jobj.getString(PHRASES_ID).equals(phrases_id))
                                        if (Integer.parseInt(jobj.getString(RATING)) > 0) {
                                            ratingsCount++;
                                            ratingsSum += jobj.getInt(RATING);
                                        }
                                }
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jobj = ja.getJSONObject(i);
                                    if (jobj.getString(USER_ID).equals(user_id) && jobj.getString(PHRASES_ID).equals(phrases_id)
                                            && Integer.parseInt(jobj.getString(RATING)) > 0) {
                                        rated = true;
                                        rating_id = jobj.getString(ID);
                                        yourRating = jobj.getString(RATING);
                                        i = ja.length();
                                        spinnerRating.setSelection((rated == true) ? 6 - Integer.parseInt(yourRating) : 0);
                                        spinnerRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                                                if (position != 0 && position != 6 - Integer.parseInt(yourRating)) {
                                                    ratingsSum = ratingsSum - Integer.parseInt(yourRating) + 6 - position;
                                                    yourRating = (6 - position) + "";
                                                    newRating = (100 * (double) ratingsSum) / ((double) ratingsCount * 100);
                                                    StringRequest postRequest = new StringRequest(Request.Method.POST, url3,
                                                            new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {
                                                                    PD.dismiss();

                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(PhrasesActivity.this);
                                                                    builder.setTitle("Շնորհակալություն")
                                                                            .setMessage("Շնորհակալություն նոր գնահատականի համար:")
                                                                            .setIcon(ratingIcons[5 - position])
                                                                            .setCancelable(false)
                                                                            .setNegativeButton("Լավ",
                                                                                    new DialogInterface.OnClickListener() {
                                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                                            selfIntent = new Intent(getApplicationContext(), PhrasesActivity.class);
                                                                                            selfIntent.putExtra("phrases_id", phrases_id);
                                                                                            selfIntent.putExtra("user_id", user_id);
                                                                                            startActivity(selfIntent);
                                                                                            finish();
                                                                                        }
                                                                                    });
                                                                    AlertDialog alert = builder.create();
                                                                    alert.show();
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
                                                            params.put("user_id", user_id + "");
                                                            params.put("phrases_id", phrases_id + "");
                                                            params.put("rating", yourRating + "");
                                                            params.put("newrating", newRating + "");
                                                            params.put("rating_id", rating_id + "");

                                                            return params;
                                                        }
                                                    };

                                                    // Adding request to request queue
                                                    MyApplication.getInstance().addToReqQueue(postRequest);
                                                }
                                                if (position == 0) {
                                                    ratingsSum = ratingsSum - Integer.parseInt(yourRating);
                                                    ratingsCount--;
                                                    newRating = (ratingsCount != 0) ? ((100 * (double) ratingsSum) / ((double) ratingsCount * 100)) : 0;
                                                    unratePhrases();
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });
                                    }
                                } // for loop ends
                                if (!rated) {
                                    spinnerRating.setSelection(0);
                                    spinnerRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            if (position != 0) {
                                                yourRating = 6 - position + "";
                                                ratingsSum = ratingsSum + Integer.parseInt(yourRating);
                                                ratingsCount++;
                                                newRating = (100 * (double) ratingsSum) / ((double) ratingsCount * 100);
                                                final Drawable[] ratingIcons = new Drawable[5];
                                                ratingIcons[0] = getResources().getDrawable(R.drawable.rating1);
                                                ratingIcons[1] = getResources().getDrawable(R.drawable.rating2);
                                                ratingIcons[2] = getResources().getDrawable(R.drawable.rating3);
                                                ratingIcons[3] = getResources().getDrawable(R.drawable.rating4);
                                                ratingIcons[4] = getResources().getDrawable(R.drawable.rating5);

                                                StringRequest postRequest = new StringRequest(Request.Method.POST, url4,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                PD.dismiss();

                                                                AlertDialog.Builder builder = new AlertDialog.Builder(PhrasesActivity.this);
                                                                builder.setTitle("Շնորհակալություն")
                                                                        .setMessage("Շնորհակալություն գնահատականի համար:")
                                                                        .setIcon(ratingIcons[Integer.parseInt(yourRating) - 1])
                                                                        .setCancelable(false)
                                                                        .setNegativeButton("Խնդրեմ",
                                                                                new DialogInterface.OnClickListener() {
                                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                                        selfIntent = new Intent(getApplicationContext(), PhrasesActivity.class);
                                                                                        selfIntent.putExtra("phrases_id", phrases_id);
                                                                                        selfIntent.putExtra("user_id", user_id);
                                                                                        startActivity(selfIntent);
                                                                                        finish();
                                                                                    }
                                                                                });
                                                                AlertDialog alert = builder.create();
                                                                alert.show();
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
                                                        params.put("phrases_id", phrases_id + "");
                                                        params.put("user_id", user_id + "");
                                                        params.put("rating", yourRating + "");
                                                        params.put("newrating", newRating + "");
                                                        params.put("rating_id", rating_id + "");
                                                        return params;
                                                    }
                                                };

                                                // Adding request to request queue
                                                MyApplication.getInstance().addToReqQueue(postRequest);
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                                }
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
        MyApplication.getInstance().addToReqQueue(jreq);
    }

    private void unratePhrases() {
        String delete_url = "http://" + StateHost.URL + "/delete_phrasesrating.php?id="
                + rating_id;

        JsonObjectRequest delete_request = new JsonObjectRequest(delete_url,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(getApplicationContext(),
                        "Սերվերի խնդիր:", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToReqQueue(delete_request);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url5,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PD.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(PhrasesActivity.this);
                        builder.setTitle("Շնորհակալություն")
                                .setMessage("Շնորհակալություն գնահատականը " +
                                        "հետ վերցնելու համար: Հեղինակը Ձեզ չի մոռանա:")
                                .setIcon(getResources().getDrawable(R.drawable.trash))
                                .setCancelable(false)
                                .setNegativeButton("Լավ",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                selfIntent = new Intent(getApplicationContext(), PhrasesActivity.class);
                                                selfIntent.putExtra("phrases_id", phrases_id);
                                                selfIntent.putExtra("user_id", user_id);
                                                startActivity(selfIntent);
                                                finish();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
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
                params.put("rating_id", rating_id);
                params.put("phrases_id", phrases_id + "");
                params.put("newrating", newRating + "");

                return params;
            }
        };

        // Adding request to request queue
        MyApplication.getInstance().addToReqQueue(postRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        addViewsForPhrases();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        addViewsForPhrases();
    }

    public void addViewsForPhrases() {

        StringRequest postRequest = new StringRequest(Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("phrases_id", phrases_id);
                params.put("views", (Integer.parseInt(views) + 1) + "");
                return params;
            }
        };

        // Adding request to request queue
        MyApplication.getInstance().addToReqQueue(postRequest);
    }
}