package com.victorio.tiketsaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.file.Files;

public class GetStartedAct extends AppCompatActivity {
    Button btn;
    Button btn_create;
    Animation top_bot;
    ImageView emblem_logo;
    TextView header_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        //load animation
        top_bot = AnimationUtils.loadAnimation(this,R.anim.top_to_bot);

        //load button
        btn = findViewById(R.id.btn_sign_in);
        btn_create = findViewById(R.id.btn_new_account_create);
        emblem_logo = findViewById(R.id.emblem);
        header_logo = findViewById(R.id.header);

        //run animation
        btn_create.startAnimation(top_bot);
        btn.startAnimation(top_bot);
        emblem_logo.startAnimation(top_bot);
        header_logo.startAnimation(top_bot);



        //Inisiasi
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abc = new Intent(GetStartedAct.this, SignInAct.class);
                startActivity(abc);

            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abc2 = new Intent(GetStartedAct.this, RegisterOneAct.class);
                startActivity(abc2);

            }
        });
//        String Email = "platkuning13@gmail.com";
//        EditText email;
//
//        email = findViewById(R.id.emailAddress);
//        boolean kondisi = true;
//        if(kondisi == false){
//            email.setText(Email);
//        }
//        else{
//            Toast.makeText(getApplicationContext(), "Ayo isi", Toast.LENGTH_SHORT).show();
//
//        }
//        btn = findViewById(R.id.btnget);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent abc = new Intent(GetStartedAct.this, GetStartedAct2.class);
//                startActivity(abc);
//                finish();
//            }
//        });
    }
}
