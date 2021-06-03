package com.example.thaonote.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME="thaonote.db";

    private Context mContext = null;
    private static DatabaseHelper sInstance;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    public static DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DAOTag.CREATE_TABLE_TAG);
        sqLiteDatabase.execSQL(DAOTodo.CREATE_TABLE_TODO);
        sqLiteDatabase.execSQL(DAOTag.FORCE_FOREIGN_KEY);
        sqLiteDatabase.execSQL(DAOTodo.FORCE_FOREIGN_KEY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
