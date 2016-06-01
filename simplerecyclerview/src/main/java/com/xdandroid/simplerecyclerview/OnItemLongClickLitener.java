package com.xdandroid.simplerecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by XingDa on 2016/05/29.
 */

public interface OnItemLongClickLitener {
    public boolean onItemLongClick(RecyclerView.ViewHolder holder, View v, int position, int viewType);
}
