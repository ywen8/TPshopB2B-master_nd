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
 * Description: 商品规格价格  model
 *
 * @version V1.0
 */
package com.tpshop.mall.model;

import java.io.Serializable;

/**
 * @author 飞龙
 */
public class SPSpecPriceModel implements SPModel, Serializable {

    private String key;                       //ID组成的KEY
    private int itemId;                       //规格id
    private String sku;                       //商品编号
    private String price;                     //价格
    private String keyName;
    private int storeCount;                   //库存
    private String specName;                  //规格名称

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "storeCount", "store_count",
                "keyName", "key_name",
                "itemId", "item_id",
        };
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getStoreCount() {
        return storeCount;
    }

    public void setStoreCount(int storeCount) {
        this.storeCount = storeCount;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int specName) {
        this.itemId = itemId;
    }

}
