package com.xdandroid.simplerecyclerview.progress;

import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

public class ProgressViewHolder extends RecyclerView.ViewHolder {

    public ProgressView progressBar;
    public FrameLayout frameLayout;

    public ProgressViewHolder(View fl) {
        super(fl);
        frameLayout = (FrameLayout) fl;
        progressBar = (ProgressView) frameLayout.findViewById(android.R.id.progress);
    }
}
