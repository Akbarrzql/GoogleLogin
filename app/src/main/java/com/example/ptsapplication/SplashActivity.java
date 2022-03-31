package com.example.ptsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = sharedPreferences.edit();

        username = sharedPreferences.getString("username", null);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //cek sharedpref
                if (username ==null || username == ""){
                    Intent home=new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(home);
                    finish();
                }else{
                    Intent home=new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(home);
                    finish();
                }


            }
        }, 3500);

    }
}