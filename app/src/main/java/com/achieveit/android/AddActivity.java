package com.achieveit.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Button saveAchieve = (Button) findViewById(R.id.save_achieve);
        saveAchieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView achieveNameView = (TextView) findViewById(R.id.achieve_name);
                TextView workloadView = (TextView) findViewById(R.id.workload);
                TextView startDateView = (TextView) findViewById(R.id.start_date);

                String achieveName = String.valueOf(achieveNameView.getText());
                int workload = Integer.parseInt(String.valueOf(workloadView.getText()));
                String startDate = String.valueOf(startDateView.getText());

                Achieve achieve = new Achieve();
                achieve.setName(achieveName);
                achieve.setTotal(workload);
                achieve.setDone(0);
                achieve.setStartDate(startDate);
                achieve.save();
            }
        });
    }
}
