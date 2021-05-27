package com.example.thaonote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thaonote.R;
import com.example.thaonote.dbhelper.UserDBHelper;
import com.example.thaonote.model.User;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegister;
    private EditText username, password;

    private UserDBHelper userDBHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.btnRegister);
        username = findViewById(R.id.inputName);
        password = findViewById(R.id.inputPassword);

        userDBHelper = new UserDBHelper(this);

        btnRegister.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                String username1 = username.getText().toString().trim();
                String pass = password.getText().toString().trim();
                User user = new User(username1, pass);

                if(username1.equals("")){
                    username.setError("Hãy điền thông tin");
                }
                else if(pass.equals("") || pass.length() < 6){
                    password.setError("Hãy điền đúng định dạng thông tin");
                }
                else{
                    if(userDBHelper.insertUser(new User(username1, pass)) == true){
                        Toast.makeText(getApplicationContext(), "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                        new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Đăng kí thành công")
                                .setContentText("Hello "+ username1)
                                .setConfirmText("OK!")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    }
                                })
                                .show();
                    }
                }
        }
    }
}
