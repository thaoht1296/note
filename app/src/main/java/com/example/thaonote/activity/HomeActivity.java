package com.example.thaonote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thaonote.R;
import com.example.thaonote.graph.BarChartActivity;
import com.example.thaonote.graph.PieChartActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView pending, tags, completes, statistic, settings, logout;
    private TextView name;
    private ImageView editprofile;
    private CircleImageView avatar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        pending.setOnClickListener(this::onClick);
        tags.setOnClickListener(this::onClick);
        completes.setOnClickListener(this::onClick);
        statistic.setOnClickListener(this::onClick);
        settings.setOnClickListener(this::onClick);
        logout.setOnClickListener(this::onClick);

        editprofile.setOnClickListener(this::onClick);
    }

    public void initView(){
        pending = findViewById(R.id.home_pending);
        tags = findViewById(R.id.home_tags);
        completes = findViewById(R.id.home_completes);
        statistic = findViewById(R.id.home_tke);
        settings = findViewById(R.id.home_settings);
        logout = findViewById(R.id.home_logout);

        name = findViewById(R.id.home_name);
        editprofile = findViewById(R.id.home_editprofile);
        avatar = findViewById(R.id.home_avatar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_pending:
                startActivity(new Intent(this, PendingActivity.class));
                break;
            case R.id.home_tags:
                startActivity(new Intent(this, TagActivity.class));
                break;
            case R.id.home_completes:
                startActivity(new Intent(this, CompletedTodos.class));
                break;
            case R.id.home_tke:
                startActivity(new Intent(this, PieChartActivity.class));
                break;
            case R.id.home_settings:
                startActivity(new Intent(this, AppSettings.class));
                break;
            case R.id.home_logout:
//                AlertDialog.Builder builder=new AlertDialog.Builder(this);
//                builder.setMessage("Bạn có muốn thoát không?");
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        System.exit(0);
//                    }
//                });
//                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//                AlertDialog alert=builder.create();
//                alert.show();

                new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Bạn có muốn thoát không?")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                System.exit(0);
                            }
                        })
                        .setCancelButton("Hủy", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
                break;
            case R.id.home_editprofile:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
        }
    }
}
