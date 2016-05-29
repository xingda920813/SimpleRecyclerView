package com.xdandroid.simplerecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.List;

/**
 * Created by XingDa on 2016/5/9.
 */
public class SimpleRecyclerView extends RecyclerView {

    protected boolean hasAddedItemDecor = false;
    protected View emptyView,errorView;

    public SimpleRecyclerView(Context context) {
        super(context);
    }
    public SimpleRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public SimpleRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected AdapterDataObserver emptyObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            RecyclerView.Adapter<?> adapter =  (RecyclerView.Adapter<?>)getAdapter();
            if(adapter != null && emptyView != null) {
                if(adapter.getItemCount() <= 1) {
                    emptyView.setVisibility(View.VISIBLE);
                    setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.GONE);
                    setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        super.setAdapter(adapter);
        if(adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }
        emptyObserver.onChanged();
        if (getAdapter() instanceof StickyRecyclerHeadersAdapter) {
            StickyRecyclerHeadersAdapter stickyHeadersRVAdapter = (StickyRecyclerHeadersAdapter) getAdapter();
            final StickyRecyclerHeadersDecoration headersDecoration = new StickyRecyclerHeadersDecoration(stickyHeadersRVAdapter);
            if (!hasAddedItemDecor) {
                addItemDecoration(headersDecoration);
                hasAddedItemDecor = true;
            }
            getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override public void onChanged() {
                    headersDecoration.invalidateHeaders();
                }
            });
        }
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        emptyObserver.onChanged();
    }

    public void showErrorView(View errorView) {
        RecyclerView.Adapter<?> adapter =  (RecyclerView.Adapter<?>)getAdapter();
        if(adapter != null && errorView != null) {
            this.errorView = errorView;
            this.errorView.setVisibility(View.VISIBLE);
            setVisibility(View.GONE);
        }
    }

    public void hideErrorView() {
        RecyclerView.Adapter<?> adapter =  (RecyclerView.Adapter<?>)getAdapter();
        if(adapter != null && this.errorView != null) {
            this.errorView.setVisibility(View.GONE);
            setVisibility(View.VISIBLE);
        }
    }

    // TODO: This is Divider.
    public static class Divider extends RecyclerView.ItemDecoration {

        protected Drawable mDivider;
        protected boolean mIsHorizontal;
        protected int leftOffset,topOffset,rightOffset,bottomOffset;

        public Divider(Context context, @Nullable Integer dividerDrawableResId, boolean isHorizontal, int leftOffset, int topOffset, int rightOffset, int bottomOffset) {
            if (dividerDrawableResId != null && dividerDrawableResId > 0) {
                this.mDivider = context.getResources().getDrawable(dividerDrawableResId);
            } else {
                final TypedArray a = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
                this.mDivider = a.getDrawable(0);
                a.recycle();
            }
            this.mIsHorizontal = isHorizontal;
            this.leftOffset = leftOffset;
            this.topOffset = topOffset;
            this.rightOffset = rightOffset;
            this.bottomOffset = bottomOffset;
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            if (mIsHorizontal) {
                drawHorizontal(c, parent);
            } else {
                drawVertical(c, parent);
            }
        }

        protected void drawVertical(Canvas c, RecyclerView parent) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left + leftOffset, top + topOffset, right + rightOffset, bottom + bottomOffset);
                mDivider.draw(c);
            }
        }

        protected void drawHorizontal(Canvas c, RecyclerView parent) {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getHeight() - parent.getPaddingBottom();
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int left = child.getRight() + params.rightMargin;
                final int right = left + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left + leftOffset, top + topOffset, right + rightOffset, bottom + bottomOffset);
                mDivider.draw(c);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (mIsHorizontal) {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            } else {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            }
        }
    }

    public static final class Please_Make_Your_Adapter_Class_As_Abstract_Class {private Please_Make_Your_Adapter_Class_As_Abstract_Class(){}}
    public static final class Let_Activity_Or_Fragment_Implement_These_Methods {private Let_Activity_Or_Fragment_Implement_These_Methods(){}}

    // TODO: This is MultiViewTypeAdapter.
    public static abstract class MultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        protected boolean mIsLoading;
        protected int mThreshold = 7;
        public void setThreshold(int threshold) {
            this.mThreshold = threshold;
        }

        protected abstract void onLoadMore(Please_Make_Your_Adapter_Class_As_Abstract_Class Void);
        protected abstract boolean hasMoreElements(Let_Activity_Or_Fragment_Implement_These_Methods Void);
        protected abstract ViewHolder onViewHolderCreate(ViewGroup parent, int viewType);
        protected abstract void onViewHolderBind(ViewHolder holder, int position, int viewType);
        protected abstract int getViewType(int position);
        protected abstract int getCount();

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            FrameLayout frameLayout = new FrameLayout(parent.getContext());
            ViewGroup.MarginLayoutParams outerParams = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            outerParams.setMargins(0, SimpleUIUtils.dp2px(parent.getContext(), 6), 0, SimpleUIUtils.dp2px(parent.getContext(), 6));
            frameLayout.setLayoutParams(outerParams);
            ProgressBar progressBar = new ProgressBar(parent.getContext());
            FrameLayout.LayoutParams innerParams = new FrameLayout.LayoutParams(SimpleUIUtils.dp2px(parent.getContext(), 40), SimpleUIUtils.dp2px(parent.getContext(), 40));
            innerParams.gravity = Gravity.CENTER;
            progressBar.setLayoutParams(innerParams);
            progressBar.setId(android.R.id.progress);
            frameLayout.addView(progressBar);
            return viewType == 65535 ? new ProgressViewHolder(frameLayout) : onViewHolderCreate(parent, viewType);
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
                            mOnItemClickLitener.onItemClick(holder.itemView, holder.getAdapterPosition(), getViewType(holder.getAdapterPosition()));
                        }
                    });
                }
                if (mOnItemLongClickLitener != null) {
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            return mOnItemLongClickLitener.onItemLongClick(holder.itemView, holder.getAdapterPosition(), getViewType(holder.getAdapterPosition()));
                        }
                    });
                }
            }
        }

        protected static class ProgressViewHolder extends RecyclerView.ViewHolder {
            protected ProgressBar mProgressBar;
            protected FrameLayout mFrameLayout;
            protected ProgressViewHolder(View view) {
                super(view);
                mFrameLayout = (FrameLayout) view;
                mProgressBar = (ProgressBar) mFrameLayout.findViewById(android.R.id.progress);
            }
        }

        @Override
        public int getItemCount() {
            return getCount() + 1;
        }

        public void setLoadingFalse() {
            mIsLoading = false;
        }

        public interface OnItemClickLitener {
            public void onItemClick(View view, int position, int viewType);
        }
        protected OnItemClickLitener mOnItemClickLitener;
        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }

        public interface OnItemLongClickLitener {
            public boolean onItemLongClick(View view, int position, int viewType);
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

    // TODO: This is SingleViewTypeAdapter.
    public static abstract class SingleViewTypeAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        protected List<T> list;
        protected boolean mIsLoading;
        protected int mThreshold = 7;
        public void setThreshold(int threshold) {
            this.mThreshold = threshold;
        }

        protected abstract void onLoadMore(Please_Make_Your_Adapter_Class_As_Abstract_Class Void);
        protected abstract boolean hasMoreElements(Let_Activity_Or_Fragment_Implement_These_Methods Void);
        protected abstract ViewHolder onViewHolderCreate(List<T> list, ViewGroup parent);
        protected abstract void onViewHolderBind(List<T> list, ViewHolder holder, int position);

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            FrameLayout frameLayout = new FrameLayout(parent.getContext());
            ViewGroup.MarginLayoutParams outerParams = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            outerParams.setMargins(0, SimpleUIUtils.dp2px(parent.getContext(), 6), 0, SimpleUIUtils.dp2px(parent.getContext(), 6));
            frameLayout.setLayoutParams(outerParams);
            ProgressBar progressBar = new ProgressBar(parent.getContext());
            FrameLayout.LayoutParams innerParams = new FrameLayout.LayoutParams(SimpleUIUtils.dp2px(parent.getContext(), 40), SimpleUIUtils.dp2px(parent.getContext(), 40));
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
                            mOnItemClickLitener.onItemClick(holder.itemView, holder.getAdapterPosition());
                        }
                    });
                }
                if (mOnItemLongClickLitener != null) {
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            return mOnItemLongClickLitener.onItemLongClick(holder.itemView, holder.getAdapterPosition());
                        }
                    });
                }
            }
        }

        protected static class ProgressViewHolder extends RecyclerView.ViewHolder {
            protected ProgressBar mProgressBar;
            protected FrameLayout mFrameLayout;
            protected ProgressViewHolder(View view) {
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

        public interface OnItemClickLitener {
            public void onItemClick(View view, int position);
        }
        protected OnItemClickLitener mOnItemClickLitener;
        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }

        public interface OnItemLongClickLitener {
            public boolean onItemLongClick(View view, int position);
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
}
