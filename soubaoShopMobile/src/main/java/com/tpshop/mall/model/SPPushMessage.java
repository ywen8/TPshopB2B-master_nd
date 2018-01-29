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
 * Description: 推广消息  model
 *
 * @version V1.0
 */
package com.tpshop.mall.model;

import java.io.Serializable;

/**
 * @author 飞龙
 */
public class SPPushMessage implements Serializable, SPModel {

    private int id;                               //消息ID
    private int isRead;                           //是否已读
    private String title;                         //消息标题
    private String msgId;
    private String message;                       //消息内容
    private String receiverTime;                  //接收时间

    public SPPushMessage(String title, String message, String msgId) {
        this.title = title;
        this.message = message;
        this.msgId = msgId;
    }

    public SPPushMessage(int id, String title, String message, String msgId, String receiverTime, int isRead) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.msgId = msgId;
        this.receiverTime = receiverTime;
        this.isRead = isRead;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReceiverTime() {
        return receiverTime;
    }

    public void setReceiverTime(String receiverTime) {
        this.receiverTime = receiverTime;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[0];
    }

}
