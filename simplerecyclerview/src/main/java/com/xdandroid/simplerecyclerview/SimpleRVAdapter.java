package com.xdandroid.simplerecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.List;

/**
 * Created by XingDa on 2016/5/9.
 */
public abstract class SimpleRVAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<T> list;
    private boolean mIsLoading;
    private int mThreshold = 7;
    public void setThreshold(int threshold) {
        this.mThreshold = threshold;
    }

    protected abstract void onLoadMore();
    protected abstract boolean hasMoreElements();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FrameLayout frameLayout = new FrameLayout(parent.getContext());
        ViewGroup.LayoutParams outerParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        frameLayout.setLayoutParams(outerParams);
        ProgressBar progressBar = new ProgressBar(parent.getContext());
        FrameLayout.LayoutParams innerParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        innerParams.gravity = Gravity.CENTER;
        progressBar.setLayoutParams(innerParams);
        progressBar.setId(android.R.id.progress);
        frameLayout.addView(progressBar);
        return new ProgressViewHolder(frameLayout);
    }

    @Override
    public int getItemViewType(int position) {
        return 65535;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (!mIsLoading && position >= getItemCount() - mThreshold && hasMoreElements() && list.size() > 0) {
            mIsLoading = true;
            onLoadMore();
        }
        if (position == list.size()) {
            ((ProgressViewHolder) holder).mProgressBar.setVisibility(mIsLoading ? View.VISIBLE : View.GONE);
        }
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, holder.getAdapterPosition());
                }
            });
        }
    }

    public static final class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar mProgressBar;
        public FrameLayout mFrameLayout;
        public ProgressViewHolder(final View view) {
            super(view);
            mFrameLayout = (FrameLayout) view;
            mProgressBar = (ProgressBar) mFrameLayout.findViewById(android.R.id.progress);
        }
    }

    @Override
    public int getItemCount() {
        return (list == null) ? 0 : (list.size() + 1);
    }

    public void setLoadingFalse() {
        mIsLoading = false;
    }

    public void changeList(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setList(List<T> list) {
        this.list = list;
        notifyItemRangeInserted(0, list.size());
    }

    public void add(T t) {
        int originalSize = list.size();
        list.add(t);
        notifyItemInserted(originalSize);
    }

    public void addAll(List<T> newList) {
        int originalSize = list.size();
        list.addAll(newList);
        notifyItemRangeInserted(originalSize,newList.size());
    }

    public interface OnItemClickLitener {
        public void onItemClick(View view, int position);
    }
    private OnItemClickLitener mOnItemClickLitener;
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
