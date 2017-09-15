package com.achieveit.android;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by YT on 17/9/15/015.
 */

public class DragItemTouchHelperCallback extends ItemTouchHelper.Callback {

    public ItemMoveListener itemMoveListener;

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END;
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int src = viewHolder.getAdapterPosition();
        int des = target.getAdapterPosition();
        if (null != itemMoveListener) {
            itemMoveListener.onMove(src, des);
        }
        recyclerView.getAdapter().notifyItemMoved(src, des);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    public interface ItemMoveListener {
        void onMove(int src, int des);
    }
}
