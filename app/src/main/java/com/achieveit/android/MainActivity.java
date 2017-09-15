package com.achieveit.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static List<Goal> mGoalList = new ArrayList<>();
    private GoalAdapter adapter;


    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                AddActivity.actionStart(MainActivity.this, null);
            }
        });

        initialize();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.??, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.settings) {
//            Toast.makeText(this,"settings",Toast.LENGTH_SHORT).show();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        for (int i = 0; i < adapter.mGoalList.size(); i++) {
            Goal goal = adapter.mGoalList.get(i);
            goal.setSort(i);
            goal.save();
        }
    }

    private void initData() {
        mGoalList = DataSupport.order("sort").find(Goal.class);
    }

    private void initialize() {
        initData();
        if (mGoalList == null || mGoalList.isEmpty()) {
            findViewById(R.id.init_hint).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.init_hint).setVisibility(View.GONE);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.goal_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new GoalAdapter(mGoalList, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        DragItemTouchHelperCallback dragCallback = new DragItemTouchHelperCallback();
        dragCallback.itemMoveListener = adapter;
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(dragCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
