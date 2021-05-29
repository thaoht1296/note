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
import com.example.thaonote.adapter.PendingTodoAdapter;
import com.example.thaonote.dbhelper.TagDBHelper;
import com.example.thaonote.dbhelper.TodoDBHelper;
import com.example.thaonote.model.PendingModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PendingActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView pendingTodos;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<PendingModel> pendingModels;
    private PendingTodoAdapter pendingTodoAdapter;
    private FloatingActionButton addNewTodo;
    private TagDBHelper tagDBHelper;
    private String getTagTitleString;
    private TodoDBHelper todoDBHelper;
    private LinearLayout linearLayout;
    private ImageView completed, all_tags, back1;

    private EditText search;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);
        initView();

        addNewTodo.setOnClickListener(this::onClick);
        completed.setOnClickListener(this::onClick);
        all_tags.setOnClickListener(this::onClick);
        back1.setOnClickListener(this::onClick);



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = search.getText().toString();
                txt = txt.toLowerCase();
                if (!txt.equalsIgnoreCase("")) {
                    ArrayList<PendingModel> newPendingModels =new ArrayList<>();
                    for(PendingModel pendingModel : pendingModels){
                        String getTodoTitle= pendingModel.getTodoTitle().toLowerCase();
                        String getTodoContent= pendingModel.getTodoContent().toLowerCase();
                        String getTodoTag= pendingModel.getTodoTag().toLowerCase();

                        if(getTodoTitle.contains(txt) || getTodoContent.contains(txt) || getTodoTag.contains(txt)){
                            newPendingModels.add(pendingModel);
                        }
                    }
                    pendingTodoAdapter.filterTodos(newPendingModels);
                    pendingTodoAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void initView(){
        pendingTodos=(RecyclerView)findViewById(R.id.pending_todos_view);
        linearLayout=(LinearLayout)findViewById(R.id.no_pending_todo_section);
        tagDBHelper=new TagDBHelper(this);
        todoDBHelper=new TodoDBHelper(this);

        if(todoDBHelper.countTodos()==0){
            linearLayout.setVisibility(View.VISIBLE);
            pendingTodos.setVisibility(View.GONE);
        }else{
            pendingModels =new ArrayList<>();
            pendingModels =todoDBHelper.fetchAllTodos();
            pendingTodoAdapter=new PendingTodoAdapter(pendingModels,this);
        }
        linearLayoutManager=new LinearLayoutManager(this);
        pendingTodos.setAdapter(pendingTodoAdapter);
        pendingTodos.setLayoutManager(linearLayoutManager);
        addNewTodo=(FloatingActionButton)findViewById(R.id.fabAddTodo);
        search = findViewById(R.id.search);
        completed = findViewById(R.id.completed);
        all_tags = findViewById(R.id.all_tags);
        back1 = findViewById(R.id.back1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabAddTodo:
                if(tagDBHelper.countTags()==0){
                    showDialog();
                }else{
                    showNewTodoDialog();
                }
                break;
            case R.id.all_tags:
                Intent pend_tag = new Intent(this, TagActivity.class);
                startActivity(pend_tag);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                Toast.makeText(this, "thaodebug", Toast.LENGTH_LONG).show();
                break;
            case R.id.completed:
                Intent pend_com = new Intent(this,CompletedTodos.class);
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

    //show add new todos dialog and adding the todos into the database
    private void showNewTodoDialog(){
        //getting current calendar credentials
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

        //stores all the tags title in string format
        ArrayAdapter<String> tagsModelArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,tagDBHelper.fetchTagStrings());
        //setting dropdown view resouce for spinner
        tagsModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //setting the spinner adapter
        todoTags.setAdapter(tagsModelArrayAdapter);
        todoTags.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getTagTitleString=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        final EditText todoDate=view.findViewById(R.id.todo_date);
        final EditText todoTime=view.findViewById(R.id.todo_time);

        //getting the tododate
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

        //getting the todos time
        todoTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog=new TimePickerDialog(PendingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        calendar.set(Calendar.HOUR_OF_DAY,i);
                        calendar.set(Calendar.MINUTE,i1);
                        String timeFormat= DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
                        todoTime.setText(timeFormat);
                    }
                },hour,minute,false);
                timePickerDialog.show();
            }
        });
        TextView cancel = (TextView)view.findViewById(R.id.cancel);
        TextView addTodo = (TextView)view.findViewById(R.id.add_new_todo);

        addTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting all the values from add new todos dialog
                String getTodoTitle=todoTitle.getText().toString();
                String getTodoContent=todoContent.getText().toString();
                int todoTagID=tagDBHelper.fetchTagID(getTagTitleString);
                String getTodoDate=todoDate.getText().toString();
                String getTime=todoTime.getText().toString();

                //checking the data fiels
                boolean isTitleEmpty=todoTitle.getText().toString().isEmpty();
                boolean isContentEmpty=todoContent.getText().toString().isEmpty();
                boolean isDateEmpty=todoDate.getText().toString().isEmpty();
                boolean isTimeEmpty=todoTime.getText().toString().isEmpty();

                //adding the todos
                if(isTitleEmpty){
                    todoTitle.setError("Yêu cầu tiêu đề");
                }else if(isContentEmpty){
                    todoContent.setError("Yêu cầu nội dung !");
                }else if(isDateEmpty){
                    todoDate.setError("Chưa điền ngày tháng !");
                }else if(isTimeEmpty){
                    todoTime.setError("Chưa điền thời gian !");
                }else if(todoDBHelper.addNewTodo(
                        new PendingModel(getTodoTitle,getTodoContent, String.valueOf(todoTagID),getTodoDate,getTime))){

                    Toast.makeText(PendingActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();


                    // notification

                    String [] time_spilt=getTodoDate.split("/");
                    int date_alarm = Integer.parseInt(time_spilt[0]);
                    System.out.println(date_alarm);
                    int month_alarm = Integer.parseInt(time_spilt[1])-1;
                    System.out.println(month_alarm);
                    int year_alarm = Integer.parseInt(time_spilt[2]);
                    System.out.println(year_alarm);
                    Calendar calendar_alarm = Calendar.getInstance();
                    calendar_alarm.setTimeInMillis(System.currentTimeMillis());
                    calendar_alarm.set(year_alarm,month_alarm,date_alarm);

                    AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

                    Intent intent = new Intent(PendingActivity.this,
                            MyReceiver.class);
                    intent.putExtra("myAction", "mDoNotify");
                    intent.putExtra("Title", getTodoTitle);
                    intent.putExtra("Description", getTodoContent);

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
