package com.xdandroid.sample.adapter;

import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import com.xdandroid.sample.R;
import com.xdandroid.sample.bean.*;
import com.xdandroid.simplerecyclerview.*;

import java.util.*;

public abstract class GridAdapter extends SingleViewTypeAdapter<SampleBean> {

    @Override
    protected RecyclerView.ViewHolder onViewHolderCreate(List<SampleBean> list, ViewGroup parent) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false));
    }

    @Override
    protected void onViewHolderBind(List<SampleBean> list, RecyclerView.ViewHolder holder, int position) {
        VH vh = (VH) holder;
        vh.tvGrid.setText(list.get(position).title);
        vh.cardGrid.setOnClickListener(v -> Toast.makeText(v.getContext(), "Clicked " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show());
    }

    static final class VH extends RecyclerView.ViewHolder {
        VH(View v) {
            super(v);
            cardGrid = (CardView) v.findViewById(R.id.card_grid);
            tvGrid = (TextView) v.findViewById(R.id.tv_grid);
        }
        CardView cardGrid;
        TextView tvGrid;
    }

    @Override
    protected int getItemSpanSizeForGrid(int position, int viewType, int spanCount) {
        return 1;
    }
}
