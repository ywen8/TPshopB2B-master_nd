package com.tpshop.mall.model.distribute;

import com.tpshop.mall.model.SPModel;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by zw on 2017/6/9
 */
public class SPStoreInfo implements SPModel, Serializable {

    private int hadAdd;                      //已上架数量
    private int waitAdd;                     //待上架数量
    private String headPic;                  //用户头像
    private SPStorePic storePic;
    private JSONObject storeObject;

    public void setWaitAdd(int waitAdd) {
        this.waitAdd = waitAdd;
    }

    public int getWaitAdd() {
        return waitAdd;
    }

    public void setHadAdd(int hadAdd) {
        this.hadAdd = hadAdd;
    }

    public int getHadAdd() {
        return hadAdd;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setStoreObject(JSONObject storeObject) {
        this.storeObject = storeObject;
    }

    public JSONObject getStoreObject() {
        return storeObject;
    }

    public void setStorePic(SPStorePic storePic) {
        this.storePic = storePic;
    }

    public SPStorePic getStorePic() {
        return storePic;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "waitAdd", "wait_add_num",
                "hadAdd", "had_add_num",
                "headPic", "head_pic",
                "storeObject", "store",
        };
    }

}
