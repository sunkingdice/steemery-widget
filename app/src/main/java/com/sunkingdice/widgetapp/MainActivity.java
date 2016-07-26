package com.sunkingdice.widgetapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        Intent i = new Intent(MainActivity.this, MyAppWidgetProvider.class);
                        i.setAction(MyAppWidgetProvider.ACTION_APPWIDGET_UPDATE);
                        sendBroadcast(i);
                        Log.d("TEST","send");

                    }
                });
            }
        };
        timer.scheduleAtFixedRate(task, 0, (1000*60)*5);
    }
}
