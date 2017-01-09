package com.xdandroid.simplerecyclerview;

import android.support.v7.widget.*;
import android.view.*;

public interface OnItemLongClickListener {
    boolean onItemLongClick(RecyclerView.ViewHolder holder, View v, int position, int viewType);
}
