package com.pengdst.note_ink.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ClientService {
    @FormUrlEncoded
    @POST("user/register")
    Call<ResponseBody> register(
            @Field("username") String username,
            @Field("password") String password,
            @Field("email") String email
    );

    @GET("login")
    Call<ResponseBody> login(
            @Query("username") String username,
            @Query("password") String password
    );

    @GET("select/:id")
    Call<ResponseBody> select(
            @Query("id") int id
    );

    @POST("auth/logout")
    Call<ResponseBody> logout();
}
