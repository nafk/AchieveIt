package com.achieveit.android;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    private Goal mGoal = null;
    private EditText mGoalNameView;
    private EditText mTotalView;
    private EditText mDoneView;
    private EditText mStartDateView;
    private EditText mPlanEndDateView;
    private EditText mEndDateView;
    private EditText mRemarkView;

    private TextView mDaysView;
    private TextView mTaskPerDayView;
    private TextView mTotalCopyView;
    private TextView mDoneCopyView;
    private TextView mUnDoneView;
    private TextView mDaysLeftView;
    private TextView mTaskPerDayOfDaysLeftView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(this.getString(R.string.new_goal));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mGoalNameView = (EditText) findViewById(R.id.goal_name);
        mTotalView = (EditText) findViewById(R.id.total);
        mDoneView = (EditText) findViewById(R.id.done);
        mStartDateView = (EditText) findViewById(R.id.start_date);
        mPlanEndDateView = (EditText) findViewById(R.id.plan_end_date);
        mEndDateView = (EditText) findViewById(R.id.end_date);
        mRemarkView = (EditText) findViewById(R.id.remark);

        mDaysView = (TextView) findViewById(R.id.days);
        mTaskPerDayView = (TextView) findViewById(R.id.task_per_day);
        mTotalCopyView = (TextView) findViewById(R.id.total_copy);
        mDoneCopyView = (TextView) findViewById(R.id.done_copy);
        mUnDoneView = (TextView) findViewById(R.id.undone);
        mDaysLeftView = (TextView) findViewById(R.id.days_left);
        mTaskPerDayOfDaysLeftView = (TextView) findViewById(R.id.task_per_day_of_days_left);


        Intent intent = getIntent();
        mGoal = (Goal) intent.getSerializableExtra("goal");

        if (null != mGoal) {
            toolbar.setTitle(this.getString(R.string.edit_goal));
            mGoalNameView.setText(mGoal.getName());
            mTotalView.setText(String.valueOf(mGoal.getTotal()));
            mDoneView.setText(String.valueOf(mGoal.getDone()));
            mStartDateView.setText(mGoal.getStartDate());
            mPlanEndDateView.setText(mGoal.getPlanEndDate());

            mTotalCopyView.setText(String.valueOf(mGoal.getTotal()));
            mDoneCopyView.setText(String.valueOf(mGoal.getDone()));
            int unDone = mGoal.getTotal() - mGoal.getDone();
            mUnDoneView.setText(String.valueOf(unDone));

            if (!TextUtils.isEmpty(mGoal.getPlanEndDate())) {
                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                int daysLeft = calcDaysBetween(today, mGoal.getPlanEndDate());
                float taskPerDayOfDaysLeft = (float) unDone / (float) daysLeft;
                taskPerDayOfDaysLeft = (float) (Math.round(taskPerDayOfDaysLeft * 10)) / 10;
                mDaysLeftView.setText(String.valueOf(daysLeft));
                mTaskPerDayOfDaysLeftView.setText(String.valueOf(taskPerDayOfDaysLeft));


                if (!TextUtils.isEmpty(mGoal.getStartDate())) {
                    int days = calcDaysBetween(mGoal.getStartDate(), mGoal.getPlanEndDate());
                    mDaysView.setText(String.valueOf(days));
                    String totalStr = mTotalView.getText().toString();
                    if (!totalStr.isEmpty()) {
                        float taskPerDay = Float.valueOf(totalStr) / (float) days;
                        taskPerDay = (float) (Math.round(taskPerDay * 10)) / 10;
                        mTaskPerDayView.setText(String.valueOf(taskPerDay));

                    }
                }
            }

            mEndDateView.setText(mGoal.getEndDate());
            mRemarkView.setText(mGoal.getRemark());
        }


        processDate(mStartDateView);
        processDate(mPlanEndDateView);
        processDate(mEndDateView);
        bindPlusAndMinusButtons();

        mTotalView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mTotalCopyView.setText(s.toString());
                String doneStr = mDoneView.getText().toString();
                if (!s.toString().isEmpty() && !doneStr.isEmpty()) {
                    int total = Integer.valueOf(s.toString());
                    int done = Integer.valueOf(doneStr);
                    mUnDoneView.setText(String.valueOf(total - done));
                    if (!mDaysLeftView.getText().toString().isEmpty()) {
                        float taskPerDayOfDaysLeft = (float) (total - done) / Float.valueOf(mDaysLeftView.getText().toString());
                        taskPerDayOfDaysLeft = (float) (Math.round(taskPerDayOfDaysLeft * 10)) / 10;
                        mTaskPerDayOfDaysLeftView.setText(String.valueOf(taskPerDayOfDaysLeft));
                    }
                } else {
                    mUnDoneView.setText("");
                    mTaskPerDayOfDaysLeftView.setText("");
                }
                String daysStr = mDaysView.getText().toString();
                if (!s.toString().isEmpty() && !daysStr.isEmpty()) {
                    int total = Integer.valueOf(s.toString());
                    int days = Integer.valueOf(daysStr);

                    float taskPerDay = (float) total / (float) days;
                    taskPerDay = (float) (Math.round(taskPerDay * 10)) / 10;
                    mTaskPerDayView.setText(String.valueOf(taskPerDay));
                } else {
                    mTaskPerDayView.setText("");
                }
            }
        });

        mDoneView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mDoneCopyView.setText(s.toString());
                String totalStr = mTotalView.getText().toString();
                if (!s.toString().isEmpty() && !totalStr.isEmpty()) {
                    int done = Integer.valueOf(s.toString());
                    int total = Integer.valueOf(totalStr);
                    mUnDoneView.setText(String.valueOf(total - done));
                    if (!mDaysLeftView.getText().toString().isEmpty()) {
                        float taskPerDayOfDaysLeft = (float) (total - done) / Float.valueOf(mDaysLeftView.getText().toString());
                        taskPerDayOfDaysLeft = (float) (Math.round(taskPerDayOfDaysLeft * 10)) / 10;
                        mTaskPerDayOfDaysLeftView.setText(String.valueOf(taskPerDayOfDaysLeft));
                    }
                } else {
                    mUnDoneView.setText("");
                    mTaskPerDayOfDaysLeftView.setText("");
                }
                /*String daysStr = mDaysView.getText().toString();
                if (!s.toString().isEmpty() && !daysStr.isEmpty()) {
                    int total = Integer.valueOf(s.toString());
                    int days = Integer.valueOf(daysStr);

                    float taskPerDay = (float) total / (float) days;
                    taskPerDay = (float) (Math.round(taskPerDay * 10)) / 10;
                    mTaskPerDayView.setText(String.valueOf(taskPerDay));
                }else{
                    mTaskPerDayView.setText("");
                }*/
            }
        });
    }

    private int calcDaysBetween(String startDate, String planEndDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            long ms = sdf.parse(planEndDate).getTime() - sdf.parse(startDate).getTime();
            int days = (int) (ms / (1000 * 3600 * 24));
            if (days >= 0) {
                return days + 1;
            } else {
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    private void bindPlusAndMinusButtons() {
        Button totalPlus = (Button) findViewById(R.id.total_plus);
        Button totalMinus = (Button) findViewById(R.id.total_minus);
        Button donePlus = (Button) findViewById(R.id.done_plus);
        Button doneMinus = (Button) findViewById(R.id.done_minus);

        totalPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totalStr = mTotalView.getText().toString();
                if (totalStr.isEmpty()) {
                    totalStr = "0";
                }
                int total = Integer.valueOf(totalStr);
                if (++total >= 99999999) {
                    total = 99999999;
                }
                mTotalView.setText(String.valueOf(total));
            }
        });

        totalMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totalStr = mTotalView.getText().toString();
                if (totalStr.isEmpty()) {
                    totalStr = "1";
                }
                int total = Integer.valueOf(totalStr);
                if (--total <= 1) {
                    total = 1;
                }
                mTotalView.setText(String.valueOf(total));
            }
        });


        donePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String doneStr = mDoneView.getText().toString();
                if (doneStr.isEmpty()) {
                    doneStr = "0";
                }

                int done = Integer.valueOf(doneStr);
                if (++done >= 99999999) {
                    done = 99999999;
                }
                mDoneView.setText(String.valueOf(done));
            }
        });

        doneMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String doneStr = mDoneView.getText().toString();
                if (doneStr.isEmpty()) {
                    doneStr = "1";
                }

                int done = Integer.valueOf(doneStr);
                if (--done <= 0) {
                    done = 0;
                }
                mDoneView.setText(String.valueOf(done));
            }
        });

//        totalMinus.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    for (; ; ) {
//                        int total = Integer.valueOf(mTotalView.getText().toString());
//                        mTotalView.setText(String.valueOf(--total));
//
//                        if (event.getAction() == MotionEvent.ACTION_UP) {
//                            break;
//                        }
//                        try {
//                            Thread.sleep(2000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                }
//                return false;
//
//            }
//        });
    }

    private void processDate(final TextView dateView) {
        int year;
        int month;
        int day;
        Calendar calendar = Calendar.getInstance();
        //TODO MM-dd-yyyy...
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if (!dateView.getText().toString().isEmpty()) {
            String dateStr = dateView.getText().toString();
            try {
                Date date = sdf.parse(dateStr);
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String monthStr = ++month < 10 ? "0" + String.valueOf(month) : String.valueOf(month);
                String dayStr = day < 10 ? "0" + String.valueOf(day) : String.valueOf(day);
                String date = year + "-" + monthStr + "-" + dayStr;
                dateView.setText(date);

                if (R.id.start_date == dateView.getId()) {
                    String end = mPlanEndDateView.getText().toString();
                    if (!end.isEmpty()) {
                        int days = calcDaysBetween(date, end);
                        mDaysView.setText(String.valueOf(days));
                        String totalStr = mTotalView.getText().toString();
                        if (!totalStr.isEmpty()) {
                            float taskPerDay = Float.valueOf(totalStr) / (float) days;
                            taskPerDay = (float) (Math.round(taskPerDay * 10)) / 10;
                            mTaskPerDayView.setText(String.valueOf(taskPerDay));
                        }
                    }
                } else if (R.id.plan_end_date == dateView.getId()) {
                    String start = mStartDateView.getText().toString();
                    if (!start.isEmpty()) {
                        int days = calcDaysBetween(start, date);
                        mDaysView.setText(String.valueOf(days));
                        String totalStr = mTotalView.getText().toString();
                        if (!totalStr.isEmpty()) {
                            float taskPerDay = Float.valueOf(totalStr) / (float) days;
                            taskPerDay = (float) (Math.round(taskPerDay * 10)) / 10;
                            mTaskPerDayView.setText(String.valueOf(taskPerDay));
                        }
                    }

                    String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    int daysLeft = calcDaysBetween(today, date);
                    mDaysLeftView.setText(String.valueOf(daysLeft));
                    if (!mUnDoneView.getText().toString().isEmpty()) {
                        float taskPerDayOfDaysLeft = Float.valueOf(mUnDoneView.getText().toString()) / (float) daysLeft;
                        taskPerDayOfDaysLeft = (float) (Math.round(taskPerDayOfDaysLeft * 10)) / 10;
                        mTaskPerDayOfDaysLeftView.setText(String.valueOf(taskPerDayOfDaysLeft));
                    }


                }
            }
        }, year, month, day);

        dateView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (null != imm) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }

                datePicker.show();
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app_menu, menu);
        MenuItem deleteGoal = menu.findItem(R.id.delete_goal);
        if (null == mGoal) {
            deleteGoal.setVisible(false);
        }
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
//            Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
//            return true;
//        }


        if (id == R.id.save_goal) {
            String GoalName = mGoalNameView.getText().toString();
            if (GoalName.isEmpty()) {
                String tip = this.getString(R.string.name_not_empty);
                Toast.makeText(AddActivity.this, tip, Toast.LENGTH_SHORT).show();
            } else {
                String totalStr = mTotalView.getText().toString();
                String doneStr = mDoneView.getText().toString();
                String startDate = mStartDateView.getText().toString();
                String planEndDate = mPlanEndDateView.getText().toString();
                String endDate = mEndDateView.getText().toString();
                String remark = mRemarkView.getText().toString();

                Integer total = "".equals(totalStr) ? 1 : Integer.parseInt(totalStr);
                Integer done = "".equals(doneStr) ? 0 : Integer.parseInt(doneStr);

                Goal goal1 = new Goal();
                goal1.setName(GoalName);
                goal1.setTotal(total);
                goal1.setDone(done);
                goal1.setStartDate(startDate);
                goal1.setPlanEndDate(planEndDate);
                goal1.setEndDate(endDate);
                goal1.setRemark(remark);

                if (null != mGoal) {
                    goal1.update(mGoal.getId());
                } else {
                    goal1.save();
                }
                MainActivity.actionStart(AddActivity.this);
            }
            return true;
        }

        if (id == R.id.delete_goal) {
            if (null != mGoal) {
                DataSupport.delete(Goal.class, mGoal.getId());
                MainActivity.actionStart(AddActivity.this);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
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
