package com.xdandroid.sample;

import android.os.*;
import android.support.design.widget.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;

import com.xdandroid.sample.util.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ViewPager viewPager = ((ViewPager) findViewById(R.id.view_pager));
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new BasicFragment();
                    case 1:
                        return new GridFragment();
                    case 2:
                        return new PinnedFragment();
                    case 3:
                        return new GroupFragment();
                }
                return new Fragment();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "基本使用";
                    case 1:
                        return "多列布局";
                    case 2:
                        return "固定头部";
                    case 3:
                        return "分组显示";
                }
                return "";
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                return new FragmentUtils().instantiateItem(getSupportFragmentManager(),
                        container, getItem(position), getPageTitle(position).toString());
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                new FragmentUtils().destroyItem(getSupportFragmentManager(), object);
            }
        });
        ((TabLayout) findViewById(R.id.tabs)).setupWithViewPager(viewPager);
    }
}
