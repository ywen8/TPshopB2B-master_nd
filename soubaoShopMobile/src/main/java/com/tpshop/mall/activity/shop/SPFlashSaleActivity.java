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
 * Description:促销/抢购 活动列表
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.shop;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.adapter.SPFlashSaleTabAdapter;
import com.tpshop.mall.entity.SPCommonListModel;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.shop.SPShopRequest;
import com.tpshop.mall.model.SPHomeBanners;
import com.tpshop.mall.model.shop.SPFlashTime;
import com.tpshop.mall.utils.SPUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.SimpleViewPagerDelegate;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.flash_sale)
public class SPFlashSaleActivity extends SPBaseActivity {

    private SPHomeBanners mAd;
    List<SPFlashTime> mFlashTimes;
    public static String[] mTitles;

    @ViewById(R.id.magic_indicator)
    MagicIndicator mMagicIndicator;

    @ViewById(R.id.view_pager)
    ViewPager mViewPager;

    @ViewById(R.id.group_ad_imgv)
    ImageView mAdImgv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.title_flash_sale));
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {
        showLoadingSmallToast();
        SPShopRequest.getFlashSaleTime(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                SPCommonListModel listModel = (SPCommonListModel) response;
                if (listModel.flashTimes != null) {
                    mFlashTimes = listModel.flashTimes;
                    dealTitle();
                } else {
                    showToast("抢购数据为空!");
                }
                if (listModel.ad != null) {
                    mAd = listModel.ad;
                    Glide.with(SPFlashSaleActivity.this).load(SPUtils.getImageUrl(mAd.getAdCode())).placeholder(R.drawable.icon_product_null)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mAdImgv);
                    mAdImgv.setVisibility(View.VISIBLE);
                } else {
                    mAdImgv.setVisibility(View.GONE);
                }
            }
        }, new SPFailuredListener(SPFlashSaleActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showFailedToast(msg);
            }
        });
    }

    public void dealTitle() {
        if (mFlashTimes == null || mFlashTimes.size() < 1) return;
        SPFlashSaleTabAdapter fragPagerAdapter = new SPFlashSaleTabAdapter(getSupportFragmentManager(), mFlashTimes);
        mViewPager.setAdapter(fragPagerAdapter);
        mViewPager.setOffscreenPageLimit(mFlashTimes.size());
        mTitles = fragPagerAdapter.getTitles();
        final CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setScrollPivotX(0.15f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitles == null ? 0 : mTitles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(mTitles[index]);
                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.sub_title));
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.light_red));
                simplePagerTitleView.setTextSize(12);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setLineHeight(0);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setYOffset(UIUtil.dip2px(context, 3));
                indicator.setColors(getResources().getColor(R.color.light_red));
                return indicator;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);
        mMagicIndicator.setDelegate(new SimpleViewPagerDelegate(mViewPager));
    }

}
