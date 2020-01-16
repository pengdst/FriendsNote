package com.pengdst.note_ink.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pengdst.note_ink.R;
import com.pengdst.note_ink.model.UserModel;
import com.pengdst.note_ink.room.AppDatabase;
import com.pengdst.note_ink.room.UserRoom;

public class RegisterActivity extends AppCompatActivity {
    EditText etUsername, etPassword, etEmail;
    Button btRegister;
    UserRoom userRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userRoom = AppDatabase.db(getApplicationContext()).userRoom();

        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btRegister = findViewById(R.id.btRegister);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel userModel = new UserModel();
                userModel.setEmail(etEmail.toString());
                userModel.setUsername(etUsername.toString());
                userModel.setPassword(etPassword.toString());
                userRoom.insert(userModel);
                onBackPressed();
            }
        });
    }
}
