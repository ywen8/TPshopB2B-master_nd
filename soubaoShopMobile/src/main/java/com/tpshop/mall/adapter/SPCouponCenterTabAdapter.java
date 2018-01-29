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
 * Date: @date 2015年11月12日 下午8:08:13
 * Description: 优惠券领券中心 TabAdapter
 *
 * @version V1.0
 */
package com.tpshop.mall.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tpshop.mall.fragment.SPCouponCenterFragment;
import com.tpshop.mall.model.SPCategory;

import java.util.ArrayList;
import java.util.List;

public class SPCouponCenterTabAdapter extends FragmentPagerAdapter {

    private List<SPCategory> mCategorys;
    private List<SPCouponCenterFragment> mFragments;

    public SPCouponCenterTabAdapter(FragmentManager fm, List<SPCategory> categorys) {
        super(fm);
        this.mCategorys = categorys;
        mFragments = new ArrayList<>();
        if (this.mCategorys != null) {
            for (SPCategory category : mCategorys) {
                SPCouponCenterFragment centerFragment = SPCouponCenterFragment.newInstance(category);
                mFragments.add(centerFragment);
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (this.mCategorys == null) return "错误标题";
        return this.mCategorys.get(position).getName();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

}
