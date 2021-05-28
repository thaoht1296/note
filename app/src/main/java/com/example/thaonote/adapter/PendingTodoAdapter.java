package com.example.thaonote.adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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

    //showing confirmation dialog for deleting the todos
    private void showDeleteDialog(final int tagID){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa ghi chú");
        builder.setMessage("Bạn có chắc chắn muốn xóa ?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(todoDBHelper.removeTodo(tagID)){
                    Toast.makeText(context, "Xóa thành công !", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, PendingActivity.class));
                }
            }
        }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Ghi chú chưa bị xóa !", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, PendingActivity.class));
            }
        }).create().show();
    }

    @Override
    public int getItemCount() {
        return pendingModels.size();
    }

    //showing edit dialog for editing todos according to the todoid
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
        //stores all the tags title in string format
        ArrayAdapter<String> tagsModelArrayAdapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,tagDBHelper.fetchTagStrings());
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

        //setting the default values coming from the database
        todoTitle.setText(todoDBHelper.fetchTodoTitle(todoID));
        todoContent.setText(todoDBHelper.fetchTodoContent(todoID));
        todoDate.setText(todoDBHelper.fetchTodoDate(todoID));
        todoTime.setText(todoDBHelper.fetchTodoTime(todoID));

        //getting current calendar credentials
        final Calendar calendar= Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        final int hour=calendar.get(Calendar.HOUR);
        final int minute=calendar.get(Calendar.MINUTE);

        //getting the tododate
        todoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(Calendar.YEAR,i);
                        calendar.set(Calendar.MONTH,i1);
                        calendar.set(Calendar.DAY_OF_MONTH,i2);
                        todoDate.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime()));
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        //getting the todos time
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
                    context.startActivity(new Intent(context,PendingActivity.class));
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

    //showing confirmation dialog for making the todo completed
    private void showCompletedDialog(final int tagID){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Hoàn thành công việc");
        builder.setMessage("Đã hoàn thành công việc cần làm ?");
        builder.setPositiveButton("Hoàn thành", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(todoDBHelper.makeCompleted(tagID)){
                    context.startActivity(new Intent(context, CompletedTodos.class));
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
