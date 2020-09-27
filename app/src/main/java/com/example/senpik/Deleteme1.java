package com.example.senpik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Deleteme1 extends AppCompatActivity {

    private Button uninstall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleteme1);
        uninstall=findViewById(R.id.kill);
        uninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_DELETE);
                intent.setData(Uri.parse("package:com.example.senpik"));
                startActivity(intent);
            }
        });
    }
}
