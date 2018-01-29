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
 * Description:产品列表搜索结果页
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.activity.common.SPSearchCommonActivity_;
import com.tpshop.mall.adapter.DividerGridItemDecoration;
import com.tpshop.mall.adapter.SPProductListSecAdapter;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.fragment.SPProductListFilterFragment;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.shop.SPShopRequest;
import com.tpshop.mall.model.SPCategory;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.model.shop.SPShopOrder;
import com.tpshop.mall.widget.SPProductFilterTabView;
import com.tpshop.mall.widget.swipetoloadlayout.OnLoadMoreListener;
import com.tpshop.mall.widget.swipetoloadlayout.OnRefreshListener;
import com.tpshop.mall.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import org.json.JSONObject;

import java.util.List;

/**
 * @author 飞龙
 */
public class SPProductListSearchResultActivity extends SPBaseActivity implements SPProductListSecAdapter.OnItemClickListener, OnRefreshListener,
        OnLoadMoreListener, SPProductFilterTabView.OnSortClickListener {

    String mHref;                  //请求URL
    String mSearchkey;             //搜索关键字
    int mPageIndex = 1;
    ImageView backImgv;
    TextView priceTxtv;
    EditText searchText;           //搜索文本框
    TextView salenumTxtv;
    SPCategory mCategory;          //分类
    TextView syntheisTxtv;
    SPShopOrder mShopOrder;        //排序实体
    private View emptyView;
    List<SPProduct> mProducts;
    DrawerLayout mDrawerLayout;
    SPProductListSecAdapter mAdapter;
    SPProductFilterTabView mFilterTabView;
    SPProductListFilterFragment mFilterFragment;
    SuperRefreshRecyclerView refreshRecyclerView;
    private static SPProductListSearchResultActivity instance;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SPMobileConstants.MSG_CODE_FILTER_CHANGE_ACTION:
                    if (msg.obj != null) {
                        mHref = msg.obj.toString();
                        refreshData();
                        mDrawerLayout.closeDrawers();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.product_list);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.product_list_header);
        Intent intent = getIntent();
        if (intent != null)
            mCategory = (SPCategory) intent.getSerializableExtra("category");
        super.init();
        instance = this;
    }

    public static SPProductListSearchResultActivity getInstance() {
        return instance;
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchText.setFocusable(false);
        searchText.setFocusableInTouchMode(false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        startupSearch(intent);
    }

    @Override
    public void initSubViews() {
        mFilterTabView = (SPProductFilterTabView) findViewById(R.id.filter_tabv);
        mFilterTabView.setOnSortClickListener(this);
        refreshRecyclerView = (SuperRefreshRecyclerView) findViewById(R.id.super_recyclerview);
        emptyView = findViewById(R.id.empty_lstv);
        refreshRecyclerView.init(new GridLayoutManager(this, 2), this, this);
        refreshRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));      //设置分割线
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(true);
        mAdapter = new SPProductListSecAdapter(this, this);
        refreshRecyclerView.setAdapter(mAdapter);
        syntheisTxtv = (TextView) findViewById(R.id.sort_button_synthesis);
        salenumTxtv = (TextView) findViewById(R.id.sort_button_salenum);
        priceTxtv = (TextView) findViewById(R.id.sort_button_price);
        searchText = (EditText) findViewById(R.id.search_edtv);
        backImgv = (ImageView) findViewById(R.id.title_back_imgv);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        SPMobileApplication.getInstance().productListType = 2;
        mFilterFragment = (SPProductListFilterFragment) getSupportFragmentManager().findFragmentById(R.id.right_rlayout);
    }

    @Override
    public boolean isDealBack() {
        return true;
    }

    @Override
    public void dealBack() {
        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT))
            mDrawerLayout.closeDrawers();
        else
            finish();
    }

    @Override
    public void initEvent() {
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SPProductListSearchResultActivity.this, SPSearchCommonActivity_.class);
                if (!SPStringUtils.isEmpty(mSearchkey))
                    intent.putExtra("searchKey", mSearchkey);
                SPProductListSearchResultActivity.this.startActivity(intent);
            }
        });
        backImgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealBack();
            }
        });
    }

    @Override
    public void initData() {
        startupSearch(getIntent());
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
        SPShopRequest.searchResultProductListWithPage(mPageIndex, mSearchkey, mHref, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object data) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                try {
                    mDataJson = (JSONObject) data;
                    if (mDataJson != null && mDataJson.has("product")) {
                        mProducts = (List<SPProduct>) mDataJson.get("product");
                        mShopOrder = (SPShopOrder) mDataJson.get("order");
                        if (mProducts != null && mProducts.size() > 0) {
                            mAdapter.updateData(mProducts);
                            refreshRecyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        } else {
                            refreshRecyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        }
                        if (SPProductListFilterFragment.getInstance(mHandler) != null)
                            SPProductListFilterFragment.getInstance(mHandler).setDataSource(mDataJson);
                    } else {
                        mAdapter.updateData(null);
                        showToast("没有数据");
                        refreshRecyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                refreshView();
            }
        }, new SPFailuredListener(SPProductListSearchResultActivity.this) {
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
        SPShopRequest.searchResultProductListWithPage(mPageIndex, mSearchkey, mHref, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object data) {
                refreshRecyclerView.setLoadingMore(false);
                try {
                    mDataJson = (JSONObject) data;
                    if (mDataJson != null) {
                        mShopOrder = (SPShopOrder) mDataJson.get("order");
                        List<SPProduct> results = (List<SPProduct>) mDataJson.get("product");
                        if (results != null && results.size() > 0 && mProducts != null) {
                            mProducts.addAll(results);
                            mAdapter.updateData(mProducts);
                            mAdapter.notifyDataSetChanged();
                        }
                        if (SPProductListFilterFragment.getInstance(mHandler) != null)
                            SPProductListFilterFragment.getInstance(mHandler).setDataSource(mDataJson);
                    }
                    refreshView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new SPFailuredListener(SPProductListSearchResultActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                refreshRecyclerView.setLoadingMore(false);
                showFailedToast(msg);
                mPageIndex--;
            }
        });
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
            case filter:
                openRightFilterView();
                return;
        }
        refreshData();
    }

    public void openRightFilterView() {
        mDrawerLayout.openDrawer(Gravity.RIGHT);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT);
    }

    public void refreshView() {
        if (mShopOrder != null && mShopOrder.getSortAsc() != null) {
            if (mShopOrder.getSortAsc().equalsIgnoreCase("desc"))
                mFilterTabView.setSort(true);
            else
                mFilterTabView.setSort(false);
        }
    }

    public void startupSearch(Intent intent) {
        if (intent != null && intent.hasExtra("searchKey")) {
            mSearchkey = intent.getStringExtra("searchKey");
            if (searchText != null) searchText.setText(mSearchkey);
            refreshData();
        }
    }

    @Override
    public void onItemClick(SPProduct product) {
        startupActivity(product.getGoodsID());
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT))
            mDrawerLayout.closeDrawers();
        else
            finish();
    }

}
