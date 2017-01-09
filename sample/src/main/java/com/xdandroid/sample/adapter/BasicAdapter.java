package com.xdandroid.sample.adapter;

import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import com.xdandroid.sample.R;
import com.xdandroid.sample.bean.*;
import com.xdandroid.simplerecyclerview.Adapter;

import java.util.*;

public abstract class BasicAdapter extends Adapter {

    List<SampleBean> mSampleList;

    public void setList(List<SampleBean> sampleList) {
        mSampleList = sampleList;
        notifyDataSetChanged();
    }

    @Override
    protected RecyclerView.ViewHolder onViewHolderCreate(ViewGroup parent, int viewType) {
        if (viewType == SampleBean.TYPE_BANNER) {
            return new BannerVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false));
        } else {
            return new TextVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false));
        }
    }

    @Override
    protected void onViewHolderBind(RecyclerView.ViewHolder holder, int position, int viewType) {
        if (viewType == SampleBean.TYPE_BANNER) {
            BannerVH bannerVH = (BannerVH) holder;
            bannerVH.image.setImageResource(mSampleList.get(position).imageResId);
        } else {
            TextVH textVH = (TextVH) holder;
            textVH.title.setText(mSampleList.get(position).title);
            textVH.content.setText(mSampleList.get(position).content);
        }
    }

    @Override
    protected int getViewType(int position) {
        return mSampleList.get(position).type;
    }

    @Override
    protected int getCount() {
        return mSampleList != null ? mSampleList.size() : 0;
    }

    static final class TextVH extends RecyclerView.ViewHolder {
        TextVH(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.tv_title);
            content = (TextView) v.findViewById(R.id.tv_content);
        }

        TextView title, content;
    }

    static final class BannerVH extends RecyclerView.ViewHolder {
        BannerVH(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.iv_image);
        }
        ImageView image;
    }

    @Override
    protected int getItemSpanSizeForGrid(int position, int viewType, int spanSize) {
        return 1;
    }
}
