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
 * Date: @date 2015年10月20日 下午7:52:58
 * Description:Activity 积分商城
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.shop;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.adapter.ListDividerItemDecoration;
import com.tpshop.mall.adapter.SPIntegralListAdapter;
import com.tpshop.mall.entity.SPCommonListModel;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.shop.SPShopRequest;
import com.tpshop.mall.model.SPHomeBanners;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.utils.SPUtils;
import com.tpshop.mall.widget.SPListThreeFilterView;
import com.tpshop.mall.widget.swipetoloadlayout.OnLoadMoreListener;
import com.tpshop.mall.widget.swipetoloadlayout.OnRefreshListener;
import com.tpshop.mall.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by admin on 2016/7/1
 */
@EActivity(R.layout.integral_mall)
public class SPIntegralMallActivity extends SPBaseActivity implements SPIntegralListAdapter.OnItemClickListener, OnRefreshListener,
        OnLoadMoreListener, SPListThreeFilterView.OnSortClickListener {

    int pageIndex;
    String mSortType;                 //排序方式
    ImageView adImgv;
    SPHomeBanners mAd;                //广告
    List<SPProduct> products;
    SPIntegralListAdapter mAdapter;
    SPListThreeFilterView filterTbv;

    @ViewById(R.id.super_recyclerview)
    SuperRefreshRecyclerView refreshRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.title_integral));
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        View emptyView = findViewById(R.id.empty_lstv);
        refreshRecyclerView.setEmptyView(emptyView);
        refreshRecyclerView.init(new LinearLayoutManager(this), this, this);
        Drawable drawable = getResources().getDrawable(R.drawable.divider_grid_product_list);
        refreshRecyclerView.addItemDecoration(new ListDividerItemDecoration(drawable));             //设置分割线
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(true);
        adImgv = (ImageView) findViewById(R.id.group_ad_imgv);
        filterTbv = (SPListThreeFilterView) findViewById(R.id.filter_tabv);
        filterTbv.updateType(SPListThreeFilterView.FilterSortType.INTEGRAL_MALL);
        filterTbv.setOnSortClickListener(this);
        mAdapter = new SPIntegralListAdapter(this);
        refreshRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        adImgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.adTopage(SPIntegralMallActivity.this, mAd);
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
        pageIndex = 1;
        showLoadingSmallToast();
        SPShopRequest.integralMall(pageIndex, mSortType, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                SPCommonListModel commonListModel = (SPCommonListModel) response;
                if (commonListModel.products != null && commonListModel.products.size() > 0) {
                    products = commonListModel.products;
                    mAdapter.updateData(products);
                    refreshRecyclerView.showData();
                } else {
                    refreshRecyclerView.showEmpty();
                }
                if (commonListModel.ad != null) {
                    mAd = commonListModel.ad;
                    Glide.with(SPIntegralMallActivity.this).load(SPUtils.getImageUrl(mAd.getAdCode())).asBitmap().fitCenter()
                            .placeholder(R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(adImgv);
                    adImgv.setVisibility(View.VISIBLE);
                } else {
                    adImgv.setVisibility(View.GONE);
                }
            }
        }, new SPFailuredListener(SPIntegralMallActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                showFailedToast(msg);
            }
        });
    }

    public void loadMoreData() {
        pageIndex++;
        SPShopRequest.integralMall(pageIndex, mSortType, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                refreshRecyclerView.setLoadingMore(false);
                if (response != null) {
                    SPCommonListModel commonListModel = (SPCommonListModel) response;
                    if (commonListModel.products != null && commonListModel.products.size() > 0) {
                        List<SPProduct> list = commonListModel.products;
                        products.addAll(list);
                        mAdapter.updateData(products);
                    }
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                refreshRecyclerView.setLoadingMore(false);
                showFailedToast(msg);
                pageIndex--;
            }
        });
    }

    @Override
    public void onItemClick(String goodsId) {
        Intent intent = new Intent(this, SPProductDetailActivity_.class);
        intent.putExtra("goodsID", goodsId);
        startActivity(intent);
    }

    @Override
    public void onFilterClick(String sortType) {
        this.mSortType = sortType;
        refreshData();
    }

}
