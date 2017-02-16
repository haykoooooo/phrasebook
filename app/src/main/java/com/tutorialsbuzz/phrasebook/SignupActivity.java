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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
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

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    EditText name, surname, login, password, password2, address, telephone, email, email2, email3;
    Spinner spinnerDay, spinnerMonth, spinnerYear;
    RadioButton radiom, radiof;
    Button signup;
    ProgressDialog PD;
    String[] days;
    String[] months;
    String[] years;
    ArrayAdapter<String> daysAdapter;
    ArrayAdapter<String> monthsAdapter;
    ArrayAdapter<String> yearsAdapter;

    String url = "http://" + StateHost.URL + "/read_allusers.php";
    String url2 = "http://" + StateHost.URL + "/register_user.php";

    ArrayList<String> usernamesList;
    ArrayList<String> emailsList;
    ArrayList<String> telephonesList;
    String usernameField, passwordField, nameField, surnameField, registerDate, emailField,
            telephoneField, addressField, birthDate, sex = "m";
    // JSON Node names
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String TELEPHONE = "telephone";
    String registerDialogText = "Շնորհավորում եմ, գրանցումն ավարտված է:";
    private Drawable registeredIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        years = new String[90];
        years[0] = "տարի...";
        for (int i = 1; i < 90; i++)
            years[i] = 2001 - i + "";
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
        spinnerDay = (Spinner) findViewById(R.id.spinner);
        spinnerMonth = (Spinner) findViewById(R.id.spinner2);
        spinnerYear = (Spinner) findViewById(R.id.spinner3);
        spinnerYear.setAdapter(yearsAdapter);
        spinnerMonth.setAdapter(monthsAdapter);
        spinnerMonth.setEnabled(false);
        spinnerDay.setAdapter(daysAdapter);
        spinnerDay.setEnabled(false);
        name = (EditText) findViewById(R.id.editText3);
        surname = (EditText) findViewById(R.id.editText4);
        login = (EditText) findViewById(R.id.editText5);
        password = (EditText) findViewById(R.id.editText6);
        password2 = (EditText) findViewById(R.id.editText7);
        address = (EditText) findViewById(R.id.editText111);
        telephone = (EditText) findViewById(R.id.editText112);
        email = (EditText) findViewById(R.id.editText113);
        email2 = (EditText) findViewById(R.id.editTextMailWebsite);
        email3 = (EditText) findViewById(R.id.editTextDot);
        radiom = (RadioButton) findViewById(R.id.radiom);
        radiof = (RadioButton) findViewById(R.id.radiof);
        signup = (Button) findViewById(R.id.button3);
        signup.setTextColor(Color.WHITE);
        signup.setBackground(getResources().getDrawable(R.drawable.longshape));
        signup.setOnClickListener(this);
        PD = new ProgressDialog(this);
        PD.setMessage("Բեռնում...");
        PD.setCancelable(false);

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    spinnerMonth.setEnabled(false);
                    spinnerDay.setEnabled(false);
                } else
                    spinnerMonth.setEnabled(true);
                if (spinnerMonth.getSelectedItemPosition() == 2 && position != 0) {
                    if ((position - 1) % 4 == 0)
                        setDays(29);
                    else setDays(28);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerMonth.setEnabled(false);
                spinnerDay.setEnabled(false);
            }
        });

        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    spinnerDay.setEnabled(false);
                if (position == 1)
                    setDays(31);
                if (position == 2) {
                    if ((spinnerYear.getSelectedItemPosition() - 1) % 4 == 0)
                        setDays(29);
                    else
                        setDays(28);
                }
                if (position == 3)
                    setDays(31);
                if (position == 4)
                    setDays(30);
                if (position == 5)
                    setDays(31);
                if (position == 6)
                    setDays(30);
                if (position == 7)
                    setDays(31);
                if (position == 8)
                    setDays(31);
                if (position == 9)
                    setDays(30);
                if (position == 10)
                    setDays(31);
                if (position == 11)
                    setDays(30);
                if (position == 12)
                    setDays(31);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerDay.setEnabled(false);
            }
        });

        spinnerYear.setFocusableInTouchMode(true);
        spinnerYear.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (spinnerYear.getWindowToken() != null) {
                        spinnerYear.performClick();
                    }
                }
            }
        });

        spinnerMonth.setFocusableInTouchMode(true);
        spinnerMonth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (spinnerMonth.getWindowToken() != null) {
                        spinnerMonth.performClick();
                    }
                }
            }
        });

        spinnerDay.setFocusableInTouchMode(true);
        spinnerDay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (spinnerDay.getWindowToken() != null) {
                        spinnerDay.performClick();
                    }
                }
            }
        });

        radiom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex = "m";
                radiof.setChecked(false);
            }
        });

        radiof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex = "f";
                radiom.setChecked(false);
            }
        });
    }

    public void setDays(int k) {
        spinnerDay.setEnabled(true);
        days = new String[k + 1];
        days[0] = "օր...";
        for (int i = 1; i < k + 1; i++)
            days[i] = i + "";
        daysAdapter = new ArrayAdapter<String>(this, R.layout.dayspinner, days);
        spinnerDay.setAdapter(daysAdapter);
    }

    @Override
    public void onClick(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clickanim));
        if (name.getText().toString().length() > 0 && surname.getText().toString().length() > 0 &&
                login.getText().toString().length() > 0 && password.getText().toString().length() > 0 &&
                password2.getText().toString().length() > 0 && email.getText().toString().length() > 0 &&
                email2.getText().toString().length() > 0 && email3.getText().toString().length() > 0 &&
                spinnerDay.getSelectedItemPosition() > 0 && spinnerMonth.getSelectedItemPosition() > 0 &&
                (spinnerYear.getSelectedItemPosition()) > 0) {
            if (password.getText().toString().equals(password2.getText().toString()) &&
                    login.getText().toString().length() > 3 && login.getText().toString().length() < 17 &&
                    password.getText().toString().length() > 5 && email.getText().toString().length() < 40
                    && email.getText().toString().length() > 2 && email2.getText().toString().length() < 15
                    && email2.getText().toString().length() > 1 && email3.getText().toString().length() < 5
                    && email3.getText().toString().length() > 1 && (email.getText().toString().length() +
                    email2.getText().toString().length() + email3.getText().toString().length()) < 49
                    && address.getText().length() < 51
                    && (telephone.getText().length() == 8 || telephone.getText().length() == 0)) {
                registerAttempt();
            } else if (login.getText().toString().length() <= 3) {
                Toast.makeText(this, "Մուտքանունը գոնե 4 տառ սարքեք: Տեր Վռազն ե՞ք:",
                        Toast.LENGTH_LONG).show();
                registerDialogText = "Շնորհավորում եմ, գրանցումն ավարտված է, Տեր Վռազ ջան:";
            } else if (login.getText().toString().length() > 16) {
                Toast.makeText(this, "Մուտքանունը շատ երկար է:",
                        Toast.LENGTH_LONG).show();
                registerDialogText = "Շնորհավորում եմ, գրանցումն ավարտված է: Վերջապես:";
            } else if (password.getText().toString().length() <= 5) {
                Toast.makeText(this, "Գաղտնաբառը պետք է ունենա առնվազն 6 նիշ: " +
                                "\n    Հանուն Ձեր իսկ անվտանգության",
                        Toast.LENGTH_LONG).show();
                registerDialogText = "Շնորհավորում եմ, գրանցումն ավարտված է, վտանգ ջան:";
            } else if (!password.getText().toString().equals(password2.getText().toString())) {
                Toast.makeText(this, "Սկլերո՞զ... Գաղտնաբառերը տարբեր են:",
                        Toast.LENGTH_LONG).show();
                registerDialogText = "Շնորհավորում եմ, գրանցումն ավարտված է: Հուսով եմ, " +
                        "գաղտնաբառը էլ չեք մոռանա:";
            } else if (email.getText().toString().length() < 3) {
                Toast.makeText(this, "էլ. փոստը շատ կարճ է: Ով ալարի, ոչ դալարի:",
                        Toast.LENGTH_LONG).show();
                registerDialogText = "Շնորհավորում եմ, գրանցումն ավարտված է, վռազ ջան:";
            } else if (email.getText().toString().length() >= 40) {
                Toast.makeText(this, "Նման երկար էլ. փոստ հիշելու ունակություն չունեմ: Կարճ կապեք:",
                        Toast.LENGTH_LONG).show();
                registerDialogText = "Շնորհավորում եմ, գրանցումն ավարտված է: Վերջապես:";
            } else if (email2.getText().toString().length() < 2) {
                Toast.makeText(this, "Էլ. փոստի 1 տառանոց կայք ո՞րտեղ եք տեսել:",
                        Toast.LENGTH_LONG).show();
                registerDialogText = "Շնորհավորում եմ, գրանցումն ավարտված է, վռազ ջան:";
            } else if (email2.getText().toString().length() >= 15) {
                Toast.makeText(this, "Էլ. փոստ դաշտում նորմալ կայքեր նշեք:",
                        Toast.LENGTH_LONG).show();
                registerDialogText = "Շնորհավորում եմ, գրանցումն ավարտված է: Վերջապես:";
            } else if (email3.getText().toString().length() < 2) {
                Toast.makeText(this, "Էլ. փոստի սխալ ֆորմատ:",
                        Toast.LENGTH_LONG).show();
            } else if (email3.getText().toString().length() >= 4) {
                Toast.makeText(this, "Էլ. փոստի սխալ ֆորմատ:",
                        Toast.LENGTH_LONG).show();
                registerDialogText = "Շնորհավորում եմ, գրանցումն ավարտված է, վտանգ ջան:";
            } else if (email.getText().toString().length() + email2.getText().toString().length() +
                    email3.getText().toString().length() > 48) {
                Toast.makeText(this, "Շաաա՜տ երկար էլ. փոստ: Չեք ալարե՞լ:",
                        Toast.LENGTH_LONG).show();
                registerDialogText = "Շնորհավորում եմ, գրանցումն ավարտված է: Չալարեք: Անպայման մուտք գործեք:";
            } else if (address.getText().toString().length() >= 51) {
                Toast.makeText(this, "Շաաա՜տ երկար հասցե: Չեք ալարե՞լ:",
                        Toast.LENGTH_LONG).show();
                registerDialogText = "Շնորհավորում եմ, գրանցումն ավարտված է: Չալարեք: Անպայման մուտք գործեք:";
            } else if (telephone.getText().toString().length() != 8 && telephone.getText().toString().length() != 0) {
                Toast.makeText(this, "Մուտքագրեք հեռախոսահամարն առանց դիմացի 0-ի",
                        Toast.LENGTH_LONG).show();
                registerDialogText = "Շնորհավորում եմ, գրանցումն ավարտված է: Բարի գալուստ:";
            }
        } else if (email.getText().toString().length() == 0) {
            Toast.makeText(this, "Առանց էլ. փոստ չենք գրանցում:",
                    Toast.LENGTH_LONG).show();
            registerDialogText = "Շնորհավորում եմ, գրանցումն ավարտված է, վռազ ջան: Դե ես վռազ եմ, OK սեղմեք:";
        } else if (spinnerYear.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Ձեր ծննդյան տարեթիվը ԳՈՆԵ ՄՈՏԱՎՈՐԱՊԵՍ չե՞ք հիշում:",
                    Toast.LENGTH_LONG).show();
            registerDialogText = "Շնորհավորում եմ, գրանցումն ավարտված է, վռազ ջան: Դե ես վռազ եմ, OK սեղմեք:";
        } else if (spinnerDay.getSelectedItemPosition() == 0 || spinnerDay.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Ձեր ծննդյան ամիս-ամսաթիվը ԳՈՆԵ ՄՈՏԱՎՈՐԱՊԵՍ չե՞ք հիշում:",
                    Toast.LENGTH_LONG).show();
            registerDialogText = "Շնորհավորում եմ, գրանցումն ավարտված է, վռազ ջան: Դե ես վռազ եմ, OK սեղմեք:";
        } else if (name.getText().toString().length() == 0 || surname.getText().toString().length() == 0) {
            Toast.makeText(this, "Ձեր անուն-ազգանունը ԳՈՆԵ ՄՈՏԱՎՈՐԱՊԵՍ չե՞ք հիշում:",
                    Toast.LENGTH_LONG).show();
            registerDialogText = "Շնորհավորում եմ, գրանցումն ավարտված է, վռազ ջան: Դե ես վռազ եմ, OK սեղմեք:";
        } else
            Toast.makeText(this, "ԲՈԼՈՐ ԴԱՇՏԵՐԸ պարտադիր են լրացման համար` ԲԱՑԱՌՈՒԹՅԱՄԲ հասցեի և հեռախոսահամարի դաշտերի", Toast.LENGTH_LONG).show();
    }

    private void registerAttempt() {
        PD.show();
        usernamesList = new ArrayList<String>();
        emailsList = new ArrayList<String>();
        telephonesList = new ArrayList<String>();
        emailField = email.getText().toString() + "@" + email2.getText().toString() + "." + email3.getText().toString();
        if (telephone.getText().toString().length() == 0)
            telephoneField = "";
        else
            telephoneField = "+374" + telephone.getText().toString();

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
                                    usernamesList.add(jobj.getString(USERNAME));
                                    emailsList.add(jobj.getString(EMAIL));
                                    telephonesList.add(jobj.getString(TELEPHONE));
                                } // for loop ends
                                int countL = 0;
                                int countE = 0;
                                int countT = 0;
                                for (int i = 0; i < usernamesList.size(); i++)
                                    if (login.getText().toString().equalsIgnoreCase(usernamesList.get(i)))
                                        countL++;
                                for (int i = 0; i < emailsList.size(); i++)
                                    if (emailField.equalsIgnoreCase(emailsList.get(i)))
                                        countE++;
                                for (int i = 0; i < telephonesList.size(); i++)
                                    if (telephoneField.length() != 0 && telephoneField.equalsIgnoreCase(telephonesList.get(i)))
                                        countT++;
                                if (countL > 0) {
                                    Toast.makeText(getApplicationContext(), login.getText().toString() + " մուտքանունը զբաղված է:", Toast.LENGTH_SHORT).show();
                                    PD.dismiss();
                                }
                                if (countE > 0) {
                                    Toast.makeText(getApplicationContext(), emailField + " փոստը զբաղված է:", Toast.LENGTH_SHORT).show();
                                    PD.dismiss();
                                }
                                if (countT > 0) {
                                    Toast.makeText(getApplicationContext(), telephoneField + " հեռախոսահամարը արդեն առկա է:", Toast.LENGTH_SHORT).show();
                                    PD.dismiss();
                                }
                                if (countE + countL + countT == 0)
                                    register();
                            } // if ends
                        } catch (JSONException e) {
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

    private void register() {
        registeredIcon = getResources().getDrawable(R.drawable.registered);
        PD.show();
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
        registerDate = year + "." + month + "." + day + ", " + hour + ":" + minute + ":" + second;
        usernameField = login.getText().toString();
        passwordField = password.getText().toString();
        nameField = name.getText().toString();
        surnameField = surname.getText().toString();
        addressField = address.getText().toString();
        String birthMonth = spinnerMonth.getSelectedItemPosition() + "";
        if (spinnerMonth.getSelectedItemPosition() < 10)
            birthMonth = "0" + birthMonth;
        String birthDay = spinnerDay.getSelectedItemPosition() + "";
        if (spinnerDay.getSelectedItemPosition() < 10)
            birthDay = "0" + birthDay;
        birthDate = (2001 - spinnerYear.getSelectedItemPosition()) + "." + birthMonth + "." + birthDay;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PD.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                        builder.setTitle("Վերջ")
                                .setMessage(registerDialogText)
                                .setIcon(registeredIcon)
                                .setCancelable(false)
                                .setNegativeButton("ОК",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                SignupActivity.this.finish();
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
                params.put("username", usernameField);
                params.put("password", passwordField);
                params.put("fname", nameField);
                params.put("surname", surnameField);
                params.put("date", registerDate);
                params.put("address", addressField);
                params.put("telephone", telephoneField);
                params.put("email", emailField);
                params.put("birthdate", birthDate);
                params.put("sex", sex);

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