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
 * Description:确认订单
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.shop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.headerfooter.songhang.library.SmartRecyclerAdapter;
import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.activity.common.SPPayListActivity_;
import com.tpshop.mall.activity.common.SPTextAreaViewActivity_;
import com.tpshop.mall.activity.person.address.SPConsigneeAddressListActivity_;
import com.tpshop.mall.activity.person.order.SPOrderCouponListActivity_;
import com.tpshop.mall.activity.person.order.SPOrderListActivity_;
import com.tpshop.mall.adapter.SPConfirmOrderAdapter;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.shop.SPShopRequest;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.model.order.SPOrder;
import com.tpshop.mall.model.person.SPConsigneeAddress;
import com.tpshop.mall.model.shop.SPCoupon;
import com.tpshop.mall.model.shop.SPStore;
import com.tpshop.mall.utils.SPOrderUtils;
import com.tpshop.mall.utils.SPServerUtils;
import com.tpshop.mall.utils.SPUtils;
import com.tpshop.mall.widget.KeyboardDialog;
import com.tpshop.mall.widget.SPArrowRowView;
import com.tpshop.mall.widget.SPRecyclerviewMarginLineDivider;
import com.tpshop.mall.widget.SwitchButton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.order_confirm_order)
public class SPConfirmOrderActivity extends SPBaseActivity implements SPConfirmOrderAdapter.SPConfirmOrderListener, View.OnClickListener {

    @ViewById(R.id.confirm_ll)
    FrameLayout confirmLl;

    @ViewById(R.id.store_listv)
    RecyclerView mRecyclerView;

    @ViewById(R.id.payfee_txtv)
    TextView payfeeTxtv;

    int points;                                        //使用的积分
    int integral;                                      //积分商品使用的积分
    float balance;                                     //使用的余额
    View mHeaderView;
    View mFooterView;
    private String pwd;                                //密码
    TextView pointTxtv;                                //现有积分
    TextView hasPointTv;
    TextView feeCutfeeTv;
    TextView balanceTxtv;                              //现有余额
    TextView buyTimeTxtv;                              //下单时间
    JSONObject amountDict;                             //结算金额汇总
    TextView feePointTxtv;
    TextView mAddressTxtv;
    SwitchButton pointSth;
    TextView feeCouponTxtv;
    TextView feeAmountTxtv;                            //实付款
    JSONObject userinfoJson;                           //用户信息(积分,余额)
    TextView mConsigneeTxtv;
    SwitchButton bananceSth;
    TextView feeBalanceTxtv;
    TextView feeGoodsFeeTxtv;
    TextView feeShoppingTxtv;
    private EditText pointEt;
    private double totalPrice;
    private EditText balanceEt;
    private TextView addressTv;
    SPArrowRowView orderInvoceArv;                     //发票
    SPConfirmOrderAdapter mAdapter;
    private String goodId, itemId, num;
    SPConsigneeAddress consigneeAddress;               //当前收货人信息
    Map<Integer, SPStore> storeMapCache;
    List<SPStore> stores = new ArrayList<>();
    private boolean isBuyNow, isIntegralGood;
    SmartRecyclerAdapter mSmartRecyclerAdapter;
    private RelativeLayout address_rl, addressRl;
    private String invoceTitle, taxpayer, invoceDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, getString(R.string.title_confirm_order));
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            isIntegralGood = getIntent().getBooleanExtra("isIntegralGood", false);
            isBuyNow = getIntent().getBooleanExtra("isBuyNow", false);
            goodId = getIntent().getStringExtra("goodId");
            itemId = getIntent().getStringExtra("itemId");
            num = getIntent().getStringExtra("num");
        }
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        confirmLl.setVisibility(View.GONE);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SPRecyclerviewMarginLineDivider(getResources()));
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.order_confirm_order_header, null);
        mFooterView = LayoutInflater.from(this).inflate(R.layout.order_confirm_order_footer, null);
        mAdapter = new SPConfirmOrderAdapter(this, this, isIntegralGood);
        mSmartRecyclerAdapter = new SmartRecyclerAdapter(mAdapter);
        mSmartRecyclerAdapter.setHeaderView(mHeaderView);
        mSmartRecyclerAdapter.setFooterView(mFooterView);
        mRecyclerView.setAdapter(mSmartRecyclerAdapter);
        address_rl = (RelativeLayout) mHeaderView.findViewById(R.id.address_rl);
        addressRl = (RelativeLayout) mHeaderView.findViewById(R.id.address_consignee_rl);
        addressTv = (TextView) mHeaderView.findViewById(R.id.address_consignee_tv);
        mConsigneeTxtv = (TextView) mHeaderView.findViewById(R.id.order_consignee_txtv);
        mAddressTxtv = (TextView) mHeaderView.findViewById(R.id.order_address_txtv);
        mAddressTxtv.setVisibility(View.GONE);
        RelativeLayout orderPointRl = (RelativeLayout) mFooterView.findViewById(R.id.order_point_rl);
        RelativeLayout hasPointRl = (RelativeLayout) mFooterView.findViewById(R.id.has_point_rl);
        hasPointTv = (TextView) mFooterView.findViewById(R.id.fee_has_point);
        feeCutfeeTv = (TextView) mFooterView.findViewById(R.id.fee_cutfee_txtv);
        feeGoodsFeeTxtv = (TextView) mFooterView.findViewById(R.id.fee_goodsfee_txtv);
        feeShoppingTxtv = (TextView) mFooterView.findViewById(R.id.fee_shopping_txtv);
        RelativeLayout couponRl = (RelativeLayout) mFooterView.findViewById(R.id.coupon_rl);
        feeCouponTxtv = (TextView) mFooterView.findViewById(R.id.fee_coupon_txtv);
        RelativeLayout pointRl = (RelativeLayout) mFooterView.findViewById(R.id.point_rl);
        feePointTxtv = (TextView) mFooterView.findViewById(R.id.fee_point_txtv);
        feeBalanceTxtv = (TextView) mFooterView.findViewById(R.id.fee_balance_txtv);
        feeAmountTxtv = (TextView) mFooterView.findViewById(R.id.fee_amount_txtv);
        buyTimeTxtv = (TextView) mFooterView.findViewById(R.id.buy_time_txtv);
        pointEt = (EditText) mFooterView.findViewById(R.id.order_point_et);
        balanceEt = (EditText) mFooterView.findViewById(R.id.order_balance_et);
        pointSth = (SwitchButton) mFooterView.findViewById(R.id.order_point_sth);
        bananceSth = (SwitchButton) mFooterView.findViewById(R.id.order_balance_sth);
        pointEt.setEnabled(false);
        balanceEt.setEnabled(false);
        pointSth.setClickable(false);
        bananceSth.setClickable(false);
        balanceTxtv = (TextView) mFooterView.findViewById(R.id.order_balance_txtv);
        pointTxtv = (TextView) mFooterView.findViewById(R.id.order_point_txtv);
        orderInvoceArv = (SPArrowRowView) mFooterView.findViewById(R.id.order_invoce_aview);
        orderInvoceArv.setSubText(getString(R.string.invoce_message));
        if (isIntegralGood) {
            orderPointRl.setVisibility(View.GONE);
            hasPointRl.setVisibility(View.VISIBLE);
            couponRl.setVisibility(View.GONE);
            pointRl.setVisibility(View.GONE);
        } else {
            hasPointRl.setVisibility(View.GONE);
        }
    }

    @Override
    public void initEvent() {
        address_rl.setOnClickListener(this);
        orderInvoceArv.setOnClickListener(this);
        pointEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (pointSth.isChecked()) {
                    if (s.toString().trim().isEmpty())
                        points = 0;
                    else
                        points = Integer.valueOf(pointEt.getText().toString());
                    loadTotalFee();      //重新计算支付金额
                }
            }
        });
        balanceEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (bananceSth.isChecked()) {
                    if (s.toString().trim().isEmpty())
                        balance = 0;
                    else
                        balance = Float.valueOf(balanceEt.getText().toString());
                    loadTotalFee();      //重新计算支付金额
                }
            }
        });
        pointSth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String pStr = pointEt.getText().toString().trim();
                if (isChecked && !pStr.isEmpty())
                    points = Integer.valueOf(pointEt.getText().toString());
                else
                    points = 0;
                loadTotalFee();      //重新计算支付金额
                pointEt.clearFocus();
                balanceEt.clearFocus();
                SPUtils.hideSoftInput(pointEt);
                SPUtils.hideSoftInput(balanceEt);
            }
        });
        bananceSth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String bStr = balanceEt.getText().toString().trim();
                if (isChecked && !bStr.isEmpty())
                    balance = Float.valueOf(balanceEt.getText().toString());
                else
                    balance = 0;
                loadTotalFee();      //重新计算支付金额
                pointEt.clearFocus();
                balanceEt.clearFocus();
                SPUtils.hideSoftInput(pointEt);
                SPUtils.hideSoftInput(balanceEt);
            }
        });
    }

    @Override
    public void initData() {
        storeMapCache = new HashMap<>();
        refreshData();
    }

    public void refreshData() {
        showLoadingSmallToast();
        String addressID = (consigneeAddress != null) ? consigneeAddress.getAddressID() : null;
        RequestParams params = new RequestParams();
        if (!SPStringUtils.isEmpty(addressID))
            params.put("address_id", addressID);
        if (isIntegralGood) {            //积分商品立即兑换订单信息
            params.put("goods_id", goodId);
            params.put("item_id", itemId);
            params.put("goods_num", num);
            SPShopRequest.getConfirmOrderData2(params, new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    hideLoadingSmallToast();
                    mDataJson = (JSONObject) response;
                    try {
                        if (mDataJson != null) {
                            if (mDataJson.has("consigneeAddress"))
                                consigneeAddress = (SPConsigneeAddress) mDataJson.get("consigneeAddress");
                            if (mDataJson.has("totalPrice"))
                                totalPrice = mDataJson.getDouble("totalPrice");
                            if (mDataJson.has("userInfo"))
                                userinfoJson = mDataJson.getJSONObject("userInfo");
                            if (mDataJson.has("store")) {
                                SPStore store = (SPStore) mDataJson.get("store");
                                stores.add(store);
                            }
                            dealModel();
                            refreshView();
                            loadTotalFee();
                            confirmLl.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast(e.getMessage());
                    }
                }
            }, new SPFailuredListener(SPConfirmOrderActivity.this) {
                @Override
                public void onRespone(String msg, int errorCode) {
                    hideLoadingSmallToast();
                    if (!SPStringUtils.isEmpty(msg)) showFailedToast(msg);
                    address_rl.setVisibility(View.VISIBLE);
                    addressTv.setVisibility(View.VISIBLE);
                    refreshView();
                    refreshTotalFee();
                    confirmLl.setVisibility(View.VISIBLE);
                }
            });
            return;
        }
        if (isBuyNow) {            //商品立即购买订单信息
            params.put("goods_id", goodId);
            params.put("item_id", itemId);
            params.put("action", "buy_now");
            params.put("goods_num", num);
        }
        SPShopRequest.getConfirmOrderData(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                mDataJson = (JSONObject) response;
                try {
                    if (mDataJson != null) {
                        if (mDataJson.has("consigneeAddress"))
                            consigneeAddress = (SPConsigneeAddress) mDataJson.get("consigneeAddress");
                        if (mDataJson.has("totalPrice"))
                            totalPrice = mDataJson.getDouble("totalPrice");
                        if (mDataJson.has("userInfo"))
                            userinfoJson = mDataJson.getJSONObject("userInfo");
                        if (mDataJson.has("storeList"))
                            stores = (List<SPStore>) mDataJson.get("storeList");
                        dealModel();
                        refreshView();
                        loadTotalFee();
                        confirmLl.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast(e.getMessage());
                }
            }
        }, new SPFailuredListener(SPConfirmOrderActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                if (!SPStringUtils.isEmpty(msg)) showFailedToast(msg);
                address_rl.setVisibility(View.VISIBLE);
                addressTv.setVisibility(View.VISIBLE);
                refreshView();
                refreshTotalFee();
                confirmLl.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 处理服务器获取的数据
     */
    public void dealModel() {
        if (stores == null) return;
        storeMapCache.clear();
        for (SPStore store : stores)
            storeMapCache.put(store.getStoreId(), store);
    }

    /**
     * 更加当前设置信息,获取商品数据(商品总价,应付金额)
     */
    public void loadTotalFee() {
        RequestParams params = getRequestParameter(1);
        showLoadingSmallToast();
        if (isIntegralGood) {
            SPShopRequest.getOrderTotalFee2(params, new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    hideLoadingSmallToast();
                    if (response != null && SPUtils.velidateJSONObject(response)) {
                        amountDict = (JSONObject) response;
                        try {
                            integral = amountDict.getInt("total_integral");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        refreshTotalFee();
                    }
                }
            }, new SPFailuredListener(SPConfirmOrderActivity.this) {
                @Override
                public void onRespone(String msg, int errorCode) {
                    hideLoadingSmallToast();
                    showFailedToast(msg);
                    integral = errorCode;
                    refreshTotalFee();
                }
            });
        } else {
            SPShopRequest.getOrderTotalFee(params, new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    hideLoadingSmallToast();
                    if (response != null) {
                        amountDict = (JSONObject) response;
                        refreshTotalFee();
                    }
                }
            }, new SPFailuredListener(SPConfirmOrderActivity.this) {
                @Override
                public void onRespone(String msg, int errorCode) {
                    hideLoadingSmallToast();
                    showFailedToast(msg);
                }
            });
        }
    }

    public void refreshView() {
        try {
            if (consigneeAddress != null) {
                address_rl.setVisibility(View.VISIBLE);
                addressRl.setVisibility(View.VISIBLE);
                addressTv.setVisibility(View.GONE);
                mConsigneeTxtv.setText(consigneeAddress.getConsignee() + "  " + consigneeAddress.getMobile());
                mAddressTxtv.setText(consigneeAddress.getFullAddress());
                mAddressTxtv.setVisibility(View.VISIBLE);
            } else {
                address_rl.setVisibility(View.VISIBLE);
                addressRl.setVisibility(View.GONE);
                addressTv.setVisibility(View.VISIBLE);
                mAddressTxtv.setVisibility(View.GONE);
            }
            feeGoodsFeeTxtv.setText("¥" + totalPrice);
            if (userinfoJson != null) {
                if (userinfoJson.has("pay_points")) {        //积分
                    int p = userinfoJson.getInt("pay_points");
                    if (p <= 0) {
                        pointEt.setEnabled(false);
                        pointSth.setClickable(false);
                    } else {
                        pointEt.setEnabled(true);
                        pointSth.setClickable(true);
                    }
                    String pointRate = SPServerUtils.getPointRate();
                    pointTxtv.setText("当前可用积分" + p + "(" + pointRate + "积分抵扣1元)");
                }
                if (userinfoJson.has("user_money")) {        //金额
                    double b = userinfoJson.getDouble("user_money");
                    if (b <= 0) {
                        balanceEt.setEnabled(false);
                        bananceSth.setClickable(false);
                    } else {
                        balanceEt.setEnabled(true);
                        bananceSth.setClickable(true);
                    }
                    balanceTxtv.setText("当前可用余额¥" + b);
                }
            }
            String buyTime = SPCommonUtils.getDateFullTime(System.currentTimeMillis());         //下单时间
            buyTimeTxtv.setText("下单时间: " + buyTime);
            List<SPStore> cacheStores = SPUtils.convertCollectToListStore(storeMapCache.values());
            if (amountDict != null) {
                if (amountDict.has("store_order_prom_title") && SPUtils.velidateJSONObject(amountDict.get("store_order_prom_title"))) {
                    JSONObject storePromTitleObj = amountDict.getJSONObject("store_order_prom_title");             //普通商品活动信息
                    for (SPStore spStore : cacheStores) {
                        if (storePromTitleObj.has(spStore.getStoreId() + "")) {
                            String storePromTitle = storePromTitleObj.getString(spStore.getStoreId() + "");
                            if (!SPStringUtils.isEmpty(storePromTitle))
                                spStore.setPromTitle(storePromTitle);
                        }
                    }
                }
                if (amountDict.has("store_shipping_price") && SPUtils.velidateJSONObject(amountDict.get("store_shipping_price"))) {
                    JSONObject shippingObj = amountDict.getJSONObject("store_shipping_price");             //普通商品物流信息
                    for (SPStore spStore : cacheStores) {
                        if (shippingObj.has(spStore.getStoreId() + "")) {
                            double shipfee = shippingObj.getDouble(spStore.getStoreId() + "");
                            if (shipfee <= 0)
                                spStore.setShipfee("包邮");
                            else
                                spStore.setShipfee(shipfee + "元");
                        }
                    }
                } else if (amountDict.has("shipping_price")) {
                    for (SPStore spStore : cacheStores) {
                        double shipfee = amountDict.getDouble("shipping_price");              //积分商品物流信息
                        if (shipfee <= 0)
                            spStore.setShipfee("包邮");
                        else
                            spStore.setShipfee(shipfee + "元");
                    }
                }
            }
            mAdapter.setData(cacheStores);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新底部商品总金额数据
     */
    public void refreshTotalFee() {
        try {
            if (amountDict != null) {
                if (amountDict.has("shipping_price"))           //物流费用
                    feeShoppingTxtv.setText("¥" + amountDict.getDouble("shipping_price"));
                if (amountDict.has("coupon_price"))             //优惠券金额
                    feeCouponTxtv.setText("¥" + amountDict.getDouble("coupon_price"));
                if (amountDict.has("user_money"))               //余额支付
                    feeBalanceTxtv.setText("¥" + amountDict.getDouble("user_money"));
                if (amountDict.has("integral_money"))           //积分支付
                    feePointTxtv.setText("¥" + amountDict.getDouble("integral_money"));
                if (amountDict.has("goods_price"))              //商品金额
                    feeGoodsFeeTxtv.setText("¥" + amountDict.getDouble("goods_price"));
                if (isIntegralGood && amountDict.has("total_integral"))
                    hasPointTv.setText(amountDict.getInt("total_integral") + "");
                if (amountDict.has("order_prom_amount"))        //活动优惠
                    feeCutfeeTv.setText("¥" + amountDict.getDouble("order_prom_amount"));
                if (amountDict.has("order_amount")) {
                    String payablesFmt = "实付款:¥" + amountDict.getString("order_amount");
                    int startIndex = 4;
                    int endIndex = payablesFmt.length();
                    SpannableString payablesSpanStr = new SpannableString(payablesFmt);
                    payablesSpanStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), startIndex,
                            endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);         //设置区域内文字颜色为洋红色
                    feeAmountTxtv.setText(payablesSpanStr);
                }
            }
            if (storeMapCache == null) return;
            Collection<SPStore> collectStores = storeMapCache.values();
            Iterator<SPStore> iterator = collectStores.iterator();
            int totalNum = 0;
            while (iterator.hasNext()) {          //计算商品数量
                SPStore store = iterator.next();
                if (store.getStoreProducts() != null) {
                    for (SPProduct product : store.getStoreProducts())
                        totalNum += product.getGoodsNum();
                }
            }
            String payables;
            if (amountDict != null && amountDict.has("order_amount")) {
                payables = amountDict.getString("order_amount");
            } else {
                if (isIntegralGood && integral <= 0) {            //当积分商品兑换积分不足时展示的商品总额和支付金额
                    payables = totalPrice * totalNum + "";
                    feeGoodsFeeTxtv.setText("¥" + payables);
                } else {
                    payables = totalPrice + "";
                }
            }
            String totalFeeFmt = "共" + totalNum + "件,支付金额¥" + payables;
            int numStartIndex = 1;
            int numEndIndex = numStartIndex + String.valueOf(totalNum).length();
            int rmbStartIndex = numEndIndex + 6;
            int rmbEndIndex = totalFeeFmt.length();
            int payStartIndex = rmbStartIndex + 1;
            int payEndIndex = totalFeeFmt.length();
            SpannableString totalFeeSpanStr = new SpannableString(totalFeeFmt);
            totalFeeSpanStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), rmbStartIndex,
                    rmbEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);             //设置区域内文字颜色为洋红色
            totalFeeSpanStr.setSpan(new RelativeSizeSpan(1.3f), payStartIndex, payEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            payfeeTxtv.setText(totalFeeSpanStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_rl:           //收货地址
                Intent resultIntent = new Intent(SPConfirmOrderActivity.this, SPConsigneeAddressListActivity_.class);
                resultIntent.putExtra("getAddress", "1");
                startActivityForResult(resultIntent, SPMobileConstants.Result_Code_GetAddress);
                break;
            case R.id.order_invoce_aview:              //发票
                Intent invoceIntent = new Intent(SPConfirmOrderActivity.this, SPOrderInvoceActivity_.class);
                startActivityForResult(invoceIntent, SPMobileConstants.Result_Code_GetInvoce);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case SPMobileConstants.Result_Code_GetAddress:            //收货地址
                consigneeAddress = (SPConsigneeAddress) data.getSerializableExtra("consignee");
                refreshData();
                break;
            case SPMobileConstants.Result_Code_GetCoupon:             //优惠券
                int storeid = data.getIntExtra("storeId", 0);
                SPCoupon selectedCoupon = (SPCoupon) data.getSerializableExtra("selectCoupon");
                SPStore store = storeMapCache.get(storeid);
                if (store != null) store.setSelectedCoupon(selectedCoupon);
                refreshView();
                loadTotalFee();
                break;
            case SPMobileConstants.Result_Code_GetSelerMessage:       //留言
                String selerMessage = data.getStringExtra("content");
                int storeId = SPMobileApplication.getInstance().getStoreId();
                if ((store = storeMapCache.get(storeId)) == null) return;
                store.setSelerMessage(selerMessage);
                refreshView();
                loadTotalFee();
                break;
            case SPMobileConstants.Result_Code_GetInvoce:             //发票
                invoceTitle = data.getStringExtra("invoce_title");
                taxpayer = data.getStringExtra("taxpayer");
                invoceDesc = data.getStringExtra("invoce_desc");
                if (invoceTitle != null && invoceDesc != null)
                    orderInvoceArv.setSubText(invoceTitle + "——" + invoceDesc);
                else
                    orderInvoceArv.setSubText(getString(R.string.invoce_message));
                loadTotalFee();
                break;
        }
    }

    @Click({R.id.pay_btn})
    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.pay_btn:
                isInputPwd();        //去结算
                break;
        }
    }

    private void isInputPwd() {
        if (consigneeAddress == null) {
            showToast("请选择收货地址");
            return;
        }
        pwd = null;            //每次提交订单时清空密码
        boolean hasPoint = !pointEt.getText().toString().trim().isEmpty() && (Integer.valueOf(pointEt.getText().toString()) > 0)
                && pointSth.isChecked();
        boolean hasBanance = !balanceEt.getText().toString().trim().isEmpty() && (Double.valueOf(balanceEt.getText().toString()) > 0)
                && bananceSth.isChecked();
        if (hasPoint || hasBanance || integral > 0) {         //使用了积分(余额)支付
            final KeyboardDialog dialog = new KeyboardDialog(this);
            dialog.setSureClickListener(new KeyboardDialog.SureClickListener() {
                @Override
                public void getPwd() {
                    if (!dialog.getPsw().trim().isEmpty()) {
                        pwd = dialog.getPsw();
                        orderCommint();
                    }
                }
            });
            dialog.show();
        } else {      //直接提交订单
            orderCommint();
        }
    }

    /**
     * 提交订单
     */
    public void orderCommint() {
        RequestParams params = getRequestParameter(2);
        showLoadingSmallToast();
        if (isIntegralGood) {
            SPShopRequest.getOrderTotalFee2(params, new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    hideLoadingSmallToast();
                    String masterOrderSN = (String) response;
                    startUpPay(masterOrderSN);
                }
            }, new SPFailuredListener(SPConfirmOrderActivity.this) {
                @Override
                public void onRespone(String msg, int errorCode) {
                    hideLoadingSmallToast();
                    showFailedToast(msg);
                }
            });
        } else {
            SPShopRequest.submitOrder(params, new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    hideLoadingSmallToast();
                    String masterOrderSN = (String) response;
                    startUpPay(masterOrderSN);
                }
            }, new SPFailuredListener(SPConfirmOrderActivity.this) {
                @Override
                public void onRespone(String msg, int errorCode) {
                    hideLoadingSmallToast();
                    showFailedToast(msg);
                }
            });
        }
    }

    /**
     * 组装请求参数
     *
     * @param type 请求类型,1:价格变动,2:生成订单
     */
    public RequestParams getRequestParameter(int type) {
        RequestParams params = new RequestParams();
        JSONObject formDataParams = new JSONObject();
        try {
            if (isIntegralGood) {
                if (consigneeAddress != null)
                    params.put("address_id", consigneeAddress.getAddressID());
                if (type == 2)
                    params.put("act", "submit_order");
                params.put("goods_id", goodId);
                params.put("item_id", itemId);
                params.put("goods_num", num);
                List<SPStore> cacheStores = SPUtils.convertCollectToListStore(storeMapCache.values());
                for (SPStore store : cacheStores) {
                    if (store.getSelerMessage() != null)
                        params.put("user_note", store.getSelerMessage());           //买家留言
                    else
                        params.put("user_note", "");
                }
                if (pwd != null)
                    params.put("pwd", SPUtils.md5WithAuthCode(pwd));
                String invoce = orderInvoceArv.getSubText();
                if (orderInvoceArv != null && !invoce.isEmpty() && !invoce.equals(getString(R.string.invoce_message))) {
                    params.put("invoice_title", invoceTitle);         //发票抬头
                    if (taxpayer != null)
                        params.put("taxpayer", taxpayer);             //纳税人识别号
                    params.put("invoice_desc", invoceDesc);           //发票内容
                } else {
                    params.put("invoice_title", "个人");
                }
                params.put("user_money", balance);               //使用余额
            } else {
                if (type == 1) {      //价格变动
                    params.put("act", "order_price");
                } else {      //提交订单
                    params.put("act", "submit_order");
                    if (pwd != null)
                        params.put("paypwd", SPUtils.md5WithAuthCode(pwd));
                    String invoce = orderInvoceArv.getSubText();
                    if (orderInvoceArv != null && !invoce.isEmpty() && !invoce.equals(getString(R.string.invoce_message))) {
                        params.put("invoice_title", invoceTitle);         //发票抬头
                        if (taxpayer != null)
                            params.put("taxpayer", taxpayer);             //纳税人识别号
                        params.put("invoice_desc", invoceDesc);           //发票内容
                    }
                }
                if (consigneeAddress != null)
                    params.put("address_id", consigneeAddress.getAddressID());
                if (isBuyNow) {
                    params.put("goods_id", goodId);
                    params.put("item_id", itemId);
                    params.put("action", "buy_now");
                    params.put("goods_num", num);
                }
                JSONObject selerMessageJson = new JSONObject();      //买家留言
                JSONObject couponTypeJson = new JSONObject();        //优惠券类型
                JSONObject couponIdJson = new JSONObject();          //优惠券ID
                JSONObject couponCodeJson = new JSONObject();        //优惠券码
                Iterator<SPStore> iterator = storeMapCache.values().iterator();
                while (iterator.hasNext()) {
                    SPStore store = iterator.next();
                    if (store.getSelectedCoupon() == null) {            //无优惠券
                        couponTypeJson.put(String.valueOf(store.getStoreId()), "0");
                        couponIdJson.put(String.valueOf(store.getStoreId()), "0");
                        couponCodeJson.put(String.valueOf(store.getStoreId()), "0");
                    } else {            //有优惠券
                        couponTypeJson.put(String.valueOf(store.getStoreId()), "1");
                        couponIdJson.put(String.valueOf(store.getStoreId()), store.getSelectedCoupon().getCouponID());
                        couponCodeJson.put(String.valueOf(store.getStoreId()), "0");
                    }
                    if (store.getSelerMessage() == null)      //买家留言
                        selerMessageJson.put(String.valueOf(store.getStoreId()), "");
                    else
                        selerMessageJson.put(String.valueOf(store.getStoreId()), store.getSelerMessage());
                }
                formDataParams.put("user_note", selerMessageJson);
                formDataParams.put("couponTypeSelect", couponTypeJson);
                formDataParams.put("coupon_id", couponIdJson);
                formDataParams.put("couponCode", couponCodeJson);
                params.put("cart_form_data", Uri.encode(formDataParams.toString()));
                params.put("pay_points", points);                //使用积分
                params.put("user_money", balance);               //使用余额
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    /**
     * 启动支付页面支付
     */
    public void startUpPay(final String masterOrderSN) {
        SPShopRequest.getOrderAmountWithMasterOrderSN(masterOrderSN, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null && Double.valueOf(response.toString()) > 0) {
                    String amount = response.toString();
                    SPOrder order = new SPOrder(masterOrderSN, amount, false);
                    gotoPay(order);          //进入支付页面支付
                } else {
                    startupOrderList();      //支付金额为0时,说明是积分或余额抵扣,不需要进入订单页面
                }
            }
        }, new SPFailuredListener(SPConfirmOrderActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showFailedToast(msg);
            }
        });
    }

    /**
     * 进入订单列表页面
     */
    public void startupOrderList() {
        Intent allOrderList = new Intent(this, SPOrderListActivity_.class);
        allOrderList.putExtra("orderStatus", SPOrderUtils.OrderStatus.all.value());
        this.startActivity(allOrderList);
        this.finish();
    }

    /**
     * 进入支付页面支付
     */
    public void gotoPay(SPOrder order) {
        SPMobileApplication.getInstance().fellBack = 2;
        Intent payIntent = new Intent(this, SPPayListActivity_.class);
        payIntent.putExtra("order", order);
        startActivity(payIntent);
        this.finish();
    }

    @Override
    public void coupontItemClick(SPStore store) {
        if (store.getCouponNum() > 0) {
            Intent couponIntent = new Intent(this, SPOrderCouponListActivity_.class);
            couponIntent.putExtra(SPMobileConstants.KEY_STORE_ID, store.getStoreId());
            couponIntent.putExtra("order_money", store.getCartTotalMoney());
            if (isBuyNow) {
                couponIntent.putExtra("item_id", itemId);
                couponIntent.putExtra("goods_id", goodId);
                couponIntent.putExtra("goods_num", num);
                couponIntent.putExtra("action", "buy_now");
            }
            startActivityForResult(couponIntent, SPMobileConstants.Result_Code_GetCoupon);
        } else {
            showToast("当前无可用优惠券");
        }
    }

    @Override
    public void selerMessageItemClick(SPStore store) {
        SPMobileApplication.getInstance().setStoreId(store.getStoreId());
        Intent invokeIntent = new Intent(this, SPTextAreaViewActivity_.class);
        startActivityForResult(invokeIntent, SPMobileConstants.Result_Code_GetSelerMessage);
    }

}
