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
 * Description: 用户中心数据接口
 *
 * @version V1.0
 */
package com.tpshop.mall.http.person;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.utils.SPJsonUtil;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPMobileHttptRequest;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.model.SPCategory;
import com.tpshop.mall.model.SPCommentData;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.model.distribute.SPDistributeGood;
import com.tpshop.mall.model.distribute.SPDistributeModel;
import com.tpshop.mall.model.distribute.SPDistributeOrder;
import com.tpshop.mall.model.distribute.SPDistributeStore;
import com.tpshop.mall.model.distribute.SPMoneyModel;
import com.tpshop.mall.model.distribute.SPStoreGood;
import com.tpshop.mall.model.distribute.SPStoreInfo;
import com.tpshop.mall.model.distribute.SPStorePic;
import com.tpshop.mall.model.distribute.SPTeamModel;
import com.tpshop.mall.model.distribute.SPUserModel;
import com.tpshop.mall.model.order.SPButtom;
import com.tpshop.mall.model.order.SPOrder;
import com.tpshop.mall.model.order.SPVrorder;
import com.tpshop.mall.model.person.SPConsigneeAddress;
import com.tpshop.mall.model.person.SPRegionModel;
import com.tpshop.mall.model.shop.SPCollect;
import com.tpshop.mall.model.shop.SPCoupon;
import com.tpshop.mall.model.shop.SPStore;
import com.tpshop.mall.utils.SPUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by admin on 2016/6/21
 */
public class SPPersonRequest {

    /**
     * 收藏/取消收藏商品
     *
     * @param successListener
     * @param failuredListener
     * @URL index.php?m=Api&c=Goods&a=collectGoodsOrNo
     */
    public static void collectOrCancelGoodsWithID(String goodsID, final SPSuccessListener successListener,
                                                  final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Goods", "collectGoodsOrNo");
        RequestParams params = new RequestParams();
        if (!SPStringUtils.isEmpty(goodsID))
            params.put("goods_id", goodsID);
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

    /**
     * 商品收藏列表
     *
     * @param successListener
     * @param failuredListener
     * @URL index.php?m=Api&c=User&a=getGoodsCollect
     */
    public static void getGoodsCollectWithSuccess(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "getGoodsCollect");
        SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        JSONArray resultArray = response.getJSONArray(SPMobileConstants.Response.RESULT);
                        List<SPCollect> collects = SPJsonUtil.fromJsonArrayToList(resultArray, SPCollect.class);
                        successListener.onRespone(msg, collects);
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
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 获取收货人列表
     *
     * @param successListener
     * @param failuredListener
     * @URL index.php/Api/User/getAddressList
     */
    public static void getConsigneeAddressList(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "getAddressList");
        SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    List<SPConsigneeAddress> consignees = null;
                    if (status > 0) {
                        if (SPUtils.velidateJSONArray(response.get(SPMobileConstants.Response.RESULT)))
                            consignees = SPJsonUtil.fromJsonArrayToList(response.getJSONArray(SPMobileConstants.Response.RESULT),
                                    SPConsigneeAddress.class);
                        successListener.onRespone(msg, consignees);
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
     * 订单列表
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=User&a=getOrderList
     */
    public static void getOrderListWithParams(RequestParams params, final SPSuccessListener successListener,
                                              final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "getOrderList");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        List<SPOrder> orders = SPJsonUtil.fromJsonArrayToList(response.getJSONArray(SPMobileConstants.Response.RESULT),
                                SPOrder.class);
                        if (orders != null) {
                            for (SPOrder order : orders) {
                                SPButtom buttom = SPJsonUtil.fromJsonToModel(order.getButtomObject(), SPButtom.class);
                                SPStore store = SPJsonUtil.fromJsonToModel(order.getStoreObject(), SPStore.class);
                                List<SPProduct> products = SPJsonUtil.fromJsonArrayToList(order.getProductsArray(), SPProduct.class);
                                order.setButtom(buttom);
                                order.setStore(store);
                                order.setProducts(products);
                            }
                        }
                        successListener.onRespone(msg, orders);
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
     * 虚拟订单列表
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=api&c=virtual&a=virtual_list
     */
    public static void getVirtualOrderList(RequestParams params, final SPSuccessListener successListener,
                                           final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("virtual", "virtual_list");
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        List<SPOrder> orders = SPJsonUtil.fromJsonArrayToList(response.getJSONArray(SPMobileConstants.Response.RESULT),
                                SPOrder.class);
                        if (orders != null) {
                            for (SPOrder order : orders) {
                                SPButtom buttom = SPJsonUtil.fromJsonToModel(order.getButtomObject(), SPButtom.class);
                                SPStore store = SPJsonUtil.fromJsonToModel(order.getStoreObject(), SPStore.class);
                                List<SPProduct> products = SPJsonUtil.fromJsonArrayToList(order.getProductsArray(), SPProduct.class);
                                order.setButtom(buttom);
                                order.setStore(store);
                                order.setProducts(products);
                            }
                        }
                        successListener.onRespone(msg, orders);
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
     * 根据订单id获取订单详情
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=Order&a=order_detail
     */
    public static void getOrderDetailByID(String orderID, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Order", "order_detail");
        RequestParams params = new RequestParams();
        params.put("id", orderID);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        SPOrder order = SPJsonUtil.fromJsonToModel(response.getJSONObject(SPMobileConstants.Response.RESULT), SPOrder.class);
                        if (order != null) {
                            SPButtom buttom = SPJsonUtil.fromJsonToModel(order.getButtomObject(), SPButtom.class);
                            SPStore store = SPJsonUtil.fromJsonToModel(order.getStoreObject(), SPStore.class);
                            List<SPProduct> products = SPJsonUtil.fromJsonArrayToList(order.getProductsArray(), SPProduct.class);
                            order.setButtom(buttom);
                            order.setStore(store);
                            order.setProducts(products);
                            if (order.getVrorderArray().length() > 0) {
                                List<SPVrorder> vrorders = SPJsonUtil.fromJsonArrayToList(order.getVrorderArray(), SPVrorder.class);
                                order.setVrorders(vrorders);
                            }
                        }
                        successListener.onRespone(msg, order);
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
     * 取消订单(未支付)
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL m=Api&c=User&a=cancelOrder
     */
    public static void cancelOrderWithOrderID(String orderID, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "cancelOrder");
        RequestParams params = new RequestParams();
        params.put("order_id", orderID);
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

    /**
     * 取消订单(已支付)
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL m=Api&c=order&a=refund_order
     */
    public static void cancelOrder(String orderID, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("order", "refund_order");
        RequestParams params = new RequestParams();
        params.put("order_id", orderID);
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

    /**
     * 确认收货
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL m=Api&c=User&a=orderConfirm
     */
    public static void confirmOrderWithOrderID(String orderID, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "orderConfirm");
        RequestParams params = new RequestParams();
        params.put("order_id", orderID);
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

    /**
     * 添加或编辑收货地址
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php/Api/User/addAddress
     */
    public static void saveUserAddressWithParameter(RequestParams params, final SPSuccessListener successListener,
                                                    final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "addAddress");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, status);
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
     * 删除收货地址信息
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=User&a=del_address
     */
    public static void delConsigneeAddressByID(String consigneeID, final SPSuccessListener successListener,
                                               final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "del_address");
        RequestParams params = new RequestParams();
        params.put("id", consigneeID);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, "1");
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
     * 填写订单获取店铺可用的优惠券
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=api&c=User&a=cart_coupons
     */
    public static void getOrderCouponList(RequestParams params, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "getCartStoreCoupons");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        List<SPCoupon> coupons = SPJsonUtil.fromJsonArrayToList(response.getJSONArray(SPMobileConstants.Response.RESULT),
                                SPCoupon.class);
                        successListener.onRespone(msg, coupons);
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
     * 获取优惠券列表
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=User&a=getCouponList
     */
    public static void getCouponListWithType(int type, int storeId, double orderMoney, int page, final SPSuccessListener successListener,
                                             final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "getCouponList");
        RequestParams params = new RequestParams();
        params.put("p", page);
        params.put("type", type);
        params.put("store_id", storeId);
        params.put("order_money", orderMoney);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        List<SPCoupon> coupons = SPJsonUtil.fromJsonArrayToList(response.getJSONArray(SPMobileConstants.Response.RESULT),
                                SPCoupon.class);
                        successListener.onRespone(msg, coupons);
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
     * 获取优惠券分类
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=Activity&a=coupon_type_list
     */
    public static void getCouponCenterCategoryWithType(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Activity", "coupon_type_list");
        RequestParams params = new RequestParams();
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        List<SPCategory> coupons = SPJsonUtil.fromJsonArrayToList(response.getJSONArray(SPMobileConstants.Response.RESULT),
                                SPCategory.class);
                        successListener.onRespone(msg, coupons);
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
     * 获取某分类下的优惠券
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=Activity&a=coupon_center
     */
    public static void getCouponCenterWithCategoryId(int pageIndex, int cateId, final SPSuccessListener successListener,
                                                     final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Activity", "coupon_center");
        RequestParams params = new RequestParams();
        params.put("cat_id", cateId);
        params.put("p", pageIndex);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        List<SPCoupon> coupons = SPJsonUtil.fromJsonArrayToList(response.getJSONArray(SPMobileConstants.Response.RESULT),
                                SPCoupon.class);
                        successListener.onRespone(msg, coupons);
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
     * 领取优惠券
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=Activity&a=get_coupon
     */
    public static void gainCoupon(String couponId, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Activity", "get_coupon");
        RequestParams params = new RequestParams();
        params.put("coupon_id", couponId);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, status);
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
     * 商品评论(图片上传)
     *
     * @param successListener
     * @param failuredListener
     * @URL index.php?m=Api&c=User&a=add_comment
     */
    public static void commentGoodsWithGoodsID(SPCommentData commentCondition, final SPSuccessListener successListener,
                                               final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "add_comment");
        RequestParams params = new RequestParams();
        params.put("order_id", commentCondition.getOrderId());            //订单id
        params.put("goods_id", commentCondition.getGoodsId());            //商品id
        params.put("rec_id", commentCondition.getRecId());
        params.put("goods_rank", commentCondition.getGoodsRank());        //商品星级
        params.put("deliver_rank", commentCondition.getDeliverRank());    //物流星级
        params.put("service_rank", commentCondition.getServiceRank());    //服务星级
        params.put("goods_score", commentCondition.getGoodsScore());      //商品评价
        params.put("is_anonymous", commentCondition.getIs_anonymous());   //是否匿名
        if (!SPStringUtils.isEmpty(commentCondition.getContent()))
            params.put("content", commentCondition.getContent());         //评论内容
        List<File> images = commentCondition.getImages();                 //晒单图片
        if (images != null) {
            for (int i = 0; i < images.size(); i++) {
                File file = images.get(i);
                try {
                    params.put("comment_img_file[" + i + "]", file, "image/png");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, 1);
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
     * 获取申请售后商品信息
     *
     * @param params
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=api&c=order&a=return_goods
     */
    public static void getApplyWithParameter(RequestParams params, final SPSuccessListener successListener,
                                             final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("order", "return_goods");
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        SPProduct product = SPJsonUtil.fromJsonToModel(response.getJSONObject(SPMobileConstants.Response.RESULT), SPProduct.class);
                        successListener.onRespone(msg, product);
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
     * 是否可以申请售后
     *
     * @param recId
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=api&c=User&a=return_goods_status
     */
    public static void getApplyStatus(int recId, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        RequestParams params = new RequestParams();
        params.put("rec_id", recId);
        String url = SPMobileHttptRequest.getRequestUrl("User", "return_goods_status");
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int result = response.getInt(SPMobileConstants.Response.RESULT);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, result);
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
     * 申请退换货
     *
     * @param params
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=Order&a=return_goods
     */
    public static void exchangeApplyWithParameter(RequestParams params, List<File> images, final SPSuccessListener successListener,
                                                  final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Order", "return_goods");
        if (images != null && images.size() > 0) {
            for (int i = 0; i < images.size(); i++) {
                File file = images.get(i);
                try {
                    params.put("return_imgs[" + i + "]", file, "image/png");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, response);
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
     * 获取店铺收藏列表
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL api&c=user&a=getUserCollectStore
     */
    public static void getCollectStore(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("user", "getUserCollectStore");
        SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        JSONArray results = response.getJSONArray(SPMobileConstants.Response.RESULT);
                        List<SPStore> stores = SPJsonUtil.fromJsonArrayToList(results, SPStore.class);
                        successListener.onRespone(msg, stores);
                    } else {
                        failuredListener.onRespone(msg, status);
                    }
                } catch (Exception e) {
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
     * 分销首页数据
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=api&c=distribut&a=index
     */
    public static void getDistributionData(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("distribut", "index");
        SPMobileHttptRequest.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        JSONObject results = response.getJSONObject(SPMobileConstants.Response.RESULT);
                        SPDistributeModel distributeModel = SPJsonUtil.fromJsonToModel(results, SPDistributeModel.class);
                        SPMoneyModel moneyModel = SPJsonUtil.fromJsonToModel(distributeModel.getMoneyObjct(), SPMoneyModel.class);
                        distributeModel.setMoneyModel(moneyModel);
                        SPUserModel userModel = SPJsonUtil.fromJsonToModel(distributeModel.getUserObjct(), SPUserModel.class);
                        distributeModel.setUserModel(userModel);
                        successListener.onRespone(msg, distributeModel);
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
     * 分销成员列表
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=Distribut&a=lower_list
     */
    public static void getDistributionMember(RequestParams params, final SPSuccessListener successListener,
                                             final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Distribut", "lower_list");
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        JSONArray results = response.getJSONArray(SPMobileConstants.Response.RESULT);
                        List<SPTeamModel> teamModels = SPJsonUtil.fromJsonArrayToList(results, SPTeamModel.class);
                        successListener.onRespone(msg, teamModels);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
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
     * 获取分销店铺信息
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=Distribut&a=store
     */
    public static void getDistributionInfo(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Distribut", "store");
        SPMobileHttptRequest.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        JSONObject results = response.getJSONObject(SPMobileConstants.Response.RESULT);
                        SPDistributeStore distributeStore = SPJsonUtil.fromJsonToModel(results, SPDistributeStore.class);
                        successListener.onRespone(msg, distributeStore);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
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
     * 设置分销店铺信息
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=Distribut&a=store
     */
    public static void changeDistributionInfo(RequestParams params, final SPSuccessListener successListener,
                                              final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Distribut", "store");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, response);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
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
     * 分销订单列表
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=Distribut&a=order_list
     */
    public static void getDistributionOrder(RequestParams params, final SPSuccessListener successListener,
                                            final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Distribut", "order_list");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        JSONArray results = response.getJSONArray(SPMobileConstants.Response.RESULT);
                        List<SPDistributeOrder> distributeOrders = SPJsonUtil.fromJsonArrayToList(results, SPDistributeOrder.class);
                        if (distributeOrders != null) {
                            for (SPDistributeOrder distributeOrder : distributeOrders) {
                                List<SPDistributeGood> distributeGoods = SPJsonUtil.fromJsonArrayToList(distributeOrder.getGoodsArray(),
                                        SPDistributeGood.class);
                                distributeOrder.setDistributeGoods(distributeGoods);
                            }
                        }
                        successListener.onRespone(msg, distributeOrders);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
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
     * 分销店铺概况
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=Distribut&a=store_summery
     */
    public static void getDistributionStore(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Distribut", "store_summery");
        SPMobileHttptRequest.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        JSONObject results = response.getJSONObject(SPMobileConstants.Response.RESULT);
                        SPStoreInfo storeInfo = SPJsonUtil.fromJsonToModel(results, SPStoreInfo.class);
                        if (storeInfo != null) {
                            SPStorePic storePic = SPJsonUtil.fromJsonToModel(storeInfo.getStoreObject(), SPStorePic.class);
                            storeInfo.setStorePic(storePic);
                        }
                        successListener.onRespone(msg, storeInfo);
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
     * 分销店铺商品列表
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=Distribut&a=my_store
     */
    public static void getDistributionStoreGoods(RequestParams params, final SPSuccessListener successListener,
                                                 final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Distribut", "my_store");
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        JSONArray results = response.getJSONArray(SPMobileConstants.Response.RESULT);
                        List<SPStoreGood> storeGoods = SPJsonUtil.fromJsonArrayToList(results, SPStoreGood.class);
                        successListener.onRespone(msg, storeGoods);
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
     * 获取区域下级地址列表
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=index&a=get_region
     */
    public static void getRegion(String regionId, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        RequestParams params = new RequestParams();
        params.put("parent_id", regionId);
        String url = SPMobileHttptRequest.getRequestUrl("index", "get_region");
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        JSONArray results = response.getJSONArray(SPMobileConstants.Response.RESULT);
                        List<SPRegionModel> regions = SPJsonUtil.fromJsonArrayToList(results, SPRegionModel.class);
                        successListener.onRespone(msg, regions);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
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
     * 修改手机号
     *
     * @param params
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=api&c=user&a=change_mobile
     */
    public static void updateUserMobile(RequestParams params, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("user", "change_mobile");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, status);
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
     * 设置(修改)支付密码
     *
     * @param params
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL /index.php?m=Api&c=User&a=paypwd
     */
    public static void setPaypwd(RequestParams params, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "paypwd");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, status);
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
