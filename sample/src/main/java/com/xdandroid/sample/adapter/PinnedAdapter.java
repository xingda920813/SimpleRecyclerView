package com.xdandroid.sample.adapter;

import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import com.xdandroid.sample.R;
import com.xdandroid.sample.bean.*;
import com.xdandroid.simplerecyclerview.*;
import com.xdandroid.simplerecyclerview.stickyheaders.*;

import java.util.*;

/**
 * Created by XingDa on 2016/08/10.
 */

public class PinnedAdapter extends SingleViewTypeAdapter<SampleBean> implements StickyRecyclerHeadersAdapter<PinnedAdapter.HeaderVH> {

    @Override
    protected RecyclerView.ViewHolder onViewHolderCreate(List<SampleBean> list, ViewGroup parent) {
        return new ItemVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple, parent, false));
    }

    @Override
    protected void onViewHolderBind(List<SampleBean> list, RecyclerView.ViewHolder holder, int position) {
        ItemVH itemVH = (ItemVH) holder;
        itemVH.tvTitle.setText(list.get(position).title);
        itemVH.tvContent.setText(list.get(position).content);
    }

    @Override
    public long getHeaderId(int position) {
        return position / 10;
    }

    @Override
    public HeaderVH onCreateHeaderViewHolder(ViewGroup parent) {
        return new HeaderVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(HeaderVH holder, int position) {
        holder.tvHeader.setText("Group : " + String.valueOf(position + 1) + " - " + String.valueOf(position + 10));
    }

    static final class ItemVH extends RecyclerView.ViewHolder {
        ItemVH(View v) {
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.tv_title);
            tvContent = (TextView) v.findViewById(R.id.tv_content);
        }
        TextView tvTitle, tvContent;
    }

    static final class HeaderVH extends RecyclerView.ViewHolder {
        HeaderVH(View v) {
            super(v);
            tvHeader = (TextView) v.findViewById(R.id.tv_header);
        }
        TextView tvHeader;
    }

    @Override
    protected void onLoadMore(Void please_make_your_adapter_class_as_abstract_class) {

    }

    @Override
    protected boolean hasMoreElements(Void let_activity_or_fragment_implement_these_methods) {
        return false;
    }

    @Override
    protected int getItemSpanSizeForGrid(int position, int viewType, int spanCount) {
        return 1;
    }
}
