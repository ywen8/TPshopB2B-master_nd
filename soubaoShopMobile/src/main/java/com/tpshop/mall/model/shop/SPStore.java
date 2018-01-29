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
 * Date: @date 2015年11月26日 下午10:13:37
 * Description:商品列表, 过滤器
 *
 * @version V1.0
 */
package com.tpshop.mall.model.shop;

import android.support.annotation.NonNull;

import com.tpshop.mall.model.SPModel;
import com.tpshop.mall.model.SPProduct;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2016/6/18
 */
public class SPStore implements SPModel, Comparable<SPStore>, Serializable {

    private String lon;                                                  //经度
    private String lat;                                                  //纬度
    private int storeId;                                                 //店铺ID
    private int hotGoods;                                                //热销产品
    private int newGoods;                                                //上新产品
    private Integer type;                                                //类型type,1:选中菜单,2:规格,3:属性,4:品牌,5:价格
    private int isCollect;                                               //是否收藏
    private int isOwnShop;                                               //是否自营店;0:否,1:是
    private int couponNum;                                               //可用优惠券数量
    private String shipfee;                                              //物流信息
    private float distance;                                              //距离
    private int totalGoods;                                              //商品总数
    private int storeCount;                                              //全部商品数量
    private int storeSales;                                              //店铺销量
    private String storeZy;                                              //店铺简介
    private String storeQQ;                                              //联系QQ
    private String selected;                                             //该商品是否在购物车中选择
    private String location;                                             //所在地
    private String slideUrl;                                             //店铺轮播广告
    private String cityName;                                             //城市
    private String promTitle;                                            //店铺优惠
    private String freePrice;                                            //包邮价格
    private String storeTime;                                            //开店时间
    private String gradeName;                                            //等级名称
    private String storeName;                                            //店铺名称
    private String sellerTel;                                            //商家电话
    private String storeLogo;                                            //店铺logo
    private float desccredit;                                            //商品描述相符
    private int storeCollect;                                            //关注数量
    private String storePhone;                                           //联系电话
    private String sellerName;                                           //卖家名称
    private String serviceDate;                                          //保修时间
    private String companyName;                                          //公司名称
    private String storeAddress;                                         //地址
    private String selerMessage;                                         //卖家留言
    private String provinceName;                                         //省份
    private String districtName;                                         //区
    private float servicecredit;                                         //服务态度
    private float deliverycredit;                                        //物流速度
    private double cartTotalMoney;                                       //小计金额
    private SPCoupon selectedCoupon;                                     //选中的优惠券
    private List<SPProduct> hotProducts;                                 //热卖商品
    private List<SPProduct> newProducts;                                 //新品推荐
    transient private JSONArray hotArray;
    transient private JSONArray newArray;
    private List<SPProduct> storeProducts;                               //店铺商品列表
    transient private JSONArray couponArray;                             //优惠券
    private List<SPProduct> recommendProducts;                           //推荐商品
    transient private JSONArray recommendArray;
    transient private JSONArray storeProductArray;

    public SPStore() {
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "storeId", "store_id",
                "storeName", "store_name",
                "sellerName", "seller_name",
                "companyName", "company_name",
                "sellerTel", "store_phone",
                "storeTime", "store_time",
                "storeAddress", "store_address",
                "storeLogo", "store_logo",
                "storeQQ", "store_qq",
                "levelName", "level_name",
                "storeProductArray", "cartList",
                "couponArray", "couponList",
                "desccredit", "store_desccredit",
                "servicecredit", "store_servicecredit",
                "deliverycredit", "store_deliverycredit",
                "storeCount", "store_count",
                "hotArray", "hot_goods_list",
                "recommendArray", "recomend_goods",
                "newArray", "new_goods_list",
                "slideUrl", "mb_slide",
                "storeSales", "store_sales",
                "provinceName", "province_name",
                "cityName", "city_name",
                "districtName", "district_name",
                "isOwnShop", "is_own_shop",
                "storeCollect", "store_collect",
                "totalGoods", "total_goods",
                "newGoods", "new_goods",
                "hotGoods", "hot_goods",
                "isCollect", "is_collect",
                "storeZy", "store_zy",
                "cartTotalMoney", "store_goods_price",
                "serviceDate", "auto_service_date",
                "freePrice", "store_free_price",
                "lon", "longitude",
                "lat", "latitude",
        };
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public double getCartTotalMoney() {
        return cartTotalMoney;
    }

    public void setCartTotalMoney(double cartTotalMoney) {
        this.cartTotalMoney = cartTotalMoney;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSellerTel() {
        return sellerTel;
    }

    public void setSellerTel(String sellerTel) {
        this.sellerTel = sellerTel;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStoreTime() {
        return storeTime;
    }

    public void setStoreTime(String storeTime) {
        this.storeTime = storeTime;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public JSONArray getStoreProductArray() {
        return storeProductArray;
    }

    public void setStoreProductArray(JSONArray storeProductArray) {
        this.storeProductArray = storeProductArray;
    }

    public JSONArray getCouponArray() {
        return couponArray;
    }

    public void setCouponArray(JSONArray couponArray) {
        this.couponArray = couponArray;
    }

    public List<SPProduct> getStoreProducts() {
        return storeProducts;
    }

    public void setStoreProducts(List<SPProduct> storeProducts) {
        this.storeProducts = storeProducts;
    }

    public String getStoreZy() {
        return storeZy;
    }

    public void setStoreZy(String storeZy) {
        this.storeZy = storeZy;
    }

    @Override
    public int compareTo(@NonNull SPStore another) {
        if (storeId < another.storeId) {
            return -1;
        } else if (storeId > another.storeId) {
            return 1;
        } else {
            return 0;
        }
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public SPCoupon getSelectedCoupon() {
        return selectedCoupon;
    }

    public void setSelectedCoupon(SPCoupon selectedCoupon) {
        this.selectedCoupon = selectedCoupon;
    }

    public String getSelerMessage() {
        return selerMessage;
    }

    public void setSelerMessage(String selerMessage) {
        this.selerMessage = selerMessage;
    }

    public float getDesccredit() {
        return desccredit;
    }

    public void setDesccredit(float desccredit) {
        this.desccredit = desccredit;
    }

    public float getServicecredit() {
        return servicecredit;
    }

    public void setServicecredit(float servicecredit) {
        this.servicecredit = servicecredit;
    }

    public float getDeliverycredit() {
        return deliverycredit;
    }

    public void setDeliverycredit(float deliverycredit) {
        this.deliverycredit = deliverycredit;
    }

    public int getStoreCount() {
        return storeCount;
    }

    public void setStoreCount(int storeCount) {
        this.storeCount = storeCount;
    }

    public int getStoreCollect() {
        return storeCollect;
    }

    public void setStoreCollect(int storeCollect) {
        this.storeCollect = storeCollect;
    }

    public List<SPProduct> getHotProducts() {
        return hotProducts;
    }

    public void setHotProducts(List<SPProduct> hotProducts) {
        this.hotProducts = hotProducts;
    }

    public JSONArray getHotArray() {
        return hotArray;
    }

    public void setHotArray(JSONArray hotArray) {
        this.hotArray = hotArray;
    }

    public List<SPProduct> getRecommendProducts() {
        return recommendProducts;
    }

    public void setRecommendProducts(List<SPProduct> recommendProducts) {
        this.recommendProducts = recommendProducts;
    }

    public JSONArray getRecommendArray() {
        return recommendArray;
    }

    public void setRecommendArray(JSONArray recommendArray) {
        this.recommendArray = recommendArray;
    }

    public List<SPProduct> getNewProducts() {
        return newProducts;
    }

    public void setNewProducts(List<SPProduct> newProducts) {
        this.newProducts = newProducts;
    }

    public JSONArray getNewArray() {
        return newArray;
    }

    public void setNewArray(JSONArray newArray) {
        this.newArray = newArray;
    }

    public String getSlideUrl() {
        return slideUrl;
    }

    public void setSlideUrl(String slideUrl) {
        this.slideUrl = slideUrl;
    }

    public String getStoreQQ() {
        return storeQQ;
    }

    public void setStoreQQ(String storeQQ) {
        this.storeQQ = storeQQ;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public int getStoreSales() {
        return storeSales;
    }

    public void setStoreSales(int storeSales) {
        this.storeSales = storeSales;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public int getIsOwnShop() {
        return isOwnShop;
    }

    public void setIsOwnShop(int isOwnShop) {
        this.isOwnShop = isOwnShop;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(int isCollect) {
        this.isCollect = isCollect;
    }

    public int getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(int couponNum) {
        this.couponNum = couponNum;
    }

    public int getTotalGoods() {
        return totalGoods;
    }

    public void setTotalGoods(int totalGoods) {
        this.totalGoods = totalGoods;
    }

    public int getNewGoods() {
        return newGoods;
    }

    public void setNewGoods(int newGoods) {
        this.newGoods = newGoods;
    }

    public int getHotGoods() {
        return hotGoods;
    }

    public void setHotGoods(int hotGoods) {
        this.hotGoods = hotGoods;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setFreePrice(String freePrice) {
        this.freePrice = freePrice;
    }

    public String getFreePrice() {
        return freePrice;
    }

    public String getShipfee() {
        return shipfee;
    }

    public void setShipfee(String shipfee) {
        this.shipfee = shipfee;
    }

    public void setPromTitle(String promTitle) {
        this.promTitle = promTitle;
    }

    public String getPromTitle() {
        return promTitle;
    }

}
