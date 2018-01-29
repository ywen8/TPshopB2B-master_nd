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
 * Description: 店铺产品分类  model
 *
 * @version V1.0
 */
package com.tpshop.mall.model;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.List;

/**
 * @author 飞龙
 */
public class SPStoreGoodsClass implements SPModel, Serializable {

    private int id;                                                           //分类ID
    private String name;                                                      //分类名称
    private int storeId;                                                      //店铺ID
    private JSONArray childrenArray;                                          //子分类
    private SPStoreGoodsClass parentGoodsClass;
    private List<SPStoreGoodsClass> childrenGoodsClasses;                     //子分类

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "id", "cat_id",
                "name", "cat_name",
                "storeId", "store_id",
                "childrenArray", "children"
        };
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public List<SPStoreGoodsClass> getChildrenGoodsClasses() {
        return childrenGoodsClasses;
    }

    public void setChildrenGoodsClasses(List<SPStoreGoodsClass> childrenGoodsClasses) {
        this.childrenGoodsClasses = childrenGoodsClasses;
    }

    public JSONArray getChildrenArray() {
        return childrenArray;
    }

    public void setChildrenArray(JSONArray childrenArray) {
        this.childrenArray = childrenArray;
    }

    public SPStoreGoodsClass getParentGoodsClass() {
        return parentGoodsClass;
    }

    public void setParentGoodsClass(SPStoreGoodsClass parentGoodsClass) {
        this.parentGoodsClass = parentGoodsClass;
    }

}
