package com.example.thaonote.graph;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thaonote.R;
import com.example.thaonote.activity.HomeActivity;
import com.example.thaonote.dbhelper.TagDBHelper;
import com.example.thaonote.dbhelper.TodoDBHelper;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class PieChartActivity extends AppCompatActivity {

    private TextView tvht, tvcht;
    private PieChart pieChart;
    private ImageView back1;

    private TodoDBHelper todoDBHelper;
    private TagDBHelper tagDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        back1 = findViewById(R.id.back1);
        tvht = findViewById(R.id.tvhthanh);
        tvcht = findViewById(R.id.tvchoanthanh);
        pieChart = findViewById(R.id.piechart);

        tagDBHelper=new TagDBHelper(this);
        todoDBHelper=new TodoDBHelper(this);

        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PieChartActivity.this, HomeActivity.class));
            }
        });


        tvht.setText(Integer.toString(todoDBHelper.countCompletedTodos()));
        tvcht.setText(Integer.toString(todoDBHelper.countTodos()));

        pieChart.addPieSlice(
                new PieModel(
                        "HoanThanh",
                        Integer.parseInt(tvht.getText().toString()),
                        Color.parseColor("#FFA726")));
        pieChart.addPieSlice(
                new PieModel(
                        "ChuaHoanThanh",
                        Integer.parseInt(tvcht.getText().toString()),
                        Color.parseColor("#66BB6A")));

        pieChart.startAnimation();
    }
}