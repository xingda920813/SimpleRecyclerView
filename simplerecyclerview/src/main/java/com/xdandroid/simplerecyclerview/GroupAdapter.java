package com.xdandroid.simplerecyclerview;

import android.support.v7.widget.*;
import android.view.*;

import java.util.*;

public abstract class GroupAdapter<Title, ChildItem> extends Adapter {

    protected List<Group<Title, ChildItem>> mGroupList;
    protected List<Integer> mTitlePositionList = new ArrayList<>();

    public GroupAdapter(List<Group<Title, ChildItem>> groupList) {
        mGroupList = groupList;
    }

    protected abstract RecyclerView.ViewHolder onTitleVHCreate(ViewGroup parent);
    protected abstract RecyclerView.ViewHolder onChildItemVHCreate(ViewGroup parent);
    protected abstract void onTitleVHBind(RecyclerView.ViewHolder holder, Title title);
    protected abstract void onChildItemVHBind(RecyclerView.ViewHolder holder, Title title, ChildItem childItem);

    @Override
    protected RecyclerView.ViewHolder onViewHolderCreate(ViewGroup parent, int viewType) {
        if (viewType == 32767) {
            return onTitleVHCreate(parent);
        } else if (viewType == 0) {
            return onChildItemVHCreate(parent);
        }
        return null;
    }

    @Override
    protected void onViewHolderBind(RecyclerView.ViewHolder holder, int position, int viewType) {
        if (viewType == 32767) {
            onTitleVHBind(holder, getTitle(position));
        } else if (viewType == 0) {
            UIUtils.TitleChildItemBean<Title, ChildItem> titleAndChildItem = getTitleAndChildItem(position);
            onChildItemVHBind(holder, titleAndChildItem.title, titleAndChildItem.childItem);
        }
    }

    protected Title getTitle(int positionInRV_viewType_title) {
        int titleOrder = mTitlePositionList.indexOf(positionInRV_viewType_title);
        return mGroupList.get(titleOrder).title;
    }

    protected UIUtils.TitleChildItemBean<Title, ChildItem> getTitleAndChildItem(int positionInRV_viewType_childItem) {
        int titleOrder = -1;
        int childItemOrder = 0;
        for (Integer titlePos : mTitlePositionList) {
            if (titleOrder >= mGroupList.size() - 1 || positionInRV_viewType_childItem < titlePos) break;
            childItemOrder = positionInRV_viewType_childItem - titlePos - 1;
            titleOrder = titleOrder + 1;
        }
        Title title = mGroupList.get(titleOrder).title;
        ChildItem childItem = mGroupList.get(titleOrder).childItemList.get(childItemOrder);
        return new UIUtils.TitleChildItemBean<>(title, childItem);
    }

    protected int computeTotalCount() {
        int totalCounts = 0;
        int positionInRV = 0;
        for (Group<Title, ChildItem> group : mGroupList) {
            mTitlePositionList.add(positionInRV);
            int childItemCounts = group.childItemList != null ? group.childItemList.size() : 0;
            positionInRV = positionInRV + childItemCounts + 1;
            totalCounts = totalCounts + childItemCounts;
        }
        return totalCounts;
    }

    @Override
    protected int getViewType(int positionInRV) {
        return mTitlePositionList.contains(positionInRV) ? 32767 : 0;
    }

    @Override
    protected int getCount() {
        if (mGroupList == null || mGroupList.size() <= 0) return 0;
        return mGroupList.size() + computeTotalCount();
    }

    @Override
    protected int getItemSpanSizeForGrid(int positionInRV, int viewType, int spanCount) {
        switch (viewType) {
            case 32767:
                return spanCount;
            default:
                return 1;
        }
    }
}
