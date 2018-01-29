package com.tpshop.mall.model.shop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2016/7/2
 */
public class SPJsonArray extends JSONArray {

    public SPJsonArray clone() {
        SPJsonArray dataArray = new SPJsonArray();
        try {
            for (int i = 0; i < this.length(); i++) {
                JSONObject formJson = new JSONObject();
                JSONObject object = this.getJSONObject(i);
                formJson.put("storeID", object.getInt("storeID"));
                formJson.put("cartID", object.getString("cartID"));
                formJson.put("goodsNum", object.getInt("goodsNum"));
                formJson.put("selected", object.getString("selected"));
                dataArray.put(formJson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataArray;
    }

}
