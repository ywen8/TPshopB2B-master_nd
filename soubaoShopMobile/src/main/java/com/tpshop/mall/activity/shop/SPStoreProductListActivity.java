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
 * Description: 店铺产品列表
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.adapter.DividerGridItemDecoration;
import com.tpshop.mall.adapter.SPProductListSecAdapter;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.condition.SPProductCondition;
import com.tpshop.mall.http.shop.SPStoreRequest;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.model.shop.SPShopOrder;
import com.tpshop.mall.widget.SPProductFilterTabView;
import com.tpshop.mall.widget.swipetoloadlayout.OnLoadMoreListener;
import com.tpshop.mall.widget.swipetoloadlayout.OnRefreshListener;
import com.tpshop.mall.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import org.json.JSONObject;

import java.util.List;

public class SPStoreProductListActivity extends SPBaseActivity implements SPProductListSecAdapter.OnItemClickListener, OnRefreshListener,
        OnLoadMoreListener, SPProductFilterTabView.OnSortClickListener {

    int catId;                            //分类ID
    int storeId;
    String mHref;                         //请求URL
    String type = "";
    String mSort = "";                    //排序字段
    int mPageIndex = 1;
    String mOrder = "asc";                //asc:升序,desc:降序
    SPShopOrder mShopOrder;
    List<SPProduct> mProducts;
    SPProductListSecAdapter mAdapter;
    SPProductFilterTabView mFilterTabView;
    private static SPStoreProductListActivity instance;
    private SuperRefreshRecyclerView refreshRecyclerView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.setCustomerTitle(true, true, getString(R.string.title_store_product_list));
        super.onCreate(bundle);
        SPMobileApplication.getInstance().isFilterShow = false;
        setContentView(R.layout.store_product_list);
        super.init();
        instance = this;
    }

    public static SPStoreProductListActivity getInstance() {
        return instance;
    }

    @Override
    public void initSubViews() {
        mFilterTabView = (SPProductFilterTabView) findViewById(R.id.filter_tabv);
        refreshRecyclerView = (SuperRefreshRecyclerView) findViewById(R.id.super_recyclerview);
        View emptyView = findViewById(R.id.empty_lstv);
        refreshRecyclerView.setEmptyView(emptyView);
        refreshRecyclerView.init(new GridLayoutManager(this, 2), this, this);
        refreshRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));            //设置分割线
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(true);
        mAdapter = new SPProductListSecAdapter(this, this);
        refreshRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mFilterTabView.setOnSortClickListener(this);
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
        if (getIntent() != null) {
            storeId = getIntent().getIntExtra("storeId", 0);
            catId = getIntent().getIntExtra("catId", 0);
            type = getIntent().getStringExtra("type");
        }
        SPProductCondition condition = new SPProductCondition();
        if (storeId > 0)
            condition.storeID = storeId;
        if (catId > 0)
            condition.categoryID = catId;
        condition.type = type;
        condition.href = mHref;
        condition.page = mPageIndex;
        condition.orderby = mSort;
        condition.orderdesc = mOrder;
        showLoadingSmallToast();
        SPStoreRequest.getStoreGoodsList(condition, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                try {
                    if (response != null) {
                        JSONObject resultJson = (JSONObject) response;
                        if (resultJson.has("products"))
                            mProducts = (List<SPProduct>) resultJson.get("products");
                        if (resultJson.has("shopOrder"))
                            mShopOrder = (SPShopOrder) resultJson.get("shopOrder");
                        if (mProducts != null && mProducts.size() > 0) {
                            mAdapter.updateData(mProducts);
                            refreshRecyclerView.showData();
                        } else {
                            refreshRecyclerView.showEmpty();
                        }
                    }
                    refreshView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new SPFailuredListener(SPStoreProductListActivity.this) {
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
        SPProductCondition condition = new SPProductCondition();
        if (storeId > 0)
            condition.storeID = storeId;
        if (catId > 0)
            condition.categoryID = catId;
        condition.type = type;
        condition.href = mHref;
        condition.page = mPageIndex;
        condition.orderby = mSort;
        condition.orderdesc = mOrder;
        SPStoreRequest.getStoreGoodsList(condition, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                refreshRecyclerView.setLoadingMore(false);
                try {
                    if (response != null) {
                        JSONObject resultJson = (JSONObject) response;
                        mShopOrder = (SPShopOrder) resultJson.get("shopOrder");
                        List<SPProduct> results = (List<SPProduct>) resultJson.get("products");
                        if (results != null) {
                            mProducts.addAll(results);
                            mAdapter.updateData(mProducts);
                        }
                    }
                    refreshView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new SPFailuredListener(SPStoreProductListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                refreshRecyclerView.setLoadingMore(false);
                showFailedToast(msg);
                mPageIndex--;
            }
        });
    }

    @Override
    public void onItemClick(SPProduct product) {
        startupActivity(product.getGoodsID());
    }

    public void startupActivity(String goodsID) {
        Intent intent = new Intent(this, SPProductDetailActivity_.class);
        intent.putExtra("goodsID", goodsID);
        startActivity(intent);
    }

    @Override
    public void onFilterClick(SPProductFilterTabView.ProductSortType sortType) {
        switch (sortType) {
            case composite:
                if (mShopOrder != null)
                    mHref = mShopOrder.getDefaultHref();
                break;
            case salenum:
                if (mShopOrder != null)
                    mHref = mShopOrder.getSaleSumHref();
                break;
            case price:
                if (mShopOrder != null)
                    mHref = mShopOrder.getPriceHref();
                break;
        }
        refreshData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPMobileApplication.getInstance().isFilterShow = true;
    }

    public void refreshView() {
        if (mShopOrder != null && mShopOrder.getSortAsc() != null) {
            if (mShopOrder.getSortAsc().equalsIgnoreCase("desc"))
                mFilterTabView.setSort(true);
            else
                mFilterTabView.setSort(false);
        }
    }

}
