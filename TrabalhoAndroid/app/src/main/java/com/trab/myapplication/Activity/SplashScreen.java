package com.trab.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.trab.myapplication.Model.ConnectionFactory;
import com.trab.myapplication.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {
    long delay = 100;
    int percent = 0;
    TimerTask timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        ConnectionFactory coneccao = new ConnectionFactory(getApplicationContext());
        final ProgressBar progressbar = (ProgressBar) findViewById(R.id.progressBar);
        progressbar.getProgressDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);
        final TextView showPercent = (TextView) findViewById(R.id.progress);
        /*final Timer timer = new Timer();
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                percent+=5;
                progressbar.setProgress(percent);
                showPercent.setText(percent+"%");
                if(percent>=100){
                    timer.cancel();
                    finish();
                    SharedPreferences preferences = getSharedPreferences(LoginScreen.SAVED_USER,0);
                    if(preferences.contains("LoggedUserId")&&preferences.getInt("LoggedUserId",-1)>=0){
                        Intent intent = new Intent(SplashScreen.this,TimeLine.class);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(SplashScreen.this,LoginScreen.class);
                        startActivity(intent);
                    }
                }
            }
        };
        timer.schedule(timerTask,delay);*/

        final Timer t = new Timer();
        timer = new TimerTask() {
            @Override
            public void run() {
                percent+=5;
                progressbar.setProgress(percent);
                showPercent.setText(percent+"%");
                if(percent == 100){
                    t.cancel();
                    SharedPreferences preferences = getSharedPreferences(LoginScreen.SAVED_USER,0);
                    StartActivity(preferences.contains("LoggedUserId")&&preferences.getInt("LoggedUserId",-1)>=0);
                    finish();
                }
            }
        };
        t.schedule(timer,0,100);
        /*if(percent==100){
            timer.cancel();

        }*/
    }
    public void StartActivity(boolean bool){
        if(bool){
            Intent intent = new Intent(this,TimeLine.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this,LoginScreen.class);
            startActivity(intent);
        }
    }
}
