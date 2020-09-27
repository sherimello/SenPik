package com.example.senpik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.FileOutputStream;
import java.io.IOException;

public class Deleteme extends AppCompatActivity {

    private Button kill;
    private ImageView status;
    private TextView statement;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleteme);
        statement = findViewById(R.id.statement);
        status = findViewById(R.id.statuspic);
        progressBar=findViewById(R.id.progress);
        kill = findViewById(R.id.kill);
        progressBar.setVisibility(View.GONE);

        kill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kill.getText().equals("Kill Me"))
                Delete();
                else
                    uninstall();
            }
        });
    }

    private void uninstall() {

        Intent intent=new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:com.example.senpik"));
        startActivity(intent);
    }

    private void Delete() {
        //removing user...
        progressBar.setVisibility(View.VISIBLE);





        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("test").getRef();
        databaseReference1.removeValue();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("FEMALE");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    String url=String.valueOf(dataSnapshot1.child("imageUri").getValue());
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(url);
                    storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // File deleted successfully
                            Log.e("firebasestorage", "onSuccess: deleted file");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Uh-oh, an error occurred!
                            Log.e("firebasestorage", "onFailure: did not delete file");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //saving state..............
        String Blood="deleted";
        try {
            FileOutputStream fileOutputStream = openFileOutput("state.txt", MODE_PRIVATE);
            fileOutputStream.write(Blood.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                status.setBackgroundResource(R.drawable.dead);
                statement.setText("Dead!");
                statement.setTextSize(45);
                kill.setText("Uninstall");
            }
        },3000);

        }



}

