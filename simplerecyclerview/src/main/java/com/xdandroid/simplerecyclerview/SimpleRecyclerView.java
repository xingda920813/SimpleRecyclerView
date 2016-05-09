package com.xdandroid.simplerecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

/**
 * Created by XingDa on 2016/5/9.
 */
public class SimpleRecyclerView extends RecyclerView {

    private boolean hasAddedItemDecor = false;
    private View emptyView;

    public SimpleRecyclerView(Context context) {
        super(context);
    }
    public SimpleRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public SimpleRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            Adapter<?> adapter =  (Adapter<?>)getAdapter();
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
    public void setAdapter(Adapter adapter) {
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
    }
}
