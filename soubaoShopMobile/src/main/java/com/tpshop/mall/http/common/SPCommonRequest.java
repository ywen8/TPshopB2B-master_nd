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
 * Date: @date 2015年10月28日 下午9:31:20
 * Description: 通用WEB请求
 *
 * @version V1.0
 */
package com.tpshop.mall.http.common;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.entity.TPLocation;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPMobileHttptRequest;
import com.tpshop.mall.http.base.SPSuccessListener;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by admin on 2017/4/15
 */
public class SPCommonRequest {

    /**
     * 根据中文地址获取经纬度
     *
     * @Description: 该接口有限制0.6万次/天,如果要增加访问量请前往百度地图扩容
     * @url api.map.baidu.com
     */
    public static void geocoder(String city, String address, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = "http://api.map.baidu.com/geocoder/v2/?output=json&address=%s&city=%s&ak=%s&mcode=%s";
        String reqUrl = String.format(url, address, city, SPMobileConstants.baidumap_ak, SPMobileConstants.baidumap_mcode);
        SPMobileHttptRequest.post(reqUrl, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status == 0) {
                        JSONObject resultJson = response.getJSONObject("result");
                        JSONObject locationJson = resultJson.getJSONObject("location");
                        String lng = locationJson.getString("lng");
                        String lat = locationJson.getString("lat");
                        TPLocation location = new TPLocation(lng, lat);
                        successListener.onRespone("success", location);
                    } else {
                        failuredListener.onRespone("反地址编码失败", status);
                    }
                } catch (Exception e) {
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

}
