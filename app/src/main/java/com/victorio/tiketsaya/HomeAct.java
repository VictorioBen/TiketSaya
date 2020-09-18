package com.victorio.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ImageViewCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeAct extends AppCompatActivity {
    BillingProcessor bp;
    Button btn_upgrade_pro;
    LinearLayout btnPisa, btnTorri, btnPagoda, btnCandi, btnSphink, btnMonas;
    ImageView photo_home_user;
    TextView nama_lengkap, bio, balance;
    DatabaseReference reference;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";


    //  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getUsernameLocal();

        //     bp = new BillingProcessor(this, "", this);
        //   bp.initialize();

        //load button
        btnPisa = findViewById(R.id.btn_ticket_pisa);
        btnTorri = findViewById(R.id.btn_ticket_tori);
        btnPagoda = findViewById(R.id.btn_ticket_pagoda);
        btnCandi = findViewById(R.id.btn_ticket_candi);
        btnSphink = findViewById(R.id.btn_ticket_sphinx);
        btnMonas = findViewById(R.id.btn_ticket_monas);
        //load my profile image
        photo_home_user = findViewById(R.id.photo_home_user);
        //load text view
        nama_lengkap = findViewById(R.id.nama_lengkap);
        bio = findViewById(R.id.bio);
        balance = findViewById(R.id.user_balance);

        //menggambil data dari firebase
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama_lengkap.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                bio.setText(dataSnapshot.child("bio").getValue().toString());
                balance.setText("Rp. " + dataSnapshot.child("user_balance").getValue().toString());
                Picasso.with(HomeAct.this).load(dataSnapshot.child("url_photo_profile").getValue()
                        .toString()).noFade().into(photo_home_user);
//                Glide.with(HomeAct.this).load(dataSnapshot.child("url_photo_profile").getValue().toString()).dontAnimate()
//                        .into(photo_home_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //button tiap menu
        btnPisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pisa = new Intent(HomeAct.this, TicketDetailAct.class);
                //meletakan data kepada inten
                pisa.putExtra("jenis_tiket", "Pisa");
                startActivity(pisa);
            }
        });

        btnTorri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent torri = new Intent(HomeAct.this, TicketDetailAct.class);
                torri.putExtra("jenis_tiket", "Torri");
                startActivity(torri);
            }
        });

        btnPagoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pagoda = new Intent(HomeAct.this, TicketDetailAct.class);

                pagoda.putExtra("jenis_tiket", "Pagoda");
                startActivity(pagoda);
            }
        });

        btnCandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent candi = new Intent(HomeAct.this, TicketDetailAct.class);
                candi.putExtra("jenis_tiket", "Candi");
                startActivity(candi);
            }
        });

        btnSphink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sphinx = new Intent(HomeAct.this, TicketDetailAct.class);
                sphinx.putExtra("jenis_tiket", "Sphinx");
                startActivity(sphinx);
            }
        });

        btnMonas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent monas = new Intent(HomeAct.this, TicketDetailAct.class);
                monas.putExtra("jenis_tiket", "Monas");
                startActivity(monas);
            }
        });
        //


        //button photo profil to detail profile
        photo_home_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myprofile = new Intent(HomeAct.this, MyProfileAct.class);
                startActivity(myprofile);
            }
        });

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        if (bp != null) {
//            bp.release();
//        }
//        super.onDestroy();
//    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");

    }

//    @Override
//    public void onProductPurchased(String productId, TransactionDetails details) {
//        Toast.makeText(getApplicationContext(), "Gagal beli", Toast.LENGTH_SHORT).show();
//        //update ke firebase
//    }
//
//    @Override
//    public void onPurchaseHistoryRestored() {
//
//    }
//
//    @Override
//    public void onBillingError(int errorCode, Throwable error) {
//        Toast.makeText(getApplicationContext(), "Gagal beli", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onBillingInitialized() {
//
//    }
}
