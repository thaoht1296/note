package com.example.thaonote.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thaonote.R;
import com.example.thaonote.graph.PieChartActivity;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView pending, tags, completes, statistic, settings, logout;
    public String username;

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


    }

    public void initView(){
        pending = findViewById(R.id.home_pending);
        tags = findViewById(R.id.home_tags);
        completes = findViewById(R.id.home_completes);
        statistic = findViewById(R.id.home_tke);
        settings = findViewById(R.id.home_settings);
        logout = findViewById(R.id.home_logout);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_pending:
                Intent home_pending = new Intent(this, PendingActivity.class);
                startActivity(home_pending);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.home_tags:
                Intent home_tags = new Intent(this, TagActivity.class);
                startActivity(home_tags);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.home_completes:
                Intent home_comp = new Intent(this, CompletedActivity.class);
                startActivity(home_comp);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.home_tke:
                Intent home_tk = new Intent(this, PieChartActivity.class);
                startActivity(home_tk);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.home_settings:
                Intent home_set = new Intent(this, SettingActivity.class);
                startActivity(home_set);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.home_logout:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setMessage("Bạn có muốn thoát không?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        System.exit(0);
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert=builder.create();
                alert.show();

                break;
        }
    }
}
