package com.tpshop.mall.model.distribute;

import com.tpshop.mall.model.SPModel;

import java.io.Serializable;

/**
 * Created by zw on 2017/6/7
 */
public class SPTeamModel implements SPModel, Serializable {

    private int userId;
    private int regTime;
    private String headPic;
    private String nickName;
    private String distributMoney;

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setDistributMoney(String distributMoney) {
        this.distributMoney = distributMoney;
    }

    public String getDistributMoney() {
        return distributMoney;
    }

    public void setRegTime(int regTime) {
        this.regTime = regTime;
    }

    public int getRegTime() {
        return regTime;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getHeadPic() {
        return headPic;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "nickName", "nickname",
                "userId", "user_id",
                "distributMoney", "distribut_money",
                "regTime", "reg_time",
                "headPic", "head_pic",
        };
    }

}
