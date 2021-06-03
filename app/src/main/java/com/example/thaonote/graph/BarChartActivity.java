package com.example.thaonote.graph;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.thaonote.R;

import com.example.thaonote.activity.HomeActivity;
import com.example.thaonote.dbhelper.DAOTag;
import com.example.thaonote.dbhelper.DAOTodo;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;


public class BarChartActivity extends AppCompatActivity {

    private ImageView back1;
    private BarChart barChart;
    private BarData barData;
    private BarDataSet barDataSet;
    private ArrayList barEntries;

    private DAOTodo DAOTodo;
    private DAOTag DAOTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        barChart = findViewById(R.id.barchart);
        back1 = findViewById(R.id.back1);

        DAOTag =new DAOTag(this);
        DAOTodo =new DAOTodo(this);


        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BarChartActivity.this, HomeActivity.class));
            }
        });

        getEntries();
        barDataSet = new BarDataSet(barEntries, "");
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(18f);
    }

    private void getEntries() {
        barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1f, DAOTodo.countCompletedTodos()));
        barEntries.add(new BarEntry(2f, DAOTodo.countTodos()));
    }
}