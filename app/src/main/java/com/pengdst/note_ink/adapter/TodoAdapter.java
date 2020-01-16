package com.pengdst.note_ink.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pengdst.note_ink.R;
import com.pengdst.note_ink.model.TodoModel;
import com.pengdst.note_ink.room.AppDatabase;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    Context context;
    List<TodoModel> list;
    ClickCallback callback;

    public TodoAdapter(Context context, List<TodoModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setCallback(ClickCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final TodoModel todo = list.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onItemClicked(todo, position);
            }
        });
        holder.chStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(context, todo.getMessage()+" is "+isChecked, Toast.LENGTH_SHORT ).show();
                todo.setStatus(isChecked);
                AppDatabase.db(context).todoRoom().update(todo);
            }
        });
        holder.ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onItemDeleted(todo, position);
            }
        });
        holder.bind(todo);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton ibDelete;
        TextView tvMessage;
        CheckBox chStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ibDelete = itemView.findViewById(R.id.ib_deletetodo);
            tvMessage = itemView.findViewById(R.id.tv_todomsg);
            chStatus = itemView.findViewById(R.id.ch_status);
        }

        public void bind(TodoModel todo) {
            tvMessage.setText(todo.getMessage());
            chStatus.setChecked(todo.getStatus());
        }
    }

    public interface ClickCallback{
        void onItemClicked(TodoModel todo, int position);

        void onItemDeleted(TodoModel todo, int position);
    }
}
