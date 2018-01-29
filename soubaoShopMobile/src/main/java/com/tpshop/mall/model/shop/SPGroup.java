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

import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.model.SPModel;

import java.io.Serializable;

/**
 * @author 飞龙
 */
public class SPGroup implements SPModel, Serializable {

    private int buyNum;                         //商品已购买数
    private String title;                       //标题
    private String price;                       //现价
    private long endTime;                       //结束时间
    private String itemId;                      //规格ID
    private String rebate;                      //折扣率
    private int virtualNum;                     //虚拟购买数量
    private String goodsId;                     //商品ID
    private long serverTime;                    //服务器时间
    private String goodsPrice;                  //原价
    private String marketPrice;                 //市场价

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "goodsId", "goods_id",
                "itemId", "item_id",
                "virtualNum", "virtual_num",
                "buyNum", "buy_num",
                "marketPrice", "market_price",
                "goodsPrice", "goods_price",
                "endTime", "end_time",
                "serverTime", "server_time",
        };
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getRebate() {
        if (SPStringUtils.isEmpty(rebate)) return "0.0";
        return rebate;
    }

    public void setRebate(String rebate) {
        this.rebate = rebate;
    }

    public int getVirtualNum() {
        return virtualNum;
    }

    public void setVirtualNum(int virtualNum) {
        this.virtualNum = virtualNum;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
