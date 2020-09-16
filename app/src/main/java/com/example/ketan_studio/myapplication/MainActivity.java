package com.example.ketan_studio.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    Button choose,upload,show;
    int garary = 2;
    Uri ImageUri;
    ImageView imageview;
    StorageReference ImageReff;
    String dowmloadUri;
    DatabaseReference myReff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        choose = (Button)findViewById(R.id.choose);
        show = (Button)findViewById(R.id.show);
        imageview = (ImageView)findViewById(R.id.imageview);
        upload = (Button)findViewById(R.id.upload);

        ImageReff = FirebaseStorage.getInstance().getReference().child("Images");

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,garary);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateImage();
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ShowActivity.class);
                startActivity(i);
            }
        });
    }

    private void ValidateImage() {
        if (ImageUri == null){
            Toast.makeText(this, "Image Not set", Toast.LENGTH_SHORT).show();
        }else {
            StoreImagewithIMgormation();
        }
    }

    private void StoreImagewithIMgormation() {
        final StorageReference filepath= ImageReff.child(ImageUri.getLastPathSegment());

        final UploadTask uploadTask = filepath.putFile(ImageUri);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Toast.makeText(MainActivity.this, "Sucess", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        dowmloadUri = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Uri Geted", Toast.LENGTH_SHORT).show();
                            SaveImageToDataBase();
                        }else {
                            Toast.makeText(MainActivity.this, "Uri Getting Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void SaveImageToDataBase() {
        myReff = FirebaseDatabase.getInstance().getReference("Image");
        HashMap<String,Object> ma = new HashMap<>();
        ma.put("image",dowmloadUri);
        myReff.push().setValue(ma);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == garary && resultCode == RESULT_OK && data!= null){
            ImageUri = data.getData();
            imageview.setImageURI(ImageUri);
        }
    }
}
