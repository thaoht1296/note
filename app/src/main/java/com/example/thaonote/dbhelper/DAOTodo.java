package com.example.thaonote.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.thaonote.model.Completed;
import com.example.thaonote.model.Pending;

import java.util.ArrayList;


public class DAOTodo extends DAOBase {

    //table todo
    public static final String TABLE_TODO_NAME="thao_todo";
    public static final String TODO_ID="todo_id";
    public static final String TODO_TITLE="todo_title";
    public static final String TODO_CONTENT="todo_content";
    public static final String TODO_TAG="todo_tag";
    public static final String TODO_DATE="todo_date";
    public static final String TODO_TIME="todo_time";
    public static final String TODO_STATUS="todo_status";
    public static final String DEFAULT_STATUS="notcompleted";
    public static final String STATUS_COMPLETED="completed";

    //tag table and columns
    public static final String TABLE_TAG_NAME="thao_tag";
    public static final String TAG_ID="tag_id";
    public static final String TAG_TITLE="tag_title";

    //thực thi khóa ngoại
    public static final String FORCE_FOREIGN_KEY="PRAGMA foreign_keys=ON";

    public static final String CREATE_TABLE_TODO = "CREATE TABLE IF NOT EXISTS " + TABLE_TODO_NAME+"("+
            TODO_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
            TODO_TITLE+" TEXT NOT NULL,"+TODO_CONTENT+" TEXT NOT NULL,"+
            TODO_TAG +" INTEGER NOT NULL,"+TODO_DATE+" TEXT NOT NULL,"+
            TODO_TIME+" TEXT NOT NULL,"+TODO_STATUS+" TEXT NOT NULL DEFAULT " + DEFAULT_STATUS+
            ",FOREIGN KEY("+TODO_TAG+") REFERENCES "+TABLE_TAG_NAME+"("+TAG_ID+") ON UPDATE CASCADE ON DELETE CASCADE"+")";

    public DAOTodo(Context context) {
        super(context);
    }


    //thêm mới ghi chú
    public boolean add(Pending pending){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DAOTodo.TODO_TITLE, pending.getTodoTitle());
        contentValues.put(DAOTodo.TODO_CONTENT, pending.getTodoContent());
        contentValues.put(DAOTodo.TODO_TAG, pending.getTodoTag());
        contentValues.put(DAOTodo.TODO_DATE, pending.getTodoDate());
        contentValues.put(DAOTodo.TODO_TIME, pending.getTodoTime());
        contentValues.put(DAOTodo.TODO_STATUS,DAOTodo.DEFAULT_STATUS);
        sqLiteDatabase.insert(DAOTodo.TABLE_TODO_NAME,null,contentValues);
        sqLiteDatabase.close();
        return true;
    }


    // đếm số lượng ghi chú chưa hoàn thành
    public int countNotCompleted(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String[] tableColumns = new String[] {DAOTodo.TODO_ID};
        String[] whereArgs = new String[]{DAOTodo.DEFAULT_STATUS};
        String whereClause = DAOTodo.TODO_STATUS+"=?";

        Cursor cursor = sqLiteDatabase.query(DAOTodo.TABLE_TODO_NAME, tableColumns, whereClause, whereArgs,
                null, null, null);
        return cursor.getCount();
    }

    //đếm số lượng ghi chú đã hoàn thành
    public int countCompleted(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String[] tableColumns = {DAOTodo.TODO_ID};
        String[] whereArgs = {DAOTodo.STATUS_COMPLETED};
        String whereClause = DAOTodo.TODO_STATUS+"=?";

        Cursor cursor = sqLiteDatabase.query(DAOTodo.TABLE_TODO_NAME, tableColumns, whereClause, whereArgs,
                null, null, null);
        return cursor.getCount();
    }

    //List all ghi chú chưa hoàn thành
    public ArrayList<Pending> getAllTodos(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Pending> pendings =new ArrayList<>();

        String whereClause = DAOTodo.TODO_STATUS + "=?";
        String[] whereArgs = {DAOTodo.DEFAULT_STATUS};
        String orderBy = DAOTodo.TABLE_TODO_NAME+"."+DAOTodo.TODO_ID + " ASC";
        String table = "SELECT * FROM " + DAOTodo.TABLE_TODO_NAME+" INNER JOIN " + DAOTodo.TABLE_TAG_NAME+" ON " + DAOTodo.TABLE_TODO_NAME+"."+ DAOTodo.TODO_TAG+"="+
                DAOTodo.TABLE_TAG_NAME+"." + DAOTodo.TAG_ID;

        Cursor cursor = sqLiteDatabase.query(table, null, whereClause, whereArgs,
                null, null, orderBy);

        while (cursor.moveToNext()){
            Pending pending =new Pending();
            pending.setTodoID(cursor.getInt(cursor.getColumnIndex(DAOTodo.TODO_ID)));
            pending.setTodoTitle(cursor.getString(cursor.getColumnIndex(DAOTodo.TODO_TITLE)));
            pending.setTodoContent(cursor.getString(cursor.getColumnIndex(DAOTodo.TODO_CONTENT)));
            pending.setTodoTag(cursor.getString(cursor.getColumnIndex(DAOTodo.TAG_TITLE)));
            pending.setTodoDate(cursor.getString(cursor.getColumnIndex(DAOTodo.TODO_DATE)));
            pending.setTodoTime(cursor.getString(cursor.getColumnIndex(DAOTodo.TODO_TIME)));
            pendings.add(pending);
        }
        cursor.close();
        sqLiteDatabase.close();
        return pendings;
    }

    //list all ghi chú đã hoàn thành
    public ArrayList<Completed> getAllCompleted(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Completed> completeds =new ArrayList<>();


        String whereClause = DAOTodo.TODO_STATUS + "=?";
        String[] whereArgs = {DAOTodo.STATUS_COMPLETED};
        String orderBy = DAOTodo.TABLE_TODO_NAME+"."+DAOTodo.TODO_ID + " ASC";
        String table = "SELECT * FROM " + DAOTodo.TABLE_TODO_NAME+" INNER JOIN " + DAOTodo.TABLE_TAG_NAME+" ON " + DAOTodo.TABLE_TODO_NAME+"."+ DAOTodo.TODO_TAG+"="+
                DAOTodo.TABLE_TAG_NAME+"." + DAOTodo.TAG_ID;

        Cursor cursor = sqLiteDatabase.query(table, null, whereClause, whereArgs,
                null, null, orderBy);

        while (cursor.moveToNext()){
            Completed completed =new Completed();
            completed.setTodoID(cursor.getInt(cursor.getColumnIndex(DAOTodo.TODO_ID)));
            completed.setTodoTitle(cursor.getString(cursor.getColumnIndex(DAOTodo.TODO_TITLE)));
            completed.setTodoContent(cursor.getString(cursor.getColumnIndex(DAOTodo.TODO_CONTENT)));
            completed.setTodoTag(cursor.getString(cursor.getColumnIndex(DAOTodo.TAG_TITLE)));
            completed.setTodoDate(cursor.getString(cursor.getColumnIndex(DAOTodo.TODO_DATE)));
            completed.setTodoTime(cursor.getString(cursor.getColumnIndex(DAOTodo.TODO_TIME)));
            completeds.add(completed);
        }
        cursor.close();
        sqLiteDatabase.close();
        return completeds;
    }

    // update ghi chú theo todo_id
    public boolean updateTodo(Pending pending){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DAOTodo.TODO_TITLE, pending.getTodoTitle());
        contentValues.put(DAOTodo.TODO_CONTENT, pending.getTodoContent());
        contentValues.put(DAOTodo.TODO_TAG, pending.getTodoTag());
        contentValues.put(DAOTodo.TODO_DATE, pending.getTodoDate());
        contentValues.put(DAOTodo.TODO_TIME, pending.getTodoTime());
        contentValues.put(DAOTodo.TODO_STATUS,DAOTodo.DEFAULT_STATUS);

        String whereClause = DAOTodo.TODO_ID+"=?";
        String[] whereArgs = new String[]{String.valueOf(pending.getTodoID())};

        sqLiteDatabase.update(DAOTodo.TABLE_TODO_NAME, contentValues, whereClause, whereArgs);
        return true;
    }

    //tạo ghi chú đã hoàn thành theo id
    public boolean makeCompleted(int todoID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DAOTodo.TODO_STATUS,DAOTodo.STATUS_COMPLETED);

        String whereClause = DAOTodo.TODO_ID+"=?";
        String[] whereArgs = new String[]{String.valueOf(todoID)};

        sqLiteDatabase.update(DAOTodo.TABLE_TODO_NAME,contentValues,whereClause,
                whereArgs);
        sqLiteDatabase.close();
        return true;
    }

    //xóa ghi chú chưa hoàn thành theo id
    public boolean removeTodo(int todoID){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String whereClause = DAOTodo.TODO_ID+"=?";
        String[] whereArgs = {String.valueOf(todoID)};

        sqLiteDatabase.delete(DAOTodo.TABLE_TODO_NAME, whereClause, whereArgs);
        sqLiteDatabase.close();
        return true;
    }

    //xóa tất cả ghi chú đã hoàn thành
    public boolean removeCompletedTodos(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String whereClause = DAOTodo.TODO_STATUS+"=?";
        String[] whereArgs = {DAOTodo.STATUS_COMPLETED};

        sqLiteDatabase.delete(DAOTodo.TABLE_TODO_NAME,whereClause, whereArgs);
        sqLiteDatabase.close();
        return true;
    }


    // lấy tiêu đề của ghi chú theo id
    public String getTodoTitle(int todoID){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String[] tableColumns = new String[] {DAOTodo.TODO_TITLE};
        String whereClause = DAOTodo.TODO_ID+"=?";
        String[] whereArgs = {String.valueOf(todoID)};

        Cursor cursor=sqLiteDatabase.query(DAOTodo.TABLE_TODO_NAME, tableColumns, whereClause, whereArgs,
                null, null, null);
        String title="";
        if(cursor.moveToFirst()){
            title=cursor.getString(cursor.getColumnIndex(DAOTodo.TODO_TITLE));
        }
        cursor.close();
        sqLiteDatabase.close();
        return title;
    }

    //lấy nội dung của ghi chú theo id
    public String getTodoContent(int todoID){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String[] tableColumns = new String[] {DAOTodo.TODO_CONTENT};
        String whereClause = DAOTodo.TODO_ID+"=?";
        String[] whereArgs = {String.valueOf(todoID)};

        Cursor cursor=sqLiteDatabase.query(DAOTodo.TABLE_TODO_NAME, tableColumns, whereClause, whereArgs,
                null, null, null);

        String content="";
        if(cursor.moveToFirst()){
            content=cursor.getString(cursor.getColumnIndex(DAOTodo.TODO_CONTENT));
        }
        cursor.close();
        sqLiteDatabase.close();
        return content;
    }

    //lấy date của ghi chú theo id
    public String getTodoDate(int todoID){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String[] tableColumns = new String[] {DAOTodo.TODO_DATE};
        String whereClause = DAOTodo.TODO_ID+"=?";
        String[] whereArgs = {String.valueOf(todoID)};

        Cursor cursor=sqLiteDatabase.query(DAOTodo.TABLE_TODO_NAME, tableColumns, whereClause, whereArgs,
                null, null, null);

        String date="";
        if(cursor.moveToFirst()){
            date=cursor.getString(cursor.getColumnIndex(DAOTodo.TODO_DATE));
        }
        cursor.close();
        sqLiteDatabase.close();
        return date;
    }

    //lấy time của ghi chú theo id
    public String getTodoTime(int todoID){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String[] tableColumns = new String[] {DAOTodo.TODO_TIME};
        String whereClause = DAOTodo.TODO_ID+"=?";
        String[] whereArgs = {String.valueOf(todoID)};

        Cursor cursor=sqLiteDatabase.query(DAOTodo.TABLE_TODO_NAME, tableColumns, whereClause, whereArgs,
                null, null, null);

        String time="";
        if(cursor.moveToFirst()){
            time=cursor.getString(cursor.getColumnIndex(DAOTodo.TODO_TIME));
        }
        cursor.close();
        sqLiteDatabase.close();
        return time;
    }
}
