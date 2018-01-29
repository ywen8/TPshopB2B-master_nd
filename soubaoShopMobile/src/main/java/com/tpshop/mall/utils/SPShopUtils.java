/*
 * tpshop
 * ============================================================================
 * * 版权所有 2015-2027 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ----------------------------------------------------------------------------
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * $Author: 飞龙  16/01/15 $
 * $description:  商品操作工具类
 */
package com.tpshop.mall.utils;

import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.model.SPSpecPriceModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/6/21
 */
public class SPShopUtils {

    /**
     * 对规格ID按升序排序之后,组成的key获取对应的价格和库存
     */
    public static String getPriceModelkey(Collection<String> keys) {
        List<Integer> idlist = new ArrayList<>();
        for (String id : keys) {
            try {
                idlist.add(Integer.valueOf(id));
            } catch (Exception e) {
                e.printStackTrace();
                return "0";
            }
        }
        String priceKey = null;
        if (idlist.size() > 0) {
            Collections.sort(idlist);        //对id原始顺序排序
            priceKey = SPStringUtils.listToString(idlist, "_");
        }
        return priceKey;
    }

    /**
     * 根据规格获取库存数量
     */
    public static int getShopStoreCount(String key, Map<String, SPSpecPriceModel> specPriceMap) {
        try {
            SPSpecPriceModel specPrice = specPriceMap.get(key);
            if (specPrice == null) return 0;
            return specPrice.getStoreCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据规格获取相关价格
     */
    public static String getShopprice(String key, Map<String, SPSpecPriceModel> specPriceMap) {
        try {
            SPSpecPriceModel specPrice = specPriceMap.get(key);
            if (specPrice == null) return "";
            return specPrice.getPrice();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据规格获取规格id
     */
    public static int getItemId(String key, Map<String, SPSpecPriceModel> specPriceMap) {
        try {
            SPSpecPriceModel specPrice = specPriceMap.get(key);
            if (specPrice == null) return 0;
            return specPrice.getItemId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据规格组合获取和规格item_id组名称map(已选规格)
     */
    public static Map<String, String> getSelectSpecMapByItemId(int itemId, Map<String, SPSpecPriceModel> specPriceMap,
                                                               Map<String, String> itemIdForGroupNameMap) {
        if (specPriceMap == null) return null;
        Map<String, String> selectSpecMap = new HashMap<>();         //保存选择的规格ID
        SPSpecPriceModel selectSpecPrice = null;
        for (Map.Entry<String, SPSpecPriceModel> entry : specPriceMap.entrySet()) {              //根据item_id找到规格值(spec_goods_price实体)
            SPSpecPriceModel priceModel = entry.getValue();
            if (priceModel.getItemId() == itemId) {
                selectSpecPrice = priceModel;
                break;
            }
        }
        if (selectSpecPrice == null) return null;
        String key = selectSpecPrice.getKey();              //解析spec_goods_price实体中的key
        String[] keyIds = key.split("_");
        for (String subId : keyIds) {             //根据每个属性id找到规格item_id
            String specKey = itemIdForGroupNameMap.get(subId);
            selectSpecMap.put(specKey, subId);
        }
        return selectSpecMap;
    }

    /**
     * 获取产品规格
     */
    public static String getGoodsSpec(String key, Map<String, SPSpecPriceModel> specPriceMap) {
        try {
            SPSpecPriceModel specPrice = specPriceMap.get(key);
            if (specPrice == null) return "";
            return specPrice.getKeyName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取商品编号
     */
    public static String getGoodsSku(String key, Map<String, SPSpecPriceModel> specPriceMap) {
        try {
            SPSpecPriceModel specPrice = specPriceMap.get(key);
            String sku = "0";
            if (specPrice == null || (sku = specPrice.getSku()) == null) return sku;
            return sku;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 处理推荐商品数据(每两个分为一组)
     */
    public static List<List<SPProduct>> handleRecommendGoods(List<SPProduct> data) {
        return handleRecommendGoods(data, 3);
    }

    /**
     * 处理推荐商品数据(每两个分为一组)
     */
    public static List<List<SPProduct>> handleRecommendGoods(List<SPProduct> data, int countOfPage) {
        List<List<SPProduct>> handleData = new ArrayList<>();
        int length = data.size() / countOfPage;
        if (data.size() % countOfPage != 0)
            length = data.size() / countOfPage + 1;
        for (int i = 0; i < length; i++) {
            List<SPProduct> recommendGoods = new ArrayList<>();
            for (int j = 0; j < (i * countOfPage + j == data.size() ? 1 : countOfPage); j++)
                recommendGoods.add(data.get(i * countOfPage + j));
            handleData.add(recommendGoods);
        }
        return handleData;
    }

}
