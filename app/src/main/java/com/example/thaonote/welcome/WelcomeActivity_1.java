package com.example.thaonote.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thaonote.R;
import com.example.thaonote.activity.HomeActivity;

public class WelcomeActivity_1 extends AppCompatActivity {


    private Button skip, next;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_1);
        skip = findViewById(R.id.btn_skip);
        next = findViewById(R.id.btn_next);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity_1.this, HomeActivity.class));

                // animation slide activity
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity_1.this, WelcomeActivity_2.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

            }
        });
    }

}
