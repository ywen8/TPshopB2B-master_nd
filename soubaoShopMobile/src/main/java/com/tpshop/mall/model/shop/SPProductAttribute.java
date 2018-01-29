package com.tpshop.mall.model.shop;

import com.tpshop.mall.model.SPModel;

import java.io.Serializable;

/**
 * Created by admin on 2016/6/20
 */
public class SPProductAttribute implements SPModel, Serializable {

    private String attrID;                        //属性ID
    private String goodsID;                       //商品ID
    private String attrName;                      //属性名称
    private String attrValue;                     //属性值
    private String attrPrice;                     //属性价格

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "attrID", "attr_id",
                "goodsID", "goods_id",
                "attrValue", "attr_value",
                "attrPrice", "attr_price",
                "attrName", "attr_name"
        };
    }

    public String getAttrID() {
        return attrID;
    }

    public void setAttrID(String attrID) {
        this.attrID = attrID;
    }

    public String getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(String goodsID) {
        this.goodsID = goodsID;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    public String getAttrPrice() {
        return attrPrice;
    }

    public void setAttrPrice(String attrPrice) {
        this.attrPrice = attrPrice;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

}
