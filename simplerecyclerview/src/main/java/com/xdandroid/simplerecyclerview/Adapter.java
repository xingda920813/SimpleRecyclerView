package com.xdandroid.simplerecyclerview;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.xdandroid.materialprogressview.MaterialProgressView;

/**
 * Created by XingDa on 2016/05/29.
 */

public abstract class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected MaterialProgressViewHolder mMaterialProgressViewHolder;
    protected int[] mColorSchemeColors;
    protected boolean mIsLoading;
    protected int mThreshold = 7;
    protected boolean mUseMaterialProgress;

    public void setThreshold(int threshold) {
        this.mThreshold = threshold;
    }

    public void setUseMaterialProgress(boolean useMaterialProgress, int[] colors) {
        this.mUseMaterialProgress = useMaterialProgress;
        this.mColorSchemeColors = colors;
    }

    public void setColorSchemeColors(int[] colors) {
        this.mColorSchemeColors = colors;
    }

    protected abstract void onLoadMore(Void please_Make_Your_Adapter_Class_As_Abstract_Class);
    protected abstract boolean hasMoreElements(Void let_Activity_Or_Fragment_Implement_These_Methods);
    protected abstract RecyclerView.ViewHolder onViewHolderCreate(ViewGroup parent, int viewType);
    protected abstract void onViewHolderBind(RecyclerView.ViewHolder holder, int position, int viewType);
    protected abstract int getViewType(int position);
    protected abstract int getCount();
    protected abstract int getItemSpanSizeForGrid(int position, int viewType, int spanCount);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType != 65535) {
            return onViewHolderCreate(parent, viewType);
        } else {
            FrameLayout frameLayout = new FrameLayout(parent.getContext());
            ViewGroup.MarginLayoutParams outerParams = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            outerParams.setMargins(0, UIUtils.dp2px(parent.getContext(), 6), 0, UIUtils.dp2px(parent.getContext(), 6));
            frameLayout.setLayoutParams(outerParams);
            if (mUseMaterialProgress) {
                MaterialProgressView materialProgressView = new MaterialProgressView(parent.getContext());
                if (mColorSchemeColors == null || mColorSchemeColors.length <= 0) {
                    int colorAccentId = parent.getContext().getResources().getIdentifier("colorAccent", "color", parent.getContext().getPackageName());
                    int color;
                    if (colorAccentId > 0) {
                        color = parent.getContext().getResources().getColor(colorAccentId);
                    } else {
                        color = Color.parseColor("#FF4081");
                    }
                    materialProgressView.setColorSchemeColors(new int[]{color});
                } else {
                    materialProgressView.setColorSchemeColors(mColorSchemeColors);
                }
                FrameLayout.LayoutParams innerParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                innerParams.gravity = Gravity.CENTER;
                materialProgressView.setLayoutParams(innerParams);
                materialProgressView.setId(android.R.id.secondaryProgress);
                frameLayout.addView(materialProgressView);
                mMaterialProgressViewHolder = new MaterialProgressViewHolder(frameLayout);
                return mMaterialProgressViewHolder;
            } else {
                ProgressBar progressBar = new ProgressBar(parent.getContext());
                FrameLayout.LayoutParams innerParams = new FrameLayout.LayoutParams(UIUtils.dp2px(parent.getContext(), 40), UIUtils.dp2px(parent.getContext(), 40));
                innerParams.gravity = Gravity.CENTER;
                progressBar.setLayoutParams(innerParams);
                progressBar.setId(android.R.id.progress);
                frameLayout.addView(progressBar);
                return new ProgressViewHolder(frameLayout);
            }
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
            if (holder instanceof ProgressViewHolder) {
                ((ProgressViewHolder) holder).mProgressBar.setVisibility(mIsLoading ? View.VISIBLE : View.GONE);
            } else if (mUseMaterialProgress && holder instanceof MaterialProgressViewHolder) {
                ((MaterialProgressViewHolder) holder).mProgressBar.setVisibility(mIsLoading ? View.VISIBLE : View.GONE);
            }
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
        if (mUseMaterialProgress && mMaterialProgressViewHolder != null) {
            mMaterialProgressViewHolder.mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    protected OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    protected OnItemLongClickLitener mOnItemLongClickLitener;

    public void setOnItemLongClickLitener(OnItemLongClickLitener mOnItemLongClickLitener) {
        this.mOnItemLongClickLitener = mOnItemLongClickLitener;
    }

    protected abstract class ProgressSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        protected int spanCount;

        protected ProgressSpanSizeLookup(int spanCount) {
            this.spanCount = spanCount;
        }

        @Override
        public int getSpanSize(int position) {
            int viewType = getItemViewType(position);
            if (viewType == 65535) {
                return spanCount;
            } else {
                int itemSpanSize = getItemSpanSize(position, viewType, spanCount);
                if (itemSpanSize < 1) itemSpanSize = 1;
                if (itemSpanSize > spanCount) itemSpanSize = spanCount;
                return itemSpanSize;
            }
        }

        protected abstract int getItemSpanSize(int position, int viewType, int spanCount);
    }

    public ProgressSpanSizeLookup getSpanSizeLookup(int spanCount) {
        return new ProgressSpanSizeLookup(spanCount) {
            @Override
            protected int getItemSpanSize(int position, int viewType, int spanCount) {
                return getItemSpanSizeForGrid(position, viewType, spanCount);
            }
        };
    }

    public void onAdded() {
        notifyItemInserted(getCount() - 1);
        setLoadingFalse();
    }

    public void onRemoved() {
        onRemovedLast();
    }

    public void onRemovedLast() {
        notifyItemRemoved(getCount());
        setLoadingFalse();
    }

    public void onAddedAll(int newDataSize) {
        notifyItemRangeInserted(getCount() - newDataSize, newDataSize);
        setLoadingFalse();
    }
}
