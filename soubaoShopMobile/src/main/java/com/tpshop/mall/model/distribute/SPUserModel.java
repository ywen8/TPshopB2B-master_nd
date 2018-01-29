package com.tpshop.mall.model.distribute;

import com.tpshop.mall.model.SPModel;

import java.io.Serializable;

/**
 * Created by zw on 2017/6/6
 */
public class SPUserModel implements SPModel, Serializable {

    private String headPic;
    private String nickName;
    private String userMoney;

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setUserMoney(String userMoney) {
        this.userMoney = userMoney;
    }

    public String getUserMoney() {
        return userMoney;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "nickName", "nickname",
                "headPic", "head_pic",
                "userMoney", "user_money",
        };
    }

}
