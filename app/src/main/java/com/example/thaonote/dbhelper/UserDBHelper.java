package com.example.thaonote.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.thaonote.model.User;

public class UserDBHelper {
    private Context context;
    private DatabaseHelper databaseHelper;

    public UserDBHelper(Context context) {
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context);
    }

    public Boolean insertUser(User user){
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("username", user.getUsername());
        contentValues.put("password", user.getPassword());
        sqLiteDatabase.insert("user", null, contentValues);
        sqLiteDatabase.close();
        return true;
    }
    public boolean getCheckLogin(String username, String password){
        SQLiteDatabase sqLiteDatabase=this.databaseHelper.getReadableDatabase();
        String query="SELECT * FROM user " + " WHERE " + "username =?" + " AND " + "password =?";
        Cursor cursor = sqLiteDatabase.rawQuery(query,new String[]{username, password});
        return (cursor.getCount()>0)?true:false;
    }

}
