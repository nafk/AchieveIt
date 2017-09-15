package com.achieveit.android;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by YT on 17/8/10/010.
 */

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.ViewHolder> implements DragItemTouchHelperCallback.ItemMoveListener {

    protected List<Goal> mGoalList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView goalName;
        TextView done;
        TextView total;
        TextView startDate;
        TextView startDateText;
        TextView progress;
        ProgressBar progressBar;

        View goalView;

        public ViewHolder(View view) {
            super(view);
            goalView = view;
            goalName = (TextView) view.findViewById(R.id.goal_name);
            done = (TextView) view.findViewById(R.id.done);
            total = (TextView) view.findViewById(R.id.total);
            startDate = (TextView) view.findViewById(R.id.start_date);
            startDateText = (TextView) view.findViewById(R.id.start_date_text);
            progress = (TextView) view.findViewById(R.id.progress);
            progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);


        }
    }

    public GoalAdapter(List<Goal> goalList, Context context) {
        mGoalList = goalList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_list_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.goalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Goal goal = mGoalList.get(position);
                AddActivity.actionStart(v.getContext(), goal);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Goal goal = mGoalList.get(position);
        holder.goalName.setText(goal.getName());
        holder.done.setText(String.valueOf(goal.getDone()));
        holder.total.setText(String.valueOf(goal.getTotal()));
        if (TextUtils.isEmpty(goal.getStartDate())) {
            holder.startDateText.setText("");
            holder.startDate.setText("");
        } else {
            String s = mContext.getString(R.string.start_date);
            holder.startDateText.setText(s);
            holder.startDate.setText(goal.getStartDate());
        }


        float result = (float) goal.getDone() / (float) goal.getTotal() * 100f;

        BigDecimal bd = new BigDecimal(result);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        String percentStr = String.valueOf(bd) + "%";
        holder.progress.setText(percentStr);
        holder.progressBar.setProgress((int) result);
    }

    @Override
    public int getItemCount() {
        return mGoalList.size();
    }

    @Override
    public void onMove(int src, int des) {
        Goal g = mGoalList.get(src);
        mGoalList.remove(src);
        mGoalList.add(des, g);
    }


}
