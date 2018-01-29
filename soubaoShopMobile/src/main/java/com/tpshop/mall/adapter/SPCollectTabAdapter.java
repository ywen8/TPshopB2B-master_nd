package com.tpshop.mall.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tpshop.mall.fragment.SPGoodsCollectFragment;
import com.tpshop.mall.fragment.SPStoreCollectFragment;

public class SPCollectTabAdapter extends FragmentPagerAdapter {

    private SPGoodsCollectFragment goodsCollectFragment;                          //商品
    private SPStoreCollectFragment storeCollectFragment;                          //店铺
    private static String[] collectTitles = new String[]{"商品", "店铺"};          //Titles的标识

    public SPCollectTabAdapter(FragmentManager fm) {
        super(fm);
        goodsCollectFragment = new SPGoodsCollectFragment();
        storeCollectFragment = new SPStoreCollectFragment();
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return goodsCollectFragment;
        else
            return storeCollectFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return collectTitles[position];
    }

    @Override
    public int getCount() {
        return collectTitles.length;
    }

}
