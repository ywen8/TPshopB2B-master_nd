package com.tpshop.mall.model.distribute;

import com.tpshop.mall.model.SPModel;

import java.io.Serializable;

/**
 * Created by zw on 2017/6/7
 */
public class SPDistributeGood implements SPModel, Serializable {

    private int orderId;
    private int goodsId;
    private int goodsNum;                   //商品数量
    private String goodsSn;                 //商品编号
    private int commission;
    private String specName;                //商品属性
    private String costPrice;               //节省金额
    private String goodsName;
    private String distribut;               //商品佣金
    private String goodsPrice;              //商品总价
    private String marketPrice;             //市场价
    private String originalImg;

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

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

    public void setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn;
    }

    public String getGoodsSn() {
        return goodsSn;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getSpecName() {
        return specName;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public int getCommission() {
        return commission;
    }

    public void setDistribut(String distribut) {
        this.distribut = distribut;
    }

    public String getDistribut() {
        return distribut;
    }

    public void setOriginalImg(String originalImg) {
        this.originalImg = originalImg;
    }

    public String getOriginalImg() {
        return originalImg;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "orderId", "order_id",
                "goodsId", "goods_id",
                "goodsName", "goods_name",
                "goodsSn", "goods_sn",
                "goodsNum", "goods_num",
                "marketPrice", "market_price",
                "goodsPrice", "goods_price",
                "costPrice", "cost_price",
                "specName", "spec_key_name",
                "commission", "commission",
                "distribut", "distribut",
                "originalImg", "original_img",
        };
    }

}
