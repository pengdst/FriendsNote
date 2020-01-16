package com.pengdst.note_ink.ui.page.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pengdst.note_ink.R;
import com.pengdst.note_ink.adapter.TodoAdapter;
import com.pengdst.note_ink.model.TodoModel;
import com.pengdst.note_ink.room.AppDatabase;
import com.pengdst.note_ink.room.TodoRoom;
import com.pengdst.note_ink.ui.AddTodoActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class TodoFragment extends Fragment implements TodoAdapter.ClickCallback, View.OnClickListener {

    RecyclerView rvNote;
    FloatingActionButton fabNote;
    TodoAdapter adapter;
    List<TodoModel> todos = new ArrayList<>();
    TodoRoom room;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        room = AppDatabase.db(getContext()).todoRoom();
        todos = room.selectAll();
        adapter = new TodoAdapter(getContext(), todos);
        adapter.setCallback(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_todo, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvNote = view.findViewById(R.id.rv_todo);
        fabNote = view.findViewById(R.id.fabTodo);

        fabNote.setOnClickListener(this);

        rvNote.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNote.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        todos.clear();
        todos.addAll(room.selectAll());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            todos.clear();
            todos.addAll(room.selectAll());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClicked(TodoModel todo, int position) {
        Intent intent = new Intent(getContext(), AddTodoActivity.class);
        intent.putExtra("id", todo.getId());
        startActivityForResult(intent, 21);
    }

    @Override
    public void onItemDeleted(TodoModel todo, int position) {
        Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
        AppDatabase.db(getContext()).todoRoom().delete(todo);
        adapter.notifyDataSetChanged();
        todos.clear();
        todos.addAll(room.selectAll());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabTodo:
                Intent intent = new Intent(getContext(), AddTodoActivity.class);
                startActivityForResult(intent, 20);
                break;
        }

    }
}