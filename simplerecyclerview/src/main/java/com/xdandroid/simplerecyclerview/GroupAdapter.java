package com.xdandroid.simplerecyclerview;

import android.annotation.*;
import android.support.v7.widget.*;
import android.view.*;

import java.util.*;

@SuppressLint("UseSparseArrays")
public abstract class GroupAdapter<Title, ChildItem> extends Adapter {

    protected List<Group<Title, ChildItem>> mGroupList;
    protected Map<Integer, Integer> mTitleOrderPositionMap = new HashMap<>();

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
            TitleChildItemBean<Title, ChildItem> titleAndChildItem = getTitleAndChildItem(position);
            onChildItemVHBind(holder, titleAndChildItem.title, titleAndChildItem.childItem);
        }
    }

    protected Title getTitle(int positionInRV_viewType_title) {
        for (Map.Entry<Integer, Integer> entry : mTitleOrderPositionMap.entrySet()) {
            if (entry.getValue().equals(positionInRV_viewType_title)) {
                return mGroupList.get(entry.getKey()).title;
            }
        }
        return null;
    }

    protected TitleChildItemBean<Title, ChildItem> getTitleAndChildItem(int positionInRV_viewType_childItem) {
        int titleOrder = -1;
        int childItemOrder = 0;
        for (int i = 0; i < mGroupList.size(); i++) {
            int titlePos = mTitleOrderPositionMap.get(i);
            if (titleOrder >= mGroupList.size() - 1 || positionInRV_viewType_childItem < titlePos) break;
            childItemOrder = positionInRV_viewType_childItem - titlePos - 1;
            titleOrder = titleOrder + 1;
        }
        Title title = mGroupList.get(titleOrder).title;
        ChildItem childItem = mGroupList.get(titleOrder).childItemList.get(childItemOrder);
        return new TitleChildItemBean<>(title, childItem);
    }

    protected int computeTotalCount() {
        int totalCounts = 0;
        int positionInRV = 0;
        for (int i = 0; i < mGroupList.size(); i++) {
            mTitleOrderPositionMap.put(i, positionInRV);
            List<ChildItem> childItemList = mGroupList.get(i).childItemList;
            int childItemCounts = childItemList != null ? childItemList.size() : 0;
            positionInRV = positionInRV + childItemCounts + 1;
            totalCounts = totalCounts + childItemCounts;
        }
        return totalCounts;
    }

    @Override
    protected int getViewType(int positionInRV) {
        return mTitleOrderPositionMap.containsValue(positionInRV) ? 32767 : 0;
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
