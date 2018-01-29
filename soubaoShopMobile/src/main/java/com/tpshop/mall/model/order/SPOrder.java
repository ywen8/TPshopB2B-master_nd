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
 * Date: @date 2015年10月27日 下午9:14:42
 * Description: 订单  model
 *
 * @version V1.0
 */
package com.tpshop.mall.model.order;

import com.tpshop.mall.model.SPModel;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.model.shop.SPStore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2016/6/29
 */
public class SPOrder implements SPModel, Serializable {

    private int payBtn;                                //支付按钮
    private int storeId;                               //店铺ID
    private String city;                               //城市
    private String town;                               //街道/镇
    private String email;
    private String isCod;                              //是否货到付款
    private int orderType;                             //订单类型
    private int payStatus;                             //支付状态
    private String mobile;
    private SPStore store;                             //店铺信息
    private int cancelBtn;                             //取消订单按钮
    private int returnBtn;                             //联系客服/退换货
    private String address;                            //详细地址
    private String payCode;                            //支付code
    private String payName;                            //支付方式名称
    private String orderID;                            //订单ID
    private String orderSN;                            //订单编号
    private String addTime;                            //创建时间
    private String storeQQ;                            //联系QQ
    private int receiveBtn;                            //确认收货按钮
    private int commentBtn;                            //评价按钮
    private String province;                           //省份
    private String district;                           //地区
    private String integral;                           //使用积分
    private String userNote;                           //发票抬头
    private int orderStatus;                           //订单状态号
    private SPButtom buttom;                           //按钮组合
    private String taxpayer;                           //纳税人识别号
    private int shippingBtn;                           //查看物流
    private String invoiceNO;                          //快递单号
    private String storeName;                          //店铺名称
    private String userMoney;                          //使用余额
    private String consignee;                          //收货人
    private String storePhone;                         //联系电话
    private String goodsPrice;                         //商品总额
    private boolean isVirtual;                         //是否是虚拟订单
    private String couponPrice;                        //优惠券金额
    private String orderAmount;                        //应付款金额
    private String shippingTime;                       //发货时间
    private String invoiceTitle;                       //发票抬头
    private String totalAddress;                       //完整地址
    private String shippingCode;                       //物流code
    private String shippingName;                       //物流名称
    private String shippingPrice;                      //运费
    private String integralMoney;                      //积分抵扣金额
    private String shippingStatus;                     //发货状态
    private String orderStatusCode;                    //订单状态:英文
    private String orderStatusDesc;                    //订单状态:中文
    private String orderPromAmount;                    //活动优惠
    private List<SPProduct> products;                  //商品列表
    private List<SPVrorder> vrorders;
    private transient JSONArray vrorderArray;
    private transient JSONObject storeObject;
    private transient JSONObject buttomObject;
    private transient JSONArray productsArray;

    public SPOrder() {
    }

    public SPOrder(String orderSN, String orderAmount, boolean isVirtual) {
        this.orderSN = orderSN;
        this.orderAmount = orderAmount;
        this.isVirtual = isVirtual;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "orderID", "order_id",
                "orderSN", "order_sn",
                "orderType", "order_prom_type",
                "orderStatus", "order_status",
                "mobile", "mobile",
                "vrorderArray", "vrorder",
                "shippingStatus", "shipping_status",
                "shippingPrice", "shipping_price",
                "payStatus", "pay_status",
                "shippingCode", "shipping_code",
                "shippingName", "shipping_name",
                "payCode", "pay_code",
                "payName", "pay_name",
                "orderPromAmount", "order_prom_amount",
                "invoiceTitle", "invoice_title",
                "taxpayer", "taxpayer",
                "orderAmount", "order_amount",
                "shippingTime", "shipping_time",
                "isCod", "is_cod",
                "invoiceNO", "invoice_no",
                "goodsPrice", "goods_price",
                "couponPrice", "coupon_price",
                "integralMoney", "integral_money",
                "userMoney", "user_money",
                "town", "twon",
                "addTime", "add_time",
                "storeId", "store_id",
                "storeName", "store_name",
                "storeQQ", "store_qq",
                "storePhone", "store_phone",
                "totalAddress", "total_address",
                "userNote", "user_note",
                "buttomObject", "order_button",
                "storeObject", "store",
                "productsArray", "order_goods",
                "payBtn", "pay_btn",
                "cancelBtn", "cancel_btn",
                "receiveBtn", "receive_btn",
                "commentBtn", "comment_btn",
                "shippingBtn", "shipping_btn",
                "returnBtn", "return_btn",
                "orderStatusCode", "order_status_code",
                "orderStatusDesc", "order_status_detail",
        };
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }

    public String getOrderSN() {
        return orderSN;
    }

    public void setOrderSN(String orderSN) {
        this.orderSN = orderSN;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public String getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(String shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotalAddress() {
        return totalAddress;
    }

    public void setTotalAddress(String totalAddress) {
        this.totalAddress = totalAddress;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<SPVrorder> getVrorders() {
        return vrorders;
    }

    public void setVrorders(List<SPVrorder> vrorders) {
        this.vrorders = vrorders;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getShippingCode() {
        return shippingCode;
    }

    public void setShippingCode(String shippingCode) {
        this.shippingCode = shippingCode;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getTaxpayer() {
        return taxpayer;
    }

    public void setTaxpayer(String taxpayer) {
        this.taxpayer = taxpayer;
    }

    public String getInvoiceNO() {
        return invoiceNO;
    }

    public void setInvoiceNO(String invoiceNO) {
        this.invoiceNO = invoiceNO;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public boolean isVirtual() {
        return isVirtual;
    }

    public void setVirtual(boolean isVirtual) {
        this.isVirtual = isVirtual;
    }

    public String getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(String shippingTime) {
        this.shippingTime = shippingTime;
    }

    public String getIsCod() {
        return isCod;
    }

    public void setIsCod(String isCod) {
        this.isCod = isCod;
    }

    public SPButtom getButtom() {
        return buttom;
    }

    public void setButtom(SPButtom buttom) {
        this.buttom = buttom;
    }

    public SPStore getStore() {
        return store;
    }

    public void setStore(SPStore store) {
        this.store = store;
    }

    public List<SPProduct> getProducts() {
        return products;
    }

    public void setProducts(List<SPProduct> products) {
        this.products = products;
    }

    public JSONObject getButtomObject() {
        return buttomObject;
    }

    public void setButtomObject(JSONObject buttomObject) {
        this.buttomObject = buttomObject;
    }

    public JSONObject getStoreObject() {
        return storeObject;
    }

    public void setStoreObject(JSONObject storeObject) {
        this.storeObject = storeObject;
    }

    public JSONArray getProductsArray() {
        return productsArray;
    }

    public void setProductsArray(JSONArray productsArray) {
        this.productsArray = productsArray;
    }

    public JSONArray getVrorderArray() {
        return vrorderArray;
    }

    public void setVrorderArray(JSONArray vrorderArray) {
        this.vrorderArray = vrorderArray;
    }

    public int getPayBtn() {
        return payBtn;
    }

    public void setPayBtn(int payBtn) {
        this.payBtn = payBtn;
    }

    public int getCancelBtn() {
        return cancelBtn;
    }

    public void setCancelBtn(int cancelBtn) {
        this.cancelBtn = cancelBtn;
    }

    public int getReceiveBtn() {
        return receiveBtn;
    }

    public void setReceiveBtn(int receiveBtn) {
        this.receiveBtn = receiveBtn;
    }

    public int getCommentBtn() {
        return commentBtn;
    }

    public void setCommentBtn(int commentBtn) {
        this.commentBtn = commentBtn;
    }

    public int getShippingBtn() {
        return shippingBtn;
    }

    public void setShippingBtn(int shippingBtn) {
        this.shippingBtn = shippingBtn;
    }

    public int getReturnBtn() {
        return returnBtn;
    }

    public void setReturnBtn(int returnBtn) {
        this.returnBtn = returnBtn;
    }

    public String getOrderStatusCode() {
        return orderStatusCode;
    }

    public void setOrderStatusCode(String orderStatusCode) {
        this.orderStatusCode = orderStatusCode;
    }

    public String getOrderStatusDesc() {
        return orderStatusDesc;
    }

    public void setOrderStatusDesc(String orderStatusDesc) {
        this.orderStatusDesc = orderStatusDesc;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(String couponPrice) {
        this.couponPrice = couponPrice;
    }

    public String getIntegralMoney() {
        return integralMoney;
    }

    public void setIntegralMoney(String integralMoney) {
        this.integralMoney = integralMoney;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(String userMoney) {
        this.userMoney = userMoney;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public String getStoreQQ() {
        return storeQQ;
    }

    public void setStoreQQ(String storeQQ) {
        this.storeQQ = storeQQ;
    }

    public String getOrderPromAmount() {
        return orderPromAmount;
    }

    public void setOrderPromAmount(String orderPromAmount) {
        this.orderPromAmount = orderPromAmount;
    }

}
