package com.xdandroid.simplerecyclerview;

import android.support.v7.widget.*;
import android.view.*;

/**
 * Created by XingDa on 2016/05/29.
 */

public interface OnItemClickListener {
    public void onItemClick(RecyclerView.ViewHolder holder, View v, int position, int viewType);
}
