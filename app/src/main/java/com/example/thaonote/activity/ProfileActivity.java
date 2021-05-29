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

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private CircleImageView avatar;

    private EditText name, pass, newpass;
    private Button update;
    private ImageView back1;

    private UserDBHelper userDBHelper;

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
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
            case R.id.pro_update:
                String username1 = name.getText().toString().trim();
                String password = pass.getText().toString().trim();
                String newpassword = newpass.getText().toString().trim();

                User user = new User(username1, password);

                if(username1.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Hãy nhập thông tin!", Toast.LENGTH_LONG).show();
                }else{
                    Boolean isExist = userDBHelper.getCheckLogin(username1, password);
                    Toast.makeText(getApplicationContext(), "check login thanh cong", Toast.LENGTH_SHORT).show();
                    if(isExist){
                        userDBHelper.changepassword(username1, newpassword);
                        new SweetAlertDialog(ProfileActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Cập nhật thông tin thành công")
                                .setContentText("Nên cập nhật thông tin sáu tháng một lần!")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();

                    } else {
                        new SweetAlertDialog(ProfileActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Cập nhật thông tin không thành công")
                                .setContentText("Thông tin không chính xác! Vui lòng điền lại")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    }
                }
                break;
        }
    }
}
