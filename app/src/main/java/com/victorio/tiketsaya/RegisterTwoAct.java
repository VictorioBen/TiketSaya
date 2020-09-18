package com.victorio.tiketsaya;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegisterTwoAct extends AppCompatActivity {
    EditText bio, nama_lengkap;
    Button btn_ok, btn_add_photo;
    ImageView pic_photo_register, btn_back;
    Uri photo_location;
    Integer photo_max = 1;
    DatabaseReference reference;
    StorageReference storage;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);

        getUsernameLocal();

        //load pencarian id button
        bio = findViewById(R.id.bio);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        btn_ok = findViewById(R.id.btn_continue);
        btn_add_photo = findViewById(R.id.btn_add_photo);
        pic_photo_register = findViewById(R.id.pic_photo_register_user);
        btn_back = findViewById(R.id.btn_back_regis_two);

        //eksekusi bos
        //balik ke regis 1
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(RegisterTwoAct.this, RegisterOneAct.class);
                startActivity(back);
            }
        });

        //button menambahkan photo
        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });
        //submit register
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mengubah button continue ketika sudah diklik menjadi loading
                btn_ok.setEnabled(false);
                btn_ok.setText("Loading...");
                final String xnama_lengkap = nama_lengkap.getText().toString();
                final String xbio = bio.getText().toString();
                if (xnama_lengkap.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Nama Lengkap tidak boleh kosong ! ", Toast.LENGTH_SHORT).show();
                    btn_ok.setText("CONTINUE");
                    btn_ok.setEnabled(true);
                } else if (xbio.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Passion tidak boleh kosong ! ", Toast.LENGTH_SHORT).show();
                    btn_ok.setText("CONTINUE");
                    btn_ok.setEnabled(true);
                } else if (photo_location == null) {
                    Toast.makeText(getApplicationContext(), "Foto wajib diisi ! ", Toast.LENGTH_SHORT).show();
                    btn_ok.setText("CONTINUE");
                    btn_ok.setEnabled(true);
                } else {
                    //menyimpan kepada database
                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                    storage = FirebaseStorage.getInstance().getReference().child("PhotoUser").child(username_key_new);

                    //validasi file apakah ada
                    if (photo_location != null) {
                        final StorageReference storageReference1 = storage.child(System.currentTimeMillis() + "." + getFileExtension(photo_location));
                        storageReference1.putFile(photo_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //mengambil url foto dari firebase
                                storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String uri_photo = uri.toString();
                                        reference.getRef().child("url_photo_profile").setValue(uri_photo);
                                        reference.getRef().child("nama_lengkap").setValue(nama_lengkap.getText().toString());
                                        reference.getRef().child("bio").setValue(bio.getText().toString());
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        //pindah activity
                                        Intent abc = new Intent(RegisterTwoAct.this, SuccessRegisterAct.class);
                                        startActivity(abc);
                                        finish();

                                    }
                                });


                            }
                        });
                    }
                }
            }
        });

    }

    String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    //membuat method mengambil foto
    public void findPhoto() {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).noFade().centerCrop().fit().into(pic_photo_register);

        }
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }


}
