package com.example.thaonote.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thaonote.R;
import com.example.thaonote.adapter.CompletedAdapter;
import com.example.thaonote.dbhelper.DAOTodo;
import com.example.thaonote.model.Completed;

import java.util.ArrayList;

public class CompletedActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView revCompletes;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Completed> clist;
    private CompletedAdapter completedAdapter;
    private LinearLayout linearLayout;
    private DAOTodo DAOTodo;
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
                String txt_search = search.getText().toString().trim();
                txt_search = txt_search.toLowerCase();
                if (!txt_search.equalsIgnoreCase("")) {
                    ArrayList<Completed> newCompleteds =new ArrayList<>();
                    for(Completed completed : clist){
                        String getTodoTitle= completed.getTodoTitle();
                        String getTodoContent= completed.getTodoContent();
                        String getTodoTag= completed.getTodoTag();

                        if(getTodoTitle.contains(txt_search) || getTodoContent.contains(txt_search) || getTodoTag.contains(txt_search)){
                            newCompleteds.add(completed);
                        }
                    }
                    completedAdapter.listFilter(newCompleteds);
                    completedAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void initView(){
        revCompletes = findViewById(R.id.revCompletes);
        DAOTodo = new DAOTodo(this);
        linearLayout=findViewById(R.id.no_completed) ;
        if(DAOTodo.countCompleted() > 0){
            linearLayout.setVisibility(View.GONE);
            revCompletes.setVisibility(View.VISIBLE);
            clist =new ArrayList<>();
            clist = DAOTodo.getAllCompleted();
            completedAdapter =new CompletedAdapter(clist,this);

        }else{
            linearLayout.setVisibility(View.VISIBLE);
            revCompletes.setVisibility(View.GONE);
        }
        linearLayoutManager=new LinearLayoutManager(this);
        revCompletes.setAdapter(completedAdapter);
        revCompletes.setLayoutManager(linearLayoutManager);

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
                Intent com_set = new Intent(this, SettingActivity.class);
                startActivity(com_set);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.back1:
                Intent com_back = new Intent(this, HomeActivity.class);
                startActivity(com_back);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
        }
    }

    //Xác nhận xóa tât cả ghi chú
    private void deleteDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn muốn xóa ghi chú đã hoàn thành không ?");
        builder.setPositiveButton("Xóa tất cả", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(DAOTodo.removeCompletedTodos()){
                    Intent com = new Intent(CompletedActivity.this, CompletedActivity.class);
                    startActivity(com);
                    Toast.makeText(CompletedActivity.this, "Xóa tất cả ghi chú hoàn thành thành công !", Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(CompletedActivity.this, "Ghi chú chưa được xóa !", Toast.LENGTH_SHORT).show();
            }
        }).create().show();
    }
}
