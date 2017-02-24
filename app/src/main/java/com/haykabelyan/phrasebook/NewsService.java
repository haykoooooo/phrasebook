package com.haykabelyan.phrasebook;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class NewsService extends Service {

    NotificationManager notificationManager;
    int user_id;
    String fname, surname, login, sessions, password, address, telephone, email, sex, birthdate,
            news_id, header, content, image, rating, views, date;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            int i;

            @Override
            public void run() {
                i++;
                JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.GET, "http://" + StateHost.URL + "/read_allnews_last.php",
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
                                            header = jobj.getString("header");
                                            content = jobj.getString("content");
                                            date = jobj.getString("date");
                                            image = jobj.getString("image");
                                            views = jobj.getString("views");
                                            rating = jobj.getString("rating");
                                            Date date0 = new Date();
                                            date0.setYear(Integer.parseInt(date.substring(0, 4)) - 1900);
                                            date0.setMonth(Integer.parseInt(date.substring(5, 7)) - 1);
                                            date0.setDate(Integer.parseInt(date.substring(8, 10)));
                                            date0.setHours(Integer.parseInt(date.substring(12, 14)));
                                            date0.setMinutes(Integer.parseInt(date.substring(15, 17)));
                                            date0.setSeconds(0);
                                            if ((long) ((long) System.currentTimeMillis() - (long) date0.getTime()) <= (long) 180000)
                                                sendNotification();
                                        } // for loop ends
                                    } // if ends
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                // Adding request to request queue
                MyApplication.getInstance().addToReqQueue(jreq);
            }
        }, 180000);
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    private void sendNotification() {
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(getApplicationContext());
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("id", user_id);
        intent.putExtra("login", login);
        intent.putExtra("password", password);
        intent.putExtra("fname", fname);
        intent.putExtra("surname", surname);
        intent.putExtra("address", address);
        intent.putExtra("sex", sex);
        intent.putExtra("telephone", telephone);
        intent.putExtra("email", email);
        intent.putExtra("birthdate", birthdate);
        intent.putExtra("sessions", sessions);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = nBuilder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.notification).setWhen(System.currentTimeMillis()).setVibrate(new long[]{500, 500})
                .setContentTitle("Նոր լուր:")
                .setContentText(header).build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.flags |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(1, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}