package com.achieveit.android;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        final EditText goalNameView = (EditText) findViewById(R.id.goal_name);
        final EditText totalView = (EditText) findViewById(R.id.total);
        final EditText doneView = (EditText) findViewById(R.id.done);
        final EditText startDateView = (EditText) findViewById(R.id.start_date);
        final EditText remarkView = (EditText) findViewById(R.id.remark);

        Intent intent = getIntent();
        final Goal goal = (Goal) intent.getSerializableExtra("goal");

        if (null != goal) {
            goalNameView.setText(goal.getName());
            totalView.setText(String.valueOf(goal.getTotal()));
            doneView.setText(String.valueOf(goal.getDone()));
            startDateView.setText(goal.getStartDate());
            remarkView.setText(goal.getRemark());

            Button deleteGoal = (Button) findViewById(R.id.delete_goal);
            deleteGoal.setVisibility(View.VISIBLE);
            deleteGoal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataSupport.delete(Goal.class, goal.getId());
                    MainActivity.actionStart(AddActivity.this);
                }
            });
        }

        int year;
        int month;
        int day;
        Calendar calendar = Calendar.getInstance();
        if (!startDateView.getText().toString().isEmpty()) {
            String startDate = startDateView.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = sdf.parse(startDate);
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int day) {
                startDateView.setText(year + "-" + (month + 1) + "-" + day);
            }
        }, year, month, day);

        startDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show();
            }
        });

        Button saveGoal = (Button) findViewById(R.id.save_goal);
        saveGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String GoalName = goalNameView.getText().toString();

                if (GoalName.isEmpty()) {
                    Toast.makeText(AddActivity.this, "请填写成就名称", Toast.LENGTH_SHORT).show();
                } else {
                    String totalStr = totalView.getText().toString();
                    String doneStr = doneView.getText().toString();
                    String startDate = startDateView.getText().toString();
                    String remark = remarkView.getText().toString();

                    Integer total = "".equals(totalStr) ? 1 : Integer.parseInt(totalStr);
                    Integer done = "".equals(doneStr) ? 0 : Integer.parseInt(doneStr);

                    Goal goal1 = new Goal();
                    goal1.setName(GoalName);
                    goal1.setTotal(total);
                    goal1.setDone(done);
                    goal1.setStartDate(startDate);
                    goal1.setRemark(remark);

                    if (null != goal) {
                        goal1.update(goal.getId());
                    } else {
                        goal1.save();
                    }
                    MainActivity.actionStart(AddActivity.this);
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void actionStart(Context context, Goal goal) {
        Intent intent = new Intent(context, AddActivity.class);
        intent.putExtra("goal", goal);
        context.startActivity(intent);
    }
}
