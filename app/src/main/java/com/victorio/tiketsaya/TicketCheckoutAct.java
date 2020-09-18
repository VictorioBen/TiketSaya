package com.victorio.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class TicketCheckoutAct extends AppCompatActivity {
    Button btn_pay, btn_min, btn_plus;
    LinearLayout btn_back;
    TextView txt_jumlah_ticket, txt_balance_cs, txt_harga_tiket, nama_wisata, lokasi, ketentuan;
    ImageView img_tidak_cukup;
    Integer nomor_transaksi = new Random().nextInt();
    Integer val_jml_ticket = 1;
    Integer mybalance = 0;
    Integer tot_harga = 0;
    Integer val_harga_tiket = 0;
    Integer sisa_balance = 0;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";
    String date_wisata = "";
    String time_wisata = "";
    DatabaseReference reference, reference2, reference3, reference4;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);

        getUsernameLocal();
        //mengambil String dari activity lain atau inten
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");

        //load image
        img_tidak_cukup = findViewById(R.id.image_tidak_cukup);

        //load button
        btn_pay = findViewById(R.id.btn_pay);
        btn_min = findViewById(R.id.btn_min);
        btn_plus = findViewById(R.id.btn_plus);
        btn_back = findViewById(R.id.btn_back_ticket_checkout);

        //load text
        txt_jumlah_ticket = findViewById(R.id.txt_jumlah_ticket);
        txt_balance_cs = findViewById(R.id.user_balance);
        txt_harga_tiket = findViewById(R.id.txt_harga_tiket);
        nama_wisata = findViewById(R.id.nama_wisata);
        lokasi = findViewById(R.id.lokasi);
        ketentuan = findViewById(R.id.ketentuan);

        //mengatur jumlah ticket awal dan button
        txt_jumlah_ticket.setText(val_jml_ticket.toString());
        btn_min.animate().alpha(0).setDuration(300).start();
        btn_min.setEnabled(false);

        //mengatur image tidak cukup diawal
        img_tidak_cukup.setVisibility(View.GONE);

        //mengambil data dari firebase dengan child users dan user key
        reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mybalance = Integer.valueOf(dataSnapshot.child("user_balance").getValue().toString());
                //mengatur dompet customer diawal
                txt_balance_cs.setText("Rp. " + mybalance + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        //MENGAMBIL DATA DARI FIREBASE BERDASARKAN INTENT
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //menyimpan data yang ada dengan yang baru
                nama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                lokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                ketentuan.setText(dataSnapshot.child("ketentuan").getValue().toString());
                date_wisata = dataSnapshot.child("date_wisata").getValue().toString();
                time_wisata = dataSnapshot.child("time_wisata").getValue().toString();
                val_harga_tiket = Integer.valueOf(dataSnapshot.child("harga_tiket").getValue().toString());
                //mengatur harga tiket diawal
                txt_harga_tiket.setText("Rp" + val_harga_tiket + "");
                tot_harga = val_jml_ticket * val_harga_tiket;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //menggunakan LinearLayout button back
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        //menggunakan  button mines
        btn_min.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                val_jml_ticket -= 1;
                txt_jumlah_ticket.setText(val_jml_ticket.toString());
                if (val_jml_ticket < 2) {
                    btn_min.animate().alpha(0).setDuration(300).start();
                    btn_min.setEnabled(false);
                }
                tot_harga = val_jml_ticket * val_harga_tiket;
                txt_harga_tiket.setText("Rp" + tot_harga.toString());
                if (tot_harga <= mybalance) {
                    btn_pay.animate().translationY(0).alpha(1).setDuration(350).start();
                    btn_pay.setEnabled(true);
                    txt_balance_cs.setTextColor(Color.parseColor("#203DD1"));
                    img_tidak_cukup.setVisibility(View.GONE);

                }
            }
        });

        //menggunakan button plus
        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                val_jml_ticket += 1;
                txt_jumlah_ticket.setText(val_jml_ticket.toString());
                if (val_jml_ticket > 1) {
                    btn_min.animate().alpha(1).setDuration(300).start();
                    btn_min.setEnabled(true);
                }
                tot_harga = val_jml_ticket * val_harga_tiket;
                txt_harga_tiket.setText("Rp" + tot_harga.toString());
                if (tot_harga > mybalance) {
                    btn_pay.animate().translationY(250).alpha(0).setDuration(350).start();
                    btn_pay.setEnabled(false);
                    txt_balance_cs.setTextColor(Color.parseColor("#D1206B"));
                    img_tidak_cukup.setVisibility(View.VISIBLE);

                }
            }
        });


        //menggunakan button pay
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mengambil data user kepada firebase dan membuat table baru "MyTickets"
                reference3 = FirebaseDatabase.getInstance().getReference().child("MyTickets")
                        .child(username_key_new).child(nama_wisata.getText().toString() + nomor_transaksi);
                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reference3.getRef().child("id_ticket").setValue(nama_wisata.getText().toString() + nomor_transaksi);
                        reference3.getRef().child("nama_wisata").setValue(nama_wisata.getText().toString());
                        reference3.getRef().child("lokasi").setValue(lokasi.getText().toString());
                        reference3.getRef().child("ketentuan").setValue(ketentuan.getText().toString());
                        reference3.getRef().child("jumlah_tiket").setValue(val_jml_ticket.toString());
                        reference3.getRef().child("date_wisata").setValue(date_wisata);
                        reference3.getRef().child("time_wisata").setValue(time_wisata);

                        Intent pay = new Intent(TicketCheckoutAct.this, SuccessBuyAct.class);
                        startActivity(pay);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                //UPDATE BALANCE KEPADA USERS (YANG SAAT INI LOGIN)
                reference4 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                reference4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        sisa_balance = mybalance - tot_harga;
                        reference4.getRef().child("user_balance").setValue(sisa_balance);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
