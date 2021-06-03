package com.example.thaonote.adapter;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thaonote.R;
import com.example.thaonote.activity.MyReceiver;
import com.example.thaonote.activity.PendingActivity;
import com.example.thaonote.dbhelper.DAOTag;
import com.example.thaonote.dbhelper.DAOTodo;
import com.example.thaonote.model.Pending;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.PendingDataHolder>{
    private ArrayList<Pending> plist;
    private Context context;
    private String getTagTitle;
    private DAOTag DAOTag;
    private DAOTodo DAOTodo;

    public PendingAdapter(ArrayList<Pending> list, Context context) {
        this.plist = list;
        this.context = context;
    }

    @Override
    public PendingDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_pending,parent,false);
        return new PendingDataHolder(view);
    }

    @Override
    public void onBindViewHolder(PendingDataHolder holder, int position) {
        DAOTodo =new DAOTodo(context);
        final Pending pending = plist.get(position);
        holder.todoTitle.setText(pending.getTodoTitle());
        holder.todoContent.setText(pending.getTodoContent());
        holder.todoDate.setText(pending.getTodoDate());
        holder.todoTag.setText(pending.getTodoTag());
        holder.todoTime.setText(pending.getTodoTime());
        holder.option.setOnClickListener(new View.OnClickListener() {
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
                                showDialogEdit(pending.getTodoID());
                                return true;
                            case R.id.delete:
                                showDeleteDialog(pending.getTodoID());
                                return true;
                            default:
                                return false;
                        }
                    }
                });
            }
        });
        holder.makeCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCompletedDialog(pending.getTodoID());
            }
        });
    }

    //Dialog xóa ghi chú
    private void showDeleteDialog(final int tagID){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa ghi chú");
        builder.setMessage("Bạn có chắc chắn muốn xóa ?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(DAOTodo.removeTodo(tagID)){
                    Toast.makeText(context, "Xóa thành công !", Toast.LENGTH_SHORT).show();
                    Intent it1 = new Intent(context, PendingActivity.class);
                    context.startActivity(it1);
                }
            }
        }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Ghi chú chưa bị xóa !", Toast.LENGTH_SHORT).show();
                Intent it1 = new Intent(context, PendingActivity.class);
                context.startActivity(it1);
            }
        }).create().show();
    }

    @Override
    public int getItemCount() {
        return plist.size();
    }

    //sửa ghi chú theo id
    private void showDialogEdit(final int todoID){
        DAOTodo =new DAOTodo(context);
        DAOTag =new DAOTag(context);

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.activity_edit_pending,null);
        builder.setView(view);

        EditText todoTitle = view.findViewById(R.id.todo_title);
        EditText todoContent = view.findViewById(R.id.todo_content);
        Spinner todoTags = view.findViewById(R.id.todo_tag);

        ArrayAdapter<String> tagsModelArrayAdapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item, DAOTag.getAllTitle());

        tagsModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        todoTags.setAdapter(tagsModelArrayAdapter);
        todoTags.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getTagTitle=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        EditText todoDate=view.findViewById(R.id.todo_date);
        EditText todoTime=view.findViewById(R.id.todo_time);

        //lấy các giá trị từ csdl
        todoTitle.setText(DAOTodo.getTodoTitle(todoID));
        todoContent.setText(DAOTodo.getTodoContent(todoID));
        todoDate.setText(DAOTodo.getTodoDate(todoID));
        todoTime.setText(DAOTodo.getTodoTime(todoID));


        Calendar calendar= Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int hour=calendar.get(Calendar.HOUR);
        int minute=calendar.get(Calendar.MINUTE);


        todoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
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
                TimePickerDialog timePickerDialog=new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int x, int y) {
                        Calendar newCalendar= Calendar.getInstance();
                        newCalendar.set(Calendar.HOUR,x);
                        newCalendar.set(Calendar.MINUTE,y);
                        String timeFormat= DateFormat.getTimeInstance(DateFormat.SHORT).format(newCalendar.getTime());
                        todoTime.setText(timeFormat);
                    }
                },hour,minute,false);
                timePickerDialog.show();
            }
        });
        TextView cancel=(TextView)view.findViewById(R.id.cancel);
        TextView addTodo=(TextView)view.findViewById(R.id.add_new_todo);

        addTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String getTodoTitle = todoTitle.getText().toString();
                String getTodoContent = todoContent.getText().toString();
                int todoTagID = DAOTag.getOneID(getTagTitle);
                String getTodoDate = todoDate.getText().toString();
                String getTime = todoTime.getText().toString();

                String datePattern = "^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$";

                if(getTodoTitle.equalsIgnoreCase("") && getTodoTitle.length() > 50){
                    todoTitle.setError("Yêu cầu tiêu đề");
                }else if(getTodoContent.equalsIgnoreCase("") && getTodoContent.length() > 100){
                    todoContent.setError("Yêu cầu nội dung !");
                }else if(getTodoDate.equalsIgnoreCase("") && getTodoDate.matches(datePattern)){
                    todoDate.setError("Ngày tháng chưa đúng định dạng !");
                }else if(getTime.equalsIgnoreCase("")){
                    todoTime.setError("Chưa điền thời gian !");
                }else if(DAOTodo.updateTodo(
                        new Pending(todoID,getTodoTitle,getTodoContent, String.valueOf(todoTagID),getTodoDate,getTime)
                )){
                    Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                    Intent it1 = new Intent(context, PendingActivity.class);
                    context.startActivity(it1);

                    // notifacation
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

                    AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

                    Intent intent = new Intent(context,
                            MyReceiver.class);
                    intent.putExtra("toDoAction", "toDoNotify");
                    intent.putExtra("toDoTitle", getTodoTitle);
                    intent.putExtra("toDoContent", getTodoContent);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                            0, intent, 0);
                    am.set(AlarmManager.RTC_WAKEUP, calendar_alarm.getTimeInMillis(), pendingIntent);

                    Intent pend = new Intent(context, PendingActivity.class);
                    context.startActivity(pend);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context,PendingActivity.class));
            }
        });
        builder.create().show();
    }

    //dialog xác nhận ghi chú hoàn thành
    private void showCompletedDialog(final int tagID){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Hoàn thành công việc");
        builder.setMessage("Đã hoàn thành công việc cần làm ?");
        builder.setPositiveButton("Hoàn thành", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(DAOTodo.makeCompleted(tagID)){
                    Intent it1 = new Intent(context, PendingActivity.class);
                    context.startActivity(it1);
                }
            }
        }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }

    public class PendingDataHolder extends RecyclerView.ViewHolder {
        TextView todoTitle,todoContent,todoTag,todoDate,todoTime;
        ImageView option,makeCompleted;
        public PendingDataHolder(View view) {
            super(view);
            todoTitle = view.findViewById(R.id.pending_todo_title);
            todoContent = view.findViewById(R.id.pending_todo_content);
            todoTag = view.findViewById(R.id.todo_tag);
            todoDate = view.findViewById(R.id.todo_date);
            todoTime = view.findViewById(R.id.todo_time);
            option = view.findViewById(R.id.option);
            makeCompleted = view.findViewById(R.id.make_completed);
        }
    }

    // filterTodos
    public void listFilter(ArrayList<Pending> newlist){
        plist =new ArrayList<>();
        plist.addAll(newlist);
        notifyDataSetChanged();
    }
}
