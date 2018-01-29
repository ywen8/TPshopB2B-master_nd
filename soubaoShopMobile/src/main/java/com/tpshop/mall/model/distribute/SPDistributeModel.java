package com.tpshop.mall.model.distribute;

import com.tpshop.mall.model.SPModel;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by zw on 2017/6/6
 */
public class SPDistributeModel implements SPModel, Serializable {

    private String reward;
    private int storeTime;
    private String storeName;
    private String salesVolume;
    private JSONObject userObjct;
    private JSONObject moneyObjct;
    private SPUserModel userModel;
    private SPMoneyModel moneyModel;

    public void setSalesVolume(String salesVolume) {
        this.salesVolume = salesVolume;
    }

    public String getSalesVolume() {
        return salesVolume;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getReward() {
        return reward;
    }

    public void setMoneyModel(SPMoneyModel moneyModel) {
        this.moneyModel = moneyModel;
    }

    public SPMoneyModel getMoneyModel() {
        return moneyModel;
    }

    public void setMoneyObjct(JSONObject moneyObjct) {
        this.moneyObjct = moneyObjct;
    }

    public JSONObject getMoneyObjct() {
        return moneyObjct;
    }

    public void setStoreTime(int storeTime) {
        this.storeTime = storeTime;
    }

    public int getStoreTime() {
        return storeTime;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setUserModel(SPUserModel userModel) {
        this.userModel = userModel;
    }

    public SPUserModel getUserModel() {
        return userModel;
    }

    public void setUserObjct(JSONObject userObjct) {
        this.userObjct = userObjct;
    }

    public JSONObject getUserObjct() {
        return userObjct;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "salesVolume", "sales_volume",
                "reward", "reward",
                "moneyObjct", "money",
                "storeTime", "store_time",
                "storeName", "store_name",
                "userObjct", "user",
        };
    }

}
