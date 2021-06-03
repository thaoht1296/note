package com.example.thaonote.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.thaonote.R;
import com.example.thaonote.dbhelper.DAOTodo;
import com.example.thaonote.model.Completed;

import java.util.ArrayList;

public class CompletedAdapter extends RecyclerView.Adapter<CompletedAdapter.CompletedDataHolder>{
    private ArrayList<Completed> clist;
    private Context context;
    private DAOTodo DAOTodo;

    public CompletedAdapter(ArrayList<Completed> list, Context context) {
        this.clist = list;
        this.context = context;
    }

    @Override
    public CompletedDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_complete,parent,false);
        return new CompletedDataHolder(view);
    }

    @Override
    public void onBindViewHolder(CompletedDataHolder holder, int position) {
        DAOTodo =new DAOTodo(context);
        Completed completed = clist.get(position);
        // gạch ngang
        holder.todoTitle.setPaintFlags(holder.todoTitle.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.todoTitle.setText(completed.getTodoTitle());
        holder.todoContent.setText(completed.getTodoContent());
        holder.todoTag.setText(completed.getTodoTag());
        holder.todoDate.setText(completed.getTodoDate());
        holder.todoTime.setText(completed.getTodoTime());
    }

    @Override
    public int getItemCount() {
        return clist.size();
    }

    public class CompletedDataHolder extends RecyclerView.ViewHolder {
        TextView todoTitle,todoContent,todoTag,todoDate,todoTime;
        public CompletedDataHolder(View view) {
            super(view);
            todoTitle  = view.findViewById(R.id.completed_todo_title);
            todoContent  = view.findViewById(R.id.completed_todo_content);
            todoTag  = view.findViewById(R.id.todo_tag);
            todoDate  = view.findViewById(R.id.todo_date);
            todoTime  = view.findViewById(R.id.todo_time);
        }
    }

    public void listFilter(ArrayList<Completed> newlist){
        clist =new ArrayList<>();
        clist.addAll(newlist);
        notifyDataSetChanged();
    }
}
