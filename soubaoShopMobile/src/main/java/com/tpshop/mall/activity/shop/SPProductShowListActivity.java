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
 * Description: 产品展示列表
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.adapter.SPProductShowListAdapter;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.model.SPProduct;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * @author 飞龙
 */
@EActivity(R.layout.product_show_list)
public class SPProductShowListActivity extends SPBaseActivity {

    List<SPProduct> mProducts;
    SPProductShowListAdapter mAdapter;

    @ViewById(R.id.product_listv)
    ListView productListv;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle bundle) {
        super.setCustomerTitle(true, true, getString(R.string.title_product_list));
        super.onCreate(bundle);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
    }

    @Override
    public void initData() {
        mAdapter = new SPProductShowListAdapter(this, mHandler);
        productListv.setAdapter(mAdapter);
        mProducts = (List<SPProduct>) SPMobileApplication.getInstance().list;
        if (mProducts != null) mAdapter.setData(mProducts);
    }

    @Override
    public void initEvent() {
        productListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SPProduct product = (SPProduct) mAdapter.getItem(position);
                Intent intent = new Intent(SPProductShowListActivity.this, SPProductDetailActivity_.class);
                intent.putExtra("goodsID", product.getGoodsID());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case SPMobileConstants.Result_Code_Refresh:
                String goodsID = data.getStringExtra("goodsId");
                if (!SPStringUtils.isEmpty(goodsID) && mProducts != null) {
                    for (SPProduct product : mProducts) {
                        if (goodsID.equals(product.getGoodsID()))
                            product.setIsComment(1);
                    }
                    mAdapter.setData(mProducts);
                }
                break;
        }
    }

}
