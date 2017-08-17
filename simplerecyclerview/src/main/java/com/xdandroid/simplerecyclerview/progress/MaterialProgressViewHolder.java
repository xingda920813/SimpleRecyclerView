package com.xdandroid.simplerecyclerview.progress;

import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

public class MaterialProgressViewHolder extends RecyclerView.ViewHolder {

    public MaterialProgressView materialProgress;
    public FrameLayout frameLayout;

    public MaterialProgressViewHolder(View fl) {
        super(fl);
        frameLayout = (FrameLayout) fl;
        materialProgress = (MaterialProgressView) frameLayout.findViewById(android.R.id.secondaryProgress);
    }
}
