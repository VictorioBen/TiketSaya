package com.victorio.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterOneAct extends AppCompatActivity {
    DatabaseReference reference, reference_username, reference_email;
    Button next;
    EditText xusername, xpassword, xemail_address;
    ImageView btn_back;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);

        //load button
        next = findViewById(R.id.btn_continue);
        xusername = findViewById(R.id.username);
        xpassword = findViewById(R.id.password);
        xemail_address = findViewById(R.id.email_address);
        btn_back = findViewById(R.id.btn_back_regis_one);


        //lanjut ke tahap register ke 2
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ubah state menjadi loading
                next.setText("Loading ...");
                next.setEnabled(false);
                final String username = xusername.getText().toString();
                final String password = xpassword.getText().toString();
                final String email_address = xemail_address.getText().toString();
                //mengambil username dari firebase
                reference_username = FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(xusername.getText().toString());
                reference_username.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(getApplicationContext(), "Username sudah ada! ", Toast.LENGTH_SHORT).show();
                            next.setText("CONTINUE");
                            next.setEnabled(true);

                        }
                        else if (username.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Username Kosong ! ", Toast.LENGTH_SHORT).show();
                            next.setText("CONTINUE");
                            next.setEnabled(true);
                        } else if (password.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Password Kosong ! ", Toast.LENGTH_SHORT).show();
                            next.setText("CONTINUE");
                            next.setEnabled(true);
                        } else if (email_address.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Email Kosong ! ", Toast.LENGTH_SHORT).show();
                            next.setText("CONTINUE");
                            next.setEnabled(true);
                        } else {
                            //Menyimpan data kepada local storage
                            SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(username_key, xusername.getText().toString());
                            editor.apply();

                            //Simpan kepada database
                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(xusername.getText().toString());
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    dataSnapshot.getRef().child("username").setValue(xusername.getText().toString());
                                    dataSnapshot.getRef().child("password").setValue(xpassword.getText().toString());
                                    dataSnapshot.getRef().child("email_address").setValue(xemail_address.getText().toString());
                                    dataSnapshot.getRef().child("user_balance").setValue(40000);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            Intent lanjut = new Intent(RegisterOneAct.this, RegisterTwoAct.class);
                            startActivity(lanjut);
                            finish();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

//                reference_email = FirebaseDatabase.getInstance().getReference().child("Users")
//                        .child(xemail_address.getText().toString());
//                reference_email.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            Toast.makeText(getApplicationContext(), "Email sudah ada! ", Toast.LENGTH_SHORT).show();
//                            next.setText("CONTINUE");
//                            next.setEnabled(true);
//                        }
//
//                    }
//
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });

            }
        });


        //back
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

}
