package com.xdandroid.simplerecyclerview;

import android.support.v7.widget.*;

/**
 * Created by xingda on 16-8-12.
 */

public abstract class SimpleOnScrollListener extends RecyclerView.OnScrollListener {

    protected abstract void onScrollStateChanged(int newState);
    /**
     * scrolledDistance 已滑动的距离(px); distanceToEnd 还能向下/向右滑动多少(px); velocity 当前滑动速度.
     * @param scrolledDistance 已滑动的距离(px)
     * @param distanceToEnd 还能向下/向右滑动多少(px)
     * @param velocity 当前滑动速度
     */
    protected abstract void onScrolled(int scrolledDistance, int distanceToEnd, int velocity);

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        onScrollStateChanged(newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (!(recyclerView instanceof SimpleRecyclerView)) return;
        SimpleRecyclerView simpleRecyclerView = (SimpleRecyclerView) recyclerView;
        RecyclerView.LayoutManager manager = simpleRecyclerView.getLayoutManager();
        if (!(manager instanceof LinearLayoutManager)) return;
        LinearLayoutManager linearManager = (LinearLayoutManager) manager;
        if (linearManager.getOrientation() == LinearLayoutManager.VERTICAL) {
            onScrolled(simpleRecyclerView.getScrolledDistance(), simpleRecyclerView.getDistanceToEnd(), dy);
        } else {
            onScrolled(simpleRecyclerView.getScrolledDistance(), simpleRecyclerView.getDistanceToEnd(), dx);
        }
    }
}
