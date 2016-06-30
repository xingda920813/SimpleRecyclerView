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
    protected View emptyView, errorView;
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
            if (headersDecoration != null) {
                headersDecoration.invalidateHeaders();
            }
        }
    };

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        super.setAdapter(adapter);
        if (!hasRegisteredEmptyObserver && adapter != null) {
            hasRegisteredEmptyObserver = true;
            try {
                getAdapter().unregisterAdapterDataObserver(emptyObserver);
            } catch (Exception ignored) {
            }
            try {
                getAdapter().registerAdapterDataObserver(emptyObserver);
            } catch (Exception ignored) {
            }
        }
        emptyObserver.onChanged();
        if (!hasAddedItemDecor && getAdapter() instanceof StickyRecyclerHeadersAdapter) {
            hasAddedItemDecor = true;
            StickyRecyclerHeadersAdapter stickyHeadersRVAdapter = (StickyRecyclerHeadersAdapter) getAdapter();
            headersDecoration = new StickyRecyclerHeadersDecoration(stickyHeadersRVAdapter);
            addItemDecoration(headersDecoration);
            try {
                getAdapter().unregisterAdapterDataObserver(headersObserver);
            } catch (Exception ignored) {
            }
            try {
                getAdapter().registerAdapterDataObserver(headersObserver);
            } catch (Exception ignored) {
            }
        }
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        emptyObserver.onChanged();
    }

    public void showErrorView(View errorView) {
        RecyclerView.Adapter<? extends ViewHolder> adapter = (RecyclerView.Adapter<? extends ViewHolder>) getAdapter();
        if (adapter != null && errorView != null) {
            this.errorView = errorView;
            this.errorView.setVisibility(View.VISIBLE);
            setVisibility(View.GONE);
        }
    }

    public void hideErrorView() {
        RecyclerView.Adapter<? extends ViewHolder> adapter = (RecyclerView.Adapter<? extends ViewHolder>) getAdapter();
        if (adapter != null && this.errorView != null) {
            this.errorView.setVisibility(View.GONE);
            setVisibility(View.VISIBLE);
        }
    }
}
