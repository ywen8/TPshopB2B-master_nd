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
 * Date: @date 2015年11月26日 下午10:13:37
 * Description: 微信支付 -> 微信支付信息
 *
 * @version V1.0
 */
package com.tpshop.mall.model.shop;

import com.tpshop.mall.model.SPModel;

/**
 * Created by admin on 2016/10/9
 */
public class WxPayInfo implements SPModel {

    private String sign;                           //签名
    private String appid;                          //应用ID
    private String extData;                        //额外字段:订单信息
    private String prepayid;                       //商户号
    private String noncestr;                       //预支付交易会话ID
    private String timestamp;                      //时间戳
    private String partnerid;                      //商户号
    private String packageValue;                   //扩展字段

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "packageValue", "package"
        };
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getExtData() {
        return extData;
    }

    public void setExtData(String extData) {
        this.extData = extData;
    }

}
