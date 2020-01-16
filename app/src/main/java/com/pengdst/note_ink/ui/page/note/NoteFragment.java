package com.pengdst.note_ink.ui.page.note;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pengdst.note_ink.R;
import com.pengdst.note_ink.adapter.NoteAdapter;
import com.pengdst.note_ink.model.NoteModel;
import com.pengdst.note_ink.room.AppDatabase;
import com.pengdst.note_ink.room.NoteRoom;
import com.pengdst.note_ink.ui.AddNoteActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class NoteFragment extends Fragment implements View.OnClickListener, NoteAdapter.ClickCallback {
    private HomeViewModel homeViewModel;

    RecyclerView rvNote;
    FloatingActionButton fabNote;
    NoteAdapter adapter;
    List<NoteModel> todos = new ArrayList<>();
    NoteRoom room;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        room = AppDatabase.db(getContext()).noteRoom();
        todos = room.selectAll();
        adapter = new NoteAdapter(getContext(), todos);
        adapter.setCallback(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_note, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvNote = view.findViewById(R.id.rv_note);
        fabNote = view.findViewById(R.id.fabNote);

        fabNote.setOnClickListener(this);

        rvNote.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNote.setAdapter(adapter);

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
    public void onResume() {
        super.onResume();
        todos.clear();
        todos.addAll(room.selectAll());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabNote:
                Intent intent = new Intent(getContext(), AddNoteActivity.class);
                startActivityForResult(intent, 20);
                break;
        }
    }

    @Override
    public void onItemClicked(NoteModel note, int position) {
        Intent intent = new Intent(getContext(), AddNoteActivity.class);
        intent.putExtra("id", note.getId());
        startActivityForResult(intent, 21);
    }
}