package com.xdandroid.simplerecyclerview;

import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import com.xdandroid.materialprogressview.*;

public class MaterialProgressViewHolder extends RecyclerView.ViewHolder {

    protected MaterialProgressView progressBar;
    protected FrameLayout frameLayout;

    protected MaterialProgressViewHolder(View view) {
        super(view);
        frameLayout = (FrameLayout) view;
        progressBar = (MaterialProgressView) frameLayout.findViewById(android.R.id.secondaryProgress);
    }
}
