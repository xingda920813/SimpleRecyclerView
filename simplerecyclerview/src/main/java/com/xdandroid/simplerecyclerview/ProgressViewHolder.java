package com.xdandroid.simplerecyclerview;

import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

public class ProgressViewHolder extends RecyclerView.ViewHolder {

    protected ProgressBar progressBar;
    protected FrameLayout frameLayout;

    protected ProgressViewHolder(View view) {
        super(view);
        frameLayout = (FrameLayout) view;
        progressBar = (ProgressBar) frameLayout.findViewById(android.R.id.progress);
    }
}
