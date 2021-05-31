package com.example.thaonote.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="thaonote";

    //table tag
    public static final String TABLE_TAG_NAME="tags";
    public static final String COL_TAG_ID="tag_id";
    public static final String COL_TAG_TITLE="tag_title";

    //table todo
    public static final String TABLE_TODO_NAME="todos";
    public static final String COL_TODO_ID="todo_id";
    public static final String COL_TODO_TITLE="todo_title";
    public static final String COL_TODO_CONTENT="todo_content";
    public static final String COL_TODO_TAG="todo_tag";
    public static final String COL_TODO_DATE="todo_date";
    public static final String COL_TODO_TIME="todo_time";
    public static final String COL_TODO_STATUS="todo_status";
    public static final String COL_DEFAULT_STATUS="pending";
    public static final String COL_STATUS_COMPLETED="completed";

    //thực thi khóa ngoại
    public static final String FORCE_FOREIGN_KEY="PRAGMA foreign_keys=ON";

    //tạo bảng tags
    private static final String CREATE_TAGS_TABLE="CREATE TABLE IF NOT EXISTS " + TABLE_TAG_NAME+"("+
            COL_TAG_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
            COL_TAG_TITLE+" TEXT NOT NULL UNIQUE)";
//            "tagUser INTEGER NOT NULL," +
//            "FOREIGN KEY (tagUser) " +  " REFERENCES user (user_id) ON UPDATE CASCADE ON DELETE CASCADE)";

    //tạo bảng todos
    private static final String CREATE_TODOS_TABLE="CREATE TABLE IF NOT EXISTS " + TABLE_TODO_NAME+"("+
            COL_TODO_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
            COL_TODO_TITLE+" TEXT NOT NULL,"+
            COL_TODO_CONTENT+" TEXT NOT NULL,"+
            COL_TODO_TAG +" INTEGER NOT NULL,"+
            COL_TODO_DATE+" TEXT NOT NULL,"+
            COL_TODO_TIME+" TEXT NOT NULL,"+
            COL_TODO_STATUS + " TEXT NOT NULL DEFAULT " +
            COL_DEFAULT_STATUS +
            ",FOREIGN KEY("+COL_TODO_TAG+") REFERENCES "+TABLE_TAG_NAME+"("+COL_TAG_ID+") ON UPDATE CASCADE ON DELETE CASCADE"+")";

    //kiểm tra bảng có tồn tại không trước khi xóa bảng
    private static final String DROP_TAGS_TABLE="DROP TABLE IF EXISTS " + TABLE_TAG_NAME;
    //xóa
    private static final String DROP_TODOS_TABLE="DROP TABLE IF EXISTS " + TABLE_TODO_NAME;



    // table user
    private static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS user(" +
           "user_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "username TEXT NOT NULL UNIQUE," +
            "password TEXT NOT NULL)";
    private static final String DROP_USER_TABLE = "DROP TABLE IF EXISTS user";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TAGS_TABLE);
        sqLiteDatabase.execSQL(CREATE_TODOS_TABLE);
        sqLiteDatabase.execSQL(FORCE_FOREIGN_KEY);
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TAGS_TABLE);
        sqLiteDatabase.execSQL(DROP_TODOS_TABLE);
        sqLiteDatabase.execSQL(DROP_USER_TABLE);
        onCreate(sqLiteDatabase);
    }
}
