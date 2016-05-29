package com.xdandroid.simplerecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

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
}
