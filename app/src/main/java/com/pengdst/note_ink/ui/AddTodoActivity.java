package com.pengdst.note_ink.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pengdst.note_ink.R;
import com.pengdst.note_ink.model.TodoModel;
import com.pengdst.note_ink.room.AppDatabase;
import com.pengdst.note_ink.room.TodoRoom;

public class AddTodoActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etTodoMessage;
    Button btnAddTodo, btnDelTodo;
    TodoRoom room;
    TodoModel todo;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        room = AppDatabase.db(getApplicationContext()).todoRoom();
        id = getIntent().getIntExtra("id", -1);

        etTodoMessage = findViewById(R.id.etTodoMsg);
        btnAddTodo = findViewById(R.id.btnAddTodo);
        btnDelTodo = findViewById(R.id.btnDeleteTodo);

        if (id != -1){
            todo = room.select(id);
            etTodoMessage.setText(todo.getMessage());
            btnAddTodo.setText("Update");
            btnDelTodo.setVisibility(View.VISIBLE);
        }

        btnAddTodo.setOnClickListener(this);
        btnDelTodo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String message = etTodoMessage.getText().toString();
        TodoModel todoModel = new TodoModel();
        if (id != -1) todoModel.setId(id);

        switch (v.getId()){
            case R.id.btnAddTodo:
                todoModel.setMessage(message);
                if (id != -1) {
                    todoModel.setStatus(todo.getStatus());
                    room.update(todoModel);
                }
                else {
                    todoModel.setStatus(false);
                    room.insert(todoModel);
                }
                break;
            case R.id.btnDeleteTodo:
                room.delete(todoModel);
                break;
        }
        setResult(RESULT_OK);
        finish();
    }
}
