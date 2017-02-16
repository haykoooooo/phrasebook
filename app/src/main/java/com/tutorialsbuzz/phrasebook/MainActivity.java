package com.tutorialsbuzz.phrasebook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends Activity implements View.OnClickListener {

    TextView login, signup, timeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = (TextView) findViewById(R.id.textViewLogin);
        signup = (TextView) findViewById(R.id.textViewSignup);
        timeText = (TextView) findViewById(R.id.textViewTime);
        login.setTextSize(36);
        signup.setTextSize(36);
        timeText.setTextSize(11);
        timeText.setTextColor(Color.rgb(6, 36, 216));
        login.setTextColor(Color.rgb(6, 36, 216));
        signup.setTextColor(Color.rgb(6, 36, 216));
        login.setOnClickListener(this);
        signup.setOnClickListener(this);
        timeText.setText("Ժամանակը Հայաստանում. ");
        new Thread(new UpdateTimeEvery100Milisecond()).start();
    }

    @Override
    public void onClick(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clickanim));
        if (view.getId() == R.id.textViewLogin) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        } else if (view.getId() == R.id.textViewSignup) {
            startActivity(new Intent(getApplicationContext(), SignupActivity.class));
            finish();
        }
    }

    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                Date dt = new Date();
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+4"));
                cal.setTime(dt);
                int year = dt.getYear() + 1900;
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int weekday = cal.get(Calendar.DAY_OF_WEEK);
                int hours = cal.get(Calendar.HOUR_OF_DAY);
                int minutes = dt.getMinutes();
                int seconds = dt.getSeconds();
                String dayOfWeek = "";
                switch (weekday) {
                    case 1:
                        dayOfWeek = "կիրակի";
                        break;
                    case 2:
                        dayOfWeek = "երկուշաբթի";
                        break;
                    case 3:
                        dayOfWeek = "երեքշաբթի";
                        break;
                    case 4:
                        dayOfWeek = "չորեքշաբթի";
                        break;
                    case 5:
                        dayOfWeek = "հինգշաբթի";
                        break;
                    case 6:
                        dayOfWeek = "ուրբաթ";
                        break;
                    case 7:
                        dayOfWeek = "շաբաթ";
                        break;
                }
                timeText.setText("Երևանի ժամանակ. " + year + "." +
                        (month < 10 ? "0" + month : month) + "." + (day < 10 ? "0" + day : day)
                        + ", " + (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes)
                        + ":" + (seconds < 10 ? "0" + seconds : seconds) + ", օրը " + dayOfWeek);
            }
        });
    }

    class UpdateTimeEvery100Milisecond implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }
}