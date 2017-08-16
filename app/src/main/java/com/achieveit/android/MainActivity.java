package com.achieveit.android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static List<Goal> goalList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.goal_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        GoalAdapter adapter = new GoalAdapter(goalList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        Button addGoal = (Button) findViewById(R.id.add_goal);
        addGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddActivity.actionStart(MainActivity.this, null);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.goal_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        GoalAdapter adapter = new GoalAdapter(goalList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {

        List<Goal> resultList = DataSupport.findAll(Goal.class);
        goalList = resultList;
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
