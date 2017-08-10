package com.achieveit.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Achieve> achieveList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.achieve_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        AchieveAdapter adapter = new AchieveAdapter(achieveList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        Achieve achieve1 = new Achieve();
        achieve1.setName("first goal");
        achieve1.setDone(20);
        achieve1.setTotal(580);
        achieve1.setStartDate("2017-08-10");

        Achieve achieve2 = new Achieve();
        achieve2.setName("second goal");
        achieve2.setDone(350);
        achieve2.setTotal(1580);
        achieve2.setStartDate("2017-08-11");

        achieveList.add(achieve1);
        achieveList.add(achieve2);
        //
    }
}
