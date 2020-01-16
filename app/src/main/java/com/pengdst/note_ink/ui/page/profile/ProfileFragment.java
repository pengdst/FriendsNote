package com.pengdst.note_ink.ui.page.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.pengdst.note_ink.R;
import com.pengdst.note_ink.api.ClientService;
import com.pengdst.note_ink.api.RetrofitClient;
import com.pengdst.note_ink.helper.Session;
import com.pengdst.note_ink.room.AppDatabase;
import com.pengdst.note_ink.room.UserRoom;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    Session session;
    Retrofit retrofitClient;
    ClientService clientService;

    UserRoom userRoom;

    TextView tvUsername, tvEmail;
    EditText etUsername, etEmail;
    Button btCountNote, btCountTodo, btEditProfile;
    CircleImageView imProfilePic;

    String username, email;
    int id;
    private Uri selectedImageUri;
    private String imageLocation;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userRoom = AppDatabase.db(getContext()).userRoom();

        session = Session.init(getContext());
        id = session.getInt("id");

        retrofitClient = RetrofitClient.connect();
        clientService = retrofitClient.create(ClientService.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btEditProfile = view.findViewById(R.id.bt_edit_user);
        tvUsername = view.findViewById(R.id.tv_username);
        tvEmail = view.findViewById(R.id.tv_email);
        etUsername = view.findViewById(R.id.et_username);
        etEmail = view.findViewById(R.id.et_email);
        btCountNote = view.findViewById(R.id.count_note);
        btCountTodo = view.findViewById(R.id.count_todo);
        imProfilePic = view.findViewById(R.id.iv_profilepic);

        btCountNote.setText(AppDatabase.db(getContext()).noteRoom().selectAll().size() + "\nNotes");
        btCountTodo.setText(AppDatabase.db(getContext()).todoRoom().selectAll().size() + "\nTodo");
        btCountNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_nav_profile_to_nav_note);
            }
        });
        btCountTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_nav_profile_to_nav_todo);
            }
        });

        btEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUsername.getVisibility() != View.VISIBLE || etEmail.getVisibility() != View.VISIBLE) {

                    etUsername.setVisibility(View.VISIBLE);
                    etEmail.setVisibility(View.VISIBLE);
                    tvUsername.setVisibility(View.INVISIBLE);
                    tvEmail.setVisibility(View.INVISIBLE);
                    imProfilePic.setEnabled(true);

                    etUsername.setText(tvUsername.getText().toString().trim());
                    etEmail.setText(tvEmail.getText().toString().trim());

                    btEditProfile.setText("SAVE");
                    btEditProfile.setBackgroundResource(R.drawable.bt_success_ripple);

                } else {
                    etUsername.setVisibility(View.GONE);
                    etEmail.setVisibility(View.GONE);
                    tvUsername.setVisibility(View.VISIBLE);
                    tvEmail.setVisibility(View.VISIBLE);
                    imProfilePic.setEnabled(false);

                    tvUsername.setText(etUsername.getText().toString().trim());
                    tvEmail.setText(etEmail.getText().toString().trim());

                    if (selectedImageUri != null) session.set("photo", "content://com.miui.gallery.open"+imageLocation);
                    if (imageLocation != null) session.set("loca", imageLocation);

                    session.set("username", etUsername.getText().toString().trim())
                            .set("email", etEmail.getText().toString().trim());

                    btEditProfile.setText("Update Profile");
                    btEditProfile.setBackgroundResource(R.drawable.btn_accent_ripple);
                }
            }
        });

        imProfilePic.setEnabled(false);
        imProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        tvUsername.setText(session.getString("username"));
        tvEmail.setText(session.getString("email"));
        if (session.getString("photo") != null) {
            Glide.with(getContext())
                    .load(session.getString("photo"))
                    .centerCrop()
                    .into(imProfilePic);
        }

        Call<ResponseBody> call = clientService.select(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body() != null ? response.body().string() : "Kosong";
                    JSONObject object = new JSONObject(json);
                    username = object.getString("username");
                    email = object.getString("email");

//                    session.set("username", username).set("email", email);

//                    tvUsername.setText(username);
//                    tvEmail.setText(email);
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1 && data != null) {
                selectedImageUri = data.getData();
                imageLocation = selectedImageUri.getEncodedPath();
                Toast.makeText(getContext(), "Test: " + imageLocation, Toast.LENGTH_SHORT).show();
                Glide.with(getContext())
                        .load(selectedImageUri)
                        .centerCrop()
                        .into(imProfilePic);
            } else Toast.makeText(getContext(), "Image Clicked", Toast.LENGTH_SHORT).show();
        }
    }

    private void auth() {
        Retrofit retrofit = RetrofitClient.connect();
        ClientService service = retrofit.create(ClientService.class);
        Call<ResponseBody> request = service.select(
                session.getInt("id")
//                password
        );

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body() != null ? response.body().string() : "Kosong";
                    JSONObject object = new JSONObject(json);
                    int status = object.getInt("status");
                    if (status == 1) {
                        JSONObject data = object.getJSONArray("data").getJSONObject(0);
                        int id = data.getInt("id");
                        String username = data.getString("username");
                        String email = data.getString("email");
                        Log.e("Response True", id + ". " + username);

                        session.set("id", id)
                                .set("username", username)
                                .set("email", email)
                                .set("login", true);
                    } else {
                        Log.e("Response False", json);
                    }
                } catch (IOException | JSONException e) {
                    Log.e("Response False", "" + e.getMessage());
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