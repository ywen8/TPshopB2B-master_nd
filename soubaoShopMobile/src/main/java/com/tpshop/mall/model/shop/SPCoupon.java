package com.tpshop.mall.model.shop;

import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.model.SPModel;

import java.io.Serializable;

/**
 * Created by admin on 2016/6/27
 */
public class SPCoupon implements SPModel, Serializable {

    private int isget;                             //是否已经领取
    private int storeId;
    private String type;                           //发放类型
    private String code;                           //优惠券兑换码
    private int sendNum;                           //领取数量
    private String name;                           //名称
    private String money;                          //金额(面值金额)
    private String image;
    private String title;
    private String typeID;                         //优惠券类型ID
    private String userID;                         //用户ID
    private int createNum;                         //发放数量
    private String orderID;                        //订单ID
    private String useTime;                        //使用时间
    private int couponType;                        //优惠券类型,1:代金券(列表选择),2:优惠码(文本框输入)
    private String couponID;                       //优惠券ID
    private String sendTime;                       //发放时间
    private boolean isCheck;                       //是否选择使用,为了适配页面而增加的属性,对应数据库不存在该字段(只有couponType为2时才设置该值)
    private String condition;                      //使用条件
    private String useEndTime;                     //过期时间
    private String limitStore;                     //限店铺
    private String useStartTime;                   //开始时间
    private String limitCateogory;                 //限品类

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "couponID", "id",
                "typeID", "cid",
                "typeID", "uid",
                "orderID", "order_id",
                "useTime", "use_time",
                "sendTime", "send_time",
                "sendTime", "send_time",
                "storeId", "store_id",
                "createNum", "createnum",
                "sendNum", "send_num",
                "limitCateogory", "limit_cateogory",
                "limitStore", "limit_store",
                "name", "name",
                "money", "money",
                "useStartTime", "use_start_time",
                "useEndTime", "use_end_time",
                "couponObj", "coupon",
        };
    }

    public int getCreateNum() {
        return createNum;
    }

    public void setCreateNum(int createNum) {
        this.createNum = createNum;
    }

    public int getSendNum() {
        return sendNum;
    }

    public void setSendNum(int sendNum) {
        this.sendNum = sendNum;
    }

    public int getIsget() {
        return isget;
    }

    public void setIsget(int isget) {
        this.isget = isget;
    }

    public String getCouponID() {
        return couponID;
    }

    public void setCouponID(String couponID) {
        this.couponID = couponID;
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getCode() {
        if (SPStringUtils.isEmpty(code)) return "无";
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getUseEndTime() {
        return useEndTime;
    }

    public void setUseEndTime(String useEndTime) {
        this.useEndTime = useEndTime;
    }

    public String getUseStartTime() {
        return useStartTime;
    }

    public void setUseStartTime(String useStartTime) {
        this.useStartTime = useStartTime;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getCouponType() {
        return couponType;
    }

    public void setCouponType(int couponType) {
        this.couponType = couponType;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getLimitCateogory() {
        return limitCateogory;
    }

    public void setLimitCateogory(String limitCateogory) {
        this.limitCateogory = limitCateogory;
    }

    public String getLimitStore() {
        if (limitStore == null) return "全平台";
        return limitStore;
    }

    public void setLimitStore(String limitStore) {
        this.limitStore = limitStore;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
