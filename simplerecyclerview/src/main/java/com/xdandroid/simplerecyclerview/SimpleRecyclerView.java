package com.xdandroid.simplerecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.xdandroid.simplerecyclerview.stickyheaders.StickyRecyclerHeadersAdapter;
import com.xdandroid.simplerecyclerview.stickyheaders.StickyRecyclerHeadersDecoration;

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
            try {
                getAdapter().unregisterAdapterDataObserver(emptyObserver);
            } catch (Exception ignored) {}
            try {
                getAdapter().registerAdapterDataObserver(emptyObserver);
            } catch (Exception ignored) {}
        }
        emptyObserver.onChanged();
        if (!hasAddedItemDecor && getAdapter() instanceof StickyRecyclerHeadersAdapter) {
            hasAddedItemDecor = true;
            StickyRecyclerHeadersAdapter stickyHeadersRVAdapter = (StickyRecyclerHeadersAdapter) getAdapter();
            headersDecoration = new StickyRecyclerHeadersDecoration(stickyHeadersRVAdapter);
            addItemDecoration(headersDecoration);
            try {
                getAdapter().unregisterAdapterDataObserver(headersObserver);
            } catch (Exception ignored) {}
            try {
                getAdapter().registerAdapterDataObserver(headersObserver);
            } catch (Exception ignored) {}
        }
        try {
            getAdapter().unregisterAdapterDataObserver(loadingObserver);
        } catch (Exception ignored) {}
        try {
            getAdapter().registerAdapterDataObserver(loadingObserver);
        } catch (Exception ignored) {}
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
}
