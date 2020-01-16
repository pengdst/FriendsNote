package com.pengdst.note_ink.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TodoModel {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "message")
    String message;
    @ColumnInfo(name = "status")
    Boolean status;

    public TodoModel() {
    }

    public TodoModel(int id, String message, Boolean status) {
        this.id = id;
        this.message = message;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
