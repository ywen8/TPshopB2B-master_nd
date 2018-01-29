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
 * Description:Activity 团购列表
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.adapter.DividerGridItemDecoration;
import com.tpshop.mall.adapter.SPGroupListAdapter;
import com.tpshop.mall.entity.SPCommonListModel;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.shop.SPShopRequest;
import com.tpshop.mall.model.SPHomeBanners;
import com.tpshop.mall.model.shop.SPGroup;
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
@EActivity(R.layout.group_list)
public class SPGroupListActivity extends SPBaseActivity implements OnRefreshListener, OnLoadMoreListener, SPGroupListAdapter.OnItemClickListener,
        SPListThreeFilterView.OnSortClickListener {

    int pageIndex;
    List<SPGroup> groups;
    String mSortType = "";
    SPHomeBanners mAdModel;
    SPGroupListAdapter mAdapter;
    private ImageView groupAdImgv;
    SPListThreeFilterView filterTbv;

    @ViewById(R.id.super_recyclerview)
    SuperRefreshRecyclerView refreshRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.title_group));
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
        refreshRecyclerView.init(new GridLayoutManager(this, 2), this, this);
        refreshRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));       //设置分割线
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(true);
        groupAdImgv = (ImageView) findViewById(R.id.group_ad_imgv);
        filterTbv = (SPListThreeFilterView) findViewById(R.id.filter_tabv);
        filterTbv.updateType(SPListThreeFilterView.FilterSortType.GROUP);
        filterTbv.setOnSortClickListener(this);
        mAdapter = new SPGroupListAdapter(this);
        refreshRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        groupAdImgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.adTopage(SPGroupListActivity.this, mAdModel);
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
        SPShopRequest.groupList(pageIndex, mSortType, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                SPCommonListModel listModel = (SPCommonListModel) response;
                if (listModel.groups != null)
                    groups = listModel.groups;
                else
                    groups = null;
                if (listModel.ad != null) {
                    mAdModel = listModel.ad;
                    Glide.with(SPGroupListActivity.this).load(SPUtils.getImageUrl(mAdModel.getAdCode())).asBitmap().fitCenter()
                            .placeholder(R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(groupAdImgv);
                    groupAdImgv.setVisibility(View.VISIBLE);
                } else {
                    groupAdImgv.setVisibility(View.GONE);
                }
                if (groups == null || groups.size() < 1)
                    refreshRecyclerView.showEmpty();
                else
                    refreshRecyclerView.showData();
                mAdapter.updateData(groups);
            }
        }, new SPFailuredListener(SPGroupListActivity.this) {
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
        SPShopRequest.groupList(pageIndex, mSortType, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                refreshRecyclerView.setLoadingMore(false);
                SPCommonListModel listModel = (SPCommonListModel) response;
                if (listModel.groups != null && listModel.groups.size() > 0) {
                    List<SPGroup> newGroups = listModel.groups;
                    groups.addAll(newGroups);
                    mAdapter.updateData(groups);
                }
            }
        }, new SPFailuredListener(SPGroupListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                refreshRecyclerView.setLoadingMore(false);
                showFailedToast(msg);
                pageIndex--;
            }
        });
    }

    @Override
    public void onItemClick(String goodsID, String itemId) {
        Intent intent = new Intent(this, SPProductDetailActivity_.class);
        intent.putExtra("goodsID", goodsID);
        if (!SPStringUtils.isEmpty(itemId)) intent.putExtra("itemID", itemId);
        startActivity(intent);
    }

    @Override
    public void onFilterClick(String sortType) {
        this.mSortType = sortType;
        showLoadingSmallToast();
        refreshData();
    }

}
