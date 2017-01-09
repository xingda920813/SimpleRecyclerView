package com.xdandroid.simplerecyclerview;

import android.support.v7.widget.*;
import android.view.*;

public abstract class OnGroupItemLongClickListener<Title, ChildItem> implements OnItemLongClickListener {

    protected GroupAdapter<Title, ChildItem> mGroupAdapter;

    public OnGroupItemLongClickListener(GroupAdapter<Title, ChildItem> groupAdapter) {
        mGroupAdapter = groupAdapter;
    }

    @Override
    public boolean onItemLongClick(RecyclerView.ViewHolder holder, View v, int position, int viewType) {
        if (viewType == 32767) {
            TitleChildItemBean<Title, Void> titleWithOrder = mGroupAdapter.getTitleWithOrder(position);
            return onGroupItemLongClick(holder, v, position, viewType, titleWithOrder.title, titleWithOrder.titleOrder, null, -1);
        } else {
            TitleChildItemBean<Title, ChildItem> titleAndChildItem = mGroupAdapter.getTitleAndChildItem(position);
            return onGroupItemLongClick(holder, v, position, viewType, titleAndChildItem.title, titleAndChildItem.titleOrder, titleAndChildItem.childItem, titleAndChildItem.childOrder);
        }
    }

    public abstract boolean onGroupItemLongClick(RecyclerView.ViewHolder holder, View v, int adapterPos, int viewType, Title title, int titleOrder, ChildItem childItem, int childOrder);
}
