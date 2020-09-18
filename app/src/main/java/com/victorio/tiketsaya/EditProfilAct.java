package com.victorio.tiketsaya;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfilAct extends AppCompatActivity {
    DatabaseReference reference;
    ImageView photo_edit_profile,back;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";
    EditText xnama_lengkap, xbio, xusername, xpassword, xemail_address;
    Button btn_save,btn_add_photo;
    Uri photo_location;
    StorageReference storage;
    Integer photo_max = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);
        getUsernameLocal();

        photo_edit_profile = findViewById(R.id.photo_edit_profile);
        btn_add_photo = findViewById(R.id.btn_add_photo);
        xbio = findViewById(R.id.bio);
        xnama_lengkap = findViewById(R.id.nama_lengkap);
        xusername = findViewById(R.id.username);
        xpassword = findViewById(R.id.password);
        xemail_address = findViewById(R.id.email_address);
        btn_save = findViewById(R.id.btn_save);

        xusername.setEnabled(false);
        xemail_address.setEnabled(false);

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        storage = FirebaseStorage.getInstance().getReference().child("PhotoUser").child(username_key_new);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                xbio.setText(dataSnapshot.child("bio").getValue().toString());
                xnama_lengkap.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                xusername.setText(dataSnapshot.child("username").getValue().toString());
                xpassword.setText(dataSnapshot.child("password").getValue().toString());
                xemail_address.setText(dataSnapshot.child("email_address").getValue().toString());

                Picasso.with(EditProfilAct.this).load(dataSnapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit().noFade().into(photo_edit_profile);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_save.setText("Loading");
                btn_save.setEnabled(false);
             reference.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     dataSnapshot.getRef().child("username").setValue(xusername.getText().toString());
                     dataSnapshot.getRef().child("password").setValue(xpassword.getText().toString());
                     dataSnapshot.getRef().child("bio").setValue(xbio.getText().toString());
                     dataSnapshot.getRef().child("email_address").setValue(xemail_address.getText().toString());
                     dataSnapshot.getRef().child("nama_lengkap").setValue(xnama_lengkap.getText().toString());
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });
                if (photo_location != null) {
                    final StorageReference storageReference1 = storage.child(System.currentTimeMillis() + "."
                            + getFileExtension(photo_location));
                    storageReference1.putFile(photo_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //mengambil url foto dari firebase
                            storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String uri_photo = uri.toString();
                                    reference.getRef().child("url_photo_profile").setValue(uri_photo);

                                }
                            });

                        }
                    });
                }
                Intent abc = new Intent(EditProfilAct.this, MyProfileAct.class);
                startActivity(abc);
                finish();
            }

        });

        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
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
            Picasso.with(this).load(photo_location).noFade().centerCrop().fit().into(photo_edit_profile);

        }
    }
    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
