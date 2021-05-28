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
import com.example.thaonote.dbhelper.TagDBHelper;
import com.example.thaonote.model.TagsModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TagActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView allTags;
    private ArrayList<TagsModel> tagsModelModels;
    private TagAdapter tagAdapter;
    private GridLayoutManager gridLayoutManager;
    private FloatingActionButton fabAddTag;
    private TagDBHelper tagDBHelper;
    private LinearLayout linearLayout;
    private ImageView setting, back1;
    private EditText search;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        initView();
        fabAddTag.setOnClickListener(this::onClick);
        setting.setOnClickListener(this::onClick);
        back1.setOnClickListener(this::onClick);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = search.getText().toString();
                txt = txt.toLowerCase();
                if (!txt.equalsIgnoreCase("")) {
                    ArrayList<TagsModel> newTagsModelModels = new ArrayList<>();
                    for(TagsModel tagsModel: tagsModelModels){
                        String tagTitle=tagsModel.getTagTitle().toLowerCase();
                        if(tagTitle.contains(txt)){
                            newTagsModelModels.add(tagsModel);
                        }
                    }
                    tagAdapter.filterTags(newTagsModelModels);
                    tagAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "no search", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void initView(){
        allTags=(RecyclerView)findViewById(R.id.viewAllTags);
        linearLayout=(LinearLayout)findViewById(R.id.no_tags_available);
        tagDBHelper=new TagDBHelper(this);
        if(tagDBHelper.countTags()==0){
            linearLayout.setVisibility(View.VISIBLE);
            allTags.setVisibility(View.GONE);
        }else{
            allTags.setVisibility(View.VISIBLE);
            tagsModelModels = new ArrayList<>();
            tagsModelModels = tagDBHelper.fetchTags();
            tagAdapter=new TagAdapter(tagsModelModels,this);
            linearLayout.setVisibility(View.GONE);
        }
        gridLayoutManager=new GridLayoutManager(this, 2);
        allTags.setAdapter(tagAdapter);
        allTags.setLayoutManager(gridLayoutManager);
        fabAddTag = (FloatingActionButton)findViewById(R.id.fabAddTag);


        setting = findViewById(R.id.settings);
        back1 = findViewById(R.id.back1);

        search = findViewById(R.id.search);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fabAddTag:
                showNewTagDialog();
                break;
            case R.id.settings:
                startActivity(new Intent(this, AppSettings.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.back1:
                startActivity(new Intent(this, HomeActivity.class));
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
                String getTagTitle=tagTitle.getText().toString();
                boolean isTagEmpty=tagTitle.getText().toString().isEmpty();
                boolean tagExists=tagDBHelper.tagExists(getTagTitle);

                if(isTagEmpty){
                    tagTitle.setError("Yêu cầu tiêu đề thẻ!");
                }else if(tagExists){
                    tagTitle.setError("Tiêu đề đã tồn tại!");
                }else {
                    if(tagDBHelper.addNewTag(new TagsModel(getTagTitle))){
                        Toast.makeText(TagActivity.this, "Thêm thẻ thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(TagActivity.this,TagActivity.class));
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TagActivity.this,TagActivity.class));
            }
        });
        builder.create().show();
    }
}
