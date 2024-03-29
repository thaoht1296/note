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
import com.example.thaonote.dbhelper.DAOTag;
import com.example.thaonote.model.Tags;

import java.util.ArrayList;


public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagDataHolder> {
    private ArrayList<Tags> tlist;
    private Context context;
    private DAOTag DAOTag;

    public TagAdapter(ArrayList<Tags> list, Context context) {
        this.tlist = list;
        this.context = context;
    }

    @Override
    public TagDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tags,parent,false);
        return new TagDataHolder(view);
    }

    @Override
    public void onBindViewHolder(TagDataHolder holder, int position) {
        final Tags tags = tlist.get(position);
        holder.tag_title.setText(tags.getTagTitle());
        DAOTag =new DAOTag(context);
        holder.tag_menu.setOnClickListener(new View.OnClickListener() {
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
                                editTag(tags.getTagID());
                                return true;
                            case R.id.delete:
                                deleteTag(tags.getTagID());
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
        return tlist.size();
    }

    public class TagDataHolder extends RecyclerView.ViewHolder{
        TextView tag_title;
        ImageView tag_menu;
        public TagDataHolder(View v) {
            super(v);
            tag_title = v.findViewById(R.id.tag_title);
            tag_menu = v.findViewById(R.id.tag_menu);
        }
    }

    private void deleteTag(final int tagID){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa thẻ");
        builder.setMessage(R.string.tag_delete_dialog_msg);
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(DAOTag.deleteTag(tagID)){
                    Toast.makeText(context, "Xóa thẻ thành công!", Toast.LENGTH_SHORT).show();
                    Intent it1 = new Intent(context, TagActivity.class);
                    context.startActivity(it1);
                }
            }
        }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Thẻ chưa bị xóa!", Toast.LENGTH_SHORT).show();
                Intent it2 = new Intent(context, TagActivity.class);
                context.startActivity(it2);
            }
        }).create().show();
    }

    private void editTag(final int tagID){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.card_edit_tags,null);
        builder.setView(view);

        EditText tagEditTitle=view.findViewById(R.id.edit_tag_title);
        tagEditTitle.setText(DAOTag.getOneTitle(tagID));
        TextView cancel = view.findViewById(R.id.cancel);
        TextView edittag = view.findViewById(R.id.edit_new_tag);

        edittag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getTagTitle = tagEditTitle.getText().toString();
                boolean checkTag = DAOTag.checkTag(getTagTitle);

                if(!getTagTitle.equalsIgnoreCase("") && getTagTitle.length() < 25){
                    tagEditTitle.setError("Yêu cầu tiêu đề thẻ");
                }else if(checkTag == true){
                    tagEditTitle.setError("Tiêu đề thẻ đã tồn tại");
                }else if(DAOTag.updateTag(new Tags(tagID,getTagTitle))){
                    Toast.makeText(context, "Lưu thành công!", Toast.LENGTH_SHORT).show();
                    Intent it3 = new Intent(context, TagActivity.class);
                    context.startActivity(it3);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Thẻ không thay đổi!", Toast.LENGTH_SHORT).show();
                Intent it4 = new Intent(context, TagActivity.class);
                context.startActivity(it4);
            }
        });
        builder.create().show();
    }

    public void listFilter(ArrayList<Tags> newlist){
        tlist =new ArrayList<>();
        tlist.addAll(newlist);
        notifyDataSetChanged();
    }
}
