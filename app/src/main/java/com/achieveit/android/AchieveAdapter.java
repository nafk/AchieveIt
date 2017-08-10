package com.achieveit.android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by YT on 17/8/10/010.
 */

public class AchieveAdapter extends RecyclerView.Adapter<AchieveAdapter.ViewHolder> {

    private List<Achieve> mAchieveList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView achieveName;
        TextView done;
        TextView total;
        TextView startDate;
        TextView progress;
        ProgressBar progressBar;

        public ViewHolder(View view) {
            super(view);
            achieveName = (TextView) view.findViewById(R.id.achieve_name);
            done = (TextView) view.findViewById(R.id.done);
            total = (TextView) view.findViewById(R.id.total);
            startDate = (TextView) view.findViewById(R.id.start_date);
            progress = (TextView) view.findViewById(R.id.progress);
            progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        }
    }

    public AchieveAdapter(List<Achieve> achieveList) {
        mAchieveList = achieveList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.achieve_list_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Achieve achieve = mAchieveList.get(position);
        holder.achieveName.setText(achieve.getName());
        holder.done.setText(String.valueOf(achieve.getDone()));
        holder.total.setText(String.valueOf(achieve.getTotal()));
        holder.startDate.setText(achieve.getStartDate());

        float result = (float) achieve.getDone() / (float) achieve.getTotal() * 100;
        //todo 一位小数
        String percentStr = String.valueOf(result) + "%";
        holder.progress.setText(percentStr);
        holder.progressBar.setProgress((int) result);
        //test commit
    }

    @Override
    public int getItemCount() {
        return mAchieveList.size();
    }

}
