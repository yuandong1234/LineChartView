package com.yuong.linechart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private LineChartView lineChartView;
    private Button btnDay, btnMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initView() {
        lineChartView = findViewById(R.id.lineChartView);
        btnDay = findViewById(R.id.btn_day);
        btnMonth = findViewById(R.id.btn_month);
        btnDay.setOnClickListener(this);
        btnMonth.setOnClickListener(this);
    }

    private void initData() {
        lineChartView.setStyle(LineChartView.DAY);
        List<List<ViewItem>> data = DataTool.generateData();
        lineChartView.setData(data);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_day:
                initData();
                break;
            case R.id.btn_month:
                initMonthData();
                break;
        }
    }

    private void initMonthData() {
        lineChartView.setStyle(LineChartView.MONTH);
        List<List<Float>> data = DataTool.generateData2();
        lineChartView.setData2(data);
    }
}
