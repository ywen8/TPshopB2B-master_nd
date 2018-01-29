package com.tpshop.mall.model.order;

import com.tpshop.mall.model.SPModel;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/1
 */
public class SPButtom implements SPModel, Serializable {

    private int payBtn;
    private int returnBtn;
    private int cancelBtn;
    private int receiveBtn;
    private int commentBtn;
    private int shippingBtn;

    public void setPayBtn(int payBtn) {
        this.payBtn = payBtn;
    }

    public int getPayBtn() {
        return payBtn;
    }

    public void setCancelBtn(int cancelBtn) {
        this.cancelBtn = cancelBtn;
    }

    public int getCancelBtn() {
        return cancelBtn;
    }

    public void setReceiveBtn(int receiveBtn) {
        this.receiveBtn = receiveBtn;
    }

    public int getReceiveBtn() {
        return receiveBtn;
    }

    public void setCommentBtn(int commentBtn) {
        this.commentBtn = commentBtn;
    }

    public int getCommentBtn() {
        return commentBtn;
    }

    public void setShippingBtn(int shippingBtn) {
        this.shippingBtn = shippingBtn;
    }

    public int getShippingBtn() {
        return shippingBtn;
    }

    public void setReturnBtn(int returnBtn) {
        this.returnBtn = returnBtn;
    }

    public int getReturnBtn() {
        return returnBtn;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "payBtn", "pay_btn",
                "cancelBtn", "cancel_btn",
                "receiveBtn", "receive_btn",
                "commentBtn", "comment_btn",
                "shippingBtn", "shipping_btn",
                "returnBtn", "return_btn",
        };
    }

}
