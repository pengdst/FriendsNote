package com.pengdst.note_ink.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pengdst.note_ink.R;
import com.pengdst.note_ink.api.ClientService;
import com.pengdst.note_ink.api.RetrofitClient;
import com.pengdst.note_ink.helper.Session;
import com.pengdst.note_ink.model.UserModel;
import com.pengdst.note_ink.room.AppDatabase;
import com.pengdst.note_ink.room.UserRoom;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    Button btLogin;
    EditText etUsername, etPassword;
    String username = "proto", password = "akusukas", email;
    Boolean isLogin;
    Session session;
    UserRoom userRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = Session.init(getApplicationContext());
        isLogin = session.getBoolean("login");
        if (isLogin) {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btLogin = findViewById(R.id.btRegister);

        userRoom = AppDatabase.db(getApplicationContext()).userRoom();

        etUsername.setText(username);
        etPassword.setText(password);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth();
                if (isLogin) {
                    session.set("username", username)
                            .set("email", email)
                            .set("login", true);
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });
        findViewById(R.id.tvGotoRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        userRoom = AppDatabase.db(getApplicationContext()).userRoom();
    }

    private void auth(String username, String password) {
        UserModel userModel = userRoom.select(username, password);
        if (userModel != null){
            this.username = userModel.getUsername();
            this.password = userModel.getPassword();
            this.email = userModel.getEmail();

            isLogin = true;
        }else {
            Toast.makeText(getApplicationContext(), "Username atau Password salah", Toast.LENGTH_SHORT).show();
        }
    }

    private void auth() {
        Retrofit retrofit = RetrofitClient.connect();
        ClientService service = retrofit.create(ClientService.class);
        Call<ResponseBody> request = service.login(
                etUsername.getText().toString(),
                etPassword.getText().toString()
        );

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body() != null ? response.body().string() : "Kosong";
                    JSONObject object = new JSONObject(json);
                    int status = object.getInt("status");
                    if (status == 1) {
                        JSONObject data = object.getJSONObject("data");
                        int id = data.getInt("id");
                        String username = data.getString("username");
                        String email = data.getString("email");
                        Log.e("Response True", id + ". " + username);

                        session.set("id", id)
                                .set("username", username)
                                .set("email", email)
                                .set("login", true);

                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Log.e("Response False", json);
                    }
                } catch (IOException | JSONException e) {
                    Log.e("Response False", ""+e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        onFailure(null, null);
                Log.e("Response", "Fail --> " + t.toString());

            }
        });
    }
}
