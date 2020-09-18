package com.victorio.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class TicketDetailAct extends AppCompatActivity {
    LinearLayout btn;
    DatabaseReference reference;
    Button btn_buy;
    TextView titleTiket, locationTicker, foto_spot_tiket, wifi_tiket, festival_tiket, short_desc_tiket;
    ImageView header_tiket_detail, btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        //load button dari xml
        btn = findViewById(R.id.btn_back);
        btn_buy = findViewById(R.id.btn_buy);
        titleTiket = findViewById(R.id.titleTicket);
        locationTicker = findViewById(R.id.locationTicket);
        foto_spot_tiket = findViewById(R.id.foto_spot_ticket);
        wifi_tiket = findViewById(R.id.wifi_ticket);
        festival_tiket = findViewById(R.id.festival_ticket);
        short_desc_tiket = findViewById(R.id.short_desc_ticket);
        header_tiket_detail = findViewById(R.id.header_ticket_detail);
        btn_back = findViewById(R.id.btn_back_ticket_detail);

        //mengambil String dari activity lain atau inten
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");
        Toast.makeText(getApplicationContext(), "Tiket : " + jenis_tiket_baru, Toast.LENGTH_SHORT).show();
        //MENGAMBIL DATA DARI FIREBASE BERDASARKAN INTENT
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //menyimpan data yang ada dengan yang baru
                titleTiket.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                locationTicker.setText(dataSnapshot.child("lokasi").getValue().toString());
                foto_spot_tiket.setText(dataSnapshot.child("is_photo_spot").getValue().toString());
                wifi_tiket.setText(dataSnapshot.child("is_wifi").getValue().toString());
                festival_tiket.setText(dataSnapshot.child("is_festival").getValue().toString());
                short_desc_tiket.setText(dataSnapshot.child("short_desc").getValue().toString());

                Picasso.with(TicketDetailAct.this).load(dataSnapshot.child("url_thumbnail").getValue()
                        .toString()).into(header_tiket_detail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checkout = new Intent(TicketDetailAct.this, TicketCheckoutAct.class);
                //meletakan data kepada intent
                checkout.putExtra("jenis_tiket", jenis_tiket_baru);
                startActivity(checkout);

            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
    }
}
