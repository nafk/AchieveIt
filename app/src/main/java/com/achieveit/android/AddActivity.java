package com.achieveit.android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ContentFrameLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.Date;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        final EditText achieveNameView = (EditText) findViewById(R.id.achieve_name);
        final EditText totalView = (EditText) findViewById(R.id.total);
        final EditText doneView = (EditText) findViewById(R.id.done);
        final EditText startDateView = (EditText) findViewById(R.id.start_date);
        final EditText remarkView = (EditText) findViewById(R.id.remark);
        //final CalendarView calendarView = (CalendarView) findViewById(R.id.calendar);

        Intent intent = getIntent();
        final Achieve achieve = (Achieve) intent.getSerializableExtra("achieve");

        if (null != achieve) {
            achieveNameView.setText(achieve.getName());
            totalView.setText(String.valueOf(achieve.getTotal()));
            doneView.setText(String.valueOf(achieve.getDone()));
            startDateView.setText(achieve.getStartDate());
            remarkView.setText(achieve.getRemark());

            Button deleteAchieve = (Button) findViewById(R.id.delete_achieve);
            deleteAchieve.setVisibility(View.VISIBLE);
            deleteAchieve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataSupport.delete(Achieve.class, achieve.getId());
                    MainActivity.actionStart(AddActivity.this);
                }
            });


        }

        Button saveAchieve = (Button) findViewById(R.id.save_achieve);
        saveAchieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String achieveName = achieveNameView.getText().toString();

                if (achieveName.isEmpty()) {
                    Toast.makeText(AddActivity.this, "请填写成就名称", Toast.LENGTH_SHORT).show();
                } else {
                    String totalStr = totalView.getText().toString();
                    String doneStr = doneView.getText().toString();
                    String startDate = startDateView.getText().toString();
                    String remark = remarkView.getText().toString();

                    Integer total = "".equals(totalStr) ? 1 : Integer.parseInt(totalStr);
                    Integer done = "".equals(doneStr) ? 0 : Integer.parseInt(doneStr);

                    Achieve achieve1 = new Achieve();
                    achieve1.setName(achieveName);
                    achieve1.setTotal(total);
                    achieve1.setDone(done);
                    achieve1.setStartDate(startDate);
                    achieve1.setRemark(remark);

                    if (null != achieve) {
                        achieve1.update(achieve.getId());
                    } else {
                        achieve1.save();
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

    public static void actionStart(Context context, Achieve achieve) {
        Intent intent = new Intent(context, AddActivity.class);
        intent.putExtra("achieve", achieve);
        context.startActivity(intent);
    }
}
