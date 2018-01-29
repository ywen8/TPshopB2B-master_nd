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
 * Description: 商品详情, 评论标题
 *
 * @version V1.0
 */
package com.tpshop.mall.entity;

import com.tpshop.mall.model.SPModel;

import java.io.Serializable;

/**
 * @author 飞龙
 */
public class SPCommentTitleModel implements SPModel, Serializable {

    private int all;            //全部评论数
    private int bad;            //差评数
    private int great;          //好评数
    private int hasPic;         //晒图数
    private int average;        //中评数
    private double badRate;     //差评率
    private double goodRate;    //好评率
    private double averageRate; //中频率

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "all", "c0",
                "great", "c1",
                "average", "c2",
                "bad", "c3",
                "hasPic", "c4",
                "goodRate", "rate1",
                "averageRate", "rate2",
                "badRate", "rate3",
        };
    }

    public int[] datas() {
        return new int[]{all, great, average, bad};
    }

    public int getAll() {
        return all;
    }

    public int getGreat() {
        return great;
    }

    public int getAverage() {
        return average;
    }

    public int getBad() {
        return bad;
    }

    public int getHasPic() {
        return hasPic;
    }

    public double getGoodRate() {
        return goodRate;
    }

    public double getAverageRate() {
        return averageRate;
    }

    public double getBadRate() {
        return badRate;
    }

}
