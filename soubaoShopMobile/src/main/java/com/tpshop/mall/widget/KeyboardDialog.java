package com.tpshop.mall.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpshop.mall.R;
import com.tpshop.mall.activity.person.user.SPPayPwdActivity_;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.utils.SPUtils;

/**
 * Created by zw on 2017-9-23
 */
public class KeyboardDialog extends Dialog implements View.OnClickListener {

    private EditText pwdEt;           //密码输入框
    private final Context context;
    private SureClickListener mListener;

    public KeyboardDialog(Context context) {
        super(context, R.style.keyboard_dialog);
        this.context = context;
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        assert window != null;
        window.requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_keyboard);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.LEFT | Gravity.TOP;
        attributes.x = 0;
        attributes.y = new Double(SPUtils.getWindowheight(context) * 0.2).intValue();
        attributes.width = SPUtils.getWindowWidth(context);
        attributes.height = GridLayoutManager.LayoutParams.WRAP_CONTENT;
        initView();
    }

    private void initView() {
        ImageView ivCloseKey = (ImageView) findViewById(R.id.iv_close_key);
        pwdEt = (EditText) findViewById(R.id.pwd_et);
        TextView sureTvt = (TextView) findViewById(R.id.sure_tvt);
        TextView tvForget = (TextView) findViewById(R.id.tv_forget_psw);
        ivCloseKey.setOnClickListener(this);
        sureTvt.setOnClickListener(this);
        tvForget.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close_key:        //关闭dialog
                pwdEt.setText("");
                pwdEt.clearFocus();
                InputMethodManager imm = (InputMethodManager) pwdEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(pwdEt.getWindowToken(), 0);
                dismiss();
                break;
            case R.id.sure_tvt:        //确定密码
                if (mListener != null) {
                    mListener.getPwd();
                    dismiss();
                }
                break;
            case R.id.tv_forget_psw:       //忘记密码
                Intent pwdIntent = new Intent(context, SPPayPwdActivity_.class);
                pwdIntent.putExtra("value", SPMobileApplication.getInstance().getLoginUser().getMobile());
                context.startActivity(pwdIntent);
                break;
        }
    }

    @Override
    public void show() {
        super.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pwdEt.setFocusable(true);
                pwdEt.setFocusableInTouchMode(true);
                pwdEt.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) pwdEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(pwdEt, 0);
            }
        }, 300);
    }

    public String getPsw() {
        return pwdEt.getText().toString();
    }

    public interface SureClickListener {
        void getPwd();
    }

    public void setSureClickListener(SureClickListener listener) {
        this.mListener = listener;
    }

}
