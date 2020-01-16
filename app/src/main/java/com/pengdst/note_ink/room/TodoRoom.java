package com.pengdst.note_ink.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pengdst.note_ink.model.TodoModel;

import java.util.List;

@Dao
public interface TodoRoom {
    @Query("SELECT * FROM todomodel WHERE id = :id")
    TodoModel select(int id);

    @Query("SELECT * FROM todomodel")
    List<TodoModel> selectAll();

    @Insert
    void insert(TodoModel todo);

    @Update
    void update(TodoModel todo);

    @Delete
    void delete(TodoModel todo);
}
