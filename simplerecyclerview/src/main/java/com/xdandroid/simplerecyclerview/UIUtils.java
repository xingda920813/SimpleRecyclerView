package com.xdandroid.simplerecyclerview;

import android.content.*;
import android.support.v7.widget.*;

public class UIUtils {

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    protected static void registerObserver(SimpleRecyclerView recyclerView, RecyclerView.AdapterDataObserver observer) {
        try {recyclerView.getAdapter().unregisterAdapterDataObserver(observer);} catch (Exception ignored) {}
        try {recyclerView.getAdapter().registerAdapterDataObserver(observer);} catch (Exception ignored) {}
    }

    /**
     * 判断 RecyclerView 当前是否可滑动.
     */
    public static boolean isScrollable(RecyclerView rv) {
        try {
            return rv.getChildAt(rv.getChildCount() - 1).getBottom() > rv.getHeight() ||
                    rv.getChildAt(0).getTop() < 0 ||
                    rv.getChildCount() != rv.getAdapter().getItemCount();
        } catch (Exception e) {
            return true;
        }
    }
}
