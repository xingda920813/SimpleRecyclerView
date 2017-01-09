package com.xdandroid.simplerecyclerview;

import android.support.v7.widget.*;
import android.view.*;

import java.util.*;

public abstract class SingleViewTypeAdapter<T> extends Adapter {

    protected List<T> mList;

    protected abstract RecyclerView.ViewHolder onViewHolderCreate(List<T> list, ViewGroup parent);

    /**
     * 不要将position传入匿名内部类/方法内部类（如OnClickListener）。
     * Java只实现了值捕获，在匿名内部类中保存的position实际上是onBindViewHolder的position参数的一个副本。
     * 又因为int是基本类型，在设置创建匿名内部类（如设置OnClickListener）时，
     * 该int的值复制到了匿名内部类里面的position，而不是指向同一块栈内存。
     * 当position改变时（如滑动删除Item时），RecyclerView不会自动重新回调onBindViewHolder。
     * 这将导致匿名内部类（如OnClickListener）中的position还是原来的，而没有得到更新，发生点击错位的问题。
     * 要在匿名内部类（如OnClickListener）中得到当前Item的位置，请使用holder.getAdapterPosition()，
     * 通过ViewHolder动态获取当前Item的位置。而不要将position设为final或事实上是final的。
     */
    protected abstract void onViewHolderBind(List<T> list, RecyclerView.ViewHolder holder, int position);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType != 65535) {
            return onViewHolderCreate(mList, parent);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == mList.size() ? 65535 : 0;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (!mIsLoading && mList.size() > 0 && position >= mList.size() - mThreshold && hasMoreElements(null)) {
            mIsLoading = true;
            onLoadMore(null);
        }
        if (position == mList.size()) {
            if (!mUseMaterialProgress && holder instanceof ProgressViewHolder) {
                ((ProgressViewHolder) holder).progressBar.setVisibility(mIsLoading ? View.VISIBLE : View.GONE);
            } else if (mUseMaterialProgress && holder instanceof MaterialProgressViewHolder) {
                ((MaterialProgressViewHolder) holder).progressBar.setVisibility(mIsLoading ? View.VISIBLE : View.GONE);
            }
        } else {
            onViewHolderBind(mList, holder, position);
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int adapterPosition = holder.getAdapterPosition();
                        //非常罕见的情况: layout/animation进行中(一般来说，这些过程的持续时间非常短), 但用户恰好在此时点击了item;
                        //则position将传入NO_POSITION == -1, 调用List.get(-1)会导致崩溃(ArrayIndexOutOfBoundsException);
                        //这里直接丢弃掉点击事件, 让用户再点一次.
                        if (adapterPosition == RecyclerView.NO_POSITION) return;
                        mOnItemClickListener.onItemClick(holder, holder.itemView, adapterPosition, 0);
                    }
                });
            }
            if (mOnItemLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int adapterPosition = holder.getAdapterPosition();
                        return adapterPosition != RecyclerView.NO_POSITION && mOnItemLongClickListener.onItemLongClick(holder, holder.itemView, adapterPosition, 0);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mList == null) ? 0 : (mList.size() + 1);
    }

    public void setList(List<T> list) {
        this.mList = list;
        notifyDataSetChanged();
        setLoadingFalse();
    }

    public void add(T t) {
        if (t == null) {
            setLoadingFalse();
            return;
        }
        int originalSize = mList.size();
        mList.add(t);
        if (originalSize <= 0) {
            notifyDataSetChanged();
        } else {
            notifyItemInserted(originalSize);
        }
        setLoadingFalse();
    }

    public void add(int position, T t) {
        if (t == null) {
            setLoadingFalse();
            return;
        }
        mList.add(position, t);
        if (mList.size() <= 1) {
            notifyDataSetChanged();
        } else {
            notifyItemInserted(position);
        }
        setLoadingFalse();
    }

    public void remove(int position) {
        mList.remove(position);
        if (mList.size() <= 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRemoved(position);
        }
        setLoadingFalse();
    }

    public void remove() {
        removeLast();
    }

    public void removeLast() {
        int originalSize = mList.size();
        mList.remove(originalSize - 1);
        if (mList.size() <= 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRemoved(originalSize - 1);
        }
        setLoadingFalse();
    }

    public void removeAll(int positionStart, int itemCount) {
        for (int i = positionStart; i < positionStart + itemCount; i++) {
            mList.remove(positionStart);
        }
        if (mList.size() <= 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(positionStart, itemCount);
        }
        setLoadingFalse();
    }

    public void set(int position, T t) {
        mList.set(position, t);
        notifyItemChanged(position);
        setLoadingFalse();
    }

    public void setAll(int positionStart, int itemCount, T t) {
        for (int i = positionStart; i < positionStart + itemCount; i++) {
            mList.set(i, t);
        }
        notifyItemRangeChanged(positionStart, itemCount);
        setLoadingFalse();
    }

    public void addAll(int position, List<T> newList) {
        if (newList == null || newList.size() <= 0) {
            setLoadingFalse();
            return;
        }
        int originalSize = mList.size();
        mList.addAll(position, newList);
        if (originalSize <= 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(position, newList.size());
        }
        setLoadingFalse();
    }

    public void addAll(List<T> newList) {
        if (newList == null || newList.size() <= 0) {
            setLoadingFalse();
            return;
        }
        int originalSize = mList.size();
        mList.addAll(newList);
        if (originalSize <= 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(originalSize, newList.size());
        }
        setLoadingFalse();
    }

    public List<T> getList() {
        return mList;
    }

    @Deprecated protected native final RecyclerView.ViewHolder onViewHolderCreate(ViewGroup parent, int viewType);
    @Deprecated protected native final void onViewHolderBind(RecyclerView.ViewHolder holder, int position, int viewType);
    @Deprecated protected native final int getViewType(int position);
    @Deprecated protected native final int getCount();
    @Deprecated public final void onAdded() {super.onAdded();}
    @Deprecated public final void onAdded(int position) {super.onAdded(position);}
    @Deprecated public final void onAddedAll(int newDataSize) {super.onAddedAll(newDataSize);}
    @Deprecated public final void onAddedAll(int position, int newDataSize) {super.onAddedAll(position, newDataSize);}
    @Deprecated public final void onListSet() {super.onListSet();}
    @Deprecated public final void onRemoveAll(int positionStart, int itemCount) {super.onRemoveAll(positionStart, itemCount);}
    @Deprecated public final void onRemoved() {super.onRemoved();}
    @Deprecated public final void onRemoved(int position) {super.onRemoved(position);}
    @Deprecated public final void onRemovedLast() {super.onRemovedLast();}
    @Deprecated public final void onSet(int position) {super.onSet(position);}
    @Deprecated public final void onSetAll(int positionStart, int itemCount) {super.onSetAll(positionStart, itemCount);}
}
