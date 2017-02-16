package com.tutorialsbuzz.phrasebook;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TabHost;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    TabHost tabHost;
    ProgressDialog PD;
    TextView textViewWelcome, textViewAccount, textViewLogOut, nTextView, textViewNewsArchive,
            textViewUSA, textViewCuba, textViewBrazil, textViewAntarctica, textViewGermany,
            textViewUkraine, textViewEgypt, textViewMozambique, textViewArmenia, textViewRussia,
            textViewAustralia, textViewIndia, textViewChina, textViewJapan;
    ListView newsList, horoscopeListView;
    RadioButton radioButtonDate, radioButtonViews, radioButtonRating;
    Spinner spinnerTime, horoscopeSpinner;
    GridView horoscopeGridView;
    FloatingActionButton searchButton;

    int user_id, zodiacSign;
    int todaysNewsCount, foundedPhrasesCount;
    String fname, surname, login, logoutDate, sessions, sex, password, address, telephone, email,
            birthdate, today, header, content, image, date, views, rating, news_id, zodiac = "",
            birthMonth, birthDay, phrases_id, country, author;
    String[] news;
    String[] images;
    ArrayList<String> idList, headerList, contentList, imageList, dateList, viewsList, ratingList,
            idListP, authorListP, contentListP, dateListP, imageListP, viewsListP, ratingListP, countryListP;

    ArrayAdapter<String> timeAdapter, horoscopeAdapter;
    MyHoroscopeGrid myHoroscopeGrid;
    HoroscopeGrid horoscopeGrid;

    String url0 = "http://" + StateHost.URL + "/read_allnews.php";
    String urlp = "http://" + StateHost.URL + "/read_allphrases.php";
    String url0v = "http://" + StateHost.URL + "/arrange_news_by_views.php";
    String url0r = "http://" + StateHost.URL + "/arrange_news_by_ratings.php";
    String url = "http://" + StateHost.URL + "/add_session.php";

    // JSON Node names
    public static final String ID = "id";
    public static final String HEADER = "header";
    public static final String CONTENT = "content";
    public static final String IMAGE = "image";
    public static final String DATE = "date";
    public static final String VIEWS = "views";
    public static final String RATING = "rating";
    public static final String AUTHOR = "author";
    public static final String COUNTRY = "country";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        textViewWelcome = (TextView) findViewById(R.id.textViewWelcome);
        textViewAccount = (TextView) findViewById(R.id.textViewAccount);
        textViewLogOut = (TextView) findViewById(R.id.textViewLogOut);
        textViewNewsArchive = (TextView) findViewById(R.id.textViewNewsArchive);
        nTextView = (TextView) findViewById(R.id.textViewNews);
        textViewUSA = (TextView) findViewById(R.id.textViewUSA);
        textViewUkraine = (TextView) findViewById(R.id.textViewUkraine);
        textViewAntarctica = (TextView) findViewById(R.id.textViewAntarctica);
        textViewCuba = (TextView) findViewById(R.id.textViewCuba);
        textViewBrazil = (TextView) findViewById(R.id.textViewBrazil);
        textViewGermany = (TextView) findViewById(R.id.textViewGermany);
        textViewEgypt = (TextView) findViewById(R.id.textViewEgypt);
        textViewMozambique = (TextView) findViewById(R.id.textViewMozambique);
        textViewArmenia = (TextView) findViewById(R.id.textViewArmenia);
        textViewAustralia = (TextView) findViewById(R.id.textViewAustralia);
        textViewRussia = (TextView) findViewById(R.id.textViewRussia);
        textViewIndia = (TextView) findViewById(R.id.textViewIndia);
        textViewChina = (TextView) findViewById(R.id.textViewChina);
        textViewJapan = (TextView) findViewById(R.id.textViewJapan);
        radioButtonDate = (RadioButton) findViewById(R.id.radioButtonArrangeByDate);
        radioButtonViews = (RadioButton) findViewById(R.id.radioButtonArrangeByViews);
        radioButtonRating = (RadioButton) findViewById(R.id.radioButtonArrangeByRating);

        textViewNewsArchive.setOnClickListener(this);
        textViewAccount.setOnClickListener(this);
        textViewLogOut.setOnClickListener(this);
        radioButtonDate.setOnClickListener(this);
        radioButtonViews.setOnClickListener(this);
        radioButtonRating.setOnClickListener(this);

        timeAdapter = new ArrayAdapter<String>(this, R.layout.timespinner, new String[]{"Վերջին 24 ժամը", "Վերջին 7 օրը", "Վերջին 30 օրը"});
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime = (Spinner) findViewById(R.id.spinnerNewsTime);
        spinnerTime.setAdapter(timeAdapter);
        horoscopeAdapter = new ArrayAdapter<String>(this, R.layout.horoscopespinner, new String[]{"շաբաթվա իմ աստղագուշակը", "շաբաթվա աստղագուշակը", "տարվա աստղագուշակը"});
        horoscopeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        horoscopeSpinner = (Spinner) findViewById(R.id.spinnerHoroscope);
        horoscopeSpinner.setAdapter(horoscopeAdapter);

        final Intent intent = getIntent();
        user_id = intent.getIntExtra("id", 0);
        fname = intent.getStringExtra("fname");
        surname = intent.getStringExtra("surname");
        login = intent.getStringExtra("login");
        sessions = intent.getStringExtra("sessions");
        password = intent.getStringExtra("password");
        address = intent.getStringExtra("address");
        telephone = intent.getStringExtra("telephone");
        email = intent.getStringExtra("email");
        sex = intent.getStringExtra("sex");
        birthdate = intent.getStringExtra("birthdate");
        textViewWelcome.setText("Ողջույն, " + fname + " " + surname);

        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (radioButtonDate.isChecked())
                    switch (position) {
                        case 0:
                            showLastDaysNews(1, url0);
                            break;
                        case 1:
                            showLastDaysNews(7, url0);
                            break;
                        case 2:
                            showLastDaysNews(30, url0);
                            break;
                    }
                if (radioButtonViews.isChecked())
                    switch (position) {
                        case 0:
                            showLastDaysNews(1, url0v);
                            break;
                        case 1:
                            showLastDaysNews(7, url0v);
                            break;
                        case 2:
                            showLastDaysNews(30, url0v);
                            break;
                    }
                if (radioButtonRating.isChecked())
                    switch (position) {
                        case 0:
                            showLastDaysNews(1, url0r);
                            break;
                        case 1:
                            showLastDaysNews(7, url0r);
                            break;
                        case 2:
                            showLastDaysNews(30, url0r);
                            break;
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        birthMonth = birthdate.substring(5, 7);
        birthDay = birthdate.substring(8, 10);
        int birthmonth = Integer.parseInt(birthMonth);
        int birthday = Integer.parseInt(birthDay);
        if ((birthmonth == 3 && birthday >= 21) || (birthmonth == 4 && birthday <= 20)) {
            zodiac = "խոյ";
            zodiacSign = R.drawable.xoy;
        }
        if ((birthmonth == 4 && birthday >= 21) || (birthmonth == 5 && birthday <= 21)) {
            zodiac = "ցուլ";
            zodiacSign = R.drawable.cul;
        }
        if ((birthmonth == 5 && birthday >= 22) || (birthmonth == 6 && birthday <= 21)) {
            zodiac = "երկվորյակներ";
            zodiacSign = R.drawable.erk;
        }
        if ((birthmonth == 6 && birthday >= 22) || (birthmonth == 7 && birthday <= 22)) {
            zodiac = "խեցգետին";
            zodiacSign = R.drawable.xec;
        }
        if ((birthmonth == 7 && birthday >= 23) || (birthmonth == 8 && birthday <= 23)) {
            zodiac = "առյուծ";
            zodiacSign = R.drawable.ary;
        }
        if ((birthmonth == 8 && birthday >= 24) || (birthmonth == 9 && birthday <= 23)) {
            zodiac = "կույս";
            zodiacSign = R.drawable.kuy;
        }
        if ((birthmonth == 9 && birthday >= 24) || (birthmonth == 10 && birthday <= 23)) {
            zodiac = "կշեռք";
            zodiacSign = R.drawable.ksh;
        }
        if ((birthmonth == 10 && birthday >= 24) || (birthmonth == 11 && birthday <= 22)) {
            zodiac = "կարիճ";
            zodiacSign = R.drawable.kar;
        }
        if ((birthmonth == 11 && birthday >= 23) || (birthmonth == 12 && birthday <= 21)) {
            zodiac = "աղեղնավոր";
            zodiacSign = R.drawable.agh;
        }
        if ((birthmonth == 12 && birthday >= 22) || (birthmonth == 1 && birthday <= 20)) {
            zodiac = "այծեղջյուր";
            zodiacSign = R.drawable.ayc;
        }
        if ((birthmonth == 1 && birthday >= 21) || (birthmonth == 2 && birthday <= 18)) {
            zodiac = "ջրհոս";
            zodiacSign = R.drawable.jrh;
        }
        if ((birthmonth == 2 && birthday >= 19) || (birthmonth == 3 && birthday <= 20)) {
            zodiac = "ձկներ";
            zodiacSign = R.drawable.dzk;
        }

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        View v1 = getLayoutInflater().inflate(R.layout.news_tab_header, null);
        tabSpec.setIndicator(v1);
        tabSpec.setContent(R.id.tabContent1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        View v2 = getLayoutInflater().inflate(R.layout.ast_tab_header, null);
        tabSpec.setIndicator(v2);
        tabSpec.setContent(R.id.tabContent2);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        View v3 = getLayoutInflater().inflate(R.layout.phrases_tab_header, null);
        tabSpec.setIndicator(v3);
        tabSpec.setContent(R.id.tabContent3);
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTabByTag("tag1");

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String arg0) {
                setTabColor(tabHost);
            }
        });
        setTabColor(tabHost);

        PD = new ProgressDialog(this);
        PD.setMessage("Բեռնում...");
        PD.setCancelable(false);

        GregorianCalendar calendar = new GregorianCalendar();
        String day = calendar.get(calendar.DAY_OF_MONTH) + "";
        String month = calendar.getTime().getMonth() + 1 + "";
        String year = 1900 + calendar.getTime().getYear() + "";
        if (Integer.parseInt(day) < 10)
            day = "0" + day;
        if (Integer.parseInt(month) < 10)
            month = "0" + month;
        today = year + "." + month + "." + day;

        idList = new ArrayList<String>();
        headerList = new ArrayList<String>();
        contentList = new ArrayList<String>();
        imageList = new ArrayList<String>();
        dateList = new ArrayList<String>();
        viewsList = new ArrayList<String>();
        ratingList = new ArrayList<String>();
        idListP = new ArrayList<String>();
        authorListP = new ArrayList<String>();
        contentListP = new ArrayList<String>();
        dateListP = new ArrayList<String>();
        imageListP = new ArrayList<String>();
        viewsListP = new ArrayList<String>();
        ratingListP = new ArrayList<String>();
        countryListP = new ArrayList<String>();

        newsList = (ListView) findViewById(R.id.listViewNews);
        searchButton = (FloatingActionButton) findViewById(R.id.buttonSearchNews);

        horoscopeListView = (ListView) findViewById(R.id.listViewHoroscope);
        horoscopeGridView = (GridView) findViewById(R.id.gridViewHoroscope);
        horoscopeGrid = new HoroscopeGrid(getApplicationContext());
        myHoroscopeGrid = new MyHoroscopeGrid(this, new String[]{zodiac}, new int[]{zodiacSign});
        horoscopeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        horoscopeListView.setAdapter(myHoroscopeGrid);
                        horoscopeGridView.setAdapter(null);
                        horoscopeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intentH = new Intent(getApplicationContext(), HoroscopeActivity.class);
                                intentH.putExtra("zodiac", zodiac);
                                intentH.putExtra("type", "general");
                                startActivity(intentH);
                            }
                        });
                        break;
                    case 1:
                        horoscopeGridView.setAdapter(horoscopeGrid);
                        horoscopeGridView.setNumColumns(3);
                        horoscopeListView.setAdapter(null);
                        horoscopeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intentH = new Intent(getApplicationContext(), HoroscopeActivity.class);
                                String[] zodiacs = {"Խոյ", "Ցուլ", "Երկվորյակներ", "Խեցգետին",
                                        "Առյուծ", "Կույս", "Կշեռք", "Կարիճ",
                                        "Աղեղնավոր", "Այծեղջյուր", "Ջրհոս", "Ձկներ"};
                                intentH.putExtra("zodiac", zodiacs[position]);
                                intentH.putExtra("type", "general");
                                startActivity(intentH);
                            }
                        });
                        break;
                    case 2:
                        horoscopeGridView.setAdapter(horoscopeGrid);
                        horoscopeListView.setAdapter(null);
                        horoscopeGridView.setNumColumns(3);
                        horoscopeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intentH = new Intent(getApplicationContext(), HoroscopeActivity.class);
                                String[] zodiacs = {"Խոյ", "Ցուլ", "Երկվորյակներ", "Խեցգետին",
                                        "Առյուծ", "Կույս", "Կշեռք", "Կարիճ",
                                        "Աղեղնավոր", "Այծեղջյուր", "Ջրհոս", "Ձկներ"};
                                intentH.putExtra("zodiac", zodiacs[position]);
                                intentH.putExtra("type", "year");
                                startActivity(intentH);
                            }
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        View.OnClickListener phraseListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clickanim));
                loadPhrase(((TextView) v).getText().toString());
            }
        };
        textViewJapan.setOnClickListener(phraseListener);
        textViewChina.setOnClickListener(phraseListener);
        textViewIndia.setOnClickListener(phraseListener);
        textViewAustralia.setOnClickListener(phraseListener);
        textViewAntarctica.setOnClickListener(phraseListener);
        textViewArmenia.setOnClickListener(phraseListener);
        textViewRussia.setOnClickListener(phraseListener);
        textViewUkraine.setOnClickListener(phraseListener);
        textViewGermany.setOnClickListener(phraseListener);
        textViewEgypt.setOnClickListener(phraseListener);
        textViewMozambique.setOnClickListener(phraseListener);
        textViewBrazil.setOnClickListener(phraseListener);
        textViewCuba.setOnClickListener(phraseListener);
        textViewUSA.setOnClickListener(phraseListener);
    }

    private void loadPhrase(final String s) {
        foundedPhrasesCount = 0;
        idListP.clear();
        authorListP.clear();
        contentListP.clear();
        dateListP.clear();
        imageListP.clear();
        viewsListP.clear();
        ratingListP.clear();
        countryListP.clear();

        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.GET, urlp,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = response.getInt("success");
                            if (success == 1) {
                                JSONArray ja = response.getJSONArray("aforizmner");
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jobj = ja.getJSONObject(i);
                                    phrases_id = jobj.getString(ID);
                                    author = jobj.getString(AUTHOR);
                                    content = jobj.getString(CONTENT);
                                    date = jobj.getString(DATE);
                                    image = jobj.getString(IMAGE);
                                    views = jobj.getString(VIEWS);
                                    rating = jobj.getString(RATING);
                                    country = jobj.getString(COUNTRY);
                                    if (s.equalsIgnoreCase(country)) {
                                        foundedPhrasesCount++;
                                        idListP.add(phrases_id);
                                        authorListP.add(author);
                                        contentListP.add(content);
                                        dateListP.add(date);
                                        imageListP.add(image);
                                        viewsListP.add(views);
                                        ratingListP.add(rating);
                                        countryListP.add(country);
                                    }
                                } // for loop ends
                                if (foundedPhrasesCount == 0) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                                    builder.setTitle("Ներողություն")
                                            .setMessage("Տվյալ երկրի համար աֆորիզմներ հայտնաբերված չեն:")
                                            .setIcon(R.drawable.nophrase)
                                            .setCancelable(false)
                                            .setNegativeButton("Լավ չեք աշխատում...",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                        }
                                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                } else {
                                    int number = (int) (foundedPhrasesCount * Math.random());
                                    Intent intentP = new Intent(getApplicationContext(), PhrasesActivity.class);
                                    intentP.putExtra("user_id", user_id + "");
                                    intentP.putExtra("phrases_id", idListP.get(number));
                                    startActivity(intentP);
                                }
                            } // if ends
                        } catch (JSONException e) {
                            PD.dismiss();
                            ArrayAdapter<String> adapter0 = new ArrayAdapter<String>(getApplicationContext(), R.layout.oval,
                                    new String[]{"Սերվերի խնդիր է առկա: Խնդրում ենք փորձել մի փոքր ուշ:"});
                            newsList.setAdapter(adapter0);
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

    private void showLastDaysNews(final int days, final String sort) {
        idList.clear();
        headerList.clear();
        contentList.clear();
        dateList.clear();
        imageList.clear();
        viewsList.clear();
        ratingList.clear();
        todaysNewsCount = 0;

        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.GET, sort,
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
                                    Date date0 = new Date();
                                    date0.setYear(Integer.parseInt(date.substring(0, 4)) - 1900);
                                    date0.setMonth(Integer.parseInt(date.substring(5, 7)) - 1);
                                    date0.setDate(Integer.parseInt(date.substring(8, 10)));
                                    date0.setHours(Integer.parseInt(date.substring(12, 14)));
                                    date0.setMinutes(Integer.parseInt(date.substring(15, 17)));
                                    date0.setSeconds(0);
                                    if ((long) ((long) System.currentTimeMillis() - (long) date0.getTime()) <= (long) 86400000 * days) {
                                        todaysNewsCount++;
                                        idList.add(news_id);
                                        headerList.add(header);
                                        contentList.add(content);
                                        dateList.add(date);
                                        imageList.add(image);
                                        viewsList.add(views);
                                        ratingList.add(rating);
                                    }
                                } // for loop ends
                                news = new String[todaysNewsCount];
                                images = new String[todaysNewsCount];
                                for (int i = 0; i < todaysNewsCount; i++) {
                                    news[i] = headerList.get(i);
                                }
                                for (int i = 0; i < todaysNewsCount; i++) {
                                    images[i] = imageList.get(i).replace("images", "thumbs");
                                }
                                if (todaysNewsCount == 0) {
                                    ArrayAdapter<String> adapter0 = new ArrayAdapter<String>(getApplicationContext(), R.layout.oval,
                                            new String[]{"   Նորություններ հայտնաբերված չեն:"});
                                    newsList.setAdapter(adapter0);
                                    newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        }
                                    });
                                } else {
                                    NewsList adapter = new NewsList(UserActivity.this, news, images);
                                    newsList.setAdapter(adapter);
                                    newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent intentN = new Intent(getApplicationContext(), NewsActivity.class);
                                            intentN.putExtra("user_id", user_id + "");
                                            intentN.putExtra("news_id", idList.get(position));
                                            startActivity(intentN);
                                        }
                                    });
                                }
                            } // if ends
                        } catch (JSONException e) {
                            PD.dismiss();
                            ArrayAdapter<String> adapter0 = new ArrayAdapter<String>(getApplicationContext(), R.layout.oval,
                                    new String[]{"Սերվերի խնդիր է առկա: Խնդրում ենք փորձել մի փոքր ուշ:"});
                            newsList.setAdapter(adapter0);
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

    public void setTabColor(TabHost tabhost) {
        for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++)
            tabhost.getTabWidget().getChildAt(i).setBackgroundColor(Color.rgb(0, 128, 0)); //unselected
        tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(Color.rgb(255, 255, 153)); //1st tab selected
    }

    @Override
    public void onClick(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clickanim));
        switch (view.getId()) {
            case R.id.textViewAccount:
                Intent intent0 = new Intent(getApplicationContext(), AccountActivity.class);
                intent0.putExtra("id", user_id);
                intent0.putExtra("login", login);
                intent0.putExtra("fname", fname);
                intent0.putExtra("sex", sex);
                intent0.putExtra("zodiac", zodiac);
                intent0.putExtra("surname", surname);
                intent0.putExtra("birthdate", birthdate);
                startActivity(intent0);
                break;
            case R.id.textViewLogOut:
                logout();
                break;
            case R.id.textViewNewsArchive:
                goToNewsArchive();
                break;
            case R.id.radioButtonArrangeByDate:
                if (todaysNewsCount > 0)
                    arrangeByDate();
                break;
            case R.id.radioButtonArrangeByViews:
                if (todaysNewsCount > 0)
                    arrangeByViews();
                break;
            case R.id.radioButtonArrangeByRating:
                if (todaysNewsCount > 0)
                    arrangeByRating();
                break;
        }
    }

    private void arrangeByDate() {
        switch (spinnerTime.getSelectedItemPosition()) {
            case 0:
                showLastDaysNews(1, url0);
                break;
            case 1:
                showLastDaysNews(7, url0);
                break;
            case 2:
                showLastDaysNews(30, url0);
                break;
        }
    }

    private void arrangeByViews() {
        switch (spinnerTime.getSelectedItemPosition()) {
            case 0:
                showLastDaysNews(1, url0v);
                break;
            case 1:
                showLastDaysNews(7, url0v);
                break;
            case 2:
                showLastDaysNews(30, url0v);
                break;
        }
    }

    private void arrangeByRating() {
        switch (spinnerTime.getSelectedItemPosition()) {
            case 0:
                showLastDaysNews(1, url0r);
                break;
            case 1:
                showLastDaysNews(7, url0r);
                break;
            case 2:
                showLastDaysNews(30, url0r);
                break;
        }
    }

    private void logout() {
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
        logoutDate = year + "." + month + "." + day + ", " + hour + ":" + minute + ":" + second;
        sessions = sessions + " " + logoutDate + "(logout)" + "\n\n";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PD.dismiss();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        stopService(new Intent(getApplicationContext(), NewsService.class));
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
                params.put("username", login);
                return params;
            }
        };

        // Adding request to request queue
        MyApplication.getInstance().addToReqQueue(postRequest);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void searchInNews(View view) {
        Intent intent = new Intent(getApplicationContext(), SearchNewsActivity.class);
        intent.putExtra("user_id", user_id + "");
        startActivity(intent);
    }

    public void goToNewsArchive() {
        Intent intent = new Intent(getApplicationContext(), NewsArchiveActivity.class);
        intent.putExtra("user_id", user_id + "");
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent newsService = new Intent(this, NewsService.class);
        newsService.putExtra("id", user_id);
        newsService.putExtra("fname", fname);
        newsService.putExtra("surname", surname);
        newsService.putExtra("login", login);
        newsService.putExtra("sessions", sessions);
        newsService.putExtra("password", password);
        newsService.putExtra("address", address);
        newsService.putExtra("telephone", telephone);
        newsService.putExtra("email", email);
        newsService.putExtra("sex", sex);
        newsService.putExtra("birthdate", birthdate);
        startService(newsService);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(getApplicationContext(), NewsService.class));
    }
}