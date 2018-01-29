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
 * Date: @date 2015年10月20日 下午7:19:26
 * Description:购物车Fragment
 *
 * @version V1.0
 */
package com.tpshop.mall.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.headerfooter.songhang.library.SmartRecyclerAdapter;
import com.tpshop.mall.R;
import com.tpshop.mall.SPMainActivity;
import com.tpshop.mall.activity.person.user.SPLoginActivity_;
import com.tpshop.mall.activity.shop.SPConfirmOrderActivity_;
import com.tpshop.mall.activity.shop.SPProductDetailActivity_;
import com.tpshop.mall.activity.shop.SPStoreHomeActivity_;
import com.tpshop.mall.adapter.DividerGridItemDecoration;
import com.tpshop.mall.adapter.ListDividerItemDecoration;
import com.tpshop.mall.adapter.SPProductListSecAdapter;
import com.tpshop.mall.adapter.SPShopcartListAdapter;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.global.SPShopCartManager;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.shop.SPShopRequest;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.model.shop.SPJsonArray;
import com.tpshop.mall.model.shop.SPStore;
import com.tpshop.mall.utils.SPConfirmDialog;
import com.tpshop.mall.utils.SPUtils;
import com.tpshop.mall.widget.swipetoloadlayout.OnRefreshListener;
import com.tpshop.mall.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页 -> 购物车
 */
public class SPShopCartFragment extends SPBaseFragment implements View.OnClickListener, SPShopcartListAdapter.ShopCarClickListener,
        SPConfirmDialog.ConfirmDialogListener, OnRefreshListener, SPProductListSecAdapter.OnItemClickListener {

    Button backBtn;
    Button mLoginBtn;
    boolean isAllCheck;                             //购物车全部商品选中
    private Button buyBtn;
    List<SPStore> mStores;
    View mEmptyHeaderView;
    View mEmptyFooterView;
    private double cutFee;
    TextView emptyHintTxtv;
    boolean isStoreAllCheck;                        //店铺全部商品选中
    private double totalFee;
    private Context mContext;
    List<SPProduct> mProducts;
    private Button checkallBtn;
    private TextView cutfeeTxtv;
    private TextView totalfeeTxtv;
    boolean isOnlyRefresh = false;                  //刷新标识:判断拉取数据还是提交表单
    RecyclerView mEmptyRecyclerView;                //购物车为空时推荐列表
    private SPProduct currentProduct;
    private SPJsonArray formDataArray;
    boolean isShowSmallLoading = true;
    LinearLayoutManager mLayoutManager;
    private SPJsonArray cacheDataArray;
    static SPShopCartFragment mFragment;
    GridLayoutManager mGridLayoutManager;
    private SPShopcartListAdapter mAdapter;
    SmartRecyclerAdapter mSmartRecyclerAdapter;
    SuperRefreshRecyclerView refreshRecyclerView;
    private SPProductListSecAdapter mEmptyAdapter;

    public static SPShopCartFragment newInstance() {
        if (mFragment == null)
            mFragment = new SPShopCartFragment();
        return mFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shopcart_fragment, null, false);
        mEmptyHeaderView = inflater.inflate(R.layout.shopcart_empty_header, null);
        mEmptyFooterView = inflater.inflate(R.layout.shopcart_empty_footer, null);
        super.init(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        isOnlyRefresh = true;
        refreshData();
    }

    @Override
    public void lazyInit(View view, Bundle savedInstanceState) {
    }

    @Override
    public void initSubView(View view) {
        view.findViewById(R.id.titlebar_back_imgv).setVisibility(View.GONE);
        refreshRecyclerView = (SuperRefreshRecyclerView) view.findViewById(R.id.super_recyclerview);
        backBtn = (Button) view.findViewById(R.id.titlebar_back_btn);
        backBtn.setVisibility(View.GONE);
        TextView titleTxtv = (TextView) view.findViewById(R.id.titlebar_title_txtv);
        titleTxtv.setText(getString(R.string.title_shopcart));
        totalfeeTxtv = (TextView) view.findViewById(R.id.totalfee_txtv);
        cutfeeTxtv = (TextView) view.findViewById(R.id.cutfee_txtv);
        checkallBtn = (Button) view.findViewById(R.id.checkall_btn);
        buyBtn = (Button) view.findViewById(R.id.buy_btn);
        mLayoutManager = new LinearLayoutManager(mContext);       //设置排版方式
        refreshRecyclerView.init(mLayoutManager, this, null);
        Drawable drawable = getResources().getDrawable(R.drawable.divider_white_large);
        refreshRecyclerView.addItemDecoration(new ListDividerItemDecoration(drawable));                //设置分割线
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(false);
        mAdapter = new SPShopcartListAdapter(mContext, this);
        refreshRecyclerView.setAdapter(mAdapter);
        mEmptyRecyclerView = (RecyclerView) view.findViewById(R.id.cart_empty_listv);
        mEmptyRecyclerView.setHasFixedSize(true);
        mGridLayoutManager = new GridLayoutManager(getActivity(), 2);                                  //设置排版方式
        mEmptyRecyclerView.setLayoutManager(mGridLayoutManager);
        mEmptyRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mEmptyRecyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));            //设置分割线
        mEmptyAdapter = new SPProductListSecAdapter(getActivity(), this);
        mSmartRecyclerAdapter = new SmartRecyclerAdapter(mEmptyAdapter);
        mSmartRecyclerAdapter.setHeaderView(mEmptyHeaderView);
        mSmartRecyclerAdapter.setFooterView(mEmptyFooterView);
        mEmptyRecyclerView.setAdapter(mSmartRecyclerAdapter);
        View emptyView = view.findViewById(R.id.empty_lstv);
        refreshRecyclerView.setEmptyView(emptyView);
        mLoginBtn = (Button) mEmptyHeaderView.findViewById(R.id.login_btn);
        emptyHintTxtv = (TextView) mEmptyHeaderView.findViewById(R.id.cart_empty_hint_txtv);
    }

    @Override
    public void initEvent() {
        checkallBtn.setOnClickListener(this);
        buyBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
        formDataArray = new SPJsonArray();
        isAllCheck = false;
        isStoreAllCheck = false;
        isShowSmallLoading = false;
    }

    @Override
    public void initData() {
    }

    @Override
    public void onRefresh() {
        isOnlyRefresh = true;
        refreshData();
    }

    public void refreshData() {
        if (isShowSmallLoading) showLoadingSmallToast();
        if (SPMobileApplication.getInstance().isLogined()) {        //如果已经登录,隐藏登录按钮
            emptyHintTxtv.setText(getString(R.string.hint_cart_empty_login));
            mLoginBtn.setVisibility(View.INVISIBLE);
        } else {
            emptyHintTxtv.setText(getString(R.string.hint_cart_empty_nologin));
            mLoginBtn.setVisibility(View.VISIBLE);
        }
        if (isOnlyRefresh) formDataArray = null;        //如果是刷新数据,则表单数据置空
        SPShopRequest.getShopCartList(formDataArray, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                isShowSmallLoading = false;
                refreshRecyclerView.setRefreshing(false);
                mDataJson = (JSONObject) response;
                try {
                    if (mDataJson.has("stores")) {
                        mStores = (List<SPStore>) mDataJson.get("stores");
                        mAdapter.updateData(mStores);
                        dealModels(mStores);
                    } else {
                        mStores = new ArrayList<>();
                        mAdapter.updateData(mStores);
                        dealModels(null);
                    }
                    refreshCartNum();
                    isOnlyRefresh = false;
                } catch (Exception e) {
                    showToast(e.getMessage());
                    e.printStackTrace();
                }
                refreshEmptyData();
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                refreshEmptyData();
                refreshRecyclerView.setRefreshing(false);
                if (cacheDataArray != null)
                    formDataArray = cacheDataArray.clone();
                if (!SPUtils.isTokenExpire(errorCode))      //如果是token问题则不提示
                    showToast(msg);
            }
        });
    }

    private void refreshEmptyData() {
        if (mStores != null && mStores.size() > 0) {
            refreshRecyclerView.showData();
        } else {
            loadEmptyData();
            refreshRecyclerView.showEmpty();
        }
    }

    private void loadEmptyData() {
        SPShopRequest.guessYouLike(0, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    mProducts = (List<SPProduct>) response;
                    mEmptyAdapter.updateData(mProducts);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
            }
        });
    }

    private void refreshCartNum() {
        try {
            if (mDataJson.has("num")) {
                int num = mDataJson.getInt("num");
                SPShopCartManager shopCartManager = SPShopCartManager.getInstance(getActivity());
                shopCartManager.setShopCount(num);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将每一个购物车商品生产对应的表单数据缓存起来,以便表单提交试用
     */
    public void dealModels(List<SPStore> stores) {
        formDataArray = new SPJsonArray();
        isAllCheck = true;
        boolean isBuyButtnEnable = false;
        if (stores == null || stores.size() < 1) {
            totalFee = 0.00;
            cutFee = 0.00;
            refreshFeeView();
            checkallBtn.setBackgroundResource(R.drawable.icon_checkno);
            buyBtn.setEnabled(isBuyButtnEnable);
            return;
        }
        try {
            for (SPStore store : stores) {
                isStoreAllCheck = true;
                if (store != null && store.getStoreProducts() != null) {
                    for (SPProduct product : store.getStoreProducts()) {
                        JSONObject formJson = new JSONObject();
                        formJson.put("storeID", product.getStoreId());
                        formJson.put("cartID", product.getCartID());
                        formJson.put("goodsNum", product.getGoodsNum());
                        formJson.put("selected", product.getSelected());
                        if ("1".equals(product.getSelected()))        //验证购买按钮是否可以使用
                            isBuyButtnEnable = true;
                        if (product.getStoreCount() > 0 && "0".equals(product.getSelected()))        //如果没选中,店铺可以全选
                            isStoreAllCheck = false;
                        formDataArray.put(formJson);
                    }
                    if (isStoreAllCheck) {
                        store.setSelected("1");
                    } else {
                        store.setSelected("0");
                        isAllCheck = false;
                    }
                }
            }
            cacheDataArray = formDataArray.clone();
            if (mDataJson.has("totalFee"))
                totalFee = mDataJson.getDouble("totalFee");
            if (mDataJson.has("cutFee"))
                cutFee = mDataJson.getDouble("cutFee");
            buyBtn.setEnabled(isBuyButtnEnable);
            refreshFeeView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新商品总价
     */
    public void refreshFeeView() {
        String totalFeeFmt = "合计:¥" + totalFee;
        String cutFeeFmt = "共节省:¥" + cutFee;
        int startIndex = 3;
        int endIndex = totalFeeFmt.length();
        SpannableString totalFeeSpanStr = new SpannableString(totalFeeFmt);
        totalFeeSpanStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), startIndex, endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);      //设置区域内文字颜色为洋红色
        totalFeeSpanStr.setSpan(new RelativeSizeSpan(1.3f), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        totalfeeTxtv.setText(totalFeeSpanStr);
        cutfeeTxtv.setText(cutFeeFmt);
        if (isAllCheck)
            checkallBtn.setBackgroundResource(R.drawable.icon_checked);
        else
            checkallBtn.setBackgroundResource(R.drawable.icon_checkno);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkall_btn:        //全选或全不选
                isShowSmallLoading = true;
                checkAllOrNo();
                break;
            case R.id.buy_btn:        //去结算
                if (!SPMobileApplication.getInstance().isLogined()) {
                    showToastUnLogin();
                    Intent loginIntent = new Intent(getActivity(), SPLoginActivity_.class);
                    startActivityForResult(loginIntent, SPMobileConstants.Result_Code_Login_Refresh);
                    return;
                }
                gotoConfirmOrder();
                break;
            case R.id.login_btn:         //用户登录
                Intent loginIntent = new Intent(getActivity(), SPLoginActivity_.class);
                startActivityForResult(loginIntent, SPMobileConstants.Result_Code_Login_Refresh);
                break;
        }
    }

    public void gotoConfirmOrder() {
        Intent confirmIntent = new Intent(getActivity(), SPConfirmOrderActivity_.class);
        getActivity().startActivity(confirmIntent);
    }

    //减少商品数量
    @Override
    public void minuProductFromCart(SPProduct product) {
        try {
            boolean isReload = true;
            int length = formDataArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject cartJson = formDataArray.getJSONObject(i);
                String tempID = cartJson.getString("cartID");
                if (tempID.equals(product.getCartID())) {
                    Integer goodsNum = cartJson.getInt("goodsNum");
                    if (goodsNum > 1) {
                        goodsNum = goodsNum - 1;
                        cartJson.put("goodsNum", goodsNum);
                    } else {
                        isReload = false;
                    }
                    break;
                }
            }
            if (isReload) {
                isShowSmallLoading = true;
                refreshData();
            } else {
                showToast("不能再减了");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToast(e.getMessage());
        }
    }

    //增加商品数量
    @Override
    public void plusProductFromCart(SPProduct product) {
        try {
            isShowSmallLoading = true;
            boolean isReload = true;
            int length = formDataArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject cartJson = formDataArray.getJSONObject(i);
                String tempID = cartJson.getString("cartID");
                if (tempID.equals(product.getCartID())) {
                    Integer goodsNum = cartJson.getInt("goodsNum");
                    goodsNum = goodsNum + 1;
                    cartJson.put("goodsNum", goodsNum);
                    break;
                }
            }
            if (isReload)
                refreshData();
        } catch (Exception e) {
            e.printStackTrace();
            showToast(e.getMessage());
        }
    }

    @Override
    public void checkProductFromCart(SPProduct product, boolean checked) {
        try {
            String selected = checked ? "1" : "0";
            int length = formDataArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject cartJson = formDataArray.getJSONObject(i);
                String tempID = cartJson.getString("cartID");
                if (tempID.equals(product.getCartID())) {
                    cartJson.put("selected", selected);
                    break;
                }
            }
            refreshData();
        } catch (Exception e) {
            e.printStackTrace();
            showToast(e.getMessage());
        }
    }

    @Override
    public void deleteProductFromCart(SPProduct product) {
        currentProduct = product;
        showConfirmDialog("确定删除该商品？", "删除提醒", this, 1);
    }

    @Override
    public void onDetailClick(SPProduct product) {
        showDetail(product);
    }

    @Override
    public void onStoreCheck(SPStore store) {
        boolean needStoreCheckAll = false;        //是否可以全选
        try {
            int length = formDataArray.length();
            for (int i = 0; i < length; i++) {         //判断是否可以全选
                JSONObject cartJson = formDataArray.getJSONObject(i);
                if (store.getStoreId() == cartJson.getInt("storeID") && cartJson.getString("selected").equals("0")) {
                    needStoreCheckAll = true;
                    break;
                }
            }
            String selected = needStoreCheckAll ? "1" : "0";        //全选或反选
            for (int k = 0; k < length; k++) {
                JSONObject cartJson = formDataArray.getJSONObject(k);
                if (store.getStoreId() == cartJson.getInt("storeID")) {
                    JSONObject cartJson2 = formDataArray.getJSONObject(k);
                    cartJson2.put("selected", selected);
                }
            }
            refreshData();
        } catch (Exception e) {
            e.printStackTrace();
            showToast(e.getMessage());
        }
    }

    @Override
    public void onStoreClick(SPStore store) {
        Intent storeIntent = new Intent(SPMainActivity.getmInstance(), SPStoreHomeActivity_.class);
        storeIntent.putExtra("storeId", store.getStoreId());
        startActivity(storeIntent);
    }

    public void checkAllOrNo() {
        boolean needCheckAll = false;        //是否可以全选
        if (formDataArray == null) return;
        try {
            int length = formDataArray.length();
            for (int i = 0; i < length; i++) {          //判断是否可以全选
                JSONObject cartJson = formDataArray.getJSONObject(i);
                if (cartJson.getString("selected").equals("0")) {
                    needCheckAll = true;
                    break;
                }
            }
            String selected = needCheckAll ? "1" : "0";         //全选或反选
            int length2 = formDataArray.length();
            for (int j = 0; j < length2; j++) {
                JSONObject cartJson2 = formDataArray.getJSONObject(j);
                cartJson2.put("selected", selected);
            }
            refreshData();
        } catch (Exception e) {
            e.printStackTrace();
            showToast(e.getMessage());
        }
    }

    @Override
    public void clickOk(int actionType) {
        if (actionType == 1)
            deleteProductFromCart();
    }

    /**
     * 从购物车删除商品
     */
    public void deleteProductFromCart() {
        showLoadingSmallToast();
        SPShopRequest.deleteShopCartProductWithIds(currentProduct.getCartID(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                int count = Integer.valueOf(response.toString());
                SPShopCartManager.getInstance(getActivity()).setShopCount(count);
                showSuccessToast(msg);
                isOnlyRefresh = true;
                refreshData();
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showFailedToast(msg);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SPMobileConstants.Result_Code_Login_Refresh:
                refreshData();
                break;
        }
    }

    @Override
    public void onItemClick(SPProduct product) {
        showDetail(product);
    }

    private void showDetail(SPProduct product) {
        Intent intent = new Intent(getActivity(), SPProductDetailActivity_.class);
        intent.putExtra("goodsID", product.getGoodsID());
        getActivity().startActivity(intent);
    }

}
