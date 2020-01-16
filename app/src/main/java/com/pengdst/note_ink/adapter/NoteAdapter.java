package com.pengdst.note_ink.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pengdst.note_ink.R;
import com.pengdst.note_ink.model.NoteModel;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    Context context;
    List<NoteModel> list;
    ClickCallback callback;

    public NoteAdapter(Context context, List<NoteModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setCallback(ClickCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final NoteModel note = list.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onItemClicked(note, position);
            }
        });
        holder.bind(note);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, message;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.note_title);
            message = itemView.findViewById(R.id.note_message);
        }

        void bind(NoteModel note){
            title.setText(note.getTitle());
            message.setText(note.getMessage());
        }
    }

    public interface ClickCallback{
        void onItemClicked(NoteModel note, int position);
    }
}
