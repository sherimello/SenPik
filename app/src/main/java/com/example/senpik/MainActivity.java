package com.example.senpik;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int IMAGE_REQUEST =1;
    private Button select,send;
    private EditText name;
    ImageView imageView;
    TextView showuploads;
    private ProgressBar progressBar;
    private Uri ImageUri;
    private DatabaseReference databaseReference;
    String Name;
    private StorageTask storageTask;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        select=findViewById(R.id.choose);
        send=findViewById(R.id.send);
        name=findViewById(R.id.select);
        showuploads=findViewById(R.id.showuploads);
        progressBar=findViewById(R.id.progress);
        imageView=findViewById(R.id.image_lay);
        storageReference= FirebaseStorage.getInstance().getReference("MALE");
        databaseReference= FirebaseDatabase.getInstance().getReference("MALE");

        select.setOnClickListener(this);
        send.setOnClickListener(this);
        showuploads.setOnClickListener(this);
    }

    public void open()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==IMAGE_REQUEST&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {
            ImageUri=data.getData();
            Picasso.with(this).load(ImageUri).into(imageView);
        }
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void upload()
    {
        if (ImageUri!=null)
        {
            final StorageReference file=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(ImageUri));
            storageTask=file.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    },500);
                    Toast.makeText(MainActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                    String url=file.getDownloadUrl().toString();
                    LoadImage loadImage=new LoadImage(Name,taskSnapshot.getDownloadUrl().toString());
                    String ID=databaseReference.push().getKey();
                    databaseReference.child(ID).setValue(loadImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int)progress);
                }
            });
        }
        else
        {
            Toast.makeText(this, "No File Selected!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {

        if (view==select)
        {
            open();
        }
        if (view==send)
        {
            Name=name.getText().toString().trim();

            if (TextUtils.isEmpty(Name))
                Name="NO NAME";

            if (storageTask!=null && storageTask.isInProgress())
                Toast.makeText(this, "Task in progress!", Toast.LENGTH_SHORT).show();
            else
            upload();
        }
        if (view==showuploads)
        {
            Intent intent=new Intent(this,Images.class);
            startActivity(intent);
            finish();
        }

    }
}
