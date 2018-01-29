package com.tpshop.mall.model;

/**
 * @author liuhao
 */
public class SPMessageNotice implements SPModel {

    private String message;
    private String messageId;
    private String messageTime;
    private String messageType;                   //0系统消息,1物流通知,2优惠促销,3商品提醒,4我的资产,5商城好店
    private String messageStatus;
    private String messageCategory;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageCategory() {
        return messageCategory;
    }

    public void setMessageCategory(String messageCategory) {
        this.messageCategory = messageCategory;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "messageCategory", "category",
                "messageId", "message_id",
                "messageStatus", "status",
                "messageTime", "send_time",
                "messageType", "type",
                "message", "message",
        };
    }

}
