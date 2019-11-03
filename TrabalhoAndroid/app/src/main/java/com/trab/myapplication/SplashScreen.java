package com.trab.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {
    long delay = 100;
    int percent = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        final ProgressBar progressbar = (ProgressBar) findViewById(R.id.progressBar);
        progressbar.getProgressDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);
        final TextView showPercent = (TextView) findViewById(R.id.progress);
        final Timer timer = new Timer();
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                percent+=5;
                progressbar.setProgress(percent);
                showPercent.setText(percent+"%");
                if(percent>=100){
                    timer.cancel();
                    finish();
                    Intent intent = new Intent(SplashScreen.this,LoginScreen.class);
                    startActivity(intent);
                }
            }
        };
        timer.schedule(timerTask,delay);
    }
}
