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
 * Date: @date 2017-4-15 15:17:56
 * Description: 商品详情, 促销信息描述  model
 *
 * @version V1.0
 */
package com.tpshop.mall.entity;

import com.tpshop.mall.model.SPModel;

import java.io.Serializable;

/**
 * Created by admin on 2017/6/2
 */
public class SPActivityItem implements SPModel, Serializable {

    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "title", "title",
                "content", "content",
        };
    }

}
