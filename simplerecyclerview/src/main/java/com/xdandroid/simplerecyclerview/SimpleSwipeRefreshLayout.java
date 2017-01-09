package com.xdandroid.simplerecyclerview;

import android.app.*;
import android.content.*;
import android.support.design.widget.*;
import android.support.v4.widget.*;
import android.util.*;

public class SimpleSwipeRefreshLayout extends SwipeRefreshLayout implements AppBarLayout.OnOffsetChangedListener {

    public SimpleSwipeRefreshLayout(Context context) {
        super(context);
    }

    public SimpleSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected AppBarLayout mAppBarLayout;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!(getContext() instanceof Activity)) return;
        mAppBarLayout = (AppBarLayout) ((Activity) getContext()).findViewById(getContext().getResources().getIdentifier("appbar", "id", getContext().getPackageName()));
        if (mAppBarLayout == null) return;
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mAppBarLayout == null) return;
        mAppBarLayout.removeOnOffsetChangedListener(this);
        mAppBarLayout = null;
        super.onDetachedFromWindow();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        this.setEnabled(verticalOffset == 0);
    }

    @Override
    public void setRefreshing(final boolean refreshing) {
        post(new Runnable() {
            @Override
            public void run() {
                SimpleSwipeRefreshLayout.super.setRefreshing(refreshing);
            }
        });
    }
}
