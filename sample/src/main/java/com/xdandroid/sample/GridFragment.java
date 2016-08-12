package com.xdandroid.sample;

import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.support.v7.widget.*;
import android.view.*;

import com.xdandroid.sample.adapter.*;
import com.xdandroid.sample.bean.*;
import com.xdandroid.simplerecyclerview.*;

import java.util.*;

public class GridFragment extends Fragment {

    private SimpleRecyclerView mRecyclerView;
    private GridAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grid, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (SimpleRecyclerView) view.findViewById(R.id.recycler_view);
        setupRecyclerView();
        initData();
    }

    private void setupRecyclerView() {
        mAdapter = new GridAdapter() {

            protected void onLoadMore(Void v) {
                new Handler().postDelayed(() -> {
                    List<SampleBean> moreSampleList = new ArrayList<>();
                    for (int j = 0; j < 2; j++)
                    for (int i = 0; i < 26; i++) {
                        char c = (char) (65 + i);
                        moreSampleList.add(new SampleBean(SampleBean.TYPE_TEXT, "Title " + String.valueOf(c), "Content " + String.valueOf(c), null, 0));
                    }
                    addAll(moreSampleList);
                }, 1777);
            }

            protected boolean hasMoreElements(Void v) {
                return list.size() <= 666;
            }
        };
        GridLayoutManager gridLM = new GridLayoutManager(getActivity(), 3);
        gridLM.setSpanSizeLookup(mAdapter.getSpanSizeLookup(3));
        mRecyclerView.setLayoutManager(gridLM);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        List<SampleBean> sampleList = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            char c = (char) (65 + i);
            sampleList.add(new SampleBean(SampleBean.TYPE_TEXT, "Title " + String.valueOf(c), "Content " + String.valueOf(c), null, 0));
        }
        mAdapter.setList(sampleList);
    }
}
