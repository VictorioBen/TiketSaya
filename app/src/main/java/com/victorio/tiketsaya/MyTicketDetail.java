package com.victorio.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyTicketDetail extends AppCompatActivity {
    DatabaseReference reference;
    TextView xnama_wisata, xlokasi, xtime_wisata, xdate_wisata, xketentuan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket_detail);

        //registrasi
        xnama_wisata = findViewById(R.id.xnama_wisata);
        xlokasi  = findViewById(R.id.xlokasi);
        xtime_wisata = findViewById(R.id.xtime_wisata   );
        xketentuan = findViewById(R.id.xketentuan);
        xdate_wisata = findViewById(R.id.xdate_wisata);



        //mengambil String dari activity lain atau inten
        Bundle bundle = getIntent().getExtras();
        final String nama_wisata_baru = bundle.getString("nama_wisata");

        //mengambil data dari firebase
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(nama_wisata_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                xnama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                xlokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                xketentuan.setText(dataSnapshot.child("ketentuan").getValue().toString());
                xdate_wisata.setText(dataSnapshot.child("date_wisata").getValue().toString());
                xtime_wisata.setText(dataSnapshot.child("time_wisata").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
