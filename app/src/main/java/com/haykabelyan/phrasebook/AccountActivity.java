package com.haykabelyan.phrasebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    String fname, surname, login, password, address, sex, telephone, email, birthdate, changeDate,
            changeData, changePassword, zodiac;
    boolean eChange = false;
    boolean aChange = false;
    boolean tChange = false;
    TabHost tabHost;
    ProgressDialog PD, PD0;
    TextView textViewNSA, textViewBDA, textViewLSA, textViewADA, textViewEMA, textViewDOTA,
            textViewATA, textViewZSA, textViewTEA, textViewOPA, textViewNPA, textViewRPA, textViewSEA;
    EditText editTextADA, editTextEMA1, editTextEMA2, editTextEMA3, editTextTEA, editTextOPA,
            editTextNPA, editTextRPA;
    Button buttonSCA, buttonACA;

    String url = "http://" + StateHost.URL + "/read_allusers.php";
    String url2 = "http://" + StateHost.URL + "/change_data.php";
    String url3 = "http://" + StateHost.URL + "/change_password.php";

    ArrayList<String> emailsList;
    ArrayList<String> telephonesList;

    // JSON Node names
    public static final String EMAIL = "email";
    public static final String ADDRESS = "address";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String TELEPHONE = "telephone";

    int countE;
    int countT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        emailsList = new ArrayList<String>();
        telephonesList = new ArrayList<String>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Intent intent = getIntent();
        fname = intent.getStringExtra("fname");
        surname = intent.getStringExtra("surname");
        login = intent.getStringExtra("login");
        sex = intent.getStringExtra("sex");
        birthdate = intent.getStringExtra("birthdate");
        zodiac = intent.getStringExtra("zodiac").toLowerCase();
        textViewNSA = (TextView) findViewById(R.id.textViewNSA);
        textViewBDA = (TextView) findViewById(R.id.textViewBDA);
        textViewLSA = (TextView) findViewById(R.id.textViewLSA);
        textViewADA = (TextView) findViewById(R.id.textViewADA);
        textViewEMA = (TextView) findViewById(R.id.textViewEMA);
        textViewDOTA = (TextView) findViewById(R.id.textViewDOTA);
        textViewATA = (TextView) findViewById(R.id.textViewATA);
        textViewTEA = (TextView) findViewById(R.id.textViewTEA);
        textViewOPA = (TextView) findViewById(R.id.textViewOPA);
        textViewRPA = (TextView) findViewById(R.id.textViewRPA);
        textViewNPA = (TextView) findViewById(R.id.textViewNPA);
        textViewSEA = (TextView) findViewById(R.id.textViewSEA);
        textViewZSA = (TextView) findViewById(R.id.textViewZSA);

        editTextADA = (EditText) findViewById(R.id.editTextADA);
        editTextEMA1 = (EditText) findViewById(R.id.editTextEMA1);
        editTextEMA2 = (EditText) findViewById(R.id.editTextEMA2);
        editTextEMA3 = (EditText) findViewById(R.id.editTextEMA3);
        editTextTEA = (EditText) findViewById(R.id.editTextTEA);
        editTextOPA = (EditText) findViewById(R.id.editTextOPA);
        editTextNPA = (EditText) findViewById(R.id.editTextNPA);
        editTextRPA = (EditText) findViewById(R.id.editTextRPA);

        textViewNSA.setText(textViewNSA.getText() + " " + fname + " " + surname);
        textViewBDA.setText(textViewBDA.getText() + " " + birthdate);
        textViewLSA.setText(textViewLSA.getText() + " " + login);
        if (sex.equals("f"))
            textViewSEA.setText(textViewSEA.getText() + " իգական");
        if (sex.equals("m"))
            textViewSEA.setText(textViewSEA.getText() + " արական");
        textViewZSA.setText(textViewZSA.getText() + " " + zodiac);

        buttonSCA = (Button) findViewById(R.id.buttonSCA);
        buttonACA = (Button) findViewById(R.id.buttonACA);
        buttonSCA.setOnClickListener(this);
        buttonACA.setOnClickListener(this);
        buttonACA.setTextColor(Color.WHITE);
        buttonSCA.setTextColor(Color.WHITE);
        buttonSCA.setBackground(getResources().getDrawable(R.drawable.longshape));
        buttonACA.setBackground(getResources().getDrawable(R.drawable.longshape));

        tabHost = (TabHost) findViewById(R.id.tabHostA);
        tabHost.setup();
        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1a");
        View v1 = getLayoutInflater().inflate(R.layout.personal_data_tab_header, null);
        tabSpec.setIndicator(v1);
        tabSpec.setContent(R.id.tabContent1A);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2a");
        View v2 = getLayoutInflater().inflate(R.layout.data_tab_header, null);
        tabSpec.setIndicator(v2);
        tabSpec.setContent(R.id.tabContent2A);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3a");
        View v3 = getLayoutInflater().inflate(R.layout.password_tab_header, null);
        tabSpec.setIndicator(v3);
        tabSpec.setContent(R.id.tabContent3A);
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTabByTag("tag1a");

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

        PD0 = new ProgressDialog(this);
        PD0.setMessage("Խնդրում ենք սպասել...");
        PD0.setCancelable(false);

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
                                    if (login.equalsIgnoreCase(jobj.getString(USERNAME))) {
                                        editTextADA.setText(jobj.getString(ADDRESS));
                                        address = editTextADA.getText().toString();
                                        telephone = jobj.getString(TELEPHONE);
                                        if (telephone.length() < 5)
                                            editTextTEA.setText("");
                                        else
                                            editTextTEA.setText(telephone.substring(4));
                                        email = jobj.getString(EMAIL);
                                        editTextEMA1.setText(email.substring(0, email.indexOf("@")));
                                        editTextEMA2.setText(email.substring(email.indexOf("@") + 1, email.lastIndexOf(".")));
                                        editTextEMA3.setText(email.substring(email.lastIndexOf(".") + 1));
                                        password = jobj.getString(PASSWORD);
                                        i = ja.length();
                                    }
                                }
                            } // for loop ends
                        } catch (JSONException e) {
                            PD0.dismiss();
                            Toast.makeText(getApplicationContext(), "Սերվերի խնդիր:", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD0.dismiss();
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
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clickanim));
        switch (v.getId()) {
            case R.id.buttonSCA:
                if (editTextEMA1.getText().toString().length() * editTextEMA2.getText().toString().length()
                        * editTextEMA3.getText().toString().length() > 0 && editTextADA.getText().length() < 51
                        && editTextEMA1.getText().toString().length() < 40
                        && editTextEMA1.getText().toString().length() > 2 && editTextEMA2.getText().toString().length() < 15
                        && editTextEMA2.getText().toString().length() > 1 && editTextEMA3.getText().toString().length() < 5
                        && editTextEMA3.getText().toString().length() > 1 && (editTextEMA1.getText().toString().length() +
                        editTextEMA2.getText().toString().length() + editTextEMA3.getText().toString().length()) < 49
                        && (editTextTEA.getText().length() == 8 || editTextTEA.getText().length() == 0))
                    checkData();
                else if (editTextEMA1.getText().toString().length() * editTextEMA2.getText().toString().length()
                        * editTextEMA3.getText().toString().length() == 0)
                    Toast.makeText(this, "Լրացրեք էլ. հասցեն ամբողջությամբ:", Toast.LENGTH_LONG).show();
                else if (editTextEMA1.getText().toString().length() < 3)
                    Toast.makeText(this, "էլ. փոստը շատ կարճ է:", Toast.LENGTH_LONG).show();
                else if (editTextEMA1.getText().toString().length() >= 40)
                    Toast.makeText(this, "Նման երկար էլ. փոստ հիշելու ունակություն չունեմ: Կարճ կապեք:",
                            Toast.LENGTH_LONG).show();
                else if (editTextEMA2.getText().toString().length() < 2)
                    Toast.makeText(this, "Էլ. փոստի 1 տառանոց կայք ո՞րտեղ եք տեսել:", Toast.LENGTH_LONG).show();
                else if (editTextEMA2.getText().toString().length() >= 15)
                    Toast.makeText(this, "Էլ. փոստ դաշտում նորմալ կայքեր նշեք:", Toast.LENGTH_LONG).show();
                else if (editTextEMA3.getText().toString().length() < 2)
                    Toast.makeText(this, "Էլ. փոստի սխալ ֆորմատ:", Toast.LENGTH_LONG).show();
                else if (editTextEMA3.getText().toString().length() >= 4)
                    Toast.makeText(this, "Էլ. փոստի սխալ ֆորմատ:", Toast.LENGTH_LONG).show();
                else if (editTextEMA1.getText().toString().length() + editTextEMA2.getText().toString().length() +
                        editTextEMA3.getText().toString().length() > 48)
                    Toast.makeText(this, "Շաաա՜տ երկար էլ. փոստ: Շաաաաաաատ:", Toast.LENGTH_LONG).show();
                else if (editTextADA.getText().toString().length() >= 51)
                    Toast.makeText(this, "Շաաա՜տ երկար հասցե: Շաաաաաաատ:", Toast.LENGTH_LONG).show();
                else if (editTextTEA.getText().toString().length() != 8 && editTextTEA.getText().toString().length() != 0)
                    Toast.makeText(this, "Մուտքագրեք ՁԵՐ ԻՍԿ ԻՍԿԱԿԱՆ հեռախոսահամարը՝ առանց դիմացի 0-ի:", Toast.LENGTH_LONG).show();
                break;
            case R.id.buttonACA:
                if (editTextOPA.getText().toString().equals(password) && editTextNPA.getText().toString().
                        equals(editTextRPA.getText().toString())
                        && editTextNPA.getText().length() * editTextOPA.getText().length() *
                        editTextRPA.getText().length() > 0 && !editTextRPA.getText().toString().equals(password)
                        && editTextRPA.getText().length() > 5 && editTextRPA.getText().length() < 17)
                    setNewPassword();
                else if (editTextNPA.getText().length() * editTextOPA.getText().length() * editTextRPA.getText().length() == 0)
                    Toast.makeText(getApplicationContext(), "Բոլոր դաշտերը լրացրեք:", Toast.LENGTH_LONG).show();
                else if (editTextRPA.getText().length() <= 5)
                    Toast.makeText(getApplicationContext(), "Գաղտնաբառը պետք է ունենա առնվազն 6 նիշ: " +
                            "\n    Հանուն Ձեր իսկ անվտանգության", Toast.LENGTH_LONG).show();
                else if (editTextRPA.getText().length() >= 17)
                    Toast.makeText(getApplicationContext(), "Գաղտնաբառը շատ երկար է:", Toast.LENGTH_LONG).show();
                else if (!editTextOPA.getText().toString().equals(password))
                    Toast.makeText(getApplicationContext(), "Դուք Ձեր գաղտնաբառը չե՞ք հիշում: " +
                            "Թե՞ ուրիշի գաղտնաբառ եք ուզում փոխել... Փոխեք, ինձ ինչ:", Toast.LENGTH_LONG).show();
                else if (!editTextNPA.getText().toString().equals(editTextRPA.getText().toString()))
                    Toast.makeText(getApplicationContext(), "Եղեք ուշադիր: Կրկնեք գաղտնաբառը" +
                            " ճիշտ, կամ փոխեք:", Toast.LENGTH_LONG).show();
                else if (password.equals(editTextRPA.getText().toString()))
                    Toast.makeText(getApplicationContext(), "Ձեր գաղտնաբառը Դուք փոխեցիք նույն գաղտնաբառով: " +
                            "Իիիմաստը...", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void checkData() {
        emailsList.clear();
        telephonesList.clear();
        eChange = false;
        tChange = false;
        aChange = false;
        if (!email.equals(editTextEMA1.getText().toString() + "@" + editTextEMA2.getText().toString() + "." + editTextEMA3.getText().toString()))
            eChange = true;
        if (!telephone.equals("+374" + editTextTEA.getText().toString()) && !editTextTEA.getText().toString().equals(""))
            tChange = true;
        if (!address.equals(editTextADA.getText().toString()))
            aChange = true;
        PD0.show();

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
                                    emailsList.add(jobj.getString(EMAIL));
                                    telephonesList.add(jobj.optString(TELEPHONE));
                                } // for loop ends
                                countE = 0;
                                countT = 0;
                                for (int i = 0; i < emailsList.size(); i++)
                                    if ((editTextEMA1.getText().toString() + "@" + editTextEMA2.getText().toString() + "." + editTextEMA3.getText().toString()).equalsIgnoreCase(emailsList.get(i)))
                                        countE++;
                                for (int i = 0; i < telephonesList.size(); i++)
                                    if (editTextTEA.getText().toString().length() != 0 && ("+374" + editTextTEA.getText()).toString().equalsIgnoreCase(telephonesList.get(i)))
                                        countT++;
                                if (countE > 0 && eChange == true) {
                                    Toast.makeText(getApplicationContext(), (editTextEMA1.getText().toString() + "@" + editTextEMA2.getText().toString() + "." + editTextEMA3.getText().toString()) + " փոստը զբաղված է:", Toast.LENGTH_SHORT).show();
                                    PD0.dismiss();
                                }
                                if (countT > 0 && tChange == true) {
                                    Toast.makeText(getApplicationContext(), "+374" + editTextTEA.getText() + " հեռախոսահամարը արդեն առկա է:", Toast.LENGTH_SHORT).show();
                                    PD0.dismiss();
                                }
                                if (tChange == false && eChange == false && aChange == false) {
                                    Toast.makeText(getApplicationContext(), "Ոչ մի փոփոխություն չկա: Ի՞նչը պահեմ:", Toast.LENGTH_SHORT).show();
                                    PD0.dismiss();
                                }
                                if (!(countE > 0 && eChange == true) && !(countT > 0 && tChange == true) && (tChange == true || eChange == true || aChange == true))
                                    setNewData();
                            } // if ends
                        } catch (JSONException e) {
                            PD0.dismiss();
                            Toast.makeText(getApplicationContext(), "Սերվերի խնդիր:", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD0.dismiss();
                Toast.makeText(getApplicationContext(), "Ինտերնետ կապի խնդիր:", Toast.LENGTH_SHORT).show();
            }
        });
        // Adding request to request queue
        MyApplication.getInstance().addToReqQueue(jreq);
    }

    private void setNewData() {
        changeData = "Personal data is changed ";
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
        if (eChange == true && aChange == false && tChange == false)
            changeData += "(email) ";
        if (eChange == false && aChange == true && tChange == false)
            changeData += "(address) ";
        if (eChange == false && aChange == false && tChange == true)
            changeData += "(telephone) ";
        if (eChange == true && aChange == true && tChange == false)
            changeData += "(email,address) ";
        if (eChange == true && aChange == false && tChange == true)
            changeData += "(email,telephone) ";
        if (eChange == false && aChange == true && tChange == true)
            changeData += "(telephone,address) ";
        if (eChange == true && aChange == true && tChange == true)
            changeData += "(email,address,telephone) ";
        changeData += "on " + changeDate + "\n";
        email = editTextEMA1.getText().toString() + "@" + editTextEMA2.getText().toString() + "." + editTextEMA3.getText().toString();
        if (editTextTEA.getText().toString().length() < 5)
            telephone = "";
        else
            telephone = "+374" + editTextTEA.getText().toString();
        address = editTextADA.getText().toString();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PD0.dismiss();
                        Toast.makeText(getApplicationContext(), "Տվյալները հաջողությամբ փոփոխված են:", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD0.dismiss();
                Toast.makeText(getApplicationContext(),
                        "Սերվերի խնդիր:", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("username", login);
                params.put("changedata", changeData);
                params.put("address", address);
                params.put("telephone", telephone);
                params.put("email", email);

                return params;
            }
        };

        // Adding request to request queue
        MyApplication.getInstance().addToReqQueue(postRequest);
    }

    private void setNewPassword() {
        PD.show();
        changePassword = "";
        password = editTextRPA.getText().toString();
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
        changePassword += "\nPassword is changed on " + changeDate + "\n";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PD.dismiss();
                        Toast.makeText(getApplicationContext(), "Գաղտնաբառը հաջողությամբ փոփոխված է:", Toast.LENGTH_SHORT).show();
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
                params.put("username", login);
                params.put("changepassword", changePassword);
                params.put("password", password);

                return params;
            }
        };

        // Adding request to request queue
        MyApplication.getInstance().addToReqQueue(postRequest);
    }
}