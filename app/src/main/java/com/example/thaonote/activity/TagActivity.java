package com.example.thaonote.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thaonote.R;
import com.example.thaonote.adapter.TagAdapter;
import com.example.thaonote.dbhelper.DAOTag;
import com.example.thaonote.model.Tags;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TagActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView revTag;
    private ArrayList<Tags> tlist;
    private TagAdapter tagAdapter;
    private GridLayoutManager gridLayoutManager;
    private FloatingActionButton fabAdd;
    private DAOTag DAOTag;
    private LinearLayout linearLayout;
    private ImageView setting, back1;
    private EditText search;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        initView();


        fabAdd.setOnClickListener(this::onClick);
        setting.setOnClickListener(this::onClick);
        back1.setOnClickListener(this::onClick);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_search = search.getText().toString().trim();
                txt_search = txt_search.toLowerCase();
                if (!txt_search.equalsIgnoreCase("")) {
                    ArrayList<Tags> newTagsModels = new ArrayList<>();
                    for(Tags tags : tlist){
                        String tagTitle= tags.getTagTitle().toLowerCase();
                        if(tagTitle.contains(txt_search)){
                            newTagsModels.add(tags);
                        }
                    }
                    tagAdapter.listFilter(newTagsModels);
                    tagAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "no search", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void initView(){
        revTag=(RecyclerView)findViewById(R.id.revTag);
        linearLayout=(LinearLayout)findViewById(R.id.no_tags);
        DAOTag =new DAOTag(this);
        if(DAOTag.countTags() > 0){
            revTag.setVisibility(View.VISIBLE);
            tlist = new ArrayList<>();
            tlist = DAOTag.getAll();
            tagAdapter=new TagAdapter(tlist,this);
            linearLayout.setVisibility(View.GONE);


        }else{
            linearLayout.setVisibility(View.VISIBLE);
            revTag.setVisibility(View.GONE);
        }
        gridLayoutManager=new GridLayoutManager(this, 2);
        revTag.setAdapter(tagAdapter);
        revTag.setLayoutManager(gridLayoutManager);
        fabAdd = (FloatingActionButton)findViewById(R.id.fabAdd);


        setting = findViewById(R.id.settings);
        back1 = findViewById(R.id.back1);

        search = findViewById(R.id.search);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fabAdd:
                showNewTagDialog();
                break;
            case R.id.settings:
                Intent tag_setting = new Intent(this, SettingActivity.class);
                startActivity(tag_setting);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.back1:
                Intent tag_back = new Intent(this, HomeActivity.class);
                startActivity(tag_back);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
            default:
                return;
        }
    }
    //show add new tag dialog
    private void showNewTagDialog(){
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view=layoutInflater.inflate(R.layout.card_edit_tags,null);
        builder.setView(view);
        final EditText tagTitle= view.findViewById(R.id.edit_tag_title);
        final TextView cancel=(TextView)view.findViewById(R.id.cancel);
        final TextView addNewtag=(TextView)view.findViewById(R.id.edit_new_tag);

        addNewtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getTagTitle=tagTitle.getText().toString().trim();
                boolean checkTag = DAOTag.checkTag(getTagTitle);


                if(getTagTitle.equalsIgnoreCase("") && getTagTitle.length() > 25){
                    tagTitle.setError("Yêu cầu tiêu đề thẻ!");
                }else if(checkTag == true){
                    tagTitle.setError("Tiêu đề đã tồn tại!");
                }else {
                    if(DAOTag.add(new Tags(getTagTitle))){

                        Intent tagIntent = new Intent(TagActivity.this,TagActivity.class);
                        Toast.makeText(TagActivity.this, "Thêm thẻ thành công", Toast.LENGTH_SHORT).show();

                        startActivity(tagIntent);

                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tag_ca = new Intent(TagActivity.this,TagActivity.class);
                startActivity(tag_ca);
            }
        });
        builder.create().show();
    }
}
