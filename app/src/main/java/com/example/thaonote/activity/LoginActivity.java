package com.example.thaonote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thaonote.R;
import com.example.thaonote.dbhelper.UserDBHelper;
import com.example.thaonote.model.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private EditText username, password;
    private TextView txtRegister, txtForgotpass;
    private User u;

    private UserDBHelper userDBHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        username = findViewById(R.id.inputName);
        password = findViewById(R.id.inputPassword);
        txtRegister = findViewById(R.id.gotoRegister);
        txtForgotpass = findViewById(R.id.forgotPassword);

        userDBHelper = new UserDBHelper(this);

        btnLogin.setOnClickListener(this::onClick);
        txtRegister.setOnClickListener(this::onClick);
        txtForgotpass.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                String username1 = username.getText().toString().trim();
                String pass = password.getText().toString().trim();
                User user = new User(username1, pass);

                if(username1.isEmpty() || pass.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Hãy nhập thông tin!", Toast.LENGTH_LONG).show();
                }else{
                    Boolean isExist = userDBHelper.getCheckLogin(username1, pass);
                    if(isExist){
                        Intent loginIntent = new Intent(getApplicationContext(),
                                HomeActivity.class);
                        startActivity(loginIntent);

                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    } else {
                        password.setText(null);
                        username.setText(null);
                        Toast.makeText(LoginActivity.this, "Username hoặc password không đúng!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.gotoRegister:
                Intent registerIntent = new Intent(LoginActivity.this,
                        RegisterActivity.class);
                startActivity(registerIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }

}
