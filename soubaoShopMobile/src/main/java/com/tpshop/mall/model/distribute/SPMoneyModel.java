package com.tpshop.mall.model.distribute;

import com.tpshop.mall.model.SPModel;

import java.io.Serializable;

/**
 * Created by zw on 2017/6/6
 */
public class SPMoneyModel implements SPModel, Serializable {

    private double todayMoney;
    private double achieveMoney;
    private double withdrawalsMoney;

    public void setwithdrawalsMoney(double withdrawalsMoney) {
        this.withdrawalsMoney = withdrawalsMoney;
    }

    public double getWithdrawalsMoney() {
        return withdrawalsMoney;
    }

    public void setAchieveMoney(double achieveMoney) {
        this.achieveMoney = achieveMoney;
    }

    public double getAchieveMoney() {
        return achieveMoney;
    }

    public void setTodayMoney(double todayMoney) {
        this.todayMoney = todayMoney;
    }

    public double getTodayMoney() {
        return todayMoney;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "withdrawalsMoney", "withdrawals_money",
                "achieveMoney", "achieve_money",
                "todayMoney", "today_money",
        };
    }

}
