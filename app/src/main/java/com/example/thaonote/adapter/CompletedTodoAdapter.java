package com.example.thaonote.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.thaonote.R;
import com.example.thaonote.dbhelper.TodoDBHelper;
import com.example.thaonote.model.CompletedModel;

import java.util.ArrayList;

public class CompletedTodoAdapter extends RecyclerView.Adapter<CompletedTodoAdapter.CompletedDataHolder>{
    private ArrayList<CompletedModel> completedModels;
    private Context context;
    private TodoDBHelper todoDBHelper;

    public CompletedTodoAdapter(ArrayList<CompletedModel> completedModels, Context context) {
        this.completedModels = completedModels;
        this.context = context;
    }

    @Override
    public CompletedDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_complete,parent,false);
        return new CompletedDataHolder(view);
    }

    @Override
    public void onBindViewHolder(CompletedDataHolder holder, int position) {
        todoDBHelper=new TodoDBHelper(context);
        CompletedModel completedModel = completedModels.get(position);
        // gạch ngang
        holder.todoTitle.setPaintFlags(holder.todoTitle.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.todoTitle.setText(completedModel.getTodoTitle());
        holder.todoContent.setText(completedModel.getTodoContent());
        holder.todoTag.setText(completedModel.getTodoTag());
        holder.todoDate.setText(completedModel.getTodoDate());
        holder.todoTime.setText(completedModel.getTodoTime());
    }

    @Override
    public int getItemCount() {
        return completedModels.size();
    }

    public class CompletedDataHolder extends RecyclerView.ViewHolder {
        TextView todoTitle,todoContent,todoTag,todoDate,todoTime;
        public CompletedDataHolder(View itemView) {
            super(itemView);
            todoTitle=(TextView)itemView.findViewById(R.id.completed_todo_title);
            todoContent=(TextView)itemView.findViewById(R.id.completed_todo_content);
            todoTag=(TextView)itemView.findViewById(R.id.todo_tag);
            todoDate=(TextView)itemView.findViewById(R.id.todo_date);
            todoTime=(TextView)itemView.findViewById(R.id.todo_time);
        }
    }

    //filter the search
    public void filterCompletedTodos(ArrayList<CompletedModel> newCompletedModels){
        completedModels =new ArrayList<>();
        completedModels.addAll(newCompletedModels);
        notifyDataSetChanged();
    }
}
