package com.example.thaonote.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.thaonote.R;
import com.example.thaonote.activity.TagActivity;
import com.example.thaonote.dbhelper.TagDBHelper;
import com.example.thaonote.model.Tags;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;


public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagDataHolder> {
    private ArrayList<Tags> tagsModels;
    private Context context;
    private TagDBHelper tagDBHelper;

    public TagAdapter(ArrayList<Tags> tagsModels, Context context) {
        this.tagsModels = tagsModels;
        this.context = context;
    }

    @Override
    public TagDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tags,parent,false);
        return new TagDataHolder(view);
    }

    @Override
    public void onBindViewHolder(TagDataHolder holder, int position) {
        final Tags tagsModel=tagsModels.get(position);
        holder.tag_title.setText(tagsModel.getTagTitle());
        tagDBHelper=new TagDBHelper(context);
        holder.tag_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(context,view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_edit,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.edit:
                                editTag(tagsModel.getTagID());
                                return true;
                            case R.id.delete:
                                removeTag(tagsModel.getTagID());
                                return true;
                            default:
                                return false;
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return tagsModels.size();
    }

    public class TagDataHolder extends RecyclerView.ViewHolder{
        TextView tag_title;
        ImageView tag_option;
        public TagDataHolder(View itemView) {
            super(itemView);
            tag_title=(TextView)itemView.findViewById(R.id.tag_title);
            tag_option=(ImageView)itemView.findViewById(R.id.tags_option);
        }
    }

    //remove tag
    private void removeTag(final int tagID){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa thẻ");
        builder.setMessage(R.string.tag_delete_dialog_msg);
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(tagDBHelper.removeTag(tagID)){
                    Toast.makeText(context, "Xóa thẻ thành công!", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, TagActivity.class));
                }
            }
        }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Thẻ chưa bị xóa!", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, TagActivity.class));
            }
        }).create().show();
    }

    //update tag
    private void editTag(final int tagID){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.card_edit_tags,null);
        builder.setView(view);
        final EditText tagEditTitle=view.findViewById(R.id.edit_tag_title);
        tagEditTitle.setText(tagDBHelper.fetchTagTitle(tagID));
        final TextView cancel=(TextView)view.findViewById(R.id.cancel);
        final TextView editNewtag=(TextView)view.findViewById(R.id.edit_new_tag);

        editNewtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getTagTitle=tagEditTitle.getText().toString();
                boolean isTagEmpty=tagEditTitle.getText().toString().isEmpty();
                boolean tagExists=tagDBHelper.tagExists(getTagTitle);

                if(isTagEmpty){
                    tagEditTitle.setError("Yêu cầu tiêu đề thẻ");
                }else if(tagExists){
                    tagEditTitle.setError("Tiêu đề thẻ đã tồn tại");
                }else if(tagDBHelper.saveTag(new Tags(tagID,getTagTitle))){
                    Toast.makeText(context, "Lưu thành công!", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, TagActivity.class));
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Thẻ không thay đổi!", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, TagActivity.class));
            }
        });
        builder.create().show();
    }

    //search filter
    public void filterTags(ArrayList<Tags> newTagsModels){
        tagsModels=new ArrayList<>();
        tagsModels.addAll(newTagsModels);
        notifyDataSetChanged();
    }
}
