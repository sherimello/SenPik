package com.example.senpik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class splash extends AppCompatActivity {

    private String Text4, txt4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        FileInputStream fileInputStream4 = null;
        try {
            fileInputStream4 = openFileInput("state.txt");
            InputStreamReader inputStreamReader4 = new InputStreamReader(fileInputStream4);
            BufferedReader bufferedReader4 = new BufferedReader(inputStreamReader4);
            StringBuilder stringBuilder4 = new StringBuilder();

            while ((Text4 = bufferedReader4.readLine()) != null) {
                stringBuilder4.append(Text4);
            }
            txt4 = stringBuilder4.toString();

        } catch (FileNotFoundException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                if (TextUtils.isEmpty(txt4))
//                {
//                    startActivity(new Intent(getApplicationContext(),Deleteme.class));
//                    finish();
//                }
//                if (txt4.equals("deleted"))
//                {startActivity(new Intent(getApplicationContext(),Deleteme1.class));
//                finish();}
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();

            }
        }, 2575);
    }
}
