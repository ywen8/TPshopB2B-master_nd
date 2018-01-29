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
 * Description: 品牌
 *
 * @version V1.0
 */
package com.tpshop.mall.model.shop;

import com.tpshop.mall.model.SPModel;

/**
 * Created by admin on 2016/6/18
 */
public class SPBrand implements SPModel {

    private String url;                        //所在地
    private int brandId;                       //店铺ID
    private String logo;                       //卖家名称
    private String brandName;                  //店铺名称

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "brandId", "id",
                "brandName", "name"
        };
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
