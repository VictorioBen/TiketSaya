package com.victorio.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

public class SignInAct extends AppCompatActivity {
    TextView btn_register;
    Button btn_login;
    EditText xusername, xpassword;
    DatabaseReference reference;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //load btn
        btn_login = findViewById(R.id.btn_sign_in);
        //load texview
        btn_register = findViewById(R.id.btn_new_account);
        //load edittext
        xusername = findViewById(R.id.username);
        xpassword = findViewById(R.id.password);

        //pindah intent dan proses login masuk ke database
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mengubah bentuk button
                btn_login.setText("Redirecting...");
                btn_login.setEnabled(false);
                //mengambil user id di database
                final String username = xusername.getText().toString();
                final String password = xpassword.getText().toString();

                if (username.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Username Kosong ! ", Toast.LENGTH_SHORT).show();
                    btn_login.setText("SIGN IN");
                    btn_login.setEnabled(true);
                }
                else {
                    if (password.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Password kosong ! ", Toast.LENGTH_SHORT).show();
                        btn_login.setText("SIGN IN");
                        btn_login.setEnabled(true);
                    } else {
                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username);
                        //melakukan validasi apakah id ada di database
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    // ambil data from firebase
                                    String passwordFromFirebase = dataSnapshot.child("password").getValue().toString();

                                    //validasi password ke database
                                    if (password.equals(passwordFromFirebase)) {
                                        //Menyimpan data kepada local storage
                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(username_key, xusername.getText().toString());
                                        editor.apply();
                                        //pindah activity
                                        Intent login = new Intent(SignInAct.this, HomeAct.class);
                                        startActivity(login);
                                        btn_login.setText("Redirecting...");
                                        btn_login.setEnabled(false);
                                        finish();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Password salah ! ", Toast.LENGTH_SHORT).show();
                                        btn_login.setText("SIGN IN");
                                        btn_login.setEnabled(true);
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Username salah ! ", Toast.LENGTH_SHORT).show();
                                    btn_login.setText("SIGN IN");
                                    btn_login.setEnabled(true);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
        });
        //pindah intent
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abc = new Intent(SignInAct.this, RegisterOneAct.class);
                startActivity(abc);
            }
        });

    }
}
