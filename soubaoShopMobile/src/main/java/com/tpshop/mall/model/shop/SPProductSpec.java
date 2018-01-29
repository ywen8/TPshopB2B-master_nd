package com.tpshop.mall.model.shop;

import android.support.annotation.NonNull;

import com.tpshop.mall.model.SPModel;

import java.io.Serializable;

/**
 * Created by admin on 2016/6/20
 */
public class SPProductSpec implements SPModel, Serializable, Comparable<SPProductSpec> {

    private String src;                     //URL
    private String item;                    //规格值
    private String itemID;                  //规格ID
    private String specName;                //规格名称

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "itemID", "item_id",
                "specName", "spec_name"
        };
    }

    @Override
    public int compareTo(@NonNull SPProductSpec another) {
        return this.specName.compareTo(another.specName);
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

}
