package com.xdandroid.simplerecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

/**
 * Created by XingDa on 2016/05/29.
 */

public class ProgressViewHolder extends RecyclerView.ViewHolder {
    protected ProgressBar mProgressBar;
    protected FrameLayout mFrameLayout;
    protected ProgressViewHolder(View view) {
        super(view);
        mFrameLayout = (FrameLayout) view;
        mProgressBar = (ProgressBar) mFrameLayout.findViewById(android.R.id.progress);
    }
}
