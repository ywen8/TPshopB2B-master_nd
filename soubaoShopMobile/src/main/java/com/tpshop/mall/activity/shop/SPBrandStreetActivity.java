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
 * Date: @date 2015年11月3日 下午10:04:49
 * Description: 品牌街
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.shop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.headerfooter.songhang.library.SmartRecyclerAdapter;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.adapter.DividerGridItemDecoration;
import com.tpshop.mall.adapter.ItemRecommendAdapter;
import com.tpshop.mall.adapter.SPBrandStreetAdapter;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.entity.SPCommonListModel;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.shop.SPStoreRequest;
import com.tpshop.mall.model.SPHomeBanners;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.model.shop.SPBrand;
import com.tpshop.mall.utils.SPShopUtils;
import com.tpshop.mall.utils.SPUtils;
import com.tpshop.mall.widget.swipetoloadlayout.OnLoadMoreListener;
import com.tpshop.mall.widget.swipetoloadlayout.OnRefreshListener;
import com.tpshop.mall.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * @author 飞龙
 */
@EActivity(R.layout.brand_street)
public class SPBrandStreetActivity extends SPBaseActivity implements OnRefreshListener, OnLoadMoreListener,
        SPBrandStreetAdapter.OnBrandItemClickListener {

    View mHeaderView;
    SPHomeBanners mAd;                       //广告
    int mPageIndex = 1;
    ImageView mBrandImgv;
    List<SPBrand> mBrands;
    public List<SPProduct> mHots;            //推荐商品
    SPBrandStreetAdapter mAdapter;
    ConvenientBanner mRecommandCbanner;
    private RecommendReceiver mReceiver;
    SmartRecyclerAdapter mSmartRecyclerAdapter;

    @ViewById(R.id.super_recyclerview)
    SuperRefreshRecyclerView refreshRecyclerView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.setCustomerTitle(true, true, getString(R.string.title_brand_street));
        super.onCreate(bundle);
        IntentFilter filter = new IntentFilter(SPMobileConstants.ACTION_GOODS_RECOMMEND);
        mReceiver = new RecommendReceiver();
        registerReceiver(mReceiver, filter);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.brand_street_header, null);
        mBrandImgv = (ImageView) mHeaderView.findViewById(R.id.brand_ad_imgv);
        mRecommandCbanner = (ConvenientBanner) mHeaderView.findViewById(R.id.street_recommand_cbanner);
        mRecommandCbanner.setPageIndicator(new int[]{R.mipmap.index_white, R.mipmap.index_red});
        mRecommandCbanner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        mRecommandCbanner.setPageIndicator(new int[]{R.drawable.shape_item_index_white, R.drawable.shape_item_index_red});
        mRecommandCbanner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        refreshRecyclerView.init(new GridLayoutManager(this, 4), this, this);
        Drawable drawable = getResources().getDrawable(R.drawable.divider_grid_normal_activity);
        refreshRecyclerView.addItemDecoration(new DividerGridItemDecoration(drawable));             //设置分割线
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(true);
        mAdapter = new SPBrandStreetAdapter(this, this);
        mSmartRecyclerAdapter = new SmartRecyclerAdapter(mAdapter);
        mSmartRecyclerAdapter.setHeaderView(mHeaderView);
        refreshRecyclerView.setAdapter(mSmartRecyclerAdapter);
    }

    @Override
    public void initEvent() {
        mBrandImgv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SPUtils.adTopage(SPBrandStreetActivity.this, mAd);
            }
        });
    }

    @Override
    public void initData() {
        refreshData();
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    @Override
    public void onLoadMore() {
        loadMoreData();
    }

    public void refreshData() {
        mPageIndex = 1;
        showLoadingSmallToast();
        SPStoreRequest.brandStreet(mPageIndex, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                SPCommonListModel commonListModel = (SPCommonListModel) response;
                if (commonListModel.brands != null) {
                    mBrands = commonListModel.brands;
                    mAdapter.updateData(mBrands);
                }
                if (commonListModel.products != null) {       //推荐商品
                    mHots = commonListModel.products;
                    setRecommendGoods();
                }
                if (commonListModel.ad != null) {       //广告
                    mAd = commonListModel.ad;
                    Glide.with(SPBrandStreetActivity.this).load(SPUtils.getImageUrl(mAd.getAdCode())).asBitmap().fitCenter()
                            .placeholder(R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mBrandImgv);
                    mBrandImgv.setVisibility(View.VISIBLE);
                } else {
                    mBrandImgv.setVisibility(View.GONE);
                }
            }
        }, new SPFailuredListener(SPBrandStreetActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                showFailedToast(msg);
            }
        });
    }

    public void loadMoreData() {
        mPageIndex++;
        SPStoreRequest.brandStreet(mPageIndex, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                refreshRecyclerView.setLoadingMore(false);
                SPCommonListModel commonListModel = (SPCommonListModel) response;
                if (commonListModel.brands != null) {
                    mBrands.addAll(commonListModel.brands);
                    mAdapter.updateData(mBrands);
                }
            }
        }, new SPFailuredListener(SPBrandStreetActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                refreshRecyclerView.setLoadingMore(false);
                showFailedToast(msg);
            }
        });
    }

    /**
     * 设置推荐商品
     */
    public void setRecommendGoods() {
        List<List<SPProduct>> handledData = SPShopUtils.handleRecommendGoods(mHots, 3);
        mRecommandCbanner.setManualPageable(handledData.size() != 1);        //设置如果只有一组数据时不能滑动
        mRecommandCbanner.setCanLoop(handledData.size() != 1);
        mRecommandCbanner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new ItemRecommendAdapter();
            }
        }, handledData);
    }

    @Override
    public void onItemClick(String brandId) {
        Intent intent = new Intent(SPBrandStreetActivity.this, SPProductListActivity.class);
        intent.putExtra("brand_id", Integer.parseInt(brandId));
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    class RecommendReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String goodsId = SPMobileApplication.getInstance().data;
            if (intent.getAction().equals(SPMobileConstants.ACTION_GOODS_RECOMMEND)) {
                Intent detailIntent = new Intent(SPBrandStreetActivity.this, SPProductDetailActivity_.class);
                detailIntent.putExtra("goodsId", goodsId);
                startActivity(detailIntent);
            }
        }
    }

}
