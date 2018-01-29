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
 * Description:Activity 收货地址列表
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.person.address;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.activity.shop.SPConfirmOrderActivity;
import com.tpshop.mall.adapter.ListDividerItemDecoration;
import com.tpshop.mall.adapter.SPAddressListAdapter;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPPersonRequest;
import com.tpshop.mall.model.person.SPConsigneeAddress;
import com.tpshop.mall.widget.swipetoloadlayout.OnRefreshListener;
import com.tpshop.mall.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/6/27
 */
@EActivity(R.layout.person_address_list)
public class SPConsigneeAddressListActivity extends SPBaseActivity implements OnRefreshListener, SPAddressListAdapter.AddressListListener {

    SPAddressListAdapter mAdapter;
    List<SPConsigneeAddress> consignees;

    @ViewById(R.id.super_recyclerview)
    SuperRefreshRecyclerView refreshRecyclerView;

    @ViewById(R.id.empty_lstv)
    View mEmptyView;

    @Override
    public void onCreate(Bundle savedInstanceStat) {
        super.setCustomerTitle(true, true, getString(R.string.title_consignee_list));
        super.onCreate(savedInstanceStat);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        View emptyView = findViewById(R.id.empty_lstv);
        refreshRecyclerView.setEmptyView(emptyView);
        refreshRecyclerView.init(new LinearLayoutManager(this), this, null);
        Drawable drawable = getResources().getDrawable(R.drawable.divider_grid_product_list);
        refreshRecyclerView.addItemDecoration(new ListDividerItemDecoration(drawable));             //设置分割线
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(false);
        mAdapter = new SPAddressListAdapter(this, this);
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

    public void refreshData() {
        showLoadingSmallToast();
        SPPersonRequest.getConsigneeAddressList(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                if (response != null && (response instanceof ArrayList) && ((ArrayList) response).size() > 0) {
                    consignees = (List<SPConsigneeAddress>) response;
                    dealData();
                    refreshRecyclerView.showData();
                } else {
                    refreshRecyclerView.showEmpty();
                }
                mAdapter.updateData(consignees);
            }
        }, new SPFailuredListener(SPConsigneeAddressListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                refreshRecyclerView.setRefreshing(false);
                showFailedToast(msg);
            }
        });
    }

    private void dealData() {
        for (SPConsigneeAddress consigneeAddress : consignees) {
            String provinceName = consigneeAddress.getProvinceName() == null ? "" : consigneeAddress.getProvinceName();
            String cityName = consigneeAddress.getCityName() == null ? "" : consigneeAddress.getCityName();
            String districtName = consigneeAddress.getDistrictName() == null ? "" : consigneeAddress.getDistrictName();
            String twonName = consigneeAddress.getTwonName() == null ? "" : consigneeAddress.getTwonName();
            String address = consigneeAddress.getAddress() == null ? "" : consigneeAddress.getAddress();
            consigneeAddress.setFullAddress(provinceName + cityName + districtName + twonName + address);
        }
    }

    @Override
    public void onItemEdit(SPConsigneeAddress consigneeAddress) {
        Intent intent = new Intent(this, SPConsigneeAddressEditActivity_.class);
        intent.putExtra("consignee", consigneeAddress);
        startActivityForResult(intent, SPMobileConstants.Result_Code_Refresh);
    }

    @Override
    public void onItemDel(SPConsigneeAddress consigneeAddress) {
        showLoadingSmallToast();
        SPPersonRequest.delConsigneeAddressByID(consigneeAddress.getAddressID(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                showSuccessToast(msg);
                refreshData();
            }
        }, new SPFailuredListener(SPConsigneeAddressListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showFailedToast(msg);
            }
        });
    }

    @Override
    public void onItemClick(SPConsigneeAddress consigneeAddress) {
        if (getIntent() != null && getIntent().hasExtra("getAddress")) {
            Intent resultIntent = new Intent(this, SPConfirmOrderActivity.class);
            resultIntent.putExtra("consignee", consigneeAddress);
            setResult(SPMobileConstants.Result_Code_GetAddress, resultIntent);
            finish();
        }
    }

    @Click({R.id.add_address_btn})
    public void onViewClick(View v) {
        if (v.getId() == R.id.add_address_btn) {
            Intent intent = new Intent(this, SPConsigneeAddressEditActivity_.class);
            startActivityForResult(intent, SPMobileConstants.Result_Code_Refresh);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            refreshData();
    }

}
