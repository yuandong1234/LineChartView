package com.yuong.linechart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LineChartView lineChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lineChartView = findViewById(R.id.lineChartView);
        lineChartView.setStyle(LineChartView.MONTH);
        initData();
    }

    private void initData() {
//        List<List<ViewItem>> data = DataTool.generateData();
//        lineChartView.setData(data);
        List<List<Float>> data = DataTool.generateData2();
        lineChartView.setData2(data);
    }
}
