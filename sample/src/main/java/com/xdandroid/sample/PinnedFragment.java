package com.xdandroid.sample;

import android.graphics.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.support.v7.widget.*;
import android.view.*;

import com.xdandroid.sample.adapter.*;
import com.xdandroid.sample.bean.*;
import com.xdandroid.simplerecyclerview.*;

import java.util.*;

public class PinnedFragment extends Fragment {

    SimpleSwipeRefreshLayout mSwipeContainer;
    SimpleRecyclerView mRecyclerView;
    PinnedAdapter mAdapter;
    List<SampleBean> mSampleList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pinned, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mSwipeContainer = (SimpleSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mRecyclerView = (SimpleRecyclerView) view.findViewById(R.id.recycler_view);
        setupSwipeContainer();
        mRecyclerView.postDelayed(() -> {
            setupRecyclerView();
            initData();
        }, 1500);
    }

    void setupSwipeContainer() {
        mSwipeContainer.setColorSchemeResources(R.color.colorAccent);
        mSwipeContainer.setOnRefreshListener(this::initData);
        mSwipeContainer.setRefreshing(true);
    }

    void setupRecyclerView() {
        mRecyclerView.addItemDecoration(new Divider(
                //分割线宽1dp
                UIUtils.dp2px(getActivity(), 1),
                //分割线颜色#DDDDDD
                Color.parseColor("#DDDDDD"),
                false,
                //分割线左侧留出20dp的空白，不绘制
                UIUtils.dp2px(getActivity(), 20), 0, 0, 0));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new PinnedAdapter() {
            @Override
            protected void onLoadMore(Void v) {
                mRecyclerView.postDelayed(() -> {
                    for (int i = 1; i <= 105; i++) mSampleList.add(new SampleBean(SampleBean.TYPE_TEXT, "Title " + i, "Content " + i, null, 0));
                    setList(mSampleList);
                    mSwipeContainer.setRefreshing(false);
                }, 1500);
            }

            @Override
            protected boolean hasMoreElements(Void v) {
                return mSampleList != null && mSampleList.size() <= 666;
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    void initData() {
        mSampleList = new ArrayList<>();
        for (int i = 1; i <= 105; i++) mSampleList.add(new SampleBean(SampleBean.TYPE_TEXT, "Title " + i, "Content " + i, null, 0));
        mAdapter.setList(mSampleList);
        mSwipeContainer.setRefreshing(false);
    }
}
