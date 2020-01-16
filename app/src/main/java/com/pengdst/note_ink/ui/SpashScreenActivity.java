package com.pengdst.note_ink.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.pengdst.note_ink.R;

public class SpashScreenActivity extends AppCompatActivity {
    private int waktu_loading=2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //setelah loading maka akan langsung berpindah ke home activity
                Intent home=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(home);
                finish();

            }
        },waktu_loading);
    }
}
