/*
 * Copyright (C) 2015 ShenZhen Moze Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳市么子信息技术有限公司开发研制，未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 */
package com.tpshop.mall.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.tpshop.mall.R;

/**
 * @author wangqh
 */
public class SPDialogUtils {

    public static void showToast(Context context, String message) {
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.toast, null);            //加载Toast布局
        TextView mTextView = (TextView) toastRoot.findViewById(R.id.message);
        mTextView.setText(message);
        Toast mToast = new Toast(context);                                                      //Toast的初始化
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();                                        //获取屏幕高度
        mToast.setGravity(Gravity.BOTTOM, 0, height / 6);                                       //Toast的Y坐标是屏幕高度的1/6,不会出现不适配的问题
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setView(toastRoot);
        mToast.show();
    }

}
 
