package com.xdandroid.simplerecyclerview;

import android.graphics.*;
import android.support.annotation.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import com.xdandroid.materialprogressview.*;

public abstract class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected ProgressViewHolder mProgressViewHolder;
    protected MaterialProgressViewHolder mMaterialProgressViewHolder;
    protected boolean mIsLoading;
    protected int mThreshold = 7;
    protected boolean mUseMaterialProgress;
    protected boolean mCanScrollVertically = true; //默认为纵向列表
    @ColorInt protected int[] mColorSchemeColors;
    @ColorInt protected int mProgressBackgroundColor;

    public void setThreshold(int threshold) {
        this.mThreshold = threshold;
    }

    /**
     * 设置是否使用 SwipeRefreshLayout 样式的加载更多转圈, 同时设置转圈的颜色变化序列.
     */
    public void setUseMaterialProgress(boolean useMaterialProgress, @ColorInt int[] colors) {
        this.mUseMaterialProgress = useMaterialProgress;
        this.mColorSchemeColors = colors;
    }

    /**
     * 设置转圈所在圆形突起的背景色.
     */
    public void setProgressBackgroundColor(@ColorInt int color) {
        this.mProgressBackgroundColor = color;
    }

    /**
     * 设置转圈的颜色变化序列.
     */
    public void setColorSchemeColors(@ColorInt int[] colors) {
        this.mColorSchemeColors = colors;
    }

    protected abstract void onLoadMore(Void please_make_your_adapter_class_as_abstract_class);
    protected abstract boolean hasMoreElements(Void let_activity_or_fragment_implement_these_methods);
    protected abstract RecyclerView.ViewHolder onViewHolderCreate(ViewGroup parent, int viewType);
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
    protected abstract void onViewHolderBind(RecyclerView.ViewHolder holder, int position, int viewType);
    protected abstract int getViewType(int position);
    protected abstract int getCount();
    protected abstract int getItemSpanSizeForGrid(int position, int viewType, int spanCount);

    @SuppressWarnings("ResourceType")
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType != 65535) {
            return onViewHolderCreate(parent, viewType);
        } else {
            FrameLayout frameLayout = new FrameLayout(parent.getContext());
            int outerWidth = mCanScrollVertically ? FrameLayout.LayoutParams.MATCH_PARENT : FrameLayout.LayoutParams.WRAP_CONTENT;
            int outerHeight = mCanScrollVertically ? FrameLayout.LayoutParams.WRAP_CONTENT : FrameLayout.LayoutParams.MATCH_PARENT;
            FrameLayout.LayoutParams outerParams = new FrameLayout.LayoutParams(outerWidth, outerHeight, Gravity.CENTER);
            frameLayout.setLayoutParams(outerParams);
            if (mUseMaterialProgress) {
                MaterialProgressView materialProgressView = new MaterialProgressView(parent.getContext());
                if (mColorSchemeColors == null || mColorSchemeColors.length <= 0) {
                    int colorAccentId = parent.getContext().getResources().getIdentifier("colorAccent", "color", parent.getContext().getPackageName());
                    int color;
                    if (colorAccentId > 0) {
                        color = parent.getContext().getResources().getColor(colorAccentId);
                    } else {
                        color = Color.parseColor("#607D8B");
                    }
                    materialProgressView.setColorSchemeColors(new int[]{color});
                } else {
                    materialProgressView.setColorSchemeColors(mColorSchemeColors);
                }
                if (mProgressBackgroundColor != 0) {
                    materialProgressView.setProgressBackgroundColor(mProgressBackgroundColor);
                }
                FrameLayout.LayoutParams innerParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                innerParams.setMargins(0, UIUtils.dp2px(parent.getContext(), 6), 0, UIUtils.dp2px(parent.getContext(), 6));
                innerParams.gravity = Gravity.CENTER;
                materialProgressView.setLayoutParams(innerParams);
                materialProgressView.setId(android.R.id.secondaryProgress);
                frameLayout.addView(materialProgressView);
                mMaterialProgressViewHolder = new MaterialProgressViewHolder(frameLayout);
                return mMaterialProgressViewHolder;
            } else {
                ProgressBar progressBar = new ProgressBar(parent.getContext());
                FrameLayout.LayoutParams innerParams = new FrameLayout.LayoutParams(UIUtils.dp2px(parent.getContext(), 40), UIUtils.dp2px(parent.getContext(), 40));
                innerParams.setMargins(0, UIUtils.dp2px(parent.getContext(), 6), 0, UIUtils.dp2px(parent.getContext(), 6));
                innerParams.gravity = Gravity.CENTER;
                progressBar.setLayoutParams(innerParams);
                progressBar.setId(android.R.id.progress);
                frameLayout.addView(progressBar);
                mProgressViewHolder = new ProgressViewHolder(frameLayout);
                return mProgressViewHolder;
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
            if (!mUseMaterialProgress && holder instanceof ProgressViewHolder) {
                ((ProgressViewHolder) holder).progressBar.setVisibility(mIsLoading ? View.VISIBLE : View.GONE);
            } else if (mUseMaterialProgress && holder instanceof MaterialProgressViewHolder) {
                ((MaterialProgressViewHolder) holder).progressBar.setVisibility(mIsLoading ? View.VISIBLE : View.GONE);
            }
        } else {
            onViewHolderBind(holder, position, getViewType(position));
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int adapterPosition = holder.getAdapterPosition();
                        //非常罕见的情况: layout/animation进行中(一般来说，这些过程的持续时间非常短), 但用户恰好在此时点击了item;
                        //则position将传入NO_POSITION == -1, 调用List.get(-1)会导致崩溃(ArrayIndexOutOfBoundsException);
                        //这里直接丢弃掉点击事件, 让用户再点一次.
                        if (adapterPosition == RecyclerView.NO_POSITION) return;
                        mOnItemClickListener.onItemClick(holder, holder.itemView, adapterPosition, getViewType(adapterPosition));
                    }
                });
            }
            if (mOnItemLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int adapterPosition = holder.getAdapterPosition();
                        return adapterPosition != RecyclerView.NO_POSITION && mOnItemLongClickListener
                                .onItemLongClick(holder, holder.itemView, adapterPosition, getViewType(adapterPosition));
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
        if (!mUseMaterialProgress && mProgressViewHolder != null && mProgressViewHolder.progressBar.isShown()) {
            mProgressViewHolder.progressBar.setVisibility(View.INVISIBLE);
        } else if (mUseMaterialProgress && mMaterialProgressViewHolder != null && mMaterialProgressViewHolder.progressBar.isShown()) {
            mMaterialProgressViewHolder.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    protected OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    protected OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
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

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
        if (lm != null) mCanScrollVertically = lm.canScrollVertically();
    }

    public void onListSet() {
        notifyDataSetChanged();
        setLoadingFalse();
    }

    public void onAdded() {
        if (getCount() <= 1) {
            onListSet();
            return;
        }
        notifyItemInserted(getCount() - 1);
        setLoadingFalse();
    }

    public void onAdded(int position) {
        if (getCount() <= 1) {
            onListSet();
            return;
        }
        notifyItemInserted(position);
        setLoadingFalse();
    }

    public void onRemoved(int position) {
        if (getCount() <= 0) {
            onListSet();
            return;
        }
        notifyItemRemoved(position);
        setLoadingFalse();
    }

    public void onRemoved() {
        onRemovedLast();
    }

    public void onRemovedLast() {
        if (getCount() <= 0) {
            onListSet();
            return;
        }
        notifyItemRemoved(getCount());
        setLoadingFalse();
    }

    public void onRemoveAll(int positionStart, int itemCount) {
        if (getCount() <= 0) {
            onListSet();
            return;
        }
        notifyItemRangeRemoved(positionStart, itemCount);
        setLoadingFalse();
    }

    public void onSet(int position) {
        notifyItemChanged(position);
        setLoadingFalse();
    }

    public void onSetAll(int positionStart, int itemCount) {
        notifyItemRangeChanged(positionStart, itemCount);
        setLoadingFalse();
    }

    public void onAddedAll(int position, int newDataSize) {
        if (getCount() <= newDataSize) {
            onListSet();
            return;
        }
        notifyItemRangeInserted(position, newDataSize);
        setLoadingFalse();
    }

    public void onAddedAll(int newDataSize) {
        if (getCount() <= newDataSize) {
            onListSet();
            return;
        }
        notifyItemRangeInserted(getCount() - newDataSize, newDataSize);
        setLoadingFalse();
    }
}
