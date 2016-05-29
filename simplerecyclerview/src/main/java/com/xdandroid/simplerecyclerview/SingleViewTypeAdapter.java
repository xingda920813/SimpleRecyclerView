package com.xdandroid.simplerecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.List;

/**
 * Created by XingDa on 2016/05/29.
 */

public abstract class SingleViewTypeAdapter<T> extends Adapter {

    protected List<T> list;

    protected abstract RecyclerView.ViewHolder onViewHolderCreate(List<T> list, ViewGroup parent);
    protected abstract void onViewHolderBind(List<T> list, RecyclerView.ViewHolder holder, int position);

    protected final RecyclerView.ViewHolder onViewHolderCreate(ViewGroup parent, int viewType) {throw new UnsupportedOperationException();}
    protected final void onViewHolderBind(RecyclerView.ViewHolder holder, int position, int viewType) {throw new UnsupportedOperationException();}
    protected final int getViewType(int position) {throw new UnsupportedOperationException();}
    protected final int getCount() {throw new UnsupportedOperationException();}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FrameLayout frameLayout = new FrameLayout(parent.getContext());
        ViewGroup.MarginLayoutParams outerParams = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        outerParams.setMargins(0, UIUtils.dp2px(parent.getContext(), 6), 0, UIUtils.dp2px(parent.getContext(), 6));
        frameLayout.setLayoutParams(outerParams);
        ProgressBar progressBar = new ProgressBar(parent.getContext());
        FrameLayout.LayoutParams innerParams = new FrameLayout.LayoutParams(UIUtils.dp2px(parent.getContext(), 40), UIUtils.dp2px(parent.getContext(), 40));
        innerParams.gravity = Gravity.CENTER;
        progressBar.setLayoutParams(innerParams);
        progressBar.setId(android.R.id.progress);
        frameLayout.addView(progressBar);
        return viewType == 65535 ? new ProgressViewHolder(frameLayout) : onViewHolderCreate(list, parent);
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
            ((ProgressViewHolder) holder).mProgressBar.setVisibility(mIsLoading ? View.VISIBLE : View.GONE);
        } else {
            onViewHolderBind(list, holder, holder.getAdapterPosition());
            if (mOnItemClickLitener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(holder.itemView, holder.getAdapterPosition(), 0);
                    }
                });
            }
            if (mOnItemLongClickLitener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return mOnItemLongClickLitener.onItemLongClick(holder.itemView, holder.getAdapterPosition(), 0);
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
        if (list == null) {
            setLoadingFalse();
            if (this.list != null) {
                notifyItemRemoved(this.list.size());
            }
            return;
        }
        if (this.list != null && this.list.size() > 0) {
            changeList(list);
            return;
        }
        if (list.size() <= 0) {
            setLoadingFalse();
            if (this.list != null) {
                notifyItemRemoved(this.list.size());
            }
            return;
        }
        this.list = list;
        notifyItemRangeInserted(0, list.size());
        setLoadingFalse();
    }

    public void add(T t) {
        if (t == null) {
            setLoadingFalse();
            if (this.list != null) {
                notifyItemRemoved(this.list.size());
            }
            return;
        }
        int originalSize = list.size();
        list.add(t);
        notifyItemInserted(originalSize);
        setLoadingFalse();
    }

    public void add(int position, T t) {
        if (t == null) {
            setLoadingFalse();
            if (this.list != null) {
                notifyItemRemoved(this.list.size());
            }
            return;
        }
        list.add(position,t);
        notifyItemInserted(position);
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

    public void set(int position,T t) {
        list.set(position,t);
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
        if (newList == null || newList.size() <= 0) {
            setLoadingFalse();
            if (this.list != null) {
                notifyItemRemoved(this.list.size());
            }
            return;
        }
        list.addAll(newList);
        notifyItemRangeInserted(position,newList.size());
        setLoadingFalse();
    }

    public void addAll(List<T> newList) {
        if (newList == null || newList.size() <= 0) {
            setLoadingFalse();
            if (this.list != null) {
                notifyItemRemoved(this.list.size());
            }
            return;
        }
        int originalSize = list.size();
        list.addAll(newList);
        notifyItemRangeInserted(originalSize,newList.size());
        setLoadingFalse();
    }
}
