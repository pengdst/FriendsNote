package com.pengdst.note_ink.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.pengdst.note_ink.model.NoteModel;
import com.pengdst.note_ink.model.TodoModel;
import com.pengdst.note_ink.model.UserModel;

@Database(entities = {NoteModel.class, TodoModel.class, UserModel.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public static AppDatabase db(Context context){
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "noteink")
                .allowMainThreadQueries()
                .build();
    }

    public abstract NoteRoom noteRoom();
    public abstract TodoRoom todoRoom();
    public abstract UserRoom userRoom();
}
