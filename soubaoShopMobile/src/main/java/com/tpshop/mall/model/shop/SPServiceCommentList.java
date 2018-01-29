package com.tpshop.mall.model.shop;

import com.tpshop.mall.model.SPModel;
import com.tpshop.mall.model.SPServiceComment;

import org.json.JSONArray;

import java.util.List;

public class SPServiceCommentList implements SPModel {

    private String storeName;
    private List<SPServiceComment> serviceCommentDate;
    private transient JSONArray serviceCommentDateArray;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public List<SPServiceComment> getServiceCommentDate() {
        return serviceCommentDate;
    }

    public void setServiceCommentDate(List<SPServiceComment> serviceCommentDate) {
        this.serviceCommentDate = serviceCommentDate;
    }

    public JSONArray getServiceCommentDateArray() {
        return serviceCommentDateArray;
    }

    public void setServiceCommentDateArray(JSONArray serviceCommentDateArray) {
        this.serviceCommentDateArray = serviceCommentDateArray;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "storeName", "store_name",
                "serviceCommentDateArray", "order_list"
        };
    }

}
