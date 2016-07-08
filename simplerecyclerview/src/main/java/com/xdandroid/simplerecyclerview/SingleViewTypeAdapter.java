package com.xdandroid.simplerecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XingDa on 2016/05/29.
 */

public abstract class SingleViewTypeAdapter<T> extends Adapter {

    protected List<T> list;

    protected abstract RecyclerView.ViewHolder onViewHolderCreate(List<T> list, ViewGroup parent);

    protected abstract void onViewHolderBind(List<T> list, RecyclerView.ViewHolder holder, int position);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType != 65535) {
            return onViewHolderCreate(list, parent);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == list.size() ? 65535 : 0;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (!mIsLoading && list.size() > 0 && position >= list.size() - mThreshold && hasMoreElements(null)) {
            mIsLoading = true;
            onLoadMore(null);
        }
        if (position == list.size()) {
            if (!mUseMaterialProgress && holder instanceof ProgressViewHolder) {
                ((ProgressViewHolder) holder).mProgressBar.setVisibility(mIsLoading ? View.VISIBLE : View.GONE);
            } else if (mUseMaterialProgress && holder instanceof MaterialProgressViewHolder) {
                ((MaterialProgressViewHolder) holder).mProgressBar.setVisibility(mIsLoading ? View.VISIBLE : View.GONE);
            }
        } else {
            onViewHolderBind(list, holder, holder.getAdapterPosition());
            if (mOnItemClickLitener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(holder, holder.itemView, holder.getAdapterPosition(), 0);
                    }
                });
            }
            if (mOnItemLongClickLitener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return mOnItemLongClickLitener.onItemLongClick(holder, holder.itemView, holder.getAdapterPosition(), 0);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return (list == null) ? 0 : (list.size() + 1);
    }

    protected void changeList(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
        setLoadingFalse();
    }

    public void setList(List<T> list) {
        if (list == null || list.size() <= 0) {
            if (this.list != null && this.list.size() > 0) {
                int originalSize = this.list.size();
                this.list.clear();
                notifyItemRangeRemoved(0, originalSize);
            }
            setLoadingFalse();
            return;
        }
        if (this.list == null || this.list.size() <= 0) {
            this.list = list;
            notifyItemRangeInserted(0, list.size());
            setLoadingFalse();
        } else {
            changeList(list);
        }
    }

    public void add(T t) {
        if (list == null) list = new ArrayList<>();
        if (t != null) {
            int originalSize = list.size();
            list.add(t);
            notifyItemInserted(originalSize);
        }
        setLoadingFalse();
    }

    public void add(int position, T t) {
        if (list == null) list = new ArrayList<>();
        if (t != null) {
            list.add(position, t);
            notifyItemInserted(position);
        }
        setLoadingFalse();
    }

    public void remove(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        setLoadingFalse();
    }

    public void remove() {
        removeLast();
    }

    public void removeLast() {
        int originalSize = list.size();
        list.remove(originalSize - 1);
        notifyItemRemoved(originalSize - 1);
        setLoadingFalse();
    }

    public void removeAll(int positionStart, int itemCount) {
        for (int i = positionStart; i < positionStart + itemCount; i++) {
            list.remove(positionStart);
        }
        notifyItemRangeRemoved(positionStart, itemCount);
        setLoadingFalse();
    }

    public void set(int position, T t) {
        list.set(position, t);
        notifyItemChanged(position);
        setLoadingFalse();
    }

    public void setAll(int positionStart, int itemCount, T t) {
        for (int i = positionStart; i < positionStart + itemCount; i++) {
            list.set(i, t);
        }
        notifyItemRangeChanged(positionStart, itemCount);
        setLoadingFalse();
    }

    public void addAll(int position, List<T> newList) {
        if (list == null) list = new ArrayList<>();
        if (newList != null && newList.size() > 0) {
            list.addAll(newList);
            notifyItemRangeInserted(position, newList.size());
        }
        setLoadingFalse();
    }

    public void addAll(List<T> newList) {
        if (list == null) list = new ArrayList<>();
        if (newList != null && newList.size() > 0) {
            int originalSize = list.size();
            list.addAll(newList);
            notifyItemRangeInserted(originalSize, newList.size());
        }
        setLoadingFalse();
    }

    @Deprecated protected final RecyclerView.ViewHolder onViewHolderCreate(ViewGroup parent, int viewType) {throw new UnsupportedOperationException();}
    @Deprecated protected final void onViewHolderBind(RecyclerView.ViewHolder holder, int position, int viewType) {throw new UnsupportedOperationException();}
    @Deprecated protected final int getViewType(int position) {throw new UnsupportedOperationException();}
    @Deprecated protected final int getCount() {throw new UnsupportedOperationException();}
    @Deprecated public final void onAdded() {super.onAdded();}
    @Deprecated public final void onAddedAll(int newDataSize) {super.onAddedAll(newDataSize);}
    @Deprecated public final void onRemoved() {super.onRemoved();}
    @Deprecated public final void onRemovedLast() {super.onRemovedLast();}
}
