package com.xdandroid.sample.util;

import android.support.v4.app.*;
import android.view.*;

public class FragmentUtils {

    public Fragment replace(FragmentManager fm, int flResId, Fragment getInstance) {
        String fragmentName = getInstance.getClass().getSimpleName();
        FragmentTransaction transaction = fm.beginTransaction();
        if (fm.getFragments() != null) {
            for (int i = 0; i < fm.getFragments().size(); i++) {
                if (fm.getFragments().get(i) != null) {
                    transaction.hide(fm.getFragments().get(i));
                }
            }
        }
        Fragment fragment = fm.findFragmentByTag(fragmentName);
        if (fragment == null) {
            fragment = getInstance;
            transaction.add(flResId, fragment, fragmentName).commitAllowingStateLoss();
        } else {
            transaction.show(fragment).commitAllowingStateLoss();
        }
        return fragment;
    }

    public Fragment instantiateItem(FragmentManager fm, ViewGroup container, Fragment getItem, String getPageTitle) {
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag(getPageTitle);
        if (fragment == null) {
            fragment = getItem;
            transaction.add(container.getId(), fragment, getPageTitle).commitAllowingStateLoss();
        } else {
            transaction.show(fragment).commitAllowingStateLoss();
        }
        return fragment;
    }

    public void destroyItem(FragmentManager fm, Object object) {
        fm.beginTransaction().hide((Fragment) object).commitAllowingStateLoss();
    }
}
