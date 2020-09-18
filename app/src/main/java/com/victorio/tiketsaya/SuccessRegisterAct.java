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

public class SuccessRegisterAct extends AppCompatActivity {
    Button btn;
    ImageView image;
    TextView text1, text2;
    Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_register);
        //load anim
        anim = AnimationUtils.loadAnimation(this, R.anim.app_splash);

        //load button
        btn = findViewById(R.id.btn_explore);
        image = findViewById(R.id.imageSuccess);
        text1 = findViewById(R.id.textView1);
        text2 = findViewById(R.id.textView2);

        //run animation
        btn.startAnimation(anim);
        image.startAnimation(anim);
        text2.startAnimation(anim);
        text1.startAnimation(anim);

        //pindah activity
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abc = new Intent(SuccessRegisterAct.this, HomeAct.class);
                startActivity(abc);
                finish();
            }
        });
    }
}
