/*
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2099 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: 飞龙  wangqh01292@163.com
 * Date: @date 2015年11月4日 下午10:10:15
 * Description:{一句话描述该类的作用}
 *
 * @version V1.0
 */
package com.tpshop.mall.model;

import java.io.Serializable;

/**
 * @author liuhao
 */
public class SPServiceComment implements SPModel, Serializable, Comparable<SPServiceComment> {

    private String recId;                          //订单商品id
    private String addTime;                        //时间
    private String orderSn;                        //订单序号
    private String orderId;                        //订单id
    private String goodsId;                        //商品id
    private String storeId;                        //店铺id
    private String goodsNum;                       //商品数量
    private String isComment;                      //是否已评论
    private String goodsName;                      //商品名称
    private String goodsPrice;                     //商品价格
    private String orderAmount;                    //应付款金额

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getIsComment() {
        return isComment;
    }

    public void setIsComment(String isComment) {
        this.isComment = isComment;
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "orderAmount", "order_amount",
                "addTime", "add_time",
                "orderSn", "order_sn",
                "orderId", "order_id",
                "goodsId", "goods_id",
                "goodsName", "goods_name",
                "storeId", "store_id",
                "goodsPrice", "goods_price",
                "goodsNum", "goods_num",
                "isComment", "is_comment",
                "recId", "rec_id"
        };
    }

    @Override
    public int compareTo(SPServiceComment another) {
        if (storeId.length() != 0) {
            if (Integer.parseInt(storeId) < Integer.parseInt(another.storeId)) {
                return -1;
            } else if (Integer.parseInt(storeId) > Integer.parseInt(another.storeId)) {
                return 1;
            } else {
                return 0;
            }
        }
        return 0;
    }

}
