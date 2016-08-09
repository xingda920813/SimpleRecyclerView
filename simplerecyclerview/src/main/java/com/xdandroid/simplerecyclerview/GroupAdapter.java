package com.xdandroid.simplerecyclerview;

import java.util.*;

/**
 * Created by xingda on 16-8-8.
 */

public abstract class GroupAdapter extends Adapter {

    private List<Integer> mTitleOrderPositionList = new ArrayList<>();

    protected abstract int getTitleCount(Void viewType_title_32767_childItem_0, Void call_getTitleOrder_and_getTitleAndChildItemOrder);
    protected abstract int getChildItemCount(int titleOrder);

    public int getTitleOrder(int positionInRV_viewType_title) {
        return mTitleOrderPositionList.indexOf(positionInRV_viewType_title);
    }

    public int[] getTitleAndChildItemOrder(int positionInRV_viewType_childItem) {
        int titleOrder = -1;
        int childItemOrder = 0;
        for (Integer titlePos : mTitleOrderPositionList) {
            if (positionInRV_viewType_childItem < titlePos) {
                break;
            }
            childItemOrder = positionInRV_viewType_childItem - titlePos - 1;
            titleOrder = titleOrder + 1;
        }
        return new int[]{titleOrder, childItemOrder};
    }

    private int computeTotalCount() {
        int totalCounts = 0;
        int positionInRV = 0;
        for (int titleOrder = 0; titleOrder < getTitleCount(null, null); titleOrder++) {
            mTitleOrderPositionList.add(positionInRV);
            int childItemCounts = getChildItemCount(titleOrder);
            positionInRV = positionInRV + childItemCounts + 1;
            totalCounts = totalCounts + childItemCounts;
        }
        return totalCounts;
    }

    @Override
    protected int getViewType(int positionInRV) {
        return mTitleOrderPositionList.contains(positionInRV) ? 32767 : 0;
    }

    @Override
    protected int getCount() {
        return getTitleCount(null, null) + computeTotalCount();
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
