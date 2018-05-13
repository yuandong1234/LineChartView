package com.yuong.linechart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LineChartView lineChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lineChartView = findViewById(R.id.lineChartView);
        initData();
    }

    private void initData() {
        List<List<Float>> data = new ArrayList<>();
        List<Float> groupData = new ArrayList<>();
        groupData.add(60f);
        groupData.add(80f);
        groupData.add(70f);
        groupData.add(90f);
        groupData.add(90f);
        data.add(groupData);
        lineChartView.setData(data);
    }
}
