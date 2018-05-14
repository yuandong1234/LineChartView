package com.yuong.linechart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuandong on 2018/5/14.
 */

public class DataTool {

    public static List<List<ViewItem>> generateData(){

        List<List<ViewItem>> data = new ArrayList<>();

        //分组一==============================================================
        List<ViewItem> groupData = new ArrayList<>();
        ViewItem item = new ViewItem();
        item.value = 60f;
        item.time = 0;
        groupData.add(item);
        data.add(groupData);

        //分组二======================================================
        List<ViewItem> groupData2 = new ArrayList<>();
        ViewItem item2 = new ViewItem();
        item2.value = 80f;
        item2.time = 3 * 60 * 60;
        groupData2.add(item2);

        ViewItem item3 = new ViewItem();
        item3.value = 70f;
        item3.time = 4 * 60 * 60;
        groupData2.add(item3);

        ViewItem item4 = new ViewItem();
        item4.value = 90f;
        item4.time = 5 * 60 * 60;
        groupData2.add(item4);

        ViewItem item5 = new ViewItem();
        item5.value = 90f;
        item5.time = 6 * 60 * 60;
        groupData2.add(item5);

        data.add(groupData2);

        //分组三=====================================================
        List<ViewItem> groupData3 = new ArrayList<>();
        ViewItem item6 = new ViewItem();
        item6.value = 70f;
        item6.time = 8 * 60 * 60;
        groupData3.add(item6);

        ViewItem item7 = new ViewItem();
        item7.value = 60f;
        item7.time = 9 * 60 * 60;
        groupData3.add(item7);

        ViewItem item8 = new ViewItem();
        item8.value = 80f;
        item8.time = 10 * 60 * 60;
        groupData3.add(item8);

        ViewItem item9 = new ViewItem();
        item9.value = 120f;
        item9.time = 11 * 60 * 60;
        groupData3.add(item9);

        ViewItem item10 = new ViewItem();
        item10.value = 80f;
        item10.time = 12 * 60 * 60;
        groupData3.add(item10);

        data.add(groupData3);

        //分组四=====================================================
        List<ViewItem> groupData4 = new ArrayList<>();
        ViewItem item11 = new ViewItem();
        item11.value = 70f;
        item11.time = 14 * 60 * 60;
        groupData4.add(item11);

        ViewItem item12 = new ViewItem();
        item12.value = 60f;
        item12.time = 15 * 60 * 60;
        groupData4.add(item12);
        data.add(groupData4);

        //分组五=====================================================
        List<ViewItem> groupData5 = new ArrayList<>();
        ViewItem item13 = new ViewItem();
        item13.value = 70f;
        item13.time = 17 * 60 * 60;
        groupData5.add(item13);
//        ViewItem item14 = new ViewItem();
//        item14.value = 70f;
//        item14.time = 18* 60 * 60;
//        groupData4.add(item14);
        data.add(groupData5);

        //分组六====================================
        List<ViewItem> groupData6 = new ArrayList<>();
        ViewItem item15 = new ViewItem();
        item15.value = 70f;
        item15.time = 20 * 60 * 60;
        groupData6.add(item15);

        data.add(groupData6);

        //分组七
        List<ViewItem> groupData7 = new ArrayList<>();
        ViewItem item16 = new ViewItem();
        item16.value = 70f;
        item16.time = 22 * 60 * 60;
        groupData7.add(item16);

        ViewItem item17 = new ViewItem();
        item17.value = 70f;
        item17.time = 24 * 60 * 60;
        groupData7.add(item17);

        data.add(groupData7);
        return data;
    }
}
