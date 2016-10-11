package com.xdandroid.sample.adapter;

import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import com.xdandroid.sample.R;
import com.xdandroid.sample.bean.*;
import com.xdandroid.simplerecyclerview.*;

import java.util.*;

public class GroupRVAdapter extends GroupAdapter<Title, SampleBean> {

    @Override
    protected RecyclerView.ViewHolder onTitleVHCreate(ViewGroup parent) {
        return new TitleVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_title, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder onChildItemVHCreate(ViewGroup parent) {
        return new ChildItemVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_card, parent, false));
    }

    /**
     * 详见下方参数说明.
     * @param holder TitleViewHolder.
     * @param adapterPos Title 在 Adapter 中的绝对位置 ( = holder.getAdapterPosition()).
     * @param title Title.
     * @param titleOrderInAllTitles 当前 Title 所在 Group 在 List[Group] 中的相对位置.
     */
    @Override
    protected void onTitleVHBind(RecyclerView.ViewHolder holder, int adapterPos, Title title, int titleOrderInAllTitles) {
        TitleVH titleVH = (TitleVH) holder;
        titleVH.tvTitle.setText(title.topTitle);
    }

    /**
     * 详见下方参数说明.
     * @param holder ChildItemViewHolder.
     * @param adapterPos ChildItem 在 Adapter 中的绝对位置 ( = holder.getAdapterPosition()).
     * @param title Title.
     * @param titleOrderInAllTitles 当前 ChildItem 所在 Group 在 List[Group] 中的相对位置.
     * @param childOrderInCurrentGroup 当前 ChildItem 在 Group 中的相对位置.
     *                                 (第 1 个 ChildItem 的 childOrder 为 0, 即 childOrder 不包括 Title 占的位置)
     */
    @Override
    protected void onChildItemVHBind(RecyclerView.ViewHolder holder, int adapterPos, Title title, int titleOrderInAllTitles, SampleBean sampleBean, int childOrderInCurrentGroup) {
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
