package com.tpshop.mall.model.order;

import java.io.Serializable;

/**
 * Created by zw on 2017/6/1
 */
public class SPInvoce implements Serializable {

    private String name;
    private boolean isCheck;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

}
