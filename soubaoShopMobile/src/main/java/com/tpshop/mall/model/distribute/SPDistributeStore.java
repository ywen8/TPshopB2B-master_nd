package com.tpshop.mall.model.distribute;

import com.tpshop.mall.model.SPModel;

import java.io.Serializable;

/**
 * Created by zw on 2017/6/8
 */
public class SPDistributeStore implements SPModel, Serializable {

    private String qQ;
    private String tel;
    private String trueName;
    private String storeImg;
    private String storeName;

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setqQ(String qQ) {
        this.qQ = qQ;
    }

    public String getqQ() {
        return qQ;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTel() {
        return tel;
    }

    public void setStoreImg(String storeImg) {
        this.storeImg = storeImg;
    }

    public String getStoreImg() {
        return storeImg;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "storeName", "store_name",
                "trueName", "true_name",
                "qQ", "qq",
                "tel", "mobile",
                "storeImg", "store_img",
        };
    }

}
