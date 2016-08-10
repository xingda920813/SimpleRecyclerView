package com.xdandroid.simplerecyclerview;

import android.content.*;
import android.support.v7.widget.*;

import java.io.*;

/**
 * Created by XingDa on 2016/05/27.
 */

public class UIUtils {

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    static void registerObserver(SimpleRecyclerView recyclerView, RecyclerView.AdapterDataObserver observer) {
        try {
            recyclerView.getAdapter().unregisterAdapterDataObserver(observer);
        } catch (Exception ignored) {}
        try {
            recyclerView.getAdapter().registerAdapterDataObserver(observer);
        } catch (Exception ignored) {}
    }

    public static class TitleChildItemBean<Title, ChildItem> implements Serializable {
        public TitleChildItemBean(Title title, ChildItem childItem) {
            this.title = title;
            this.childItem = childItem;
        }
        public Title title;
        public ChildItem childItem;
    }
}
