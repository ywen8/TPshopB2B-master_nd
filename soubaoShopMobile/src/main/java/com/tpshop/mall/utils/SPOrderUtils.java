/*
 * tpshop
 * ============================================================================
 * * 版权所有 2015-2027 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ----------------------------------------------------------------------------
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * $Author: 飞龙  15/12/1 $
 * $description: 订单公共操作类
 */
package com.tpshop.mall.utils;

import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.model.order.SPOrder;

/**
 * Created by admin on 2016/7/1
 */
public class SPOrderUtils {

    private static final String typeAll = "";                                  //全部订单
    private static final String typeWaitPay = "WAITPAY";                       //待支付
    private static final String typeWaitSend = "WAITSEND";                     //待发货
    private static final String typeReturned = "RETURNED";                     //已退货
    private static final String typeCommented = "COMMENTED";                   //已评价
    private static final String typeWaitReceive = "WAITRECEIVE";               //待收货
    public static final String typeWaitComment = "WAITCCOMMENT";               //待评价

    public static OrderStatus getOrderStatusByValue(int value) {
        switch (value) {
            case 1:
                return OrderStatus.waitPay;
            case 2:
                return OrderStatus.waitSend;
            case 3:
                return OrderStatus.waitReceive;
            case 4:
                return OrderStatus.waitComment;
            case 5:
                return OrderStatus.commented;
            case 6:
                return OrderStatus.returned;
            case 7:
                return OrderStatus.all;
        }
        return OrderStatus.all;
    }

    /**
     * 订单状态
     */
    public enum OrderStatus {
        waitPay(1, "待付款"),
        waitSend(2, "待发货"),
        waitReceive(3, "待收货"),
        waitComment(4, "待评价"),
        commented(5, "已评价"),
        returned(6, "已退货"),
        all(7, "全部订单");
        private String name;
        private int value;

        OrderStatus(int value, String name) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int value() {
            return value;
        }
    }

    /**
     * 根据订单状态获取标题
     */
    public static String getOrderTitlteWithOrderStatus(OrderStatus orderStatus) {
        switch (orderStatus) {
            case waitPay:
                return "待付款";
            case waitSend:
                return "待发货";
            case waitReceive:
                return "待收货";
            case waitComment:
                return "待评价";
            case commented:
                return "已评价";
            case returned:
                return "已退货";
            case all:
                return "全部订单";
        }
        return "全部订单";
    }

    /**
     * 根据订单状态返回订单类型
     */
    public static String getOrderTypeWithOrderStatus(OrderStatus orderStatus) {
        switch (orderStatus) {
            case waitPay:
                return typeWaitPay;
            case waitSend:
                return typeWaitSend;
            case waitReceive:
                return typeWaitReceive;
            case waitComment:
                return typeWaitComment;
            case commented:
                return typeCommented;
            case returned:
                return typeReturned;
            case all:
                return typeAll;
        }
        return typeAll;
    }

    public static int getProductCount(SPOrder order) {
        if (order == null || order.getProducts() == null || order.getProducts().size() < 1)
            return 0;
        int count = 0;
        for (SPProduct product : order.getProducts())
            count += product.getGoodsNum();
        return count;
    }

}
