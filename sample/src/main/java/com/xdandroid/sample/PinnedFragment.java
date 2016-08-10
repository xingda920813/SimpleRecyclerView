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

/**
 * Created by xingda on 16-8-10.
 */

public class PinnedFragment extends Fragment {

    private SimpleRecyclerView mRecyclerView;
    private PinnedAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pinned, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (SimpleRecyclerView) view.findViewById(R.id.recycler_view);
        setupRecyclerView();
        initData();
    }

    private void setupRecyclerView() {
        mRecyclerView.addItemDecoration(new Divider(getActivity(), null, false, 0, 0, 0, 0));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new PinnedAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        List<SampleBean> sampleList = new ArrayList<>();
        for (int i = 1; i <= 105; i++) {
            sampleList.add(new SampleBean("Title " + i, "Content " + i));
        }
        mAdapter.setList(sampleList);
    }
}
