package com.tpshop.mall.model.distribute;

import com.tpshop.mall.model.SPModel;

import java.io.Serializable;

/**
 * Created by zw on 2017/6/13
 */
public class SPStorePic implements SPModel, Serializable {

    private String storeImg;              //店铺图
    private String storeName;             //店铺名称

    public void setStoreImg(String storeImg) {
        this.storeImg = storeImg;
    }

    public String getStoreImg() {
        return storeImg;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreName() {
        return storeName;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "storeImg", "store_img",
                "storeName", "store_name",
        };
    }

}
