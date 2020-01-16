package com.pengdst.note_ink.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pengdst.note_ink.model.UserModel;

import java.util.List;

@Dao
public interface UserRoom {
    @Query("SELECT * FROM usermodel WHERE username LIKE :username AND password LIKE :password")
    UserModel select(String username, String password);

    @Query("SELECT * FROM usermodel WHERE id = :id")
    UserModel select(int id);

    @Query("SELECT * FROM usermodel")
    List<UserModel> selectAll();

    @Insert
    void insert(UserModel user);

    @Update
    void update(UserModel user);

    @Delete
    void delete(UserModel user);
}
