package com.pengdst.note_ink.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static String URL = "https://note-ink-api.000webhostapp.com/Note-Ink/user/";

    public static Retrofit connect(){
        return new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
