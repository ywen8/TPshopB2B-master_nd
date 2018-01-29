package com.tpshop.mall.model.distribute;

import com.tpshop.mall.model.SPModel;

import java.io.Serializable;

/**
 * Created by zw on 2017/6/9
 */
public class SPStoreGood implements SPModel, Serializable {

    private int goodsId;
    private String goodsName;
    private String goodsPrice;

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsPrice(String goods_price) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "goodsId", "goods_id",
                "goodsName", "goods_name",
                "goodsPrice", "shop_price",
        };
    }

}
