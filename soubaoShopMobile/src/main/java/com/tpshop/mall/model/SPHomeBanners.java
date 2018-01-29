/*
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2099 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: 无风  peach885090@163.com
 * Date: @date 2016年7月11日 下午14:14:42
 * Description: 分类  model
 *
 * @version V1.0
 */
package com.tpshop.mall.model;

import java.io.Serializable;

/**
 * @author 无风
 */
public class SPHomeBanners implements SPModel, Serializable {

    private int pid;                  //广告位置(首页广告定位用)
    private String adLink;            //广告跳转值(根据广告类型区分)
    private String adName;            //广告名称
    private String adCode;            //广告图片
    private int mediaType;            //广告类型

    public String getAdLink() {
        return adLink;
    }

    public void setAdLink(String adLink) {
        this.adLink = adLink;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getPid() {
        return pid;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "adLink", "ad_link",
                "adName", "ad_name",
                "adCode", "ad_code",
                "mediaType", "media_type",
                "pid", "pid",
        };
    }

}
