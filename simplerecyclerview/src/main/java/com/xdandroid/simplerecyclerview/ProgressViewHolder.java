package com.xdandroid.simplerecyclerview;

import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

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
