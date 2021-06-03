package com.example.thaonote.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thaonote.R;
import com.example.thaonote.adapter.PendingAdapter;
import com.example.thaonote.dbhelper.DAOTag;
import com.example.thaonote.dbhelper.DAOTodo;
import com.example.thaonote.model.Pending;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PendingActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView revPend;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Pending> plist;
    private PendingAdapter pendingAdapter;
    private FloatingActionButton fabAdd;
    private DAOTag DAOTag;
    private String getTagTitleString;
    private DAOTodo DAOTodo;
    private LinearLayout linearLayout;
    private ImageView completed, img_tag, back1;

    private EditText search;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);
        initView();

        fabAdd.setOnClickListener(this::onClick);
        completed.setOnClickListener(this::onClick);
        img_tag.setOnClickListener(this::onClick);
        back1.setOnClickListener(this::onClick);



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_search = search.getText().toString().trim();
                txt_search = txt_search.toLowerCase();
                if (!txt_search.equalsIgnoreCase("")) {
                    ArrayList<Pending> newPendings =new ArrayList<>();
                    for(Pending pending : plist){
                        String getTodoTitle= pending.getTodoTitle().toLowerCase();
                        String getTodoContent= pending.getTodoContent().toLowerCase();
                        String getTodoTag= pending.getTodoTag().toLowerCase();

                        if(getTodoTitle.contains(txt_search) || getTodoContent.contains(txt_search) || getTodoTag.contains(txt_search)){
                            newPendings.add(pending);
                        }
                    }
                    pendingAdapter.listFilter(newPendings);
                    pendingAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void initView(){
        revPend = findViewById(R.id.revPen);
        linearLayout= findViewById(R.id.no_pending);
        DAOTag = new DAOTag(this);
        DAOTodo = new DAOTodo(this);

        if(DAOTodo.countNotCompleted() > 0){
            plist =new ArrayList<>();
            plist = DAOTodo.getAllTodos();
            pendingAdapter =new PendingAdapter(plist,this);

        }else{
            linearLayout.setVisibility(View.VISIBLE);
            revPend.setVisibility(View.GONE);
        }

        linearLayoutManager=new LinearLayoutManager(this);
        revPend.setAdapter(pendingAdapter);
        revPend.setLayoutManager(linearLayoutManager);
        fabAdd= findViewById(R.id.fab);
        search = findViewById(R.id.search);
        completed = findViewById(R.id.completed);
        img_tag = findViewById(R.id.img_tags);
        back1 = findViewById(R.id.back1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                if(DAOTag.countTags()==0){
                    showDialog();
                }else{
                    showNewTodoDialog();
                }
                break;
            case R.id.img_tags:
                Intent pend_tag = new Intent(this, TagActivity.class);
                startActivity(pend_tag);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                Toast.makeText(this, "thaodebug", Toast.LENGTH_LONG).show();
                break;
            case R.id.completed:
                Intent pend_com = new Intent(this, CompletedActivity.class);
                startActivity(pend_com);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.back1:
                Intent pend_back = new Intent(this, HomeActivity.class);
                startActivity(pend_back);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
        }
    }

    private void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Tạo thẻ");
        builder.setMessage("Không có thẻ nào trong cơ sở dữ liệu. Trước tiên bạn cần phải tạo thẻ để tiếp tục!");
        builder.setPositiveButton("Tạo mới", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(PendingActivity.this, TagActivity.class);
                startActivity(intent);
//                Toast.makeText(PendingActivity.this, "thaoht",Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }

    // hiển thị dialog khi thêm mới ghi chú
    private void showNewTodoDialog(){

        final Calendar calendar= Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        final int hour=calendar.get(Calendar.HOUR);
        final int minute=calendar.get(Calendar.MINUTE);

        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view=layoutInflater.inflate(R.layout.activity_edit_pending,null);
        builder.setView(view);

        final EditText todoTitle=view.findViewById(R.id.todo_title);
        final EditText todoContent=view.findViewById(R.id.todo_content);
        Spinner todoTags=(Spinner)view.findViewById(R.id.todo_tag);


        // lưu trữ all tags title
        ArrayAdapter<String> tagsModelArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, DAOTag.getAllTitle());

        // cài đặt view cho spinner
        tagsModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //cài đặt spiner apdater
        todoTags.setAdapter(tagsModelArrayAdapter);
        todoTags.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // lấy title ở vị trí pos spinner
                getTagTitleString=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        final EditText todoDate=view.findViewById(R.id.todo_date);
        final EditText todoTime=view.findViewById(R.id.todo_time);


        todoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(PendingActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        todoDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });


        todoTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog=new TimePickerDialog(PendingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int x, int y) {
                        calendar.set(Calendar.HOUR_OF_DAY,x);
                        calendar.set(Calendar.MINUTE,y);
                        String timeFormat= DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
                        todoTime.setText(timeFormat);
                    }
                },hour,minute,false);
                timePickerDialog.show();
            }
        });
        TextView cancel = view.findViewById(R.id.cancel);
        TextView addTodo = view.findViewById(R.id.add_new_todo);

        addTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String getTodoTitle=todoTitle.getText().toString();
                String getTodoContent=todoContent.getText().toString();
                int todoTagID= DAOTag.getOneID(getTagTitleString);
                String getTodoDate=todoDate.getText().toString();
                String getTime=todoTime.getText().toString();

                String datePattern = "^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$";

                if(getTodoTitle.equalsIgnoreCase("") && getTodoTitle.length() > 50){
                    todoTitle.setError("Yêu cầu tiêu đề");
                }else if(getTodoContent.equalsIgnoreCase("") && getTodoContent.length() > 100){
                    todoContent.setError("Yêu cầu nội dung !");
                }else if(getTodoDate.equalsIgnoreCase("") && getTodoDate.matches(datePattern)){
                    todoDate.setError("Ngày tháng chưa đúng định dạng !");
                }else if(getTime.equalsIgnoreCase("")){
                    todoTime.setError("Chưa điền thời gian !");
                }else if(DAOTodo.add(
                        new Pending(getTodoTitle,getTodoContent, String.valueOf(todoTagID),getTodoDate,getTime))){

                    Toast.makeText(PendingActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();


                    // notification

                    String [] time_spilt=getTodoDate.split("/");
                    int date_alarm = Integer.parseInt(time_spilt[0]);
//                    System.out.println(date_alarm);
                    int month_alarm = Integer.parseInt(time_spilt[1])-1;
//                    System.out.println(month_alarm);
                    int year_alarm = Integer.parseInt(time_spilt[2]);
//                    System.out.println(year_alarm);
                    Calendar calendar_alarm = Calendar.getInstance();
                    calendar_alarm.setTimeInMillis(System.currentTimeMillis());
                    calendar_alarm.set(year_alarm,month_alarm,date_alarm);

                    AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

                    Intent intent = new Intent(PendingActivity.this,
                            MyReceiver.class);
                    intent.putExtra("toDoAction", "toDoNotify");
                    intent.putExtra("toDoTitle", getTodoTitle);
                    intent.putExtra("toDoContent", getTodoContent);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(PendingActivity.this,
                            0, intent, 0);
                    am.set(AlarmManager.RTC_WAKEUP, calendar_alarm.getTimeInMillis(), pendingIntent);

                    Intent pend = new Intent(PendingActivity.this, PendingActivity.class);
                    startActivity(pend);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pend = new Intent(PendingActivity.this, PendingActivity.class);
                startActivity(pend);
            }
        });
        builder.create().show();
    }

}
