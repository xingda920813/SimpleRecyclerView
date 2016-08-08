package com.xdandroid.simplerecyclerview;

import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import com.xdandroid.materialprogressview.*;

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
