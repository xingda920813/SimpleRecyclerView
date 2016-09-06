package com.xdandroid.simplerecyclerview;

import android.support.v7.widget.*;
import android.view.*;

/**
 * Created by XingDa on 2016/05/29.
 */

/**
 * @deprecated 使用 OnItemClickListener 代替.
 */
@Deprecated
public interface OnItemClickLitener {
    /**
     * @deprecated
     */
    @Deprecated
    public void onItemClick(RecyclerView.ViewHolder holder, View v, int position, int viewType);
}
