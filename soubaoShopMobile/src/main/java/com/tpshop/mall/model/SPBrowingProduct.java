package com.tpshop.mall.model;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zw on 2017/5/23
 */
public class SPBrowingProduct implements SPModel, Serializable {

    private String date;
    private List<SPBrowItem> visitList;
    transient private JSONArray visitListArray;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<SPBrowItem> getVisitList() {
        return visitList;
    }

    public void setVisitList(List<SPBrowItem> visitList) {
        this.visitList = visitList;
    }

    public JSONArray getVisitListArray() {
        return visitListArray;
    }

    public void setVisitListArray(JSONArray visitListArray) {
        this.visitListArray = visitListArray;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "visitListArray", "visit",
        };
    }

}
