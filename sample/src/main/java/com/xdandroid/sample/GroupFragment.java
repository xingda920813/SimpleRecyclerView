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

public class GroupFragment extends Fragment {

    private SimpleRecyclerView mRecyclerView;
    private GroupRVAdapter mAdapter;
    private List<Group<Title, SampleBean>> mGroupList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (SimpleRecyclerView) view.findViewById(R.id.recycler_view);
        setupRecyclerView();
        initData();
    }

    private void setupRecyclerView() {
        mAdapter = new GroupRVAdapter();
        GridLayoutManager gridLM = new GridLayoutManager(getActivity(), 3);
        gridLM.setSpanSizeLookup(mAdapter.getSpanSizeLookup(3));
        mRecyclerView.setLayoutManager(gridLM);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            Title title = new Title("TopTitle " + i);
            List<SampleBean> sampleBeanList = new ArrayList<>();
            for (int j = 0; j <= i % 5; j++) {
                sampleBeanList.add(new SampleBean(SampleBean.TYPE_TEXT, "unused", "Item " + j, null, 0));
            }
            mGroupList.add(new Group<>(title, sampleBeanList));
        }
        mAdapter.setList(mGroupList);
    }
}
