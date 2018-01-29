/*
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2099 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: 飞龙  wangqh01292@163.com
 * Date: @date 2015年10月26日 下午9:52:45
 * Description: 促销/抢购/秒杀 商品选项卡
 *
 * @version V1.0
 */
package com.tpshop.mall.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tpshop.mall.fragment.SPFlashSaleListFragment;
import com.tpshop.mall.model.shop.SPFlashTime;

import java.util.ArrayList;
import java.util.List;

public class SPFlashSaleTabAdapter extends FragmentPagerAdapter {

    private String[] titles;             //Titles的标识
    private List<SPFlashSaleListFragment> mFlashSaleListFragments;

    public SPFlashSaleTabAdapter(FragmentManager fm, List<SPFlashTime> flashTimes) {
        super(fm);
        mFlashSaleListFragments = new ArrayList<>();
        int count;
        if (flashTimes == null || (count = flashTimes.size()) < 1) return;
        titles = new String[count];
        for (int i = 0; i < count; i++) {
            SPFlashTime flashTime = flashTimes.get(i);
            SPFlashSaleListFragment fragment = SPFlashSaleListFragment.newInstants(flashTime);
            mFlashSaleListFragments.add(fragment);
            if (i == 0) {
                flashTime.setType(1);
                titles[i] = flashTime.getTitle() + "\n秒杀中";
            } else {
                titles[i] = flashTime.getTitle() + "\n即将开场";
                flashTime.setType(2);
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFlashSaleListFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    public String[] getTitles() {
        return titles;
    }

}
