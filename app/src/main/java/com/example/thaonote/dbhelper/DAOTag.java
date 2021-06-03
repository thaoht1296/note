package com.example.thaonote.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.thaonote.model.Tags;

import java.util.ArrayList;
import java.util.List;

public class DAOTag extends DAOBase {

    public static final String TABLE_TAG_NAME="thao_tag";
    public static final String TAG_ID="tag_id";
    public static final String TAG_TITLE="tag_title";

    //thực thi khóa ngoại
    public static final String FORCE_FOREIGN_KEY="PRAGMA foreign_keys=ON";

    public static final String CREATE_TABLE_TAG = "CREATE TABLE IF NOT EXISTS " + TABLE_TAG_NAME+"("+
            TAG_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
            TAG_TITLE+" TEXT NOT NULL UNIQUE"+")";


    public DAOTag(@Nullable Context context) {
        super(context);
    }


    public boolean add(Tags tags){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DAOTag.TAG_TITLE, tags.getTagTitle());
        sqLiteDatabase.insert(DAOTag.TABLE_TAG_NAME,null,contentValues);
        sqLiteDatabase.close();
        return true;
    }

    public boolean checkTag(String tagTitle){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String whereClause = "tag_title = ?";
        String[] whereArgs = {tagTitle};
        String[] tableColumns = new String[] {DAOTag.TAG_TITLE};
        Cursor rs = sqLiteDatabase.query("thao_tag", tableColumns, whereClause, whereArgs,
                null, null, null);
        return (rs.getCount()>0)?true:false;
    }


    public int countTags(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String[] tableColumns = new String[] {DAOTag.TAG_ID};

        Cursor rs = sqLiteDatabase.query(DAOTag.TABLE_TAG_NAME, tableColumns, null, null, null, null,null);
        return rs.getCount();
    }

    public ArrayList<Tags> getAll(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Tags> list =new ArrayList<>();

        Cursor rs = sqLiteDatabase.query(DAOTag.TABLE_TAG_NAME, null, null,
                null, null, null, null);
        while ((rs != null) && (rs.moveToNext())) {
            int id= rs.getInt(0);
            String title = rs.getString(1);
            list.add(new Tags(id,title));
        }
        return list;
    }

    public boolean deleteTag(int tagID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL(DAOTag.FORCE_FOREIGN_KEY);
        String whereClause = "tag_id = ?";
        String[] whereArgs = {Integer.toString(tagID)};
        sqLiteDatabase.delete("thao_tag",
                whereClause, whereArgs);
        return true;
    }

    public boolean updateTag(Tags tags){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DAOTag.TAG_TITLE, tags.getTagTitle());

        String whereClause = DAOTag.TAG_ID + "=?";
        String[] whereArgs = {String.valueOf(tags.getTagID())};

        sqLiteDatabase.update(DAOTag.TABLE_TAG_NAME, contentValues, whereClause,
                whereArgs);
        sqLiteDatabase.close();
        return true;
    }


    public ArrayList<String> getAllTitle(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> tagsModels=new ArrayList<>();
        String[] tableColumns = new String[] {DAOTag.TAG_TITLE};
        Cursor cursor = sqLiteDatabase.query(DAOTag.TABLE_TAG_NAME, tableColumns, null,
                null, null, null, null);
        while (cursor.moveToNext()){
            tagsModels.add(cursor.getString(cursor.getColumnIndex(DAOTag.TAG_TITLE)));
        }
        cursor.close();
        sqLiteDatabase.close();
        return tagsModels;
    }


    public String getOneTitle(int tagID){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String[] tableColumns = new String[] {DAOTag.TAG_TITLE};
        String whereClause = "tag_id = ?";
        String[] whereArgs = new String[]{String.valueOf(tagID)};

        Cursor cursor = sqLiteDatabase.query(DAOTag.TABLE_TAG_NAME, tableColumns,
                whereClause, whereArgs, null, null, null);
        String title="";
        if(cursor.moveToFirst()){
            title=cursor.getString(cursor.getColumnIndex(DAOTag.TAG_TITLE));
        }
        cursor.close();
        sqLiteDatabase.close();
        return title;
    }

    public int getOneID(String tagTitle){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String[] tableColumns = new String[] {DAOTag.TAG_ID};
        String whereClause = "tag_title = ?";
        String[] whereArgs = new String[]{String.valueOf(tagTitle)};

        Cursor cursor = sqLiteDatabase.query(DAOTag.TABLE_TAG_NAME, tableColumns,
                whereClause, whereArgs, null, null, null);

        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex(DAOTag.TAG_ID));
    }


    public List<Tags> searchByTitle(String key){
        List<Tags> listTag = new ArrayList<>();
        String whereClause = "name like ?";
        String[] whereArgs = {"%"+key+"%"};
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("thao_tag", null, whereClause,
                whereArgs, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            listTag.add(new Tags(id, title));
        }
        return listTag;
    }

}
