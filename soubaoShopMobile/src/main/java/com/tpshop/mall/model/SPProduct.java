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
 * Date: @date 2015年11月4日 下午10:10:15
 * Description:{一句话描述该类的作用}
 *
 * @version V1.0
 */
package com.tpshop.mall.model;

import com.soubao.tpshop.utils.SPCommonUtils;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.entity.SPCommentTitleModel;
import com.tpshop.mall.model.shop.SPActivity;
import com.tpshop.mall.model.shop.SPProductAttribute;
import com.tpshop.mall.model.shop.SPProductSpec;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author 飞龙
 */
public class SPProduct implements SPModel, Serializable, Comparable<SPProduct> {

    private int recId;
    private int isSend;                                                //0未发货,1已发货,2已换货,3已退货,为1的时候才显示退换货申请
    private int itemId;                                                //商品规格id
    private int storeId;                                               //商家ID
    private int cat_id3;                                               //三级分类ID
    private String isNew;                                              //是否新品:(0:否,1:是)
    private String isHot;                                              //是否最热:(0:否,1:是)
    private int salesSum;                                              //累计销量
    private int goodsNum;                                              //商品所在购物车数量
    private int promType;                                              //0默认,1抢购,2团购,3优惠促销,4预售,5虚拟
    private int returnBtn;                                             //为1的时候显示,isSend,returnBtn两个字段同时位1的时候显示"申请售后"按钮
    private String weight;                                             //商品重量
    private int isCollect;                                             //是否收藏
    private int isVirtual;
    private String cartID;                                             //商品所在购物车ID
    private int isComment;                                             //是否已经评论(目前只支持一次评论)
    private String userID;                                             //用户ID
    private String specKey;                                            //选择的规格KEY(规格ID,多个规格按照"_"分隔)
    private String barCode;                                            //商品对应的sku或者说条码
    private String goodsID;                                            //商品ID
    private String goodsSN;                                            //商品编号
    private String storeQq;                                            //店铺qq
    private String brandID;                                            //品牌ID
    private int storeCount;                                            //库存数量
    private String orderID;                                            //订单ID
    private String orderSN;
    private String keywords;
    private String isOnSale;                                           //是否上架
    private String specType;                                           //规格类型
    private String goodsFee;                                           //小计金额
    private String selected;                                           //该商品是否在购物车中选择,如果选择该商品将会结算,1:选中,0:未选中
    private String promTitle;                                          //活动优惠
    private String goodsType;                                          //所属分类
    private String shopPrice;                                          //本店价(商品价格,如果商品没有规格属性,那么该价格就是商品当前售价)
    private String goodsName;                                          //商品名称
    private int giveIntegral;                                          //赠送积分
    private String storeName;
    private String pointRate;                                          //积分比例折扣
    private String categoryID;                                         //所属分类ID
    private String clickCount;                                         //点击数量
    private String goodsPrice;                                         //本店售价
    private String stockCount;                                         //库存数量
    private String distribute;                                         //佣金
    private String originalImg;                                        //商品图片
    private String commentRate;                                        //好评率
    private String browseCount;                                        //浏览数量
    private String goodsRemark;                                        //商品简单描述
    private String isRecommend;                                        //是否推荐:(0:不推荐,1:推荐)
    private String specKeyName;                                        //选择的规格,展示属性用
    private String marketPrice;                                        //市场价
    private String commentCount;                                       //评论数量
    private String goodsContent;                                       //商品详细描述
    private SPActivity activity;                                       //商品活动信息
    private String storeAddress;
    private String servicePhone;
    private String isFreePostage;                                      //是否包邮:(0:不包邮,1:包邮)
    private String imageThumlUrl;                                      //商品缩略图URL
    private int exchangeIntegral;                                      //兑换积分
    private List<String> gallerys;                                     //图片详情URL
    private String orderStatusCode;                                    //订单状态code
    private String memberGoodsPrice;                                   //会员价
    private List<SPProduct> recommends;                                //推荐商品
    private List<SPProductAttribute> attrArr;                          //商品属性
    private SPCommentTitleModel commentTitleModel;
    private Map<String, List<SPProductSpec>> specGroupMap;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "recId", "rec_id",
                "goodsID", "goods_id",
                "goodsName", "goods_name",
                "storeQq", "store_qq",
                "categoryID", "cat_id",
                "goodsSN", "goods_sn",
                "browseCount", "click_count",
                "brandID", "brand_id",
                "stockCount", "kc_count",
                "commentCount", "comment_count",
                "commentRate", "good_comment_rate",
                "marketPrice", "market_price",
                "shopPrice", "shop_price",
                "memberGoodsPrice", "member_goods_price",
                "originalImg", "original_img",
                "goodsRemark", "goods_remark",
                "goodsContent", "goods_content",
                "isOnSale", "is_on_sale",
                "isFreePostage", "is_baoyou",
                "isRecommend", "is_tuijian",
                "isNew", "isnew",
                "isHot", "is_hot",
                "goodsType", "goods_type",
                "specType", "spec_type",
                "isSend", "is_send",
                "isCollect", "is_collect",
                "storeId", "store_id",
                "giveIntegral", "give_integral",
                "exchangeIntegral", "exchange_integral",
                "pointRate", "point_rate",
                "salesSum", "sales_sum",
                "commentTitleModel", "statistics",
                "orderID", "order_id",
                "orderSN", "order_sn",
                "storeName", "store_name",
                "storeAddress", "store_address",
                "servicePhone", "service_phone",
                "promType", "prom_type",
                "cat_id3", "cat_id3",
                "itemId", "item_id",
                "isVirtual", "is_virtual",
                "cartID", "id",
                "userID", "user_id",
                "goodsNum", "goods_num",
                "specKey", "spec_key",
                "specKeyName", "spec_key_name",
                "barCode", "bar_code",
                "goodsPrice", "goods_price",
                "goodsFee", "goods_fee",
                "storeCount", "store_count",
                "isComment", "is_comment",
                "distribute", "distribut",
                "promTitle", "prom_title",
        };
    }

    public int getRecId() {
        return recId;
    }

    public void setRecId(int recId) {
        this.recId = recId;
    }

    public void setGallerys(List<String> gallerys) {
        this.gallerys = gallerys;
    }

    public int getIsSend() {
        return isSend;
    }

    public void setIsSend(int isSend) {
        this.isSend = isSend;
    }

    public int getReturnBtn() {
        return returnBtn;
    }

    public void setReturnBtn(int returnBtn) {
        this.returnBtn = returnBtn;
    }

    public String getGoodsID() {
        return goodsID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public String getGoodsSN() {
        return goodsSN;
    }

    public String getOriginalImg() {
        return originalImg;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public String getStoreQq() {
        return storeQq;
    }

    public void setStoreQq(String storeQq) {
        this.storeQq = storeQq;
    }

    public String getBrandID() {
        return brandID;
    }

    public String getStockCount() {
        return stockCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public String getCommentRate() {
        return commentRate;
    }

    public String getBrowseCount() {
        return browseCount;
    }

    public String getWeight() {
        return weight;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public String getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(String shopPrice) {
        this.shopPrice = shopPrice;
    }

    public void setMemberGoodsPrice(String memberGoodsPrice) {
        this.memberGoodsPrice = memberGoodsPrice;
    }

    public String getMemberGoodsPrice() {
        return memberGoodsPrice;
    }

    public String getGoodsRemark() {
        return goodsRemark;
    }

    public String getGoodsContent() {
        return goodsContent;
    }

    public String getClickCount() {
        return clickCount;
    }

    public String getIsOnSale() {
        return isOnSale;
    }

    public String getIsFreePostage() {
        return isFreePostage;
    }

    public String getIsRecommend() {
        return isRecommend;
    }

    public String getIsNew() {
        return isNew;
    }

    public String getIsHot() {
        return isHot;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public String getSpecType() {
        return specType;
    }

    public void setIsVirtual(int isVirtual) {
        this.isVirtual = isVirtual;
    }

    public int getIsVirtual() {
        return isVirtual;
    }

    public List<SPProductAttribute> getAttrArr() {
        return attrArr;
    }

    public void setAttrArr(List<SPProductAttribute> attrArr) {
        this.attrArr = attrArr;
    }

    public String getCartID() {
        return cartID;
    }

    public String getUserID() {
        return userID;
    }

    public String getSpecKey() {
        return specKey;
    }

    public void setSpecKeyName(String specKeyName) {
        this.specKeyName = specKeyName;
    }

    public String getSpecKeyName() {
        return specKeyName;
    }

    public String getBarCode() {
        return barCode;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public String getGoodsFee() {
        return goodsFee;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public int getStoreCount() {
        return storeCount;
    }

    public String getImageThumlUrl() {
        return SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, this.goodsID);
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public int getIsComment() {
        return isComment;
    }

    public void setIsComment(int isComment) {
        this.isComment = isComment;
    }

    public String getOrderStatusCode() {
        return orderStatusCode;
    }

    public void setOrderStatusCode(String orderStatusCode) {
        this.orderStatusCode = orderStatusCode;
    }

    public String getOrderSN() {
        return orderSN;
    }

    public void setOrderSN(String orderSN) {
        this.orderSN = orderSN;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    public int getStoreId() {
        return storeId;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public int getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(int isCollect) {
        this.isCollect = isCollect;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getGiveIntegral() {
        return giveIntegral;
    }

    public void setGiveIntegral(int giveIntegral) {
        this.giveIntegral = giveIntegral;
    }

    public String getPointRate() {
        return pointRate;
    }

    public void setPointRate(String pointRate) {
        this.pointRate = pointRate;
    }

    public int getSalesSum() {
        return salesSum;
    }

    public void setSalesSum(int salesSum) {
        this.salesSum = salesSum;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDistribute() {
        return distribute;
    }

    public void setDistribute(String distribute) {
        this.distribute = distribute;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public int compareTo(SPProduct another) {
        if (storeId < another.storeId) {
            return -1;
        } else if (storeId > another.storeId) {
            return 1;
        } else {
            return 0;
        }
    }

    public int getExchangeIntegral() {
        return exchangeIntegral;
    }

    public void setExchangeIntegral(int exchangeIntegral) {
        this.exchangeIntegral = exchangeIntegral;
    }

    public SPCommentTitleModel getCommentTitleModel() {
        return commentTitleModel;
    }

    public void setCommentTitleModel(SPCommentTitleModel commentTitleModel) {
        this.commentTitleModel = commentTitleModel;
    }

    public List<String> getGallerys() {
        return gallerys;
    }

    public int getCat_id3() {
        return cat_id3;
    }

    public void setCat_id3(int cat_id3) {
        this.cat_id3 = cat_id3;
    }

    public List<SPProduct> getRecommends() {
        return recommends;
    }

    public void setRecommends(List<SPProduct> recommends) {
        this.recommends = recommends;
    }

    public SPActivity getActivity() {
        return activity;
    }

    public void setActivity(SPActivity activity) {
        this.activity = activity;
    }

    public int getPromType() {
        return promType;
    }

    public void setPromType(int promType) {
        this.promType = promType;
    }

    public Map<String, List<SPProductSpec>> getSpecGroupMap() {
        return specGroupMap;
    }

    public void setSpecGroupMap(Map<String, List<SPProductSpec>> specGroupMap) {
        this.specGroupMap = specGroupMap;
    }

    public String getPromTitle() {
        return promTitle;
    }

    public void setPromTitle(String promTitle) {
        this.promTitle = promTitle;
    }

}
