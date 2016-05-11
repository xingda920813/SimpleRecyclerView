package com.xdandroid.sample;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.xdandroid.simplerecyclerview.SimpleRecyclerView;
import com.xdandroid.simplerecyclerview.SimpleSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SimpleRecyclerView recyclerView;
    private SimpleSwipeRefreshLayout swipeRefreshLayout;
    private SampleAdapter adapter;
    private List<SampleBean> list, addition;
    private Handler handler = new Handler();
    private static final int SPAN_SIZE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        TabLayout.Tab tab1 = tabLayout.newTab();
        tab1.setText("SECTION 1");
        tabLayout.addTab(tab1);
        TabLayout.Tab tab2 = tabLayout.newTab();
        tab2.setText("SECTION 2");
        tabLayout.addTab(tab2);
        TabLayout.Tab tab3 = tabLayout.newTab();
        tab3.setText("SECTION 3");
        tabLayout.addTab(tab3);
        swipeRefreshLayout = (SimpleSwipeRefreshLayout) findViewById(R.id.swipe_container);
        recyclerView = (SimpleRecyclerView) findViewById(R.id.recycler_view);

        setupSwipeRefreshLayout();
        setupRecyclerView();
        setupMockData();
    }

    private void setupSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(R.color.colorAccent);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {public void onRefresh() {handler.postDelayed(new Runnable() {public void run() {
            list = new ArrayList<>();
            for (int i = 0; i < 26; i++) {
                char c = (char) (65 + i);
                list.add(new SampleBean("Title " + String.valueOf(c), "Content " + String.valueOf(c)));
            }
            adapter.setList(list);
            swipeRefreshLayout.setRefreshing(false);}}, 1000);
        }});
        swipeRefreshLayout.setRefreshing(true);
    }

    private void setupRecyclerView() {
        //recyclerView.addItemDecoration(new SimpleRecyclerView.Divider(this, R.drawable.rv_divider, false, 0, 0, 0, 0));
        recyclerView.addItemDecoration(new SimpleRecyclerView.Divider(this, null, false, 0, 0, 0, 0));
        adapter = new SampleAdapter() {
            protected void onLoadMore(Void v) {handler.postDelayed(new Runnable() {public void run() {
                addAll(addition);
            }}, 1000);}
            protected boolean hasMoreElements(Void v) {
                return list.size() <= 260;
            }
        };
        adapter.setThreshold(7);
        adapter.setOnItemClickLitener(new SimpleRecyclerView.Adapter.OnItemClickLitener() {public void onItemClick(View view, int position) {
            Toast.makeText(MainActivity.this, "Clicked " + position, Toast.LENGTH_SHORT).show();
        }});
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this,SPAN_SIZE);
        //gridLayoutManager.setSpanSizeLookup(adapter.getSpanSizeLookup(SPAN_SIZE));
        //recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        /*handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setEmptyView(findViewById(R.id.empty_view));
                swipeRefreshLayout.setRefreshing(false);
            }
        },1000);*/
        /*handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.showErrorView(findViewById(R.id.error_view));
                //recyclerView.hideErrorView();
                swipeRefreshLayout.setRefreshing(false);
            }
        },1000);*/
    }

    private void setupMockData() {
        addition = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            char c = (char) (65 + i);
            addition.add(new SampleBean("Title " + String.valueOf(c), "Content " + String.valueOf(c)));
        }
        list = new ArrayList<>();
        handler.postDelayed(new Runnable() {public void run() {
            for (int i = 0; i < 26; i++) {
                char c = (char) (65 + i);
                list.add(new SampleBean("Title " + String.valueOf(c), "Content " + String.valueOf(c)));
            }
            swipeRefreshLayout.setRefreshing(false);
            adapter.setList(list);
        }}, 1000);
    }
}
