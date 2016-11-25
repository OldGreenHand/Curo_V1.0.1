package com.example.comp8715.curo.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.comp8715.curo.Persistent.StaticTool;
import com.example.comp8715.curo.R;


public class WelcomeActivity extends AppCompatActivity {
    private StaticTool staticTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        staticTool = new StaticTool(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}
