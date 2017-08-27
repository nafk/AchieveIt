package com.achieveit.android;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    private Goal mGoal = null;
    private EditText mGoalNameView = null;
    private EditText mTotalView = null;
    private EditText mDoneView = null;
    private EditText mStartDateView = null;
    private EditText mRemarkView = null;

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
        mRemarkView = (EditText) findViewById(R.id.remark);

        Intent intent = getIntent();
        mGoal = (Goal) intent.getSerializableExtra("goal");

        if (null != mGoal) {
            toolbar.setTitle(this.getString(R.string.edit_goal));
            mGoalNameView.setText(mGoal.getName());
            mTotalView.setText(String.valueOf(mGoal.getTotal()));
            mDoneView.setText(String.valueOf(mGoal.getDone()));
            mStartDateView.setText(mGoal.getStartDate());
            mRemarkView.setText(mGoal.getRemark());
        }

        processDate();
        bindPlusAndMinusButtons();
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

    private void processDate() {
        int year;
        int month;
        int day;
        Calendar calendar = Calendar.getInstance();
        if (!mStartDateView.getText().toString().isEmpty()) {
            String startDate = mStartDateView.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
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
                String monthStr = ++month < 10 ? "0" + String.valueOf(month) : String.valueOf(month);
                String dayStr = day < 10 ? "0" + String.valueOf(day) : String.valueOf(day);
                mStartDateView.setText(year + "-" + monthStr + "-" + dayStr);
            }
        }, year, month, day);

        mStartDateView.setOnTouchListener(new View.OnTouchListener() {
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
                String remark = mRemarkView.getText().toString();

                Integer total = "".equals(totalStr) ? 1 : Integer.parseInt(totalStr);
                Integer done = "".equals(doneStr) ? 0 : Integer.parseInt(doneStr);

                Goal goal1 = new Goal();
                goal1.setName(GoalName);
                goal1.setTotal(total);
                goal1.setDone(done);
                goal1.setStartDate(startDate);
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
