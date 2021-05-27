package com.example.thaonote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thaonote.R;
import com.example.thaonote.dbhelper.UserDBHelper;
import com.example.thaonote.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private CircleImageView avatar;

    private EditText name, pass, newpass;
    private Button update;
    private ImageView back1;

    private UserDBHelper userDBHelper;
    private TextView test;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();

        userDBHelper = new UserDBHelper(this);

        back1.setOnClickListener(this::onClick);
    }

    public void initView(){
        avatar = findViewById(R.id.pro_avatar);
        name = findViewById(R.id.pro_name);
        pass = findViewById(R.id.pro_pass);
        newpass = findViewById(R.id.pro_newpass);

        update = findViewById(R.id.pro_update);
        back1 = findViewById(R.id.back1);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back1:
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case R.id.pro_update:


                break;
        }
    }
}
