package com.example.thaonote.welcome;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.thaonote.R;
import com.example.thaonote.activity.HomeActivity;
import com.example.thaonote.activity.LoginActivity;

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
