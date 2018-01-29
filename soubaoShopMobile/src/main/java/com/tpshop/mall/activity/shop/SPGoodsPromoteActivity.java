/*
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2127 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: 飞龙  wangqh01292@163.com
 * Date: @date 2015-10-15 20:32:41
 * Description: 商品促销活动
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.adapter.DividerGridItemDecoration;
import com.tpshop.mall.adapter.SPGoodPromoteAdapter;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.shop.SPShopRequest;
import com.tpshop.mall.model.shop.SPPromoteGood;
import com.tpshop.mall.widget.swipetoloadlayout.OnLoadMoreListener;
import com.tpshop.mall.widget.swipetoloadlayout.OnRefreshListener;
import com.tpshop.mall.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/7/4
 */
@EActivity(R.layout.good_promote_list)
public class SPGoodsPromoteActivity extends SPBaseActivity implements OnRefreshListener, OnLoadMoreListener,
        SPGoodPromoteAdapter.OnItemClickListener {

    @ViewById(R.id.super_recyclerview)
    SuperRefreshRecyclerView refreshRecyclerView;

    private int pageIndex;
    List<SPPromoteGood> goods;
    private SPGoodPromoteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, getString(R.string.title_goods_promote));
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
        refreshRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));        //设置分割线
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(true);
        mAdapter = new SPGoodPromoteAdapter(this);
        refreshRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
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

    private void refreshData() {
        pageIndex = 1;
        showLoadingSmallToast();
        SPShopRequest.goodsPromoteList(pageIndex, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                    goods = (List<SPPromoteGood>) response;
                    mAdapter.updateData(goods);
                    refreshRecyclerView.showData();
                } else {
                    refreshRecyclerView.showEmpty();
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                showFailedToast(msg);
            }
        });
    }

    private void loadMoreData() {
        pageIndex++;
        SPShopRequest.goodsPromoteList(pageIndex, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                refreshRecyclerView.setLoadingMore(false);
                if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                    List<SPPromoteGood> promoteGoods = (List<SPPromoteGood>) response;
                    goods.addAll(promoteGoods);
                    mAdapter.updateData(goods);
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
    public void onItemClick(String goodsId, String itemId) {
        Intent intent = new Intent(this, SPProductDetailActivity_.class);
        intent.putExtra("goodsID", goodsId);
        if (!SPStringUtils.isEmpty(itemId)) intent.putExtra("itemID", itemId);
        startActivity(intent);
    }

}
