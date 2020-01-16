package com.pengdst.note_ink.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pengdst.note_ink.R;
import com.pengdst.note_ink.model.NoteModel;
import com.pengdst.note_ink.room.AppDatabase;
import com.pengdst.note_ink.room.NoteRoom;

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etNoteTitle, etNoteMessage;
    Button btnAddNote, btnDelNote;
    NoteRoom room;
    NoteModel note;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        room = AppDatabase.db(getApplicationContext()).noteRoom();
        id = getIntent().getIntExtra("id", -1);

        etNoteTitle = findViewById(R.id.etNoteTitle);
        etNoteMessage = findViewById(R.id.etNoteMsg);
        btnAddNote = findViewById(R.id.btnAddNote);
        btnDelNote = findViewById(R.id.btnDeleteNote);

        if (id != -1){
            note = room.select(id);
            etNoteTitle.setText(note.getTitle());
            etNoteMessage.setText(note.getMessage());
            btnAddNote.setText("Update");
            btnDelNote.setVisibility(View.VISIBLE);
        }

        btnAddNote.setOnClickListener(this);
        btnDelNote.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String title = etNoteTitle.getText().toString();
        String message = etNoteMessage.getText().toString();
        NoteModel noteModel = new NoteModel();
        if (id != -1) noteModel.setId(id);

        switch (v.getId()){
            case R.id.btnAddNote:
                noteModel.setMessage(message);
                noteModel.setTitle(title);
                if (id != -1) {
                    room.update(noteModel);
                }
                else room.insert(noteModel);
                break;
            case R.id.btnDeleteNote:
                room.delete(noteModel);
                break;
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String title = etNoteTitle.getText().toString();
        String message = etNoteMessage.getText().toString();
        NoteModel noteModel = new NoteModel();
        noteModel.setMessage(message);
        noteModel.setTitle(title);

        if (id != -1) {
            noteModel.setId(id);
            room.update(noteModel);
        }
        else room.insert(noteModel);
        setResult(RESULT_OK);
        finish();
    }
}
