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
 * Date: @date 2015年10月27日 下午9:14:42
 * Description: 团购  model
 *
 * @version V1.0
 */
package com.tpshop.mall.model.shop;

import com.tpshop.mall.model.SPModel;

import java.io.Serializable;

/**
 * @author 飞龙
 */
public class SPPromoteGood implements SPModel, Serializable {

    private long endTime;                       //结束时间
    private String itemId;                      //规格ID
    private String goodsId;                     //商品ID
    private long serverTime;                    //服务器时间
    private String goodsName;                   //商品名称
    private String shopPrice;                   //原价
    private String clickCount;
    private String marketPrice;                 //市场价

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "goodsId", "goods_id",
                "goodsName", "goods_name",
                "shopPrice", "shop_price",
                "clickCount", "click_count",
                "marketPrice", "market_price",
                "endTime", "end_time",
                "serverTime", "server_time",
                "itemId", "item_id",
        };
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(String shopPrice) {
        this.shopPrice = shopPrice;
    }

    public String getClickCount() {
        return clickCount;
    }

    public void setClickCount(String clickCount) {
        this.clickCount = clickCount;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

}
