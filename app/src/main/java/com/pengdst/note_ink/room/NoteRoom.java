package com.pengdst.note_ink.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pengdst.note_ink.model.NoteModel;

import java.util.List;

@Dao
public interface NoteRoom {
    @Query("SELECT * FROM notemodel WHERE id = :id")
    NoteModel select(int id);

    @Query("SELECT * FROM notemodel")
    List<NoteModel> selectAll();

    @Insert
    void insert(NoteModel note);

    @Update
    void update(NoteModel note);

    @Delete
    void delete(NoteModel note);
}
