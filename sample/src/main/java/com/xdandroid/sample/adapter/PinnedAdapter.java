package com.xdandroid.sample.adapter;

import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import com.xdandroid.sample.R;
import com.xdandroid.sample.bean.*;
import com.xdandroid.simplerecyclerview.*;
import com.xdandroid.simplerecyclerview.stickyheaders.*;

import java.util.*;

public abstract class PinnedAdapter extends SingleViewTypeAdapter<SampleBean> implements StickyRecyclerHeadersAdapter<PinnedAdapter.HeaderVH> {

    @Override
    protected RecyclerView.ViewHolder onViewHolderCreate(List<SampleBean> list, ViewGroup parent) {
        return new ItemVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false));
    }

    /**
     * @param position the adapter position
     */
    @Override
    protected void onViewHolderBind(List<SampleBean> list, RecyclerView.ViewHolder holder, int position) {
        ItemVH itemVH = (ItemVH) holder;
        itemVH.tvTitle.setText(list.get(position).title);
        itemVH.tvContent.setText(list.get(position).content);
        //返回当前元素所在 Group 的 headerId，即当前元素属于哪一组
        //long whichGroup = getHeaderId(position);
    }

    //可在 onViewHolderBind 和 onBindHeaderViewHolder 中随时调用此方法获得当前 adapterPosition 对应的 headerId.
    @Override
    public long getHeaderId(int position) {
        //这个例子是把 0 - 9 的元素作为一组，10 - 19 的元素作为一组，以此类推，每 10 个元素属于相同的一组
        //实际使用时完全可以根据 position 和 List.get(position) 得到的数据，判断里面的字段，灵活决定哪些元素属于哪些组
        return position / 10;
    }

    @Override
    public HeaderVH onCreateHeaderViewHolder(ViewGroup parent) {
        return new HeaderVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false));
    }

    /**
     * @param position the adapter position
     */
    @Override
    public void onBindHeaderViewHolder(HeaderVH holder, int position) {
        holder.tvHeader.setText("Group " + getHeaderId(position) /* 当前 header 是哪一组的 header */ +
                ": Adapter Position " + String.valueOf(position + 1) + " - " + String.valueOf(position + 10));
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
    protected int getItemSpanSizeForGrid(int position, int viewType, int spanCount) {
        return 1;
    }
}
