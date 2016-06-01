package com.xdandroid.simplerecyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

/**
 * Created by XingDa on 2016/05/29.
 */

public abstract class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected boolean mIsLoading;
    protected int mThreshold = 7;

    public void setThreshold(int threshold) {
        this.mThreshold = threshold;
    }

    protected abstract void onLoadMore(Please_Make_Your_Adapter_Class_As_Abstract_Class Void);

    protected abstract boolean hasMoreElements(Let_Activity_Or_Fragment_Implement_These_Methods Void);

    protected abstract RecyclerView.ViewHolder onViewHolderCreate(ViewGroup parent, int viewType);

    protected abstract void onViewHolderBind(RecyclerView.ViewHolder holder, int position, int viewType);

    protected abstract int getViewType(int position);

    protected abstract int getCount();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType != 65535) {
            return onViewHolderCreate(parent, viewType);
        } else {
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
            return new ProgressViewHolder(frameLayout);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == getCount() ? 65535 : getViewType(position);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (!mIsLoading && getCount() > 0 && position >= getCount() - mThreshold && hasMoreElements(null)) {
            mIsLoading = true;
            onLoadMore(null);
        }
        if (position == getCount()) {
            ((ProgressViewHolder) holder).mProgressBar.setVisibility(mIsLoading ? View.VISIBLE : View.GONE);
        } else {
            onViewHolderBind(holder, holder.getAdapterPosition(), getViewType(holder.getAdapterPosition()));
            if (mOnItemClickLitener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(holder, holder.itemView, holder.getAdapterPosition(), getViewType(holder.getAdapterPosition()));
                    }
                });
            }
            if (mOnItemLongClickLitener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return mOnItemLongClickLitener.onItemLongClick(holder, holder.itemView, holder.getAdapterPosition(), getViewType(holder.getAdapterPosition()));
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return getCount() + 1;
    }

    public void setLoadingFalse() {
        mIsLoading = false;
    }

    protected OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    protected OnItemLongClickLitener mOnItemLongClickLitener;

    public void setOnItemLongClickLitener(OnItemLongClickLitener mOnItemLongClickLitener) {
        this.mOnItemLongClickLitener = mOnItemLongClickLitener;
    }

    protected class ProgressSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        protected int spanSize;

        protected ProgressSpanSizeLookup(int spanSize) {
            this.spanSize = spanSize;
        }

        @Override
        public int getSpanSize(int i) {
            switch (getItemViewType(i)) {
                case 65535:
                    return spanSize;
                default:
                    return 1;
            }
        }
    }

    public GridLayoutManager.SpanSizeLookup getSpanSizeLookup(int spanSize) {
        return new ProgressSpanSizeLookup(spanSize);
    }
}
