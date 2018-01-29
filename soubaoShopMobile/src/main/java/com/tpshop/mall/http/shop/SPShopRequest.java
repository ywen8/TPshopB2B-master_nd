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
 * Description: 商城相关数据接口
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
import com.tpshop.mall.entity.SPActivityItem;
import com.tpshop.mall.entity.SPCommentTitleModel;
import com.tpshop.mall.entity.SPCommonListModel;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPMobileHttptRequest;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.condition.SPProductCondition;
import com.tpshop.mall.model.SPHomeBanners;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.model.SPSpecPriceModel;
import com.tpshop.mall.model.distribute.SPDistributeType;
import com.tpshop.mall.model.person.SPConsigneeAddress;
import com.tpshop.mall.model.shop.SPActivity;
import com.tpshop.mall.model.shop.SPFilter;
import com.tpshop.mall.model.shop.SPFilterItem;
import com.tpshop.mall.model.shop.SPFlashSale;
import com.tpshop.mall.model.shop.SPFlashTime;
import com.tpshop.mall.model.shop.SPGoodsComment;
import com.tpshop.mall.model.shop.SPGroup;
import com.tpshop.mall.model.shop.SPProductSpec;
import com.tpshop.mall.model.shop.SPPromoteGood;
import com.tpshop.mall.model.shop.SPShopOrder;
import com.tpshop.mall.model.shop.SPStore;
import com.tpshop.mall.model.shop.WxPayInfo;
import com.tpshop.mall.utils.SPUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * @author 飞龙
 */
public class SPShopRequest {

    /**
     * 获取商品列表
     *
     * @param productCondition
     * @param failuredListener
     * @url index.php/Api/Goods/goodsList
     */
    public static void getProductList(SPProductCondition productCondition, final SPSuccessListener successListener,
                                      final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Goods", "goodsList");
        RequestParams params = new RequestParams();
        params.put("p", productCondition.page);
        params.put("pagesize", SPMobileConstants.SizeOfPage);
        if (productCondition.href != null)
            url = SPMobileConstants.BASE_HOST + productCondition.href;
        if (productCondition.categoryID > 0)
            params.put("id", productCondition.categoryID);          //商品分类
        if (productCondition.brandID > 0)
            params.put("brand_id", productCondition.brandID);
        if (!SPStringUtils.isEmpty(productCondition.orderdesc) && !SPStringUtils.isEmpty(productCondition.orderby)) {
            params.put("orderby", productCondition.orderby);
            params.put("orderdesc", productCondition.orderdesc);
        }
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(Response.MSG);
                    JSONObject resultJson = response.getJSONObject(Response.RESULT);
                    JSONObject dataJson = new JSONObject();
                    List<SPFilter> filters = new ArrayList<>();
                    if (resultJson != null) {
                        SPShopOrder shopOrder = SPJsonUtil.fromJsonToModel(resultJson, SPShopOrder.class);
                        if (shopOrder != null)         //排序URL
                            dataJson.put("order", shopOrder);
                        if (!resultJson.isNull("goods_list")) {        //商品列表
                            JSONArray goodsList = resultJson.getJSONArray("goods_list");
                            if (goodsList != null) {
                                List<SPProduct> products = SPJsonUtil.fromJsonArrayToList(goodsList, SPProduct.class);
                                dataJson.put("product", products);
                            }
                        }
                        if (!resultJson.isNull("filter_menu")) {         //选中菜单
                            JSONArray menuJson = resultJson.getJSONArray("filter_menu");
                            if (menuJson != null) {
                                List<SPFilterItem> menus = SPJsonUtil.fromJsonArrayToList(menuJson, SPFilterItem.class);
                                for (SPFilterItem item : menus)
                                    item.setIsHighLight(true);
                                SPFilter menuFilter = new SPFilter(1, "1", "选择分类", menus);
                                dataJson.put("menu", menuFilter);
                            }
                        }
                        if (!resultJson.isNull("filter_spec")) {            //规格
                            JSONArray filterSpecJson = resultJson.getJSONArray("filter_spec");
                            if (filterSpecJson != null) {
                                List<SPFilter> specs = SPJsonUtil.fromJsonArrayToList(filterSpecJson, SPFilter.class);
                                for (SPFilter spec : specs)
                                    spec.setItems(SPJsonUtil.fromJsonArrayToList(spec.getItemJsonArray(), SPFilterItem.class));
                                filters.addAll(specs);
                            }
                        }
                        if (!resultJson.isNull("filter_attr")) {           //属性
                            JSONArray attrJson = resultJson.getJSONArray("filter_attr");
                            if (attrJson != null) {
                                List<SPFilter> attrs = SPJsonUtil.fromJsonArrayToList(attrJson, SPFilter.class);
                                for (SPFilter attr : attrs)
                                    attr.setItems(SPJsonUtil.fromJsonArrayToList(attr.getItemJsonArray(), SPFilterItem.class));
                                filters.addAll(attrs);
                            }
                        }
                        if (!resultJson.isNull("filter_brand")) {           //品牌
                            JSONArray brandJson = resultJson.getJSONArray("filter_brand");
                            if (brandJson != null) {
                                List<SPFilterItem> brands = SPJsonUtil.fromJsonArrayToList(brandJson, SPFilterItem.class);
                                SPFilter brandFilter = new SPFilter(4, "4", "品牌", brands);
                                filters.add(brandFilter);
                            }
                        }
                        if (!resultJson.isNull("filter_price")) {          //价格
                            JSONArray priceJson = resultJson.getJSONArray("filter_price");
                            if (priceJson != null) {
                                List<SPFilterItem> prices = SPJsonUtil.fromJsonArrayToList(priceJson, SPFilterItem.class);
                                SPFilter priceFilter = new SPFilter(5, "5", "价格", prices);
                                filters.add(priceFilter);
                            }
                        }
                        dataJson.put("filter", filters);
                        successListener.onRespone(msg, dataJson);
                    } else {
                        failuredListener.onRespone(msg, -1);
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
     * 获取分销商品列表
     *
     * @param productCondition
     * @param failuredListener
     * @url index.php/Api/Distribut/goods_list
     */
    public static void getDistributeProductList(SPProductCondition productCondition, final SPSuccessListener successListener,
                                                final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Distribut", "goods_list");
        RequestParams params = new RequestParams();
        params.put("p", productCondition.page);
        if (!SPStringUtils.isEmpty(productCondition.orderby))
            params.put("sort", productCondition.orderby);
        if (!SPStringUtils.isEmpty(productCondition.orderdesc))
            params.put("order", productCondition.orderdesc);
        if (productCondition.categoryID > 0)
            params.put("cat_id", productCondition.categoryID);
        if (productCondition.brandID > 0)
            params.put("brand_id", productCondition.brandID);
        if (!SPStringUtils.isEmpty(productCondition.keyword))
            params.put("key_word", productCondition.keyword);
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        JSONArray results = response.getJSONArray(Response.RESULT);
                        List<SPProduct> products = SPJsonUtil.fromJsonArrayToList(results, SPProduct.class);
                        successListener.onRespone(msg, products);
                    } else {
                        failuredListener.onRespone(msg, -1);
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
     * 分销商品类型
     *
     * @param successListener
     * @param failuredListener
     * @url index.php/Api/Distribut/goods_types
     */
    public static void getDistributeType(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Distribut", "goods_types");
        SPMobileHttptRequest.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(Response.MSG);
                    JSONObject results = response.getJSONObject(Response.RESULT);
                    Map<String, List<SPDistributeType>> hashMap = new HashMap<>();
                    if (!results.isNull("categoryList")) {
                        List<SPDistributeType> categoryList = SPJsonUtil.fromJsonArrayToList(results.getJSONArray("categoryList"),
                                SPDistributeType.class);
                        hashMap.put("categoryList", categoryList);
                    }
                    if (!results.isNull("brandList")) {
                        List<SPDistributeType> brandList = SPJsonUtil.fromJsonArrayToList(results.getJSONArray("brandList"),
                                SPDistributeType.class);
                        hashMap.put("brandList", brandList);
                    }
                    successListener.onRespone(msg, hashMap);
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
     * 添加分销商品
     *
     * @param successListener
     * @param failuredListener
     * @url index.php/Api/Distribut/add_goods
     */
    public static void addDistributeGoods(RequestParams params, final SPSuccessListener successListener,
                                          final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Distribut", "add_goods");
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, status);
                    } else {
                        failuredListener.onRespone(msg, -1);
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
     * 搜索列表(搜索结果)
     *
     * @param successListener  description
     * @param failuredListener description
     * @url index.php/Api/Goods/search
     */
    public static void searchResultProductListWithPage(int page, String searchKey, String href, final SPSuccessListener successListener,
                                                       final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Goods", "search");
        if (!SPStringUtils.isEmpty(href))
            url = SPMobileConstants.BASE_HOST + href;
        RequestParams params = new RequestParams();
        params.put("p", page);
        params.put("pagesize", SPMobileConstants.SizeOfPage);
        if (!SPStringUtils.isEmpty(searchKey))
            params.put("q", searchKey);
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(Response.MSG);
                    JSONObject resultJson = response.getJSONObject(Response.RESULT);
                    JSONObject dataJson = new JSONObject();
                    List<SPFilter> filters = new ArrayList<>();
                    if (resultJson != null) {
                        SPShopOrder shopOrder = SPJsonUtil.fromJsonToModel(resultJson, SPShopOrder.class);
                        if (shopOrder != null)        //排序URL
                            dataJson.put("order", shopOrder);
                        if (!resultJson.isNull("goods_list")) {           //商品列表
                            JSONArray goodsList = resultJson.getJSONArray("goods_list");
                            if (goodsList != null) {
                                List<SPProduct> products = SPJsonUtil.fromJsonArrayToList(goodsList, SPProduct.class);
                                dataJson.put("product", products);
                            }
                        }
                        if (!resultJson.isNull("filter_menu")) {         //选中菜单
                            JSONArray menuJson = resultJson.getJSONArray("filter_menu");
                            if (menuJson != null) {
                                List<SPFilterItem> menus = SPJsonUtil.fromJsonArrayToList(menuJson, SPFilterItem.class);
                                for (SPFilterItem item : menus)
                                    item.setIsHighLight(true);
                                SPFilter menuFilter = new SPFilter(1, "1", "选择分类", menus);
                                dataJson.put("menu", menuFilter);
                            }
                        }
                        if (!resultJson.isNull("filter_spec")) {          //规格
                            JSONArray filterSpecJson = resultJson.getJSONArray("filter_spec");
                            if (filterSpecJson != null) {
                                List<SPFilter> specs = SPJsonUtil.fromJsonArrayToList(filterSpecJson, SPFilter.class);
                                for (SPFilter spec : specs)
                                    spec.setItems(SPJsonUtil.fromJsonArrayToList(spec.getItemJsonArray(), SPFilterItem.class));
                                filters.addAll(specs);
                            }
                        }
                        if (!resultJson.isNull("filter_attr")) {           //属性
                            JSONArray attrJson = resultJson.getJSONArray("filter_attr");
                            if (attrJson != null) {
                                List<SPFilter> attrs = SPJsonUtil.fromJsonArrayToList(attrJson, SPFilter.class);
                                for (SPFilter attr : attrs)
                                    attr.setItems(SPJsonUtil.fromJsonArrayToList(attr.getItemJsonArray(), SPFilterItem.class));
                                filters.addAll(attrs);
                            }
                        }
                        if (!resultJson.isNull("filter_brand")) {            //品牌
                            JSONArray brandJson = resultJson.getJSONArray("filter_brand");
                            if (brandJson != null) {
                                List<SPFilterItem> brands = SPJsonUtil.fromJsonArrayToList(brandJson, SPFilterItem.class);
                                SPFilter brandFilter = new SPFilter(4, "4", "品牌", brands);
                                filters.add(brandFilter);
                            }
                        }
                        if (!resultJson.isNull("filter_price")) {            //价格
                            JSONArray priceJson = resultJson.getJSONArray("filter_price");
                            if (priceJson != null) {
                                List<SPFilterItem> prices = SPJsonUtil.fromJsonArrayToList(priceJson, SPFilterItem.class);
                                SPFilter priceFilter = new SPFilter(5, "5", "价格", prices);
                                filters.add(priceFilter);
                            }
                        }
                        dataJson.put("filter", filters);
                        successListener.onRespone(msg, dataJson);
                    } else {
                        failuredListener.onRespone(msg, -1);
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
     * 商品详情
     *
     * @param productCondition 参数(包含goodsID)
     * @param successListener
     * @param failuredListener
     * @url index.php/Api/Goods/goodsInfo
     */
    public static void getProductByID(SPProductCondition productCondition, final SPSuccessListener successListener,
                                      final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Goods", "goodsInfo");
        RequestParams params = new RequestParams();
        if (productCondition.goodsID > 0)
            params.put("id", productCondition.goodsID);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject dataJson = new JSONObject();
                    String msg = response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        JSONObject result = response.getJSONObject(Response.RESULT);
                        SPProduct product = null;
                        if (result.has("goods"))
                            product = SPJsonUtil.fromJsonToModel(result.getJSONObject("goods"), SPProduct.class);         //商品
                        if (result.has("goods_spec_list") && SPUtils.velidateJSONArray(result.get("goods_spec_list"))) {          //商品规格列表
                            Map<String, List<SPProductSpec>> specGroupMap = new HashMap<>();
                            JSONArray goodsSpecJsonArray = result.getJSONArray("goods_spec_list");
                            for (int i = 0; i < goodsSpecJsonArray.length(); i++) {
                                JSONObject goodsSpecJsonObj = goodsSpecJsonArray.getJSONObject(i);
                                String specName = goodsSpecJsonObj.getString("spec_name");
                                List<SPProductSpec> specs = SPJsonUtil.fromJsonArrayToList(goodsSpecJsonObj.getJSONArray("spec_list"),
                                        SPProductSpec.class);
                                specGroupMap.put(specName, specs);
                            }
                            product.setSpecGroupMap(specGroupMap);
                        }
                        if (product != null && result.has("activity") && SPUtils.velidateJSONObject(result.get("activity"))) {        //获取活动信息
                            JSONObject activityJson = result.getJSONObject("activity");
                            SPActivity activity = SPJsonUtil.fromJsonToModel(activityJson, SPActivity.class);
                            if (activityJson.has("data") && SPUtils.velidateJSONArray(activityJson.getJSONArray("data"))) {
                                List<SPActivityItem> items = SPJsonUtil.fromJsonArrayToList(activityJson.getJSONArray("data"),
                                        SPActivityItem.class);
                                activity.setDatas(items);
                            }
                            product.setActivity(activity);
                        }
                        if (result.has("store") && SPUtils.velidateJSONObject(result.get("store"))) {            //店铺数据
                            SPStore store = SPJsonUtil.fromJsonToModel(result.getJSONObject("store"), SPStore.class);
                            dataJson.put("store", store);
                        }
                        if (result.has("statistics") && SPUtils.velidateJSONObject(result.get("statistics"))) {             //评论数量统计信息
                            SPCommentTitleModel titleModel = SPJsonUtil.fromJsonToModel(result.getJSONObject("statistics"),
                                    SPCommentTitleModel.class);
                            product.setCommentTitleModel(titleModel);
                        }
                        if (product != null && result.has("gallery")) {
                            JSONArray jsonGarrys = result.getJSONArray("gallery");
                            dataJson.put("gallery", jsonGarrys);
                        }
                        if (product != null && result.has("recommend_goods")
                                && SPUtils.velidateJSONArray(result.getJSONArray("recommend_goods"))) {          //推荐商品
                            List<SPProduct> recommends = SPJsonUtil.fromJsonArrayToList(result.getJSONArray("recommend_goods"), SPProduct.class);
                            product.setRecommends(recommends);
                        }
                        if (product != null)
                            dataJson.put("product", product);
                        if (result.has("spec_goods_price") && !SPStringUtils.isEmpty(result.getString("spec_goods_price"))) {
                            JSONArray priceArray = result.getJSONArray("spec_goods_price");
                            List<SPSpecPriceModel> specPrices = SPJsonUtil.fromJsonArrayToList(priceArray, SPSpecPriceModel.class);
                            dataJson.put("price", specPrices);
                        }
                        if (result.has("consignee") && SPUtils.velidateJSONObject(result.get("consignee"))) {
                            JSONObject consigneeObj = result.getJSONObject("consignee");
                            SPConsigneeAddress address = SPJsonUtil.fromJsonToModel(consigneeObj, SPConsigneeAddress.class);
                            dataJson.put("address", address);
                        }
                        successListener.onRespone(msg, dataJson);
                    } else {
                        failuredListener.onRespone(msg, -1);
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
     * 根据规格获取商品活动
     *
     * @param successListener
     * @param failuredListener
     * @url index.php/Api/Goods/goods_activity
     */
    public static void getGoodActivity(String goodId, int itemId, final SPSuccessListener successListener,
                                       final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Goods", "goods_activity");
        RequestParams params = new RequestParams();
        params.put("goods_id", goodId);
        params.put("item_id", itemId);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        if (response.has("result") && SPUtils.velidateJSONObject(response.get("result"))) {
                            JSONObject resultJson = response.getJSONObject("result");
                            SPActivity activity = SPJsonUtil.fromJsonToModel(resultJson, SPActivity.class);
                            if (resultJson.has("data") && SPUtils.velidateJSONArray(resultJson.getJSONArray("data"))) {
                                List<SPActivityItem> items = SPJsonUtil.fromJsonArrayToList(resultJson.getJSONArray("data"), SPActivityItem.class);
                                activity.setDatas(items);
                            }
                            successListener.onRespone(msg, activity);
                        } else {
                            failuredListener.handleResponse(msg, status);
                        }
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
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
     * 添加商品到购物车(对购物车商品数量操作,数量增加或减少)
     *
     * @param goodsID          商品id
     * @param itemId           商品规格id
     * @param number           数量
     * @param successListener  success description
     * @param failuredListener failure description
     * @url index.php/Api/Goods/goods_activity
     */
    public static void shopCartGoodsOperation(String goodsID, int itemId, int number, final SPSuccessListener successListener,
                                              final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Cart", "addCart");
        RequestParams params = new RequestParams();
        params.put("goods_num", number);
        params.put("goods_id", goodsID);
        params.put("item_id", itemId);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        int count = response.getInt(Response.RESULT);
                        successListener.onRespone(msg, count);
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
     * 获取购物车商品列表
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @url index.php/Api/Cart/cartList
     */
    public static void getShopCartList(JSONArray formDataArray, final SPSuccessListener successListener,
                                       final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Cart", "cartList");
        RequestParams params = new RequestParams();
        if (formDataArray != null && formDataArray.length() > 0) {
            String formData = formDataArray.toString();
            params.put("cart_form_data", formData);
        }
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        JSONObject jsonObject = new JSONObject();
                        if (response.has("result")) {
                            JSONObject resultJson = response.getJSONObject("result");
                            if (resultJson.has("total_price")) {
                                JSONObject feeJson = resultJson.getJSONObject("total_price");
                                if (feeJson != null && feeJson.has("total_fee")) {
                                    double totalFee = feeJson.getDouble("total_fee");
                                    jsonObject.put("totalFee", totalFee);            //总金额(需要支付的金额)
                                }
                                if (feeJson != null && feeJson.has("cut_fee")) {
                                    double cutFee = feeJson.getDouble("cut_fee");        //节省金额
                                    jsonObject.put("cutFee", cutFee);
                                }
                                if (feeJson != null && feeJson.has("num")) {
                                    int num = feeJson.getInt("num");            //商品数量
                                    jsonObject.put("num", num);
                                }
                            }
                            if (resultJson.has("storeList")) {
                                List<SPStore> stores = SPJsonUtil.fromJsonArrayToList(resultJson.getJSONArray("storeList"), SPStore.class);
                                for (SPStore store : stores) {
                                    List<SPProduct> products = SPJsonUtil.fromJsonArrayToList(store.getStoreProductArray(), SPProduct.class);
                                    if (products != null)
                                        store.setStoreProducts(products);
                                }
                                jsonObject.put("stores", stores);
                            }
                        }
                        successListener.onRespone(msg, jsonObject);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
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
     * 获取确认订单数据(购物车/商品立即购买)
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @url index.php/Api/Cart/cart2
     */
    public static void getConfirmOrderData(RequestParams params, final SPSuccessListener successListener,
                                           final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Cart", "cart2");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        JSONObject jsonObject = new JSONObject();
                        if (response.has("result")) {
                            JSONObject resultJson = response.getJSONObject("result");
                            if (resultJson.has("addressList") && SPUtils.velidateJSONObject(resultJson.get("addressList"))) {          //收货地址
                                SPConsigneeAddress consignees = SPJsonUtil.fromJsonToModel(resultJson.getJSONObject("addressList"),
                                        SPConsigneeAddress.class);
                                jsonObject.put("consigneeAddress", consignees);
                            }
                            JSONObject couponNumObj = null;
                            if (resultJson.has("couponNum") && SPUtils.velidateJSONObject(resultJson.get("couponNum")))          //所有店铺优惠券数量
                                couponNumObj = resultJson.getJSONObject("couponNum");
                            if (resultJson.has("storeCartTotalPrice")) {            //价格
                                double totalPrice = resultJson.getDouble("storeCartTotalPrice");
                                jsonObject.put("totalPrice", totalPrice);
                            }
                            if (resultJson.has("userInfo") && SPUtils.velidateJSONObject(resultJson.get("userInfo"))) {
                                JSONObject userJson = resultJson.getJSONObject("userInfo");
                                jsonObject.put("userInfo", userJson);
                            }
                            if (resultJson.has("storeShippingCartList")) {
                                List<SPStore> stores = SPJsonUtil.fromJsonArrayToList(resultJson.getJSONArray("storeShippingCartList"),
                                        SPStore.class);
                                if (stores != null) {
                                    for (SPStore store : stores) {
                                        if (store.getStoreProductArray() != null) {            //商品列表
                                            List<SPProduct> products = SPJsonUtil.fromJsonArrayToList(store.getStoreProductArray(),
                                                    SPProduct.class);
                                            store.setStoreProducts(products);
                                        }
                                        if (couponNumObj != null && couponNumObj.has(store.getStoreId() + ""))
                                            store.setCouponNum(couponNumObj.getInt(store.getStoreId() + ""));
                                    }
                                }
                                jsonObject.put("storeList", stores);
                            }
                        }
                        successListener.onRespone(msg, jsonObject);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
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
     * 获取确认订单数据(积分商品立即兑换)
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @url index.php?m=Api&c=cart&a=integral
     */
    public static void getConfirmOrderData2(RequestParams params, final SPSuccessListener successListener,
                                            final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("cart", "integral");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        JSONObject jsonObject = new JSONObject();
                        if (response.has("result")) {
                            JSONObject resultJson = response.getJSONObject("result");
                            if (resultJson.has("userAddress") && SPUtils.velidateJSONObject(resultJson.get("userAddress"))) {          //收货地址
                                SPConsigneeAddress consignees = SPJsonUtil.fromJsonToModel(resultJson.getJSONObject("userAddress"),
                                        SPConsigneeAddress.class);
                                jsonObject.put("consigneeAddress", consignees);
                            }
                            if (resultJson.has("goods_price")) {            //价格
                                String totalPrice = resultJson.getString("goods_price");
                                jsonObject.put("totalPrice", Double.valueOf(totalPrice));
                            }
                            if (resultJson.has("userInfo") && SPUtils.velidateJSONObject(resultJson.get("userInfo"))) {
                                JSONObject userJson = resultJson.getJSONObject("userInfo");
                                jsonObject.put("userInfo", userJson);
                            }
                            if (resultJson.has("goods")) {
                                SPProduct product = SPJsonUtil.fromJsonToModel(resultJson.getJSONObject("goods"), SPProduct.class);
                                String pointRate = "";
                                String goodsPrice = "";
                                if (resultJson.has("point_rate"))
                                    pointRate = resultJson.getString("point_rate");
                                if (resultJson.has("goods_price"))
                                    goodsPrice = resultJson.getString("goods_price");
                                if (!SPStringUtils.isEmpty(pointRate) && !SPStringUtils.isEmpty(goodsPrice)) {
                                    String exchangeIntegral = String.valueOf(product.getExchangeIntegral());
                                    BigDecimal b1 = new BigDecimal(goodsPrice);
                                    BigDecimal b2 = new BigDecimal(exchangeIntegral);
                                    BigDecimal b3 = new BigDecimal(pointRate);
                                    String integralPrice;
                                    if ((b1.subtract(b2.divide(b3))).doubleValue() <= 0)
                                        integralPrice = (int) Math.ceil((b1.multiply(b3)).doubleValue()) + "积分";
                                    else
                                        integralPrice = "¥" + b1.subtract(b2.divide(b3)) + "+" + exchangeIntegral + "积分";
                                    product.setMemberGoodsPrice(integralPrice);
                                }
                                if (resultJson.has("goods_num"))
                                    product.setGoodsNum(resultJson.getInt("goods_num"));
                                SPStore store = SPJsonUtil.fromJsonToModel(resultJson.getJSONObject("goods").getJSONObject("store"), SPStore.class);
                                List<SPProduct> products = new ArrayList<>();
                                products.add(product);
                                store.setStoreProducts(products);
                                jsonObject.put("store", store);
                            }
                        }
                        successListener.onRespone(msg, jsonObject);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
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
     * 根据选择的订单信息查询总价(物流)
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @url index.php?m=Api&c=Cart&a=cart3
     */
    public static void getOrderTotalFee(RequestParams params, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Cart", "cart3");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        JSONObject jsonObject = null;
                        if (response.has("result"))
                            jsonObject = response.getJSONObject("result");
                        successListener.onRespone(msg, jsonObject);
                    } else {
                        failuredListener.onRespone(msg, status);
                    }
                } catch (Exception e) {
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
     * 立即兑换根据选择的订单信息查询总价(物流)
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @url index.php?m=Api&c=cart&a=integral2
     */
    public static void getOrderTotalFee2(RequestParams params, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("cart", "integral2");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        if (response.has("result"))
                            if (SPUtils.velidateJSONObject(response.get("result"))) {
                                JSONObject jsonObject = response.getJSONObject("result");
                                successListener.onRespone(msg, jsonObject);
                            } else {
                                String result = response.getString("result");
                                successListener.onRespone(msg, result);
                            }
                    } else {
                        if (response.has("result") && SPUtils.velidateJSONObject(response.get("result"))) {
                            JSONObject jsonObject = response.getJSONObject("result");
                            if (jsonObject.has("total_integral")) {
                                int integral = jsonObject.getInt("total_integral");
                                failuredListener.onRespone(msg, integral);
                            } else {
                                failuredListener.onRespone(msg, status);
                            }
                        } else {
                            failuredListener.onRespone(msg, status);
                        }
                    }
                } catch (Exception e) {
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
     * 提交订单
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @url index.php?m=Api&c=Cart&a=cart3
     */
    public static void submitOrder(RequestParams params, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Cart", "cart3");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        String masterOrderSN = null;
                        if (response.has("result"))
                            masterOrderSN = response.getString("result");
                        successListener.onRespone(msg, masterOrderSN);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
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
     * 虚拟商品提交订单
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @url index.php?m=api&c=virtual&a=add_order
     */
    public static void submitOrder2(RequestParams params, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("virtual", "add_order");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        String masterOrderSN = null;
                        if (response.has("result"))
                            masterOrderSN = response.getString("result");
                        successListener.onRespone(msg, masterOrderSN);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
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
     * 根据主订单号获取要支付的金额
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @url index.php?m=Api&c=Cart&a=cart4
     */
    public static void getOrderAmountWithMasterOrderSN(String masterOrderSN, final SPSuccessListener successListener,
                                                       final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Cart", "cart4");
        RequestParams params = new RequestParams();
        params.put("master_order_sn", masterOrderSN);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        String amount = null;
                        if (response.has("result"))
                            amount = response.getString("result");
                        successListener.onRespone(msg, amount);
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
     * 获取商品评论
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @url index.php?m=Api&c=Goods&a=getGoodsComment
     */
    public static void getGoodsCommentWithGoodsID(String goodsID, int page, int type, final SPSuccessListener successListener,
                                                  final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Goods", "getGoodsComment");
        RequestParams params = new RequestParams();
        params.put("goods_id", goodsID);
        params.put("p", page);
        params.put("type", type);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    List<SPGoodsComment> comments = null;
                    if (status > 0) {
                        if (response.has("result")) {
                            comments = SPJsonUtil.fromJsonArrayToList(response.getJSONArray("result"), SPGoodsComment.class);
                            for (SPGoodsComment goodsComment : comments) {
                                if (goodsComment.getImageArray() == null) continue;
                                List<String> images = SPMobileHttptRequest.convertJsonArrayToList(goodsComment.getImageArray());
                                goodsComment.setImages(images);
                            }
                        }
                        successListener.onRespone(msg, comments);
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
     * 删除购物车的商品
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @url index.php?m=Api&c=Cart&a=delCart
     */
    public static void deleteShopCartProductWithIds(String cartIds, final SPSuccessListener successListener,
                                                    final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Cart", "delCart");
        RequestParams params = new RequestParams();
        params.put("ids", cartIds);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        int count = response.getInt(Response.RESULT);
                        successListener.onRespone(msg, count);
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
     * 猜你喜欢/热门推荐
     *
     * @param successListener
     * @param failuredListener
     * @url index.php?m=Api&c=Goods&a=guessYouLike
     */
    public static void guessYouLike(int page, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Goods", "guessYouLike");
        RequestParams params = new RequestParams();
        params.put("p", page);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        JSONArray resulJson = response.getJSONArray(Response.RESULT);
                        List<SPProduct> products = SPJsonUtil.fromJsonArrayToList(resulJson, SPProduct.class);
                        successListener.onRespone(msg, products);
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
     * 微信支付(获取支付信息)
     *
     * @param successListener
     * @param failuredListener
     * @url index.php?m=Api&c=Wxpay&a=dopay
     */
    public static void getWxPayInfo(RequestParams params, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Wxpay", "dopay");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        JSONObject resultJson = response.getJSONObject(Response.RESULT);
                        WxPayInfo wxPayInfo = SPJsonUtil.fromJsonToModel(resultJson, WxPayInfo.class);
                        successListener.onRespone(msg, wxPayInfo);
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
     * 支付宝支付(获取支付信息)
     *
     * @param successListener
     * @param failuredListener
     * @url index.php?m=Api&c=Payment&a=alipay_sign
     */
    public static void getAlipayOrderSignInfo(RequestParams params, final SPSuccessListener successListener,
                                              final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Payment", "alipay_sign");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        String sign = response.getString(Response.RESULT);
                        successListener.onRespone(msg, sign);
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
     * 团购商品列表
     *
     * @param successListener
     * @param failuredListener
     * @url index.php?m=Api&c=activity&a=group_list
     */
    public static void groupList(int page, String sortType, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("activity", "group_list");
        RequestParams params = new RequestParams();
        params.put("p", page);
        params.put("type", sortType);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        SPCommonListModel listModel = new SPCommonListModel();
                        JSONObject jsonResult = response.getJSONObject(Response.RESULT);
                        if (jsonResult.has("groups") && SPUtils.velidateJSONArray(jsonResult.get("groups")))
                            listModel.groups = SPJsonUtil.fromJsonArrayToList(jsonResult.getJSONArray("groups"), SPGroup.class);
                        if (jsonResult.has("ad") && SPUtils.velidateJSONObject(jsonResult.get("ad")))
                            listModel.ad = SPJsonUtil.fromJsonToModel(jsonResult.getJSONObject("ad"), SPHomeBanners.class);
                        successListener.onRespone(msg, listModel);
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
     * 促销商品列表
     *
     * @param successListener
     * @param failuredListener
     * @url index.php?m=Api&c=Activity&a=promoteList
     */
    public static void goodsPromoteList(int page, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Activity", "promote_list");
        RequestParams params = new RequestParams();
        params.put("p", page);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        JSONArray jsonResult = response.getJSONArray(Response.RESULT);
                        List<SPPromoteGood> promoteGoods = SPJsonUtil.fromJsonArrayToList(jsonResult, SPPromoteGood.class);
                        successListener.onRespone(msg, promoteGoods);
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
     * 积分商城列表
     *
     * @param successListener
     * @param failuredListener
     * @url index.php?m=Api&c=Goods&a=integralMall
     */
    public static void integralMall(int page, String rank, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Goods", "integralMall");
        RequestParams params = new RequestParams();
        params.put("p", page);
        params.put("rank", rank);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        SPCommonListModel commonModel = new SPCommonListModel();
                        List<SPProduct> products;
                        JSONObject jsonResult = response.getJSONObject(Response.RESULT);
                        String pointRate = null;
                        if (jsonResult.has("point_rate"))
                            pointRate = jsonResult.getString("point_rate");
                        if (jsonResult.has("goods_list") && SPUtils.velidateJSONArray(jsonResult.get("goods_list"))) {
                            products = SPJsonUtil.fromJsonArrayToList(jsonResult.getJSONArray("goods_list"), SPProduct.class);
                            for (SPProduct product : products) {
                                if (pointRate != null) product.setPointRate(pointRate);
                            }
                            commonModel.products = products;
                        }
                        if (jsonResult.has("ad") && SPUtils.velidateJSONObject(jsonResult.get("ad")))        //广告位
                            commonModel.ad = SPJsonUtil.fromJsonToModel(jsonResult.getJSONObject("ad"), SPHomeBanners.class);
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
     * 抢购活动时间节点
     *
     * @param successListener
     * @param failuredListener
     * @url index.php?m=Api&c=activity&a=flash_sale_time
     */
    public static void getFlashSaleTime(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("activity", "flash_sale_time");
        SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        JSONObject resulJson = response.getJSONObject(Response.RESULT);
                        SPCommonListModel listModel = new SPCommonListModel();
                        if (resulJson.has("time") && SPUtils.velidateJSONArray(resulJson.getJSONArray("time")))
                            listModel.flashTimes = SPJsonUtil.fromJsonArrayToList(resulJson.getJSONArray("time"), SPFlashTime.class);
                        if (resulJson.has("ad") && SPUtils.velidateJSONArray(resulJson.getJSONObject("ad")))
                            listModel.ad = SPJsonUtil.fromJsonToModel(resulJson.getJSONObject("ad"), SPHomeBanners.class);
                        successListener.onRespone(msg, listModel);
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
     * 抢购活动列表
     *
     * @param successListener
     * @param failuredListener
     * @url index.php?m=Api&c=activity&a=flash_sale_list
     */
    public static void getFlashSaleList(int pageIndex, SPFlashTime flashTime, final SPSuccessListener successListener,
                                        final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("activity", "flash_sale_list");
        RequestParams params = new RequestParams();
        params.put("p", pageIndex);
        params.put("start_time", flashTime.getStartTime());
        params.put("end_time", flashTime.getEndTime());
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        JSONArray resulJson = response.getJSONArray(Response.RESULT);
                        List<SPFlashSale> flashSales = SPJsonUtil.fromJsonArrayToList(resulJson, SPFlashSale.class);
                        successListener.onRespone(msg, flashSales);
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
     * 商品物流存货信息
     *
     * @param successListener
     * @param failuredListener
     * @url index.php?m=Api&c=Goods&a=dispatching
     */
    public static void getDispatch(RequestParams params, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Goods", "dispatching");
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(Response.MSG);
                    int status = response.getInt(Response.STATUS);
                    if (status > 0) {
                        double result = response.getDouble(Response.RESULT);
                        successListener.onRespone(msg, result);
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

}
