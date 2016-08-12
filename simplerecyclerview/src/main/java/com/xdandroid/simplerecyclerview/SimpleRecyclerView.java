package com.xdandroid.simplerecyclerview;

import android.content.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;

import com.xdandroid.simplerecyclerview.stickyheaders.*;

/**
 * Created by XingDa on 2016/5/9.
 */
public class SimpleRecyclerView extends RecyclerView {

    protected boolean hasAddedItemDecor = false;
    protected boolean hasRegisteredEmptyObserver = false;
    protected View emptyView, errorView, loadingView;
    protected StickyRecyclerHeadersDecoration headersDecoration;

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
            RecyclerView.Adapter<? extends ViewHolder> adapter = (RecyclerView.Adapter<? extends ViewHolder>) getAdapter();
            if (adapter != null && emptyView != null) {
                if (adapter.getItemCount() <= 1) {
                    emptyView.setVisibility(View.VISIBLE);
                    setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.GONE);
                    setVisibility(View.VISIBLE);
                }
            }
        }
    };

    protected AdapterDataObserver headersObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (headersDecoration == null) return;
            headersDecoration.invalidateHeaders();
        }
    };

    protected AdapterDataObserver loadingObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            hideLoadingView();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            hideLoadingView();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            hideLoadingView();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            hideLoadingView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            hideLoadingView();
        }
    };

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        super.setAdapter(adapter);
        if (!hasRegisteredEmptyObserver && adapter != null) {
            hasRegisteredEmptyObserver = true;
            UIUtils.registerObserver(this, emptyObserver);
        }
        emptyObserver.onChanged();
        if (!hasAddedItemDecor && getAdapter() instanceof StickyRecyclerHeadersAdapter) {
            hasAddedItemDecor = true;
            StickyRecyclerHeadersAdapter stickyHeadersRVAdapter = (StickyRecyclerHeadersAdapter) getAdapter();
            headersDecoration = new StickyRecyclerHeadersDecoration(stickyHeadersRVAdapter);
            addItemDecoration(headersDecoration);
            UIUtils.registerObserver(this, headersObserver);
        }
        UIUtils.registerObserver(this, loadingObserver);
        if (adapter != null && adapter.getItemCount() > 1) {
            hideLoadingView();
        }
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        emptyObserver.onChanged();
    }

    public void showErrorView(View errorView) {
        this.errorView = errorView;
        if (errorView == null) return;
        errorView.setVisibility(View.VISIBLE);
        setVisibility(View.GONE);
        hideLoadingView();
    }

    public View hideErrorView() {
        if (errorView == null) return null;
        errorView.setVisibility(View.GONE);
        setVisibility(View.VISIBLE);
        return errorView;
    }

    /**
     * 在调用setAdapter和notify*系列方法之前调用此方法来设置LoadingView。
     * LoadingView会在setAdapter和notify*系列方法调用时自动隐藏。
     *
     * @param loadingView 通过findViewById找到的LoadingView.
     */
    public void setLoadingView(View loadingView) {
        this.loadingView = loadingView;
    }

    /**
     * 手动控制LoadingView的隐藏，一般情况下无需调用此方法。
     */
    public View hideLoadingView() {
        if (loadingView == null) return null;
        loadingView.setVisibility(GONE);
        setVisibility(VISIBLE);
        return loadingView;
    }

    /**
     * 手动控制LoadingView的显示，一般情况下无需调用此方法。
     */
    public View showLoadingView() {
        if (loadingView == null) return null;
        loadingView.setVisibility(VISIBLE);
        setVisibility(GONE);
        hideErrorView();
        return loadingView;
    }

    /**
     * 还能向下/向右滑动多少(px)
     * @return 还能向下/向右滑动多少(px)
     */
    public int getDistanceToEnd() {
        LayoutManager manager = getLayoutManager();
        if (!(manager instanceof LinearLayoutManager)) return getHeight();
        LinearLayoutManager linearManager = (LinearLayoutManager) manager;
        View firstVisibleItem = getChildAt(0);
        int firstItemPosition = linearManager.findFirstVisibleItemPosition();
        int itemCount = linearManager.getItemCount();
        if (linearManager.getOrientation() == LinearLayoutManager.VERTICAL) {
            int recyclerViewHeight = getHeight();
            int itemHeight = firstVisibleItem.getHeight();
            int firstItemBottom = linearManager.getDecoratedBottom(firstVisibleItem);
            return (itemCount - firstItemPosition - 1) * itemHeight - recyclerViewHeight + firstItemBottom;
        } else {
            int recyclerViewWidth = getWidth();
            int itemWidth = firstVisibleItem.getWidth();
            int firstItemRight = linearManager.getDecoratedRight(firstVisibleItem);
            return (itemCount - firstItemPosition - 1) * itemWidth - recyclerViewWidth + firstItemRight;
        }
    }

    /**
     * 已滑动的距离(px)
     * @return 已滑动的距离(px)
     */
    public int getScrolledDistance() {
        LayoutManager manager = getLayoutManager();
        if (!(manager instanceof LinearLayoutManager)) return 0;
        LinearLayoutManager linearManager = (LinearLayoutManager) manager;
        View firstVisibleItem = getChildAt(0);
        int firstItemPosition = linearManager.findFirstVisibleItemPosition();
        if (linearManager.getOrientation() == LinearLayoutManager.VERTICAL) {
            int itemHeight = firstVisibleItem.getHeight();
            int firstItemBottom = linearManager.getDecoratedBottom(firstVisibleItem);
            return (firstItemPosition + 1) * itemHeight - firstItemBottom;
        } else {
            int itemWidth = firstVisibleItem.getWidth();
            int firstItemRight = linearManager.getDecoratedRight(firstVisibleItem);
            return (firstItemPosition + 1) * itemWidth - firstItemRight;
        }
    }
}
