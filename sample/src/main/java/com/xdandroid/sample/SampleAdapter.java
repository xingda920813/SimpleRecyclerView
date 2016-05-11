package com.xdandroid.sample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xdandroid.simplerecyclerview.SimpleRecyclerView;

import java.util.List;

/**
 * Created by XingDa on 2016/5/11.
 */
public abstract class SampleAdapter extends SimpleRecyclerView.Adapter<SampleBean> {

    @Override
    protected RecyclerView.ViewHolder onViewHolderCreate(List<SampleBean> list, ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false));
    }

    @Override
    protected void onViewHolderBind(List<SampleBean> list, RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).title.setText(list.get(holder.getAdapterPosition()).getTitle());
        ((ViewHolder)holder).content.setText(list.get(holder.getAdapterPosition()).getContent());
    }

    @Override
        protected int getViewType(List<SampleBean> list, int position) {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);
        }
        TextView title,content;
    }
}
