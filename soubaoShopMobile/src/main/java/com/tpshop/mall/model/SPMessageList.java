package com.tpshop.mall.model;

import org.json.JSONObject;

/**
 * @author liuhao
 */
public class SPMessageList implements SPModel {

    private String messageId;
    private String messageType;                               //0系统消息,1物流通知,2优惠促销,3商品提醒,4我的资产,5商城好店
    private String messageSendTime;
    private SPMessageListData messageData;                    //商品列表
    private transient JSONObject messageDataArray;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageSendTime() {
        return messageSendTime;
    }

    public void setMessageSendTime(String messageSendTime) {
        this.messageSendTime = messageSendTime;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public SPMessageListData getMessageData() {
        return messageData;
    }

    public void setMessageData(SPMessageListData messageData) {
        this.messageData = messageData;
    }

    public JSONObject getMessageDataArray() {
        return messageDataArray;
    }

    public void setMessageDataArray(JSONObject messageDataArray) {
        this.messageDataArray = messageDataArray;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "messageId", "message_id",
                "messageSendTime", "status",
                "messageType", "type",
                "messageDataArray", "data",
        };
    }

}
