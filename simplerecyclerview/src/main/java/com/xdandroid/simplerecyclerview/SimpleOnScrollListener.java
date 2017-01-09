package com.xdandroid.simplerecyclerview;

import android.support.v7.widget.*;

/**
 * 必须配合LinearLayoutManager使用
 * Created by xingda on 16-8-12.
 */
public abstract class SimpleOnScrollListener extends RecyclerView.OnScrollListener {

    /**
     * scrolledDistance 已滑动的距离(px); distanceToEnd 还能向下/向右滑动多少(px); newState 新的滑动状态.
     * @param scrolledDistance 已滑动的距离(px)
     * @param distanceToEnd 还能向下/向右滑动多少(px)
     * @param newState 新的滑动状态
     */
    protected abstract void onScrollStateChanged(int scrolledDistance, int distanceToEnd, int newState);
    /**
     * scrolledDistance 已滑动的距离(px); distanceToEnd 还能向下/向右滑动多少(px); velocity 当前滑动速度(正负表示方向).
     * @param scrolledDistance 已滑动的距离(px)
     * @param distanceToEnd 还能向下/向右滑动多少(px)
     * @param velocity 当前滑动速度(正负表示方向)
     */
    protected abstract void onScrolled(int scrolledDistance, int distanceToEnd, int velocity);

    @Override
    public void onScrollStateChanged(RecyclerView v, int newState) {
        if (!(v instanceof SimpleRecyclerView)) return;
        SimpleRecyclerView rv = (SimpleRecyclerView) v;
        onScrollStateChanged(rv.getScrolledDistance(), rv.getDistanceToEnd(), newState);
    }

    @Override
    public void onScrolled(RecyclerView v, int velocityX, int velocityY) {
        if (!(v instanceof SimpleRecyclerView)) return;
        SimpleRecyclerView rv = (SimpleRecyclerView) v;
        RecyclerView.LayoutManager manager = rv.getLayoutManager();
        if (!(manager instanceof LinearLayoutManager)) return;
        LinearLayoutManager linearManager = (LinearLayoutManager) manager;
        if (linearManager.getOrientation() == LinearLayoutManager.VERTICAL) {
            onScrolled(rv.getScrolledDistance(), rv.getDistanceToEnd(), velocityY);
        } else {
            onScrolled(rv.getScrolledDistance(), rv.getDistanceToEnd(), velocityX);
        }
    }
}
