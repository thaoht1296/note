package com.example.thaonote.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thaonote.R;
import com.example.thaonote.adapter.CompletedTodoAdapter;
import com.example.thaonote.dbhelper.TodoDBHelper;
import com.example.thaonote.model.CompletedTodoModel;

import java.util.ArrayList;

public class CompletedTodos extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView completedTodos;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<CompletedTodoModel> completedTodoModels;
    private CompletedTodoAdapter completedTodoAdapter;
    private LinearLayout linearLayout;
    private TodoDBHelper todoDBHelper;
    private ImageView delete, settings, back1;

    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        initView();

        settings.setOnClickListener(this::onClick);
        delete.setOnClickListener(this::onClick);
        back1.setOnClickListener(this::onClick);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = search.getText().toString();
                txt = txt.toLowerCase();
                if (!txt.equalsIgnoreCase("")) {
                    ArrayList<CompletedTodoModel> newCompletedTodoModels=new ArrayList<>();
                    for(CompletedTodoModel completedTodoModel:completedTodoModels){
                        String getTodoTitle=completedTodoModel.getTodoTitle();
                        String getTodoContent=completedTodoModel.getTodoContent();
                        String getTodoTag=completedTodoModel.getTodoTag();

                        if(getTodoTitle.contains(txt) || getTodoContent.contains(txt) || getTodoTag.contains(txt)){
                            newCompletedTodoModels.add(completedTodoModel);
                        }
                    }
                    completedTodoAdapter.filterCompletedTodos(newCompletedTodoModels);
                    completedTodoAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void initView(){
        completedTodos=(RecyclerView)findViewById(R.id.completed_todos_view);
        todoDBHelper=new TodoDBHelper(this);
        linearLayout=(LinearLayout)findViewById(R.id.no_completed_todo_section) ;
        if(todoDBHelper.countCompletedTodos()==0){
            linearLayout.setVisibility(View.VISIBLE);
            completedTodos.setVisibility(View.GONE);
        }else{
            linearLayout.setVisibility(View.GONE);
            completedTodos.setVisibility(View.VISIBLE);
            completedTodoModels=new ArrayList<>();
            completedTodoModels=todoDBHelper.fetchCompletedTodos();
            completedTodoAdapter=new CompletedTodoAdapter(completedTodoModels,this);
        }
        linearLayoutManager=new LinearLayoutManager(this);
        completedTodos.setAdapter(completedTodoAdapter);
        completedTodos.setLayoutManager(linearLayoutManager);

        search = findViewById(R.id.search);
        settings = findViewById(R.id.settings);
        delete = findViewById(R.id.delete);
        back1 = findViewById(R.id.back1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete:
                deleteDialog();
                break;
            case R.id.settings:
                startActivity(new Intent(this,AppSettings.class));
                break;
            case R.id.back1:
                startActivity(new Intent(this, HomeActivity.class));
                break;
        }
    }

    //showing the delete confirmation dialog
    private void deleteDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn muốn xóa ghi chú đã hoàn thành không ?");
        builder.setPositiveButton("Xóa tất cả", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(todoDBHelper.removeCompletedTodos()){
                    startActivity(new Intent(CompletedTodos.this, CompletedTodos.class));
                    Toast.makeText(CompletedTodos.this, "Xóa tất cả ghi chú hoàn thành thành công !", Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(CompletedTodos.this, "Ghi chú chưa được xóa !", Toast.LENGTH_SHORT).show();
            }
        }).create().show();
    }
}
