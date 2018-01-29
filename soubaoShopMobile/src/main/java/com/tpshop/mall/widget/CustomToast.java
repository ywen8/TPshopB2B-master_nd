package com.tpshop.mall.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tpshop.mall.R;

/**
 * Created by zw on 2017/6/2
 */
public class CustomToast {

    private static CustomToast mToast;

    private CustomToast() {
    }

    public static CustomToast getToast() {
        if (mToast == null)
            mToast = new CustomToast();
        return mToast;
    }

    public void ToastShow(Context context, String str, int imageRes) {
        View view = LayoutInflater.from(context).inflate(R.layout.warm_dialog, null);
        TextView text = (TextView) view.findViewById(R.id.tv_warmdialog);
        ImageView warmImg = (ImageView) view.findViewById(R.id.warmImg);
        text.setText(str);                                     //设置显示文字
        warmImg.setImageResource(imageRes);                    //设置显示图片
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);                //Toast显示的位置
        toast.setDuration(Toast.LENGTH_SHORT);                 //Toast显示的时间
        toast.setView(view);
        toast.show();
    }

}
