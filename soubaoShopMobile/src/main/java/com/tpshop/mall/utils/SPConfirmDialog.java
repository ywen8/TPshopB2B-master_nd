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
 * Date: @date 2015年11月2日 下午8:30:29
 * Description: 自定义确认框
 *
 * @version V1.0
 */
package com.tpshop.mall.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpshop.mall.R;
import com.tpshop.mall.SPMainActivity;

/**
 * Created by admin on 2016/7/4
 */
public class SPConfirmDialog extends Dialog {

    public SPConfirmDialog(Context context, int theme) {
        super(context, theme);
    }

    public interface ConfirmDialogListener {
        void clickOk(int actionType);         //确定
    }

    public static class Builder {
        private String title;
        private String message;
        private Context context;
        private View contentView;
        private String positiveButtonText;
        private String negativeButtonText;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public SPConfirmDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final SPConfirmDialog dialog = new SPConfirmDialog(context, R.style.confirmDialog);
            View layout = inflater.inflate(R.layout.confirm_dialog_layout, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    (layout.findViewById(R.id.positiveButton)).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.positiveButton).setVisibility(View.GONE);
            }
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negativeButton)).setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    (layout.findViewById(R.id.negativeButton)).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.negativeButton).setVisibility(View.GONE);
            }
            if (message != null) {
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            } else if (contentView != null) {
                ((LinearLayout) layout.findViewById(R.id.content)).removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content)).addView(contentView,
                        new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
            }
            dialog.setContentView(layout);
            Window dialogWindow = dialog.getWindow();
            dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            WindowManager m = SPMainActivity.getmInstance().getWindowManager();
            Display d = m.getDefaultDisplay();                                        //获取屏幕宽、高用
            WindowManager.LayoutParams p = dialogWindow.getAttributes();              //获取对话框当前的参数值
            p.height = Float.valueOf(SPMainActivity.getmInstance().getResources().getDimension(R.dimen.dp_150)).intValue();                              //高度设置为屏幕的0.6
            p.width = (int) (d.getWidth() * 0.65);                                    //宽度设置为屏幕的0.65
            dialogWindow.setAttributes(p);
            return dialog;
        }
    }

}
