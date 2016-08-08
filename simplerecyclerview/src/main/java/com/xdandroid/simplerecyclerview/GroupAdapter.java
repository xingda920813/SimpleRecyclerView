package com.xdandroid.simplerecyclerview;

import java.util.*;

/**
 * Created by xingda on 16-8-8.
 */

public abstract class GroupAdapter extends Adapter {

    private boolean mHasComputedTotalCounts = false;
    private List<Integer> mTitleOrderPositionList = new ArrayList<>();
    private int mTitlePlusChildItemCounts = 0;

    protected abstract int getTitleCount(Void viewType_title_32767_childItem_0, Void call_getTitleOrder_and_getTitleOrderAndChildItemOrder);
    protected abstract int getChildItemCount(int titleOrder);

    public int getTitleOrder(int positionInRV_viewType_title) {
        return mTitleOrderPositionList.indexOf(positionInRV_viewType_title);
    }

    public int[] getTitleOrderAndChildItemOrder(int positionInRV_viewType_childItem) {
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
        mHasComputedTotalCounts = true;
        return totalCounts;
    }

    @Override
    protected int getViewType(int positionInRV) {
        return mTitleOrderPositionList.contains(positionInRV) ? 32767 : 0;
    }

    @Override
    protected int getCount() {
        if (!mHasComputedTotalCounts) {
            mTitlePlusChildItemCounts = computeTotalCount();
        }
        return getTitleCount(null, null) + mTitlePlusChildItemCounts;
    }

    @Override
    protected int getItemSpanSizeForGrid(int positionInRV, int viewType, int spanCount) {
        switch (viewType) {
            case 32767:
                //每一组的标题
                return spanCount;
            default:
                //一组内的项目
                return 1;
        }
    }

    @Deprecated protected final void onLoadMore(Void please_make_your_adapter_class_as_abstract_class) {}
    @Deprecated protected final boolean hasMoreElements(Void let_activity_or_fragment_implement_these_methods) {return false;}
    @Deprecated public final void onAdded() {}
    @Deprecated public final void onRemoved() {}
    @Deprecated public final void onRemovedLast() {}
    @Deprecated public final void onAddedAll(int newDataSize) {}
}
