package com.xdandroid.sample.adapter;

import android.graphics.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import com.xdandroid.sample.R;
import com.xdandroid.sample.bean.*;
import com.xdandroid.simplerecyclerview.Adapter;
import com.xdandroid.simplerecyclerview.*;

import java.util.*;

public abstract class BasicHeaderAdapter extends Adapter {

    List<SampleBean> mSampleList;
    public static final int TYPE_HEADER = 300;

    public void setList(List<SampleBean> sampleList) {
        mSampleList = sampleList;
        notifyDataSetChanged();
    }

    @Override
    protected RecyclerView.ViewHolder onViewHolderCreate(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            TextView textView = new TextView(parent.getContext());
            textView.setId(android.R.id.text1);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dp2px(parent.getContext(), 32)));
            return new HeaderVH(textView);
        }
        if (viewType == SampleBean.TYPE_BANNER) {
            return new BannerVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false));
        } else {
            return new TextVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false));
        }
    }

    @Override
    protected void onViewHolderBind(RecyclerView.ViewHolder holder, int position, int viewType) {
        if (viewType == TYPE_HEADER) {
            HeaderVH headerVH = (HeaderVH) holder;
            headerVH.textView.setGravity(Gravity.CENTER);
            headerVH.textView.setText("I am Header.");
            headerVH.textView.setTextSize(16);
            headerVH.textView.setTextColor(Color.BLACK);
            return;
        }
        //跟getViewType中一样，对position进行修正
        int newPosition = position - 1;
        if (viewType == SampleBean.TYPE_BANNER) {
            BannerVH bannerVH = (BannerVH) holder;
            bannerVH.image.setImageResource(mSampleList.get(newPosition).imageResId);
        } else {
            TextVH textVH = (TextVH) holder;
            textVH.title.setText(mSampleList.get(newPosition).title);
            textVH.content.setText(mSampleList.get(newPosition).content);
        }
    }

    @Override
    protected int getViewType(int position) {
        //第0个Item是Header
        if (position == 0) return TYPE_HEADER;
        //添加Header后，RecyclerView中的第1个Item实际上是List中的第0个Object
        int newPosition = position - 1;
        return mSampleList.get(newPosition).type;
    }

    @Override
    protected int getCount() {
        //不含Header的count数
        int countWithoutHeader = mSampleList != null ? mSampleList.size() : 0;
        //包含Header后
        return countWithoutHeader + 1;
    }

    static final class HeaderVH extends RecyclerView.ViewHolder {
        HeaderVH(View v) {
            super(v);
            textView = (TextView) v.findViewById(android.R.id.text1);
        }
        TextView textView;
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
