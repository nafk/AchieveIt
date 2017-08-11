package com.achieveit.android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static List<Achieve> achieveList = new ArrayList<>();

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

        Button addAchieve = (Button) findViewById(R.id.add_achieve);
        addAchieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddActivity.actionStart(MainActivity.this);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
        Log.d("mainactivity","onrestart~~~~~~~~~");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.achieve_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        AchieveAdapter adapter = new AchieveAdapter(achieveList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {

        List<Achieve> resultList = DataSupport.findAll(Achieve.class);
        Log.d("mainactivity","initdata:"+resultList.size());
        achieveList = resultList;
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
