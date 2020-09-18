package com.victorio.tiketsaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SuccessBuyAct extends AppCompatActivity {
    Button btn_home, btn_cek_ticket;
    Animation anim_splash, anim_bot_top, anim_top_bot;
    TextView textView1, textView2;
    ImageView img_success_buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_buy);
        //function button dari xml
        btn_cek_ticket = findViewById(R.id.btn_view_ticket);
        btn_home = findViewById(R.id.btn_home);

        //function textview dari xml
        textView1 = findViewById(R.id.textViewSuccessBuy1);
        textView2 = findViewById(R.id.textViewSuccessBuy2);

        //load image
        img_success_buy = findViewById(R.id.icon_success_buy);

        //function animasi
        anim_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        anim_bot_top = AnimationUtils.loadAnimation(this, R.anim.bot_to_top);
        anim_top_bot = AnimationUtils.loadAnimation(this, R.anim.top_to_bot);

        //Run animasi
        img_success_buy.startAnimation(anim_splash);
        textView1.startAnimation(anim_top_bot);
        textView2.startAnimation(anim_top_bot);
        btn_cek_ticket.startAnimation(anim_bot_top);
        btn_home.startAnimation(anim_bot_top);


        //load cek tiket di my profile
        btn_cek_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(SuccessBuyAct.this, MyProfileAct.class);
                startActivity(profile);
                finish();
            }
        });

        //back to home atau dashboard
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(SuccessBuyAct.this, HomeAct.class);
                startActivity(home);
                finish();
            }
        });
    }
}
