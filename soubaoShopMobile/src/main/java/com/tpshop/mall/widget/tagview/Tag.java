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
 * Date: @date 2015年11月14日 下午8:17:18
 * Description:带箭头的自定义view
 *
 * @version V1.0
 */
package com.tpshop.mall.widget.tagview;

import java.io.Serializable;

public class Tag implements Serializable {

    private int id;
    private String key;
    private String title;
    private String value;
    private boolean isChecked;
    private int backgroundResId;
    private int leftDrawableResId;
    private int rightDrawableResId;
    private static final long serialVersionUID = 2684657309332033242L;

    public Tag() {
    }

    Tag(int paramInt, String paramString) {
        this.id = paramInt;
        this.title = paramString;
    }

    int getBackgroundResId() {
        return this.backgroundResId;
    }

    public int getId() {
        return this.id;
    }

    int getLeftDrawableResId() {
        return this.leftDrawableResId;
    }

    int getRightDrawableResId() {
        return this.rightDrawableResId;
    }

    public String getTitle() {
        return this.title;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setBackgroundResId(int paramInt) {
        this.backgroundResId = paramInt;
    }

    public void setChecked(boolean paramBoolean) {
        this.isChecked = paramBoolean;
    }

    public void setId(int paramInt) {
        this.id = paramInt;
    }

    public void setLeftDrawableResId(int paramInt) {
        this.leftDrawableResId = paramInt;
    }

    public void setRightDrawableResId(int paramInt) {
        this.rightDrawableResId = paramInt;
    }

    public void setTitle(String paramString) {
        this.title = paramString;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
