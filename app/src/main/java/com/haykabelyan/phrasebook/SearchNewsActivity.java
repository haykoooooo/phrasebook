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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchNewsActivity extends AppCompatActivity {

    ProgressDialog PD;
    ListView listView;
    Button button;
    EditText editText;

    String url = "http://" + StateHost.URL + "/read_allnews.php";

    // JSON Node names
    public static final String HEADER = "header";
    public static final String CONTENT = "content";
    public static final String IMAGE = "image";
    public static final String DATE = "date";
    public static final String VIEWS = "views";
    public static final String RATING = "rating";

    int foundedNewsCount = 0;
    String word, header, content, image, date, views, rating, user_id, news_id;
    String[] news, images, words;
    ArrayList<String> headerList, contentList, imageList, dateList, viewsList, idList, ratingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_news);

        PD = new ProgressDialog(this);
        PD.setMessage("Բեռնում...");
        PD.setCancelable(false);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        button = (Button) findViewById(R.id.buttonSearchNews);
        editText = (EditText) findViewById(R.id.editTextSearchNews);
        listView = (ListView) findViewById(R.id.listViewSearchNews);
        button.setTextColor(Color.WHITE);
        button.setBackground(getResources().getDrawable(R.drawable.longshape));
        headerList = new ArrayList<String>();
        contentList = new ArrayList<String>();
        imageList = new ArrayList<String>();
        dateList = new ArrayList<String>();
        viewsList = new ArrayList<String>();
        ratingList = new ArrayList<String>();
        idList = new ArrayList<String>();

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                button.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clickanim));

                word = editText.getText().toString();
                if (!word.trim().equals("")) {
                    PD.show();
                    words = word.split(" ");
                    idList.clear();
                    headerList.clear();
                    contentList.clear();
                    imageList.clear();
                    dateList.clear();
                    viewsList.clear();
                    ratingList.clear();
                    foundedNewsCount = 0;
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        }
                    });

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
                                                news_id = jobj.getString("id");
                                                header = jobj.getString(HEADER);
                                                content = jobj.getString(CONTENT);
                                                date = jobj.getString(DATE);
                                                image = jobj.getString(IMAGE);
                                                views = jobj.getString(VIEWS);
                                                rating = jobj.getString(RATING);
                                                for (int k = 0; k < words.length; k++)
                                                    if (content.toLowerCase().contains(words[k].toLowerCase()) ||
                                                            header.toLowerCase().contains(words[k].toLowerCase())) {
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
                                                if (word.contains(" ")) {
                                                    ArrayAdapter<String> adapter0 = new ArrayAdapter<String>(getApplicationContext(), R.layout.oval,
                                                            new String[]{"   Դժբախտաբար, նման բանալիներ մեր նորություններում չեն հայտնաբերվել"});
                                                    listView.setAdapter(adapter0);
                                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        }
                                                    });
                                                } else {
                                                    ArrayAdapter<String> adapter0 = new ArrayAdapter<String>(getApplicationContext(), R.layout.oval,
                                                            new String[]{"   Դժբախտաբար, նման բանալի մեր նորություններում չի հայտնաբերվել"});
                                                    listView.setAdapter(adapter0);
                                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        }
                                                    });
                                                }
                                            } else {
                                                NewsList adapter = new NewsList(SearchNewsActivity.this, news, images);
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
                } else {
                    if (word.equals(""))
                        Toast.makeText(getApplicationContext(), "Նույնիսկ Google-ը դատարկ տող չի որոնում:", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "Դուք Ձեր գրած բառից բան հասկացա՞ք:", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}