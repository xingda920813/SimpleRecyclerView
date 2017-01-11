package com.xdandroid.simplerecyclerview;

import android.annotation.*;
import android.support.v7.widget.*;
import android.view.*;

import java.util.*;

@SuppressLint("UseSparseArrays")
public abstract class GroupAdapter<Title, ChildItem> extends Adapter {

    protected List<Group<Title, ChildItem>> mGroupList;
    protected Map<Integer, Integer> mTitleOrderPositionMap = new HashMap<>();

    protected abstract RecyclerView.ViewHolder onTitleVHCreate(ViewGroup parent);
    protected abstract RecyclerView.ViewHolder onChildItemVHCreate(ViewGroup parent);

    /**
     * 详见下方参数说明.
     * @param holder TitleViewHolder.
     * @param adapterPos Title 在 Adapter 中的绝对位置 ( = holder.getAdapterPosition()).
     * @param title Title.
     * @param titleOrderInAllTitles 当前 Title 所在 Group 在 List[Group] 中的相对位置.
     */
    protected abstract void onTitleVHBind(RecyclerView.ViewHolder holder, int adapterPos, Title title, int titleOrderInAllTitles);

    /**
     * 详见下方参数说明.
     * @param holder ChildItemViewHolder.
     * @param adapterPos ChildItem 在 Adapter 中的绝对位置 ( = holder.getAdapterPosition()).
     * @param title Title.
     * @param titleOrderInAllTitles 当前 ChildItem 所在 Group 在 List[Group] 中的相对位置.
     * @param childItem ChildItem.
     * @param childOrderInCurrentGroup 当前 ChildItem 在 Group 中的相对位置.
     *                                 (第 1 个 ChildItem 的 childOrder 为 0, 即 childOrder 不包括 Title 占的位置)
     */
    protected abstract void onChildItemVHBind(RecyclerView.ViewHolder holder, int adapterPos, Title title, int titleOrderInAllTitles, ChildItem childItem, int childOrderInCurrentGroup);

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
    protected void onViewHolderBind(final RecyclerView.ViewHolder holder, int position, int viewType) {
        if (viewType == 32767) {
            TitleChildItemBean<Title, Void> titleWithOrder = getTitleWithOrder(position);
            onTitleVHBind(holder, position, titleWithOrder.title, titleWithOrder.titleOrder);
        } else if (viewType == 0) {
            TitleChildItemBean<Title, ChildItem> titleAndChildItem = getTitleAndChildItem(position);
            onChildItemVHBind(holder, position, titleAndChildItem.title, titleAndChildItem.titleOrder, titleAndChildItem.childItem, titleAndChildItem.childOrder);
        }
        if (mOnGroupItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition == RecyclerView.NO_POSITION) return;
                    mOnGroupItemClickListener.onItemClick(holder, holder.itemView, adapterPosition, getViewType(adapterPosition));
                }
            });
        }
        if (mOnGroupItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    return adapterPosition != RecyclerView.NO_POSITION && mOnGroupItemLongClickListener.onItemLongClick(holder, holder.itemView, adapterPosition, getViewType(adapterPosition));
                }
            });
        }
    }

    /**
     * 根据 Title 在 Adapter 中的绝对位置得到 Title 对象和当前 Title 所在 Group 在 List[Group] 中的相对位置.
     * @param positionInRV_viewType_title Title 在 Adapter 中的绝对位置.
     * @return TitleChildItemBean {Title title;  int titleOrder;}
     */
    public TitleChildItemBean<Title, Void> getTitleWithOrder(int positionInRV_viewType_title) {
        for (Map.Entry<Integer, Integer> entry : mTitleOrderPositionMap.entrySet())
            if (entry.getValue().equals(positionInRV_viewType_title))
                return new TitleChildItemBean<>(mGroupList.get(entry.getKey()).title, entry.getKey(), null, -1);
        return null;
    }

    /**
     * 根据 ChildItem 在 Adapter 中的绝对位置得到 Title 对象、当前 Title 所在 Group 在 List[Group] 中的相对位置、
     * ChildItem 对象和当前 ChildItem 在 Group 中的相对位置.
     * @param positionInRV_viewType_childItem ChildItem 在 Adapter 中的绝对位置.
     * @return TitleChildItemBean {Title title;  int titleOrder;  ChildItem childItem;  int childOrder;}
     */
    public TitleChildItemBean<Title, ChildItem> getTitleAndChildItem(int positionInRV_viewType_childItem) {
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
        return new TitleChildItemBean<>(title, titleOrder, childItem, childItemOrder);
    }

    protected int computeTotalCount() {
        //notifyDataSetChanged()会调到mChildHelper.getChildCount(), 进而调到computeTotalCount()
        //每次重新计算TitleOrder与Position的对应关系时先清空上一次用于储存该关系的Map
        mTitleOrderPositionMap.clear();
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

    protected OnGroupItemClickListener mOnGroupItemClickListener;

    public void setOnGroupItemClickListener(OnGroupItemClickListener onGroupItemClickListener) {
        mOnGroupItemClickListener = onGroupItemClickListener;
    }

    protected OnGroupItemLongClickListener mOnGroupItemLongClickListener;

    public void setOnGroupItemLongClickListener(OnGroupItemLongClickListener onGroupItemLongClickListener) {
        mOnGroupItemLongClickListener = onGroupItemLongClickListener;
    }

    public void setList(List<Group<Title, ChildItem>> groupList) {
        mGroupList = groupList;
        mTitleOrderPositionMap.clear();
        notifyDataSetChanged();
        setLoadingFalse();
    }

    public void add(Group<Title, ChildItem> group) {
        if (group == null) {
            setLoadingFalse();
            return;
        }
        mGroupList.add(group);
        mTitleOrderPositionMap.clear();
        notifyDataSetChanged();
        setLoadingFalse();
    }

    public void add(int position, Group<Title, ChildItem> group) {
        if (group == null) {
            setLoadingFalse();
            return;
        }
        mGroupList.add(position, group);
        mTitleOrderPositionMap.clear();
        notifyDataSetChanged();
        setLoadingFalse();
    }

    public void remove(int position) {
        mGroupList.remove(position);
        mTitleOrderPositionMap.clear();
        notifyDataSetChanged();
        setLoadingFalse();
    }

    public void remove() {
        removeLast();
    }

    public void removeLast() {
        int originalSize = mGroupList.size();
        mGroupList.remove(originalSize - 1);
        mTitleOrderPositionMap.clear();
        notifyDataSetChanged();
        setLoadingFalse();
    }

    public void removeAll(int positionStart, int itemCount) {
        for (int i = positionStart; i < positionStart + itemCount; i++) {
            mGroupList.remove(positionStart);
        }
        mTitleOrderPositionMap.clear();
        notifyDataSetChanged();
        setLoadingFalse();
    }

    public void set(int position, Group<Title, ChildItem> group) {
        mGroupList.set(position, group);
        mTitleOrderPositionMap.clear();
        notifyDataSetChanged();
        setLoadingFalse();
    }

    public void setAll(int positionStart, int itemCount, Group<Title, ChildItem> group) {
        for (int i = positionStart; i < positionStart + itemCount; i++) {
            mGroupList.set(i, group);
        }
        mTitleOrderPositionMap.clear();
        notifyDataSetChanged();
        setLoadingFalse();
    }

    public void addAll(int position, List<Group<Title, ChildItem>> newGroupList) {
        if (newGroupList == null || newGroupList.size() <= 0) {
            setLoadingFalse();
            return;
        }
        mGroupList.addAll(position, newGroupList);
        mTitleOrderPositionMap.clear();
        notifyDataSetChanged();
        setLoadingFalse();
    }

    public void addAll(List<Group<Title, ChildItem>> newGroupList) {
        if (newGroupList == null || newGroupList.size() <= 0) {
            setLoadingFalse();
            return;
        }
        mGroupList.addAll(newGroupList);
        mTitleOrderPositionMap.clear();
        notifyDataSetChanged();
        setLoadingFalse();
    }

    public List<Group<Title, ChildItem>> getGroupList() {
        return mGroupList;
    }
}
