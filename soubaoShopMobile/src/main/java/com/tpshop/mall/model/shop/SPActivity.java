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
 * Description: 促销活动信息  model
 *
 * @version V1.0
 */
package com.tpshop.mall.model.shop;

import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.entity.SPActivityItem;
import com.tpshop.mall.model.SPModel;

import java.io.Serializable;
import java.util.List;

/**
 * @author 飞龙
 */
public class SPActivity implements SPModel, Serializable {

    private int promType;                              //活动类型,0:默认,1:抢购,2:团购,3:优惠促销,4:预售,5虚拟
    private long curTime;                              //服务器当前时间
    private int virtualNum;                            //参与人数
    private String endTime;                            //结束时间
    private float promPrice;                           //活动价格
    private String startTime;                          //开始时间
    private int promStoreCount;                        //活动库存
    private List<SPActivityItem> datas;                //活动详情信息,例如(商品促销,订单促销)

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "promType", "prom_type",
                "promPrice", "prom_price",
                "virtualNum", "virtual_num",
                "startTime", "prom_start_time",
                "curTime", "server_current_time",
                "endTime", "prom_end_time",
                "promStoreCount", "prom_store_count",
        };
    }

    public int getPromType() {
        return promType;
    }

    public void setPromType(int promType) {
        this.promType = promType;
    }

    public int getVirtualNum() {
        return virtualNum;
    }

    public void setVirtualNum(int virtualNum) {
        this.virtualNum = virtualNum;
    }

    public int getPromStoreCount() {
        return promStoreCount;
    }

    public void setPromStoreCount(int promStoreCount) {
        this.promStoreCount = promStoreCount;
    }

    public float getPromPrice() {
        return promPrice;
    }

    public void setPromPrice(float promPrice) {
        this.promPrice = promPrice;
    }

    public long getStartTime() {
        if (!SPStringUtils.isEmpty(startTime))
            return Long.valueOf(startTime);
        return 0;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        if (!SPStringUtils.isEmpty(endTime))
            return Long.valueOf(endTime);
        return 0;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<SPActivityItem> getDatas() {
        return datas;
    }

    public void setDatas(List<SPActivityItem> datas) {
        this.datas = datas;
    }

    public long getCurTime() {
        return curTime;
    }

    public void setCurTime(long curTime) {
        this.curTime = curTime;
    }

}
