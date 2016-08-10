package com.xdandroid.sample.adapter;

import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import com.xdandroid.sample.R;
import com.xdandroid.sample.bean.*;
import com.xdandroid.simplerecyclerview.*;

import java.util.*;

public abstract class SimpleAdapter extends SingleViewTypeAdapter<SampleBean> {

    @Override
    protected RecyclerView.ViewHolder onViewHolderCreate(List<SampleBean> list, ViewGroup parent) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple, parent, false));
    }

    @Override
    protected void onViewHolderBind(List<SampleBean> list, RecyclerView.ViewHolder holder, int position) {
        ((VH) holder).title.setText(list.get(holder.getAdapterPosition()).title);
        ((VH) holder).content.setText(list.get(holder.getAdapterPosition()).content);
    }

    static final class VH extends RecyclerView.ViewHolder {
        VH(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            content = (TextView) v.findViewById(R.id.content);
        }

        TextView title, content;
    }

    @Override
    protected int getItemSpanSizeForGrid(int position, int viewType, int spanSize) {
        return 1;
    }
}
