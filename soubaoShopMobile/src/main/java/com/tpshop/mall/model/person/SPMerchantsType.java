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
 * Description: 收货地址  model
 *
 * @version V1.0
 */
package com.tpshop.mall.model.person;

import java.io.Serializable;
import java.util.List;

/**
 * 收货地址
 */
public class SPMerchantsType implements Serializable {

    private int firstTypeId;                    //一级分类ID
    private String firstType;                   //一级分类
    private String thirdType;                   //三级分类
    private int secondTypeId;                   //二级分类ID
    private String secondType;                  //二级分类
    private List<Integer> thirdTypeId;          //三级分类ID

    public void setFirstType(String firstType) {
        this.firstType = firstType;
    }

    public String getFirstType() {
        return firstType;
    }

    public void setSecondType(String secondType) {
        this.secondType = secondType;
    }

    public String getSecondType() {
        return secondType;
    }

    public void setThirdType(String thirdType) {
        this.thirdType = thirdType;
    }

    public String getThirdType() {
        return thirdType;
    }

    public void setFirstTypeId(int firstTypeId) {
        this.firstTypeId = firstTypeId;
    }

    public int getFirstTypeId() {
        return firstTypeId;
    }

    public void setSecondTypeId(int secondTypeId) {
        this.secondTypeId = secondTypeId;
    }

    public int getSecondTypeId() {
        return secondTypeId;
    }

    public void setThirdTypeId(List<Integer> thirdTypeId) {
        this.thirdTypeId = thirdTypeId;
    }

    public List<Integer> getThirdTypeId() {
        return thirdTypeId;
    }

}
