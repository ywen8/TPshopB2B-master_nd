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

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ToggleButton;

import com.tpshop.mall.R;

public class TagView extends ToggleButton {

    private Context mContext;
    public boolean mCheckEnable = true;

    public TagView(Context paramContext) {
        super(paramContext);
        init(paramContext);
    }

    public TagView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init(paramContext);
    }

    public TagView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init(paramContext);
    }

    private void init(Context paramContext) {
        setTextOn(null);
        setTextOff(null);
        setText("");
        setBackgroundResource(R.drawable.tag_button_bg_unchecked);
        this.mContext = paramContext;
    }

    public void setCheckEnable(boolean paramBoolean) {
        this.mCheckEnable = paramBoolean;
        if (!this.mCheckEnable) super.setChecked(false);
    }

    public void setSelected(boolean isSelected) {
        int textColor;
        if (isSelected) {
            setBackgroundResource(R.drawable.tag_button_bg_checked);
            textColor = ContextCompat.getColor(mContext, R.color.light_red);
        } else {
            setBackgroundResource(R.drawable.tag_button_bg_unchecked);
            textColor = ContextCompat.getColor(mContext, R.color.gray);
        }
        setTextColor(textColor);
    }

}
