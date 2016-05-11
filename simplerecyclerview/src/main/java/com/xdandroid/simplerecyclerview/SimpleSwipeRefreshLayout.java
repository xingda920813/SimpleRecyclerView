package com.xdandroid.simplerecyclerview;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Created by XingDa on 2016/5/9.
 */
public class SimpleSwipeRefreshLayout extends SwipeRefreshLayout implements AppBarLayout.OnOffsetChangedListener {

    public SimpleSwipeRefreshLayout(Context context) {
        super(context);
    }

    public SimpleSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private AppBarLayout appBarLayout;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getContext() instanceof Activity) {
            appBarLayout = (AppBarLayout) ((Activity) getContext()).findViewById(getContext().getResources().getIdentifier("appbar", "id", getContext().getPackageName()));
            if (appBarLayout == null) {
                return;
            }
            appBarLayout.addOnOffsetChangedListener(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (appBarLayout == null) {
            return;
        }
        appBarLayout.removeOnOffsetChangedListener(this);
        appBarLayout = null;
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
