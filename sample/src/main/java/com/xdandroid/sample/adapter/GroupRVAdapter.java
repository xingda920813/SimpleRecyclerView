package com.xdandroid.sample.adapter;

import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import com.xdandroid.sample.R;
import com.xdandroid.sample.bean.*;
import com.xdandroid.simplerecyclerview.*;

import java.util.*;

public class GroupRVAdapter extends GroupAdapter<Title, SampleBean> {

    public GroupRVAdapter(List<Group<Title, SampleBean>> groups) {
        super(groups);
    }

    @Override
    protected RecyclerView.ViewHolder onTitleVHCreate(ViewGroup parent) {
        return new TitleVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_title, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder onChildItemVHCreate(ViewGroup parent) {
        return new ChildItemVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_card, parent, false));
    }

    @Override
    protected void onTitleVHBind(RecyclerView.ViewHolder holder, Title title) {
        TitleVH titleVH = (TitleVH) holder;
        titleVH.tvTitle.setText(title.topTitle);
    }

    @Override
    protected void onChildItemVHBind(RecyclerView.ViewHolder holder, Title title, SampleBean sampleBean) {
        ChildItemVH childItemVH = (ChildItemVH) holder;
        childItemVH.tvCard.setText(sampleBean.content);
    }

    static final class TitleVH extends RecyclerView.ViewHolder {
        TitleVH(View v) {
            super(v);
            flTitle = (FrameLayout) v.findViewById(R.id.fl_group_title);
            tvTitle = (TextView) v.findViewById(R.id.tv_group_title);
        }
        FrameLayout flTitle;
        TextView tvTitle;
    }

    static final class ChildItemVH extends RecyclerView.ViewHolder {
        ChildItemVH(View v) {
            super(v);
            llCard = (LinearLayout) v.findViewById(R.id.ll_group_card);
            tvCard = (TextView) v.findViewById(R.id.tv_group_card);
        }
        LinearLayout llCard;
        TextView tvCard;
    }

    @Override
    protected void onLoadMore(Void please_make_your_adapter_class_as_abstract_class) {

    }

    @Override
    protected boolean hasMoreElements(Void let_activity_or_fragment_implement_these_methods) {
        return false;
    }
}
