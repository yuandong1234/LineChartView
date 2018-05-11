package com.yuong.linechart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
        float[] data = {60, 80, 70, 90, 110};
        lineChartView.setData(data);
    }
}
