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
 * Description: 首页相关数据接口
 *
 * @version V1.0
 */
package com.tpshop.mall.http.home;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.utils.SPJsonUtil;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.common.SPMobileConstants.Response;
import com.tpshop.mall.entity.SPCommonListModel;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPMobileHttptRequest;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.model.SPHomeBanners;
import com.tpshop.mall.model.SPPlugin;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.model.SPServiceConfig;
import com.tpshop.mall.model.shop.SPFlashSale;
import com.tpshop.mall.utils.SPUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * @author 飞龙
 */
public class SPHomeRequest {

    /**
     * 查询系统配置信息
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @url index.php?m=Api&c=Index&a=getConfig
     */
    public static void getServiceConfig(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Index", "getConfig");
        SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        SPCommonListModel listModel = new SPCommonListModel();
                        JSONObject resultJson = response.getJSONObject(Response.RESULT);
                        if (resultJson.has("config") && SPUtils.velidateJSONArray(resultJson.getJSONArray("config")))
                            listModel.serviceConfigs = SPJsonUtil.fromJsonArrayToList(resultJson.getJSONArray("config"), SPServiceConfig.class);
                        if (resultJson.has("payment") && SPUtils.velidateJSONArray(resultJson.getJSONArray("payment"))) {        //支付配置
                            List<SPPlugin> payPlugins = SPJsonUtil.fromJsonArrayToList(resultJson.getJSONArray("payment"), SPPlugin.class);
                            Map<String, SPPlugin> payPluginMap = new HashMap<>();
                            packPluginData(payPluginMap, payPlugins);
                            listModel.payPluginMap = payPluginMap;
                        }
                        if (resultJson.has("login") && SPUtils.velidateJSONArray(resultJson.getJSONArray("login"))) {         //登录配置
                            List<SPPlugin> loginPlugins = SPJsonUtil.fromJsonArrayToList(resultJson.getJSONArray("login"), SPPlugin.class);
                            Map<String, SPPlugin> loginPluginMap = new HashMap<>();
                            packPluginData(loginPluginMap, loginPlugins);
                            listModel.loginPlugins = loginPluginMap;
                        }
                        successListener.onRespone("success", listModel);
                    } else {
                        failuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
                    failuredListener.onRespone(e.getMessage(), -1);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 获取首页数据
     *
     * @param successListener
     * @param failuredListener
     * @url index.php?m=Api&c=Index&a=homePage
     */
    public static void getHomePageData(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Index", "homePage");
        RequestParams params = new RequestParams();
        params.put("new_ad", 1);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(Response.MSG);
                    JSONObject resultJson = response.getJSONObject(Response.RESULT);
                    SPCommonListModel commonModel = new SPCommonListModel();
                    if (resultJson != null) {
                        if (!resultJson.isNull("banner")) {          //首页轮播广告
                            JSONArray bannerArray = resultJson.getJSONArray("banner");
                            if (bannerArray != null)
                                commonModel.banners = SPJsonUtil.fromJsonArrayToList(bannerArray, SPHomeBanners.class);
                        }
                        if (!resultJson.isNull("ad")) {          //首页其他广告
                            JSONArray adArray = resultJson.getJSONArray("ad");
                            if (adArray != null)
                                commonModel.ads = SPJsonUtil.fromJsonArrayToList(adArray, SPHomeBanners.class);
                        }
                        if (!resultJson.isNull("flash_sale_goods")) {          //秒杀
                            JSONArray saleArray = resultJson.getJSONArray("flash_sale_goods");
                            if (saleArray != null)
                                commonModel.flashSales = SPJsonUtil.fromJsonArrayToList(saleArray, SPFlashSale.class);
                        }
                        successListener.onRespone("success", commonModel);
                    } else {
                        failuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
                    failuredListener.onRespone(e.getMessage(), -1);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 首页猜你喜欢
     *
     * @param successListener
     * @param failuredListener
     * @url index.php?m=Api&c=Index&a=favourite
     */
    public static void getFavouritePageData(int pageIndex, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        RequestParams params = new RequestParams();
        params.put("p", pageIndex);
        params.put("page_size", SPMobileConstants.SizeOfPage);
        String url = SPMobileHttptRequest.getRequestUrl("Index", "favourite");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(Response.MSG);
                    JSONObject resultJson = response.getJSONObject(Response.RESULT);
                    if (resultJson != null && !resultJson.isNull("favourite_goods")) {           //猜你喜欢
                        JSONArray favouriteArray = resultJson.getJSONArray("favourite_goods");
                        List<SPProduct> favourites = SPJsonUtil.fromJsonArrayToList(favouriteArray, SPProduct.class);
                        successListener.onRespone("success", favourites);
                    } else {
                        failuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
                    failuredListener.onRespone(e.getMessage(), -1);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 填装插件数据
     */
    private static void packPluginData(Map<String, SPPlugin> pluginMap, List<SPPlugin> plugins) {
        if (pluginMap == null || plugins == null || plugins.size() < 1) return;
        if (plugins != null) {
            for (SPPlugin plugin : plugins) {
                if (plugin.getStatus().equals("1")) {              //插件安装后才可使用
                    String key = plugin.getCode();
                    pluginMap.put(key, plugin);
                }
            }
        }
    }

}
