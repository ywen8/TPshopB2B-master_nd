package com.tpshop.mall.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tpshop.mall.fragment.SPCommentCenterFragment;

public class SPOrderTabAdapter extends FragmentPagerAdapter {

    private static String[] orderTitles;                  //Titles的标识
    private SPCommentCenterFragment allFragment;          //全部评价
    private SPCommentCenterFragment hasFragment;          //已评价
    private SPCommentCenterFragment waitFragment;         //待晒单

    public SPOrderTabAdapter(FragmentManager fm, String[] orderTitles) {
        super(fm);
        SPOrderTabAdapter.orderTitles = orderTitles;
        allFragment = SPCommentCenterFragment.newInstance(0);
        waitFragment = SPCommentCenterFragment.newInstance(1);
        hasFragment = SPCommentCenterFragment.newInstance(2);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return allFragment;
        } else if (position == 1) {
            return waitFragment;
        } else {
            return hasFragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return orderTitles[position];
    }

    @Override
    public int getCount() {
        return orderTitles.length;
    }

    public void updateData() {
        allFragment.refreshData();
        waitFragment.refreshData();
        hasFragment.refreshData();
    }

}
