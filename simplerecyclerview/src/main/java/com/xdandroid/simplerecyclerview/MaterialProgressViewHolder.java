package com.xdandroid.simplerecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.xdandroid.materialprogressview.MaterialProgressView;

/**
 * Created by XingDa on 2016/05/29.
 */

public class MaterialProgressViewHolder extends RecyclerView.ViewHolder {

    protected MaterialProgressView mProgressBar;
    protected FrameLayout mFrameLayout;

    protected MaterialProgressViewHolder(View view) {
        super(view);
        mFrameLayout = (FrameLayout) view;
        mProgressBar = (MaterialProgressView) mFrameLayout.findViewById(android.R.id.secondaryProgress);
    }
}
