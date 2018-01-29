package com.tpshop.mall.model.distribute;

import com.tpshop.mall.model.SPModel;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zw on 2017/6/7
 */
public class SPDistributeOrder implements SPModel, Serializable {

    private int level;                         //获佣用户级别
    private int userId;                        //上级用户id
    private int status;
    private int orderId;                       //订单id
    private int confirm;                       //确定收货时间
    private int storeId;                       //店铺id
    private String money;                      //佣金
    private int buyuserId;                     //下级购买者id
    private String orderSn;                    //订单序号
    private int createTime;                    //分成记录生成时间
    private String nickName;                   //购买者昵称
    private int confirmTime;                   //确定分成或者取消时间
    private String goodsPrice;                 //商品价格
    private JSONArray goodsArray;
    private List<SPDistributeGood> distributeGoods;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setBuyuserId(int buyuserId) {
        this.buyuserId = buyuserId;
    }

    public int getBuyuserId() {
        return buyuserId;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMoney() {
        return money;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setConfirm(int confirm) {
        this.confirm = confirm;
    }

    public int getConfirm() {
        return confirm;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setConfirmTime(int confirmTime) {
        this.confirmTime = confirmTime;
    }

    public int getConfirmTime() {
        return confirmTime;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setGoodsArray(JSONArray goodsArray) {
        this.goodsArray = goodsArray;
    }

    public JSONArray getGoodsArray() {
        return goodsArray;
    }

    public void setDistributeGoods(List<SPDistributeGood> distributeGoods) {
        this.distributeGoods = distributeGoods;
    }

    public List<SPDistributeGood> getDistributeGoods() {
        return distributeGoods;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "userId", "user_id",
                "buyuserId", "buy_user_id",
                "nickName", "nickname",
                "orderSn", "order_sn",
                "orderId", "order_id",
                "goodsPrice", "goods_price",
                "money", "money",
                "level", "level",
                "createTime", "create_time",
                "confirm", "confirm",
                "status", "status",
                "confirmTime", "confirm_time",
                "storeId", "store_id",
                "goodsArray", "goods_list",
        };
    }

}
