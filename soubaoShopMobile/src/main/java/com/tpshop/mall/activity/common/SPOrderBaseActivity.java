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
 * Date: @date 2015年10月20日 下午7:52:58
 * Description:Activity 订单相关基类
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.common;

import android.content.Intent;

import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPPersonRequest;
import com.tpshop.mall.model.order.SPOrder;

/**
 * @author 飞龙
 */
public abstract class SPOrderBaseActivity extends SPBaseActivity {

    /**
     * 去支付
     */
    public void gotoPay(SPOrder order) {
        SPMobileApplication.getInstance().orderId = order.getOrderID();
        Intent payIntent = new Intent(this, SPPayListActivity_.class);
        payIntent.putExtra("order", order);
        startActivityForResult(payIntent, SPMobileConstants.Result_Code_PAY);
    }

    /**
     * 取消订单
     */
    public void cancelOrder(String orderId, SPSuccessListener successListener, SPFailuredListener failuredListener) {
        SPPersonRequest.cancelOrderWithOrderID(orderId, successListener, failuredListener);
    }

    /**
     * 取消订单
     */
    public void cancelOrder2(String orderId, SPSuccessListener successListener, SPFailuredListener failuredListener) {
        SPPersonRequest.cancelOrder(orderId, successListener, failuredListener);
    }

    /**
     * 确认收货
     */
    public void confirmOrderWithOrderID(String orderId, SPSuccessListener successListener, SPFailuredListener failuredListener) {
        SPPersonRequest.confirmOrderWithOrderID(orderId, successListener, failuredListener);
    }

    /**
     * 查看物流
     */
    public void lookShopping(SPOrder order) {
        String shippingUrl = String.format(SPMobileConstants.SHIPPING_URL, order.getOrderID());
        startWebViewActivity(shippingUrl, "查看物流");
    }

}
