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
        List<List<ViewItem>> data = DataTool.generateData();
//        List<ViewItem> groupData = new ArrayList<>();
//        ViewItem item = new ViewItem();
//        item.value = 60f;
//        item.time = 0;
//        groupData.add(item);
//
//        ViewItem item2 = new ViewItem();
//        item2.value = 80f;
//        item2.time = 1 * 60 * 60;
//        groupData.add(item2);
//
//        ViewItem item3 = new ViewItem();
//        item3.value = 70f;
//        item3.time = 2 * 60 * 60;
//        groupData.add(item3);
//
//        ViewItem item4 = new ViewItem();
//        item4.value = 90f;
//        item4.time = 3 * 60 * 60;
//        groupData.add(item4);
//
//        ViewItem item5 = new ViewItem();
//        item5.value = 90f;
//        item5.time = 4 * 60 * 60;
//        groupData.add(item5);
//
//        data.add(groupData);
//
//        List<ViewItem> groupData2 = new ArrayList<>();
//        ViewItem item6 = new ViewItem();
//        item6.value = 70f;
//        item6.time = 6 * 60 * 60;
//        groupData2.add(item6);
//
//        ViewItem item7 = new ViewItem();
//        item7.value = 60f;
//        item7.time = 7 * 60 * 60;
//        groupData2.add(item7);
//
//        ViewItem item8 = new ViewItem();
//        item8.value = 80f;
//        item8.time = 8 * 60 * 60;
//        groupData2.add(item8);
//
//        ViewItem item9 = new ViewItem();
//        item9.value = 120f;
//        item9.time = 9 * 60 * 60;
//        groupData2.add(item9);
//
//        ViewItem item10 = new ViewItem();
//        item10.value = 80f;
//        item10.time = 10 * 60 * 60;
//        groupData2.add(item10);
//
//        data.add(groupData2);
//
//        List<ViewItem> groupData3 = new ArrayList<>();
//        ViewItem item11 = new ViewItem();
//        item11.value = 70f;
//        item11.time = 12 * 60 * 60;
//        groupData3.add(item11);
//
//        ViewItem item12 = new ViewItem();
//        item12.value = 60f;
//        item12.time = 13 * 60 * 60;
//        groupData3.add(item12);
//
//        data.add(groupData3);
//
//        List<ViewItem> groupData4 = new ArrayList<>();
//        ViewItem item13 = new ViewItem();
//        item13.value = 70f;
//        item13.time = 15 * 60 * 60;
//        groupData4.add(item13);
////        ViewItem item14 = new ViewItem();
////        item14.value = 70f;
////        item14.time = 18* 60 * 60;
////        groupData4.add(item14);
//        data.add(groupData4);
//
////
//        List<ViewItem> groupData5 = new ArrayList<>();
//        ViewItem item15 = new ViewItem();
//        item15.value = 70f;
//        item15.time = 20 * 60 * 60;
//        groupData5.add(item15);

//        data.add(groupData5);
        lineChartView.setData(data);
    }
}
