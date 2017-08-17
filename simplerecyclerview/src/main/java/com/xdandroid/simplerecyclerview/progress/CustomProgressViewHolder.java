package com.xdandroid.simplerecyclerview.progress;

import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

public class CustomProgressViewHolder extends RecyclerView.ViewHolder {

    public View customProgress;
    public FrameLayout frameLayout;

    public CustomProgressViewHolder(View fl, View provided) {
        super(fl);
        frameLayout = (FrameLayout) fl;
        customProgress = provided;
    }
}
