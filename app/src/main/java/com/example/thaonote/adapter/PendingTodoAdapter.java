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
import com.example.thaonote.activity.CompletedTodos;
import com.example.thaonote.activity.MyReceiver;
import com.example.thaonote.activity.PendingActivity;
import com.example.thaonote.dbhelper.TagDBHelper;
import com.example.thaonote.dbhelper.TodoDBHelper;
import com.example.thaonote.model.PendingModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PendingTodoAdapter extends RecyclerView.Adapter<PendingTodoAdapter.PendingDataHolder>{
    private ArrayList<PendingModel> pendingModels;
    private Context context;
    private String getTagTitleString;
    private TagDBHelper tagDBHelper;
    private TodoDBHelper todoDBHelper;

    public PendingTodoAdapter(ArrayList<PendingModel> pendingModels, Context context) {
        this.pendingModels = pendingModels;
        this.context = context;
    }

    @Override
    public PendingDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_pending,parent,false);
        return new PendingDataHolder(view);
    }

    @Override
    public void onBindViewHolder(PendingDataHolder holder, int position) {
        todoDBHelper=new TodoDBHelper(context);
        final PendingModel pendingModel = pendingModels.get(position);
        holder.todoTitle.setText(pendingModel.getTodoTitle());
        holder.todoContent.setText(pendingModel.getTodoContent());
        holder.todoDate.setText(pendingModel.getTodoDate());
        holder.todoTag.setText(pendingModel.getTodoTag());
        holder.todoTime.setText(pendingModel.getTodoTime());
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
                                showDialogEdit(pendingModel.getTodoID());
                                return true;
                            case R.id.delete:
                                showDeleteDialog(pendingModel.getTodoID());
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
                showCompletedDialog(pendingModel.getTodoID());
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
                if(todoDBHelper.removeTodo(tagID)){
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
        return pendingModels.size();
    }

    //sửa ghi chú theo id
    private void showDialogEdit(final int todoID){
        todoDBHelper=new TodoDBHelper(context);
        tagDBHelper=new TagDBHelper(context);

        final AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view=layoutInflater.inflate(R.layout.activity_edit_pending,null);
        builder.setView(view);

        final EditText todoTitle=view.findViewById(R.id.todo_title);
        final EditText todoContent=view.findViewById(R.id.todo_content);
        Spinner todoTags=(Spinner)view.findViewById(R.id.todo_tag);

        ArrayAdapter<String> tagsModelArrayAdapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,tagDBHelper.fetchTagStrings());

        tagsModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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

        //lấy các giá trị từ csdl
        todoTitle.setText(todoDBHelper.fetchTodoTitle(todoID));
        todoContent.setText(todoDBHelper.fetchTodoContent(todoID));
        todoDate.setText(todoDBHelper.fetchTodoDate(todoID));
        todoTime.setText(todoDBHelper.fetchTodoTime(todoID));


        final Calendar calendar= Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        final int hour=calendar.get(Calendar.HOUR);
        final int minute=calendar.get(Calendar.MINUTE);


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
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        Calendar newCalendar= Calendar.getInstance();
                        newCalendar.set(Calendar.HOUR,i);
                        newCalendar.set(Calendar.MINUTE,i1);
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

                String getTodoTitle=todoTitle.getText().toString();
                String getTodoContent=todoContent.getText().toString();
                int todoTagID=tagDBHelper.fetchTagID(getTagTitleString);
                String getTodoDate=todoDate.getText().toString();
                String getTime=todoTime.getText().toString();


                boolean isTitleEmpty=todoTitle.getText().toString().isEmpty();
                boolean isContentEmpty=todoContent.getText().toString().isEmpty();
                boolean isDateEmpty=todoDate.getText().toString().isEmpty();
                boolean isTimeEmpty=todoTime.getText().toString().isEmpty();


                if(isTitleEmpty){
                    todoTitle.setError("Yêu cầu tiêu đề !");
                }else if(isContentEmpty){
                    todoContent.setError("Yêu cầu nội dung !");
                }else if(isDateEmpty){
                    todoDate.setError("Chưa điền ngày tháng !");
                }else if(isTimeEmpty){
                    todoTime.setError("Chưa điền thời gian !");
                }else if(todoDBHelper.updateTodo(
                        new PendingModel(todoID,getTodoTitle,getTodoContent, String.valueOf(todoTagID),getTodoDate,getTime)
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
                    intent.putExtra("myAction", "mDoNotify");
                    intent.putExtra("Title", getTodoTitle);
                    intent.putExtra("Description", getTodoContent);

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
                if(todoDBHelper.makeCompleted(tagID)){
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
        public PendingDataHolder(View itemView) {
            super(itemView);
            todoTitle=(TextView)itemView.findViewById(R.id.pending_todo_title);
            todoContent=(TextView)itemView.findViewById(R.id.pending_todo_content);
            todoTag=(TextView)itemView.findViewById(R.id.todo_tag);
            todoDate=(TextView)itemView.findViewById(R.id.todo_date);
            todoTime=(TextView)itemView.findViewById(R.id.todo_time);
            option=(ImageView)itemView.findViewById(R.id.option);
            makeCompleted=(ImageView)itemView.findViewById(R.id.make_completed);
        }
    }

    //filter the search
    public void filterTodos(ArrayList<PendingModel> newPendingModels){
        pendingModels =new ArrayList<>();
        pendingModels.addAll(newPendingModels);
        notifyDataSetChanged();
    }
}
