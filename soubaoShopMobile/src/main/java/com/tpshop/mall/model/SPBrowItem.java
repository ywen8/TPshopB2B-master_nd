package com.tpshop.mall.model;

import java.io.Serializable;

/**
 * Created by zw on 2017/5/23
 */
public class SPBrowItem implements SPModel, Serializable, Comparable<SPBrowItem> {

    private String date;
    private int visitId;
    private int goodsID;
    private int visitTime;
    private int categoryID;
    private String goodsName;
    private String shopPrice;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "visitId", "visit_id",
                "goodsID", "goods_id",
                "visitTime", "visittime",
                "goodsName", "goods_name",
                "shopPrice", "shop_price",
                "categoryID", "cat_id3",
        };
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setVisitId(int visitId) {
        this.visitId = visitId;
    }

    public int getVisitId() {
        return visitId;
    }

    public void setGoodsID(int goodsID) {
        this.goodsID = goodsID;
    }

    public int getGoodsID() {
        return goodsID;
    }

    public void setVisitTime(int visitTime) {
        this.visitTime = visitTime;
    }

    public int getVisitTime() {
        return visitTime;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setShopPrice(String shopPrice) {
        this.shopPrice = shopPrice;
    }

    public String getShopPrice() {
        return shopPrice;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    @Override
    public int compareTo(SPBrowItem another) {
        if (visitTime < another.visitTime) {
            return -1;
        } else if (visitTime > another.visitTime) {
            return 1;
        } else {
            return 0;
        }
    }

}
