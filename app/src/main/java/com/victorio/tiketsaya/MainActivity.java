package com.victorio.tiketsaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {
    Animation app_splash, bot_top;
    ImageView logo;
    TextView sub_logo;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //load animation
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        bot_top = AnimationUtils.loadAnimation(this, R.anim.bot_to_top);
        //load element
        logo = findViewById(R.id.app_logo);
        sub_logo = findViewById(R.id.subtitle_logo);

        //run animation
        logo.startAnimation(app_splash);
        sub_logo.startAnimation(bot_top);
//        logo = findViewById(R.id.applogo);
//        //memberikan event kepada logo
//        logo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //menuju halaman lain
//                Intent abc = new Intent(MainActivity.this, GetStartedAct.class);
//                startActivity(abc);
//                finish();
//            }
//        });
            getUsernameLocal();
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
        if(username_key_new.isEmpty()){
            //Timer perpindahan logo ke homepage
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Menuju halaman lain
                    Intent a = new Intent(MainActivity.this, GetStartedAct.class);
                    startActivity(a);
                    finish();
                }
            }, 2500);


        }else{
            //Timer perpindahan logo ke homepage
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Menuju halaman lain
                    Intent b = new Intent(MainActivity.this, HomeAct.class);
                    startActivity(b);
                    finish();
                }
            }, 2500);


        }
    }
}
