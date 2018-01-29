package com.tpshop.mall.model;

import com.tpshop.mall.utils.SPUtils;

/**
 * @author liuhao
 */
public class SPMessageListData implements SPModel {

    private String messageDataTitle;                      //0系统消息,1物流通知,2优惠促销,3商品提醒,4我的资产,5商城好店
    private String messageDataCover;
    private String messageDataMoney;
    private String messageDataOrderId;
    private String messageDataGoodsId;
    private String messageDataPostTime;
    private String messageDataChangeType;
    private String messageDataDiscription;

    public String getTime() {
        if (messageDataPostTime != null) {
            return SPUtils.getTimeFormPhpTime(Long.parseLong(messageDataPostTime));
        } else {
            return "数据异常";
        }
    }

    public String getMessageDataTitle() {
        return messageDataTitle;
    }

    public void setMessageDataTitle(String messageDataTitle) {
        this.messageDataTitle = messageDataTitle;
    }

    public String getMessageDataPostTime() {
        return messageDataPostTime;
    }

    public void setMessageDataPostTime(String messageDataPostTime) {
        this.messageDataPostTime = messageDataPostTime;
    }

    public String getMessageDataOrderId() {
        return messageDataOrderId;
    }

    public void setMessageDataOrderId(String messageDataOrderId) {
        this.messageDataOrderId = messageDataOrderId;
    }

    public String getMessageDataDiscription() {
        return messageDataDiscription;
    }

    public void setMessageDataDiscription(String messageDataDiscription) {
        this.messageDataDiscription = messageDataDiscription;
    }

    public String getMessageDataGoodsId() {
        return messageDataGoodsId;
    }

    public void setMessageDataGoodsId(String messageDataGoodsId) {
        this.messageDataGoodsId = messageDataGoodsId;
    }

    public String getMessageDataCover() {
        return messageDataCover;
    }

    public void setMessageDataCover(String messageDataCover) {
        this.messageDataCover = messageDataCover;
    }

    public String getMessageDataMoney() {
        return messageDataMoney;
    }

    public void setMessageDataMoney(String messageDataMoney) {
        this.messageDataMoney = messageDataMoney;
    }

    public String getMessageDataChangeType() {
        return messageDataChangeType;
    }

    public void setMessageDataChangeType(String messageDataChangeType) {
        this.messageDataChangeType = messageDataChangeType;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "messageDataTitle", "title",
                "messageDataPostTime", "post_time",
                "messageDataOrderId", "order_id",
                "messageDataDiscription", "discription",
                "messageDataGoodsId", "goods_id",
                "messageDataCover", "cover",
                "messageDataMoney", "money",
                "messageDataChangeType", "change_type",
        };
    }

}
