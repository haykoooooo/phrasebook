package com.haykabelyan.phrasebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class NewsArchiveActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner spinnerFD, spinnerFM, spinnerFY, spinnerTD, spinnerTM, spinnerTY;
    Button buttonSearch;
    ListView listView;
    ProgressDialog PD;

    String header, content, image, date, views, rating, user_id, news_id, dateFrom, dateTo;
    String[] days, months, years, news, images;
    ArrayAdapter<String> daysAdapter, monthsAdapter, yearsAdapter;
    GregorianCalendar gregorianCalendar = new GregorianCalendar();
    ArrayList<String> headerList, contentList, imageList, dateList, viewsList, idList, ratingList;

    String url = "http://" + StateHost.URL + "/read_allnews.php";

    // JSON Node names
    public static final String ID = "id";
    public static final String HEADER = "header";
    public static final String CONTENT = "content";
    public static final String IMAGE = "image";
    public static final String DATE = "date";
    public static final String VIEWS = "views";
    public static final String RATING = "rating";

    int foundedNewsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_archive);
        PD = new ProgressDialog(this);
        PD.setMessage("Բեռնում...");
        PD.setCancelable(false);
        foundedNewsCount = 0;
        listView = (ListView) findViewById(R.id.listViewNewsArchive);
        buttonSearch = (Button) findViewById(R.id.buttonSearchNewsArchive);
        buttonSearch.setTextColor(Color.WHITE);
        buttonSearch.setBackground(getResources().getDrawable(R.drawable.longshape));
        buttonSearch.setOnClickListener(this);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        final String year = 1900 + gregorianCalendar.getTime().getYear() + "";
        int y = Integer.parseInt(year) - 2017 + 2;
        years = new String[y];
        years[0] = "տարի...";
        for (int i = 1; i < y; i++)
            years[i] = 2016 + i + "";
        months = new String[13];
        months[0] = "ամիս...";
        months[1] = "հունվար";
        months[2] = "փետրվար";
        months[3] = "մարտ";
        months[4] = "ապրիլ";
        months[5] = "մայիս";
        months[6] = "հունիս";
        months[7] = "հուլիս";
        months[8] = "օգոստոս";
        months[9] = "սեպտեմբեր";
        months[10] = "հոկտեմբեր";
        months[11] = "նոյեմբեր";
        months[12] = "դեկտեմբեր";
        days = new String[]{"օր..."};
        daysAdapter = new ArrayAdapter<>(this, R.layout.dayspinner, days);
        monthsAdapter = new ArrayAdapter<>(this, R.layout.monthspinner, months);
        yearsAdapter = new ArrayAdapter<>(this, R.layout.yearspinner, years);
        daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFD = (Spinner) findViewById(R.id.spinnerFD);
        spinnerFM = (Spinner) findViewById(R.id.spinnerFM);
        spinnerFY = (Spinner) findViewById(R.id.spinnerFY);
        spinnerTD = (Spinner) findViewById(R.id.spinnerTD);
        spinnerTM = (Spinner) findViewById(R.id.spinnerTM);
        spinnerTY = (Spinner) findViewById(R.id.spinnerTY);
        spinnerFY.setAdapter(yearsAdapter);
        spinnerTY.setAdapter(yearsAdapter);
        spinnerFM.setAdapter(monthsAdapter);
        spinnerTM.setAdapter(monthsAdapter);
        spinnerFD.setAdapter(daysAdapter);
        spinnerTD.setAdapter(daysAdapter);
        headerList = new ArrayList<String>();
        contentList = new ArrayList<String>();
        imageList = new ArrayList<String>();
        dateList = new ArrayList<String>();
        viewsList = new ArrayList<String>();
        ratingList = new ArrayList<String>();
        idList = new ArrayList<String>();

        spinnerFY.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerFM.getSelectedItemPosition() == 2 && position != 0) {
                    if ((position - 1) % 4 == 0)
                        setDaysF(29);
                    else
                        setDaysF(28);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerFM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1)
                    setDaysF(31);
                if (position == 2) {
                    if (spinnerFY.getSelectedItemPosition() != 0 && spinnerFY.getSelectedItemPosition() % 4 == 0)
                        setDaysF(29);
                    else
                        setDaysF(28);
                }
                if (position == 3)
                    setDaysF(31);
                if (position == 4)
                    setDaysF(30);
                if (position == 5)
                    setDaysF(31);
                if (position == 6)
                    setDaysF(30);
                if (position == 7)
                    setDaysF(31);
                if (position == 8)
                    setDaysF(31);
                if (position == 9)
                    setDaysF(30);
                if (position == 10)
                    setDaysF(31);
                if (position == 11)
                    setDaysF(30);
                if (position == 12)
                    setDaysF(31);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerTY.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerTM.getSelectedItemPosition() == 2 && position != 0) {
                    if (position != 0 && position % 4 == 0)
                        setDaysT(29);
                    else
                        setDaysT(28);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerTM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1)
                    setDaysT(31);
                if (position == 2) {
                    if (spinnerTY.getSelectedItemPosition() != 0 && spinnerTY.getSelectedItemPosition() % 4 == 0)
                        setDaysT(29);
                    else
                        setDaysT(28);
                }
                if (position == 3)
                    setDaysT(31);
                if (position == 4)
                    setDaysT(30);
                if (position == 5)
                    setDaysT(31);
                if (position == 6)
                    setDaysT(30);
                if (position == 7)
                    setDaysT(31);
                if (position == 8)
                    setDaysT(31);
                if (position == 9)
                    setDaysT(30);
                if (position == 10)
                    setDaysT(31);
                if (position == 11)
                    setDaysT(30);
                if (position == 12)
                    setDaysT(31);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void setDaysF(int k) {
        spinnerFD.setEnabled(true);
        days = new String[k + 1];
        days[0] = "օր...";
        for (int i = 1; i < k + 1; i++)
            days[i] = i + "";
        daysAdapter = new ArrayAdapter<String>(this, R.layout.dayspinner, days);
        spinnerFD.setAdapter(daysAdapter);
    }

    public void setDaysT(int k) {
        spinnerTD.setEnabled(true);
        days = new String[k + 1];
        days[0] = "օր...";
        for (int i = 1; i < k + 1; i++)
            days[i] = i + "";
        daysAdapter = new ArrayAdapter<String>(this, R.layout.dayspinner, days);
        spinnerTD.setAdapter(daysAdapter);
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clickanim));

        switch (v.getId()) {
            case R.id.buttonSearchNewsArchive:
                if (spinnerFY.getSelectedItemPosition() != 0 && spinnerFM.getSelectedItemPosition() != 0
                        && spinnerFD.getSelectedItemPosition() != 0 && spinnerTY.getSelectedItemPosition() != 0
                        && spinnerTM.getSelectedItemPosition() != 0 && spinnerTD.getSelectedItemPosition() != 0)
                    checkData();
                else
                    Toast.makeText(getApplicationContext(), "Լրացրեք բոլոր 6 դաշտերը:", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void checkData() {
        if (spinnerFY.getSelectedItemPosition() < spinnerTY.getSelectedItemPosition() ||
                (spinnerFY.getSelectedItemPosition() == spinnerTY.getSelectedItemPosition()
                        && spinnerFM.getSelectedItemPosition() < spinnerTM.getSelectedItemPosition())
                || (spinnerFY.getSelectedItemPosition() == spinnerTY.getSelectedItemPosition()
                && spinnerFM.getSelectedItemPosition() == spinnerTM.getSelectedItemPosition()
                && spinnerFD.getSelectedItemPosition() <= spinnerTD.getSelectedItemPosition()))
            searchNews();
        else
            Toast.makeText(getApplicationContext(), "Դուք ամսաթվերը թա՞րս եք հաշվում: " +
                    "Հապա մի ուշադիր նայեք...", Toast.LENGTH_LONG).show();
    }

    private void searchNews() {
        String monthFrom = spinnerFM.getSelectedItemPosition() + "";
        if (spinnerFM.getSelectedItemPosition() < 10)
            monthFrom = "0" + monthFrom;
        String monthTo = spinnerTM.getSelectedItemPosition() + "";
        if (spinnerTM.getSelectedItemPosition() < 10)
            monthTo = "0" + monthTo;
        String dayFrom = spinnerFD.getSelectedItemPosition() + "";
        if (spinnerFD.getSelectedItemPosition() < 10)
            dayFrom = "0" + dayFrom;
        String dayTo = spinnerTD.getSelectedItemPosition() + "";
        if (spinnerTD.getSelectedItemPosition() < 10)
            dayTo = "0" + dayTo;
        dateFrom = (2016 + spinnerFY.getSelectedItemPosition()) + "." + monthFrom + "." + dayFrom;
        dateTo = (2016 + spinnerTY.getSelectedItemPosition()) + "." + monthTo + "." + dayTo;
        idList.clear();
        headerList.clear();
        contentList.clear();
        imageList.clear();
        dateList.clear();
        viewsList.clear();
        ratingList.clear();
        foundedNewsCount = 0;
        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = response.getInt("success");
                            if (success == 1) {
                                JSONArray ja = response.getJSONArray("aforizmner");
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jobj = ja.getJSONObject(i);
                                    news_id = jobj.getString(ID);
                                    header = jobj.getString(HEADER);
                                    content = jobj.getString(CONTENT);
                                    date = jobj.getString(DATE);
                                    image = jobj.getString(IMAGE);
                                    views = jobj.getString(VIEWS);
                                    rating = jobj.getString(RATING);
                                    if (date.substring(0, 10).compareTo(dateFrom) >= 0 &&
                                            date.substring(0, 10).compareTo(dateTo) <= 0) {
                                        foundedNewsCount++;
                                        idList.add(news_id);
                                        headerList.add(header);
                                        contentList.add(content);
                                        dateList.add(date);
                                        imageList.add(image);
                                        viewsList.add(views);
                                        ratingList.add(rating);
                                    }
                                } // for loop ends
                                news = new String[foundedNewsCount];
                                images = new String[foundedNewsCount];
                                for (int i = 0; i < foundedNewsCount; i++) {
                                    news[i] = headerList.get(i);
                                }
                                for (int i = 0; i < foundedNewsCount; i++) {
                                    images[i] = imageList.get(i).replace("images", "thumbs");
                                }
                                PD.dismiss();
                                if (foundedNewsCount == 0) {
                                    ArrayAdapter<String> adapter0 = new ArrayAdapter<String>(getApplicationContext(), R.layout.oval,
                                            new String[]{"   Նորություններ հայտնաբերված չեն:"});
                                    listView.setAdapter(adapter0);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        }
                                    });
                                } else {
                                    NewsList adapter = new NewsList(NewsArchiveActivity.this, news, images);
                                    listView.setAdapter(adapter);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent intentN = new Intent(getApplicationContext(), NewsActivity.class);
                                            intentN.putExtra("user_id", user_id);
                                            intentN.putExtra("news_id", idList.get(position));
                                            intentN.putExtra("header", headerList.get(position));
                                            intentN.putExtra("content", contentList.get(position));
                                            intentN.putExtra("image", imageList.get(position));
                                            intentN.putExtra("date", dateList.get(position));
                                            intentN.putExtra("views", viewsList.get(position));
                                            intentN.putExtra("rating", ratingList.get(position));
                                            startActivity(intentN);
                                        }
                                    });
                                }
                            } // if ends
                        } catch (JSONException e) {
                            PD.dismiss();
                            ArrayAdapter<String> adapter0 = new ArrayAdapter<String>(getApplicationContext(), R.layout.oval,
                                    new String[]{"   Սերվերի խնդիր է առկա: Խնդրում ենք փորձել մի փոքր ուշ:"});
                            listView.setAdapter(adapter0);
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
}