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
 * Description: 店铺相关数据接口
 *
 * @version V1.0
 */
package com.tpshop.mall.http.shop;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.utils.SPJsonUtil;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.common.SPMobileConstants.Response;
import com.tpshop.mall.entity.SPCommonListModel;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPMobileHttptRequest;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.condition.SPProductCondition;
import com.tpshop.mall.model.SPHomeBanners;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.model.SPStoreGoodsClass;
import com.tpshop.mall.model.shop.SPBrand;
import com.tpshop.mall.model.shop.SPShopOrder;
import com.tpshop.mall.model.shop.SPStore;
import com.tpshop.mall.utils.SPUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * @author 飞龙
 */
public class SPStoreRequest {

    /**
     * 获取店铺简介
     *
     * @param storeId          店铺id
     * @param successListener  success description
     * @param failuredListener failure description
     * @url index.php/Api/store/about
     */
    public static void getStoreAbout(String storeId, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("store", "about");
        RequestParams params = new RequestParams();
        params.put("store_id", storeId);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        JSONObject result = response.getJSONObject(Response.RESULT);
                        SPStore store = SPJsonUtil.fromJsonToModel(result, SPStore.class);
                        successListener.onRespone(msg, store);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
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
     * 获取店铺首页数据
     *
     * @param storeId          店铺id
     * @param successListener  success description
     * @param failuredListener failure description
     * @url index.php/Api/store/index
     */
    public static void getStoreHome(int storeId, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("store", "index");
        RequestParams params = new RequestParams();
        params.put("store_id", storeId);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        JSONObject result = response.getJSONObject(Response.RESULT);
                        SPStore store = SPJsonUtil.fromJsonToModel(result, SPStore.class);
                        if (store.getNewArray() != null)
                            store.setNewProducts(SPJsonUtil.fromJsonArrayToList(store.getNewArray(), SPProduct.class));
                        if (store.getRecommendArray() != null)
                            store.setRecommendProducts(SPJsonUtil.fromJsonArrayToList(store.getRecommendArray(), SPProduct.class));
                        if (store.getHotArray() != null)
                            store.setHotProducts(SPJsonUtil.fromJsonArrayToList(store.getHotArray(), SPProduct.class));
                        successListener.onRespone(msg, store);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
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
     * 获取店铺商品分类
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL api&c=store&a=storeGoodsClass
     */
    public static void getStoreGoodsClass(final int storeId, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        RequestParams params = new RequestParams();
        params.put("store_id", storeId);
        String url = SPMobileHttptRequest.getRequestUrl("store", "storeGoodsClass");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        JSONArray results = response.getJSONArray(Response.RESULT);
                        List<SPStoreGoodsClass> stores = SPJsonUtil.fromJsonArrayToList(results, SPStoreGoodsClass.class);
                        if (stores != null) {
                            for (SPStoreGoodsClass goodsClass : stores) {
                                if (goodsClass.getChildrenArray() == null || goodsClass.getChildrenArray().length() < 1)
                                    continue;
                                List<SPStoreGoodsClass> childrenClass = SPJsonUtil.fromJsonArrayToList(goodsClass.getChildrenArray(),
                                        SPStoreGoodsClass.class);
                                goodsClass.setChildrenGoodsClasses(childrenClass);
                            }
                        }
                        successListener.onRespone(msg, stores);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
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
     * 获取店铺商品列表
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL api&c=store&a=storeGoods
     */
    public static void getStoreGoodsList(SPProductCondition productCondition, final SPSuccessListener successListener,
                                         final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("store", "storeGoods");
        RequestParams params = new RequestParams();
        params.put("p", productCondition.page);
        params.put("pagesize", SPMobileConstants.SizeOfPage);
        params.put("store_id", productCondition.storeID);
        if (productCondition.href != null)
            url = SPMobileConstants.BASE_HOST + productCondition.href;
        if (productCondition.categoryID > 0)         //商品分类
            params.put("cat_id", productCondition.categoryID);
        if (!SPStringUtils.isEmpty(productCondition.type))
            params.put("sta", productCondition.type);
        if (!SPStringUtils.isEmpty(productCondition.orderdesc) && !SPStringUtils.isEmpty(productCondition.orderby)) {
            params.put("orderby", productCondition.orderby);
            params.put("orderdesc", productCondition.orderdesc);
        }
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        JSONObject responseJson = new JSONObject();
                        JSONObject resultJson = response.getJSONObject(Response.RESULT);
                        SPShopOrder shopOrder = SPJsonUtil.fromJsonToModel(resultJson, SPShopOrder.class);
                        List<SPProduct> products = SPJsonUtil.fromJsonArrayToList(resultJson.getJSONArray("goods_list"), SPProduct.class);
                        responseJson.put("shopOrder", shopOrder);
                        responseJson.put("products", products);
                        successListener.onRespone(msg, responseJson);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    successListener.onRespone(e.getMessage(), -1);
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
     * 品牌街
     *
     * @param successListener
     * @param failuredListener
     * @URL api&c=index&a=brand_street
     */
    public static void brandStreet(int pageIndex, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        RequestParams params = new RequestParams();
        params.put("p", pageIndex);
        String url = SPMobileHttptRequest.getRequestUrl("index", "brand_street");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        SPCommonListModel commonModel = new SPCommonListModel();
                        JSONObject resulJson = response.getJSONObject(Response.RESULT);
                        if (resulJson.has("brand_list") && SPUtils.velidateJSONArray(resulJson.getJSONArray("brand_list")))
                            commonModel.brands = SPJsonUtil.fromJsonArrayToList(resulJson.getJSONArray("brand_list"), SPBrand.class);
                        if (resulJson.has("hot_list") && SPUtils.velidateJSONArray(resulJson.getJSONArray("hot_list")))
                            commonModel.products = SPJsonUtil.fromJsonArrayToList(resulJson.getJSONArray("hot_list"), SPProduct.class);
                        if (resulJson.has("ad") && SPUtils.velidateJSONObject(resulJson.get("ad")))          //广告位
                            commonModel.ad = SPJsonUtil.fromJsonToModel(resulJson.getJSONObject("ad"), SPHomeBanners.class);
                        successListener.onRespone(msg, commonModel);
                    } else {
                        failuredListener.onRespone(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
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
     * 店铺街
     *
     * @param successListener
     * @param failuredListener
     * @URL api&c=index&a=store_street
     */
    public static void storeStreet(RequestParams params, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("index", "store_street");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        SPCommonListModel commonModel = new SPCommonListModel();
                        JSONObject resulJson = response.getJSONObject(Response.RESULT);
                        if (resulJson.has("store_list") && (resulJson.getJSONArray("store_list").length() > 0)) {         //店铺列表
                            List<SPStore> stores = SPJsonUtil.fromJsonArrayToList(resulJson.getJSONArray("store_list"), SPStore.class);
                            if (stores != null) {
                                for (SPStore store : stores) {
                                    List<SPProduct> products = SPJsonUtil.fromJsonArrayToList(store.getStoreProductArray(), SPProduct.class);
                                    store.setStoreProducts(products);
                                }
                            }
                            commonModel.stores = stores;
                        }
                        successListener.onRespone(msg, commonModel);
                    } else {
                        failuredListener.onRespone(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
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
     * 收藏/取消收藏店铺
     *
     * @param storeID
     * @param successListener
     * @param failuredListener
     * @URL index.php?m=Api&c=Store&a=collectStoreOrNo
     */
    public static void collectOrCancelStoreWithID(int storeID, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Store", "collectStoreOrNo");
        RequestParams params = new RequestParams();
        if (storeID > 0) params.put("store_id", storeID);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, msg);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
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

}
