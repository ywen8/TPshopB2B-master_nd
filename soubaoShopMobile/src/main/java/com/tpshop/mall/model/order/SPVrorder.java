package com.tpshop.mall.model.order;

import com.tpshop.mall.model.SPModel;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/1
 */
public class SPVrorder implements SPModel, Serializable {

    private int state;
    private int indate;
    private String code;

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setIndate(int indate) {
        this.indate = indate;
    }

    public int getIndate() {
        return indate;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "state", "vr_state",
                "code", "vr_code",
                "indate", "vr_indate",
        };
    }

}
