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
 * Description: 抢购/促销/秒杀 Model
 *
 * @version V1.0
 */
package com.tpshop.mall.model.shop;

import com.tpshop.mall.model.SPModel;

/**
 * Created by admin on 2016/6/18
 */
public class SPFlashSale implements SPModel {

    private int percent;
    private long endTime;
    private String price;
    private String itemId;
    private String goodsId;
    private String shopPrice;
    private String goodsName;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "endTime", "end_time",
                "goodsName", "goods_name",
                "price", "price",
                "goodsId", "goods_id",
                "shopPrice", "shop_price",
                "percent", "percent",
                "itemId", "item_id",
        };
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getEndTime() {
        return endTime;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(String shopPrice) {
        this.shopPrice = shopPrice;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

}
