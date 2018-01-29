package com.tpshop.mall.global;

import android.content.Context;
import android.content.Intent;

import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.shop.SPShopRequest;

/**
 * Created by admin on 2016/6/21
 */
public class SPShopCartManager {

    private int shopCount;
    private Context mContext;
    private static SPShopCartManager instance;

    public static SPShopCartManager getInstance(Context context) {
        if (instance == null) {
            instance = new SPShopCartManager();
            instance.mContext = context;
            if (SPMobileApplication.getInstance().isLogined())
                instance.initData();
        }
        return instance;
    }

    public void initData() {
        shopCount = 0;
    }

    /**
     * 加入购物车
     */
    public void shopCartGoodsOperation(String goodsID, int itemId, int number, final SPSuccessListener success, final SPFailuredListener failure) {
        SPShopRequest.shopCartGoodsOperation(goodsID, itemId, number, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    shopCount = Integer.valueOf(response.toString());
                    if (success != null)
                        success.onRespone(msg, shopCount);
                    if (mContext != null)
                        mContext.sendBroadcast(new Intent(SPMobileConstants.ACTION_SHOPCART_CHNAGE));
                }
            }
        }, new SPFailuredListener(failure.getViewController()) {
            @Override
            public void onRespone(String msg, int errorCode) {
                if (failure != null) failure.onRespone(msg, errorCode);
            }
        });
    }

    public void reloadCart() {
        initData();
    }

    public int getShopCount() {
        return shopCount;
    }

    public void setShopCount(int shopCount) {
        this.shopCount = shopCount;
    }

}
