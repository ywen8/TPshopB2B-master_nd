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
 * Description: 抢购时间节点
 *
 * @version V1.0
 */
package com.tpshop.mall.model.shop;

import com.tpshop.mall.model.SPModel;

/**
 * Created by admin on 2016/6/18
 */
public class SPFlashTime implements SPModel {

    private int type;                         //0:过期;1:正在抢购;2:即将开抢
    private String title;                     //显示名称
    private String endTime;                   //结束时间
    private String startTime;                 //开始时间

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "title", "font",
                "startTime", "start_time",
                "endTime", "end_time"
        };
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
