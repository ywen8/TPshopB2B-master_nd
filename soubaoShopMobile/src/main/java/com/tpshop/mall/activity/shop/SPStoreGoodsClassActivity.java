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
 * Description: 店铺商品分类
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.shop;

import android.content.Intent;
import android.os.Bundle;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.adapter.SPStoreGoodsClassAdapter;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.shop.SPStoreRequest;
import com.tpshop.mall.model.SPStoreGoodsClass;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * @author 飞龙
 */
@EActivity(R.layout.store_goods_class)
public class SPStoreGoodsClassActivity extends SPBaseActivity implements SPStoreGoodsClassAdapter.GoodsClassListener {

    private int mStoreId;
    SPStoreGoodsClassAdapter mAdapter;
    List<SPStoreGoodsClass> storeGoodsClasses;

    @ViewById(R.id.store_goods_class_gdv)
    StickyGridHeadersGridView mStoreGoodsClassGdv;

    @Override
    protected void onCreate(Bundle bundle) {
        setCustomerTitle(true, true, getString(R.string.title_store_goods_class));
        super.onCreate(bundle);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        mStoreGoodsClassGdv.setAreHeadersSticky(false);
        mAdapter = new SPStoreGoodsClassAdapter(this, this);
        mStoreGoodsClassGdv.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {
        mStoreId = getIntent().getIntExtra("storeId", 0);
        showLoadingSmallToast();
        SPStoreRequest.getStoreGoodsClass(mStoreId, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                if (response != null) {
                    storeGoodsClasses = (List<SPStoreGoodsClass>) response;
                    SPStoreGoodsClass allGoodsClass = getAllGoodsClass();
                    storeGoodsClasses.add(0, allGoodsClass);
                    mAdapter.setData(storeGoodsClasses);
                }
            }
        }, new SPFailuredListener(SPStoreGoodsClassActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showFailedToast(msg);
            }
        });
    }

    public SPStoreGoodsClass getAllGoodsClass() {
        SPStoreGoodsClass goodsClass = new SPStoreGoodsClass();
        goodsClass.setStoreId(mStoreId);
        goodsClass.setName("全部分类");
        return goodsClass;
    }

    @Override
    public void onClassItemClick(SPStoreGoodsClass goodsClass) {      //父分类查看全部
        Intent intent = new Intent(this, SPStoreProductListActivity.class);
        intent.putExtra("storeId", goodsClass.getStoreId());
        intent.putExtra("catId", goodsClass.getId());
        startActivity(intent);
    }

    @Override
    public void onItemClick(SPStoreGoodsClass goodsClass) {      //子分类
        Intent intent = new Intent(SPStoreGoodsClassActivity.this, SPStoreProductListActivity.class);
        intent.putExtra("storeId", goodsClass.getStoreId());
        intent.putExtra("catId", goodsClass.getId());
        startActivity(intent);
    }

}
