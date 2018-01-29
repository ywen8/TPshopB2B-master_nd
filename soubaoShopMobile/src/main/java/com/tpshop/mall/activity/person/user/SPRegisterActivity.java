/*
 * tpshop
 * ============================================================================
 * * 版权所有 2015-2027 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ----------------------------------------------------------------------------
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * $Author: Ben  16/07/08
 * $description: 用户注册/找回密码
 */
package com.tpshop.mall.activity.person.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.SPMainActivity;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPCapitalRequest;
import com.tpshop.mall.http.person.SPUserRequest;
import com.tpshop.mall.model.person.SPUser;
import com.tpshop.mall.utils.RandomCode;
import com.tpshop.mall.utils.SPServerUtils;
import com.tpshop.mall.utils.SPUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.jpush.android.api.JPushInterface;

@EActivity(R.layout.register_forget)
public class SPRegisterActivity extends SPBaseActivity {

    int mSmsTimeOut;                   //短信验证码超时时间
    String mCheckCode;                 //验证码
    String scene = "1";                //发送短信场景
    String mPhoneNumber;               //手机号码
    private boolean isBind;
    boolean mPwdOpen = false;
    String strRandomCode = "";
    boolean mRePwdOpen = false;
    CheckCodeCountTimer mCountDownTimer;

    @ViewById(R.id.phone_num_edtv)
    EditText phoneNumEdtv;             //手机号码

    @ViewById(R.id.check_code_edtv)
    EditText checkCodeEdtv;            //验证码

    @ViewById(R.id.submit_btn)
    Button submitBtn;                  //提交

    @ViewById(R.id.send_code_btn)
    Button sendCodeBtn;                //发送短信验证码

    @ViewById(R.id.password_edtv)
    EditText passwordEdtv;

    @ViewById(R.id.repassword_edtv)
    EditText repasswordEdtv;

    @ViewById(R.id.img_view_pwd)
    ImageButton imgViewPwd;

    @ViewById(R.id.img_view_repwd)
    ImageButton img_view_repwd;

    @ViewById(R.id.validate_code_edtv)
    EditText txtCapacheCode;

    @ViewById(R.id.txt_rand_code)
    ImageView randomCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.register_title));
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
    }

    @Override
    public void initEvent() {
        phoneNumEdtv.addTextChangedListener(textWatcherCode);
        phoneNumEdtv.addTextChangedListener(textWatcherRegister);
        checkCodeEdtv.addTextChangedListener(textWatcherRegister);
        passwordEdtv.addTextChangedListener(textWatcherRegister);
        repasswordEdtv.addTextChangedListener(textWatcherRegister);
        txtCapacheCode.addTextChangedListener(textWatcherRegister);
    }

    @Override
    public void initData() {
        if (getIntent() != null) {
            isBind = getIntent().getBooleanExtra("bind", false);
        }
        submitBtn.setText(getString(R.string.register_title));
        scene = "1";
        if (SPMobileConstants.DevTest) {
            phoneNumEdtv.setText("13800138006");
            passwordEdtv.setText("123456");
            repasswordEdtv.setText("123456");
        }
        sendCodeBtn.setText(getString(R.string.register_btn_re_code_done));
        sendCodeBtn.setBackgroundResource(R.drawable.btn_bg);
        mSmsTimeOut = SPServerUtils.getSmsTimeOut();
        mCountDownTimer = new CheckCodeCountTimer(mSmsTimeOut * 1000, 1000);
        getVeridyCode();      //获取图形验证码
    }

    private void getVeridyCode() {
        SPCapitalRequest.getVerifyCodeSuccess(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                strRandomCode = (String) response;
                randomCodeView.setImageBitmap(RandomCode.getInstance().createBitmap(strRandomCode));
            }
        }, new SPFailuredListener(SPRegisterActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showFailedToast(msg);
            }
        });
    }

    public void onClearRandomClick(View view) {
        txtCapacheCode.setText("");
    }

    @Click({R.id.submit_btn, R.id.send_code_btn})
    public void onViewClick(View v) {
        mPhoneNumber = phoneNumEdtv.getText().toString();
        if (SPStringUtils.isEmpty(mPhoneNumber)) {
            phoneNumEdtv.setError(Html.fromHtml("<font color='red'>" + getString(R.string.register_phone_number_null) + "</font>"));
            return;
        } else if (!SPUtils.isPhoneLegal(mPhoneNumber)) {
            phoneNumEdtv.setError(Html.fromHtml("<font color='red'>" + getString(R.string.register_error_phone_format_error) + "</font>"));
            return;
        }
        switch (v.getId()) {
            case R.id.submit_btn:         //注册
                register();
                break;
            case R.id.send_code_btn:      //发送短信验证码
                sendCode();
                break;
        }
    }

    //注册
    private void register() {
        mCheckCode = checkCodeEdtv.getText().toString();
        if (SPStringUtils.isEmpty(mCheckCode)) {
            checkCodeEdtv.setError(Html.fromHtml("<font color='red'>" + getString(R.string.edit_code_null) + "</font>"));
            return;
        }
        String pwd = passwordEdtv.getText().toString();
        String repwd = repasswordEdtv.getText().toString();
        if (SPStringUtils.isEmpty(pwd)) {
            passwordEdtv.setError(Html.fromHtml("<font color='red'>" + getString(R.string.register_password_null) + "</font>"));
            return;
        }
        if (SPStringUtils.isEmpty(repwd)) {
            repasswordEdtv.setError(Html.fromHtml("<font color='red'>" + getString(R.string.register_confirm_password_null) + "</font>"));
            return;
        }
        if (!pwd.equals(repwd)) {
            repasswordEdtv.setError(Html.fromHtml("<font color='red'>" + getString(R.string.register_error_info_re) + "</font>"));
            return;
        }
        if (pwd.length() < 6 || pwd.length() > 16) {
            repasswordEdtv.setError(Html.fromHtml("<font color='red'>" + getString(R.string.register_error_info) + "</font>"));
            return;
        }
        String capache = txtCapacheCode.getText().toString();
        if (!strRandomCode.equalsIgnoreCase(capache)) {
            showToast("图形验证码错误!");
            getVeridyCode();
            return;
        }
        String pushId = JPushInterface.getRegistrationID(this);
        showLoadingSmallToast();
        SPUserRequest.doRegister(mPhoneNumber, pwd, mCheckCode, pushId, capache, isBind,
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingSmallToast();
                        if (response != null) {
                            SPUser user = (SPUser) response;
                            showSuccessToast("注册成功!");
                            SPMobileApplication.getInstance().setLoginUser(user);
                            startActivity(new Intent(SPRegisterActivity.this, SPMainActivity.class));
                        }
                    }
                }, new SPFailuredListener(SPRegisterActivity.this) {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        hideLoadingSmallToast();
                        showFailedToast(msg);
                    }
                });
    }

    //发送短信验证码
    private void sendCode() {
        String picCode = txtCapacheCode.getText().toString();
        if (picCode.trim().isEmpty()) {
            showToast("请输入图形验证码");
            return;
        }
        if (!picCode.equalsIgnoreCase(strRandomCode)) {
            showToast("图形验证码错误!");
            getVeridyCode();
            return;
        }
        if (SPMobileConstants.ENABLE_SMS_CODE) {      //启用短信验证码
            SPUserRequest.sendSmsValidateCode(mPhoneNumber, scene, new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    showSuccessToast(msg);
                    mCountDownTimer.start();
                    setSendSmsButtonStatus(false);
                }
            }, new SPFailuredListener(SPRegisterActivity.this) {
                @Override
                public void onRespone(String msg, int errorCode) {
                    showFailedToast(msg);
                    setSendSmsButtonStatus(true);
                }
            });
        } else {      //未启用短信验证码
            setSendSmsButtonStatus(false);
            checkCodeEdtv.setText("1234");
        }
    }

    /**
     * 修改发送短信验证码状态
     */
    public void setSendSmsButtonStatus(boolean enable) {
        if (enable) {      //启用
            sendCodeBtn.setEnabled(true);
            sendCodeBtn.setBackgroundResource(R.drawable.btn_bg);
            sendCodeBtn.setTextColor(getResources().getColor(R.color.white));
        } else {      //禁用
            sendCodeBtn.setEnabled(false);
            sendCodeBtn.setBackgroundResource(R.drawable.btn_unpressed);
            sendCodeBtn.setTextColor(getResources().getColor(R.color.color_font_gray));
        }
    }

    private class CheckCodeCountTimer extends CountDownTimer {
        CheckCodeCountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            sendCodeBtn.setText(getString(R.string.register_btn_re_code, millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            sendCodeBtn.setText(getString(R.string.register_btn_re_code_done));
            setSendSmsButtonStatus(true);
        }
    }

    /**
     * 重新生成验证码
     */
    public void onRandomCodeClick(View view) {
        getVeridyCode();
    }

    public void onPasswordVisiableClick(View view) {
        if (mPwdOpen) {
            imgViewPwd.setImageResource(R.drawable.icon_secrecy_pwd);
            passwordEdtv.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mPwdOpen = false;
        } else {
            imgViewPwd.setImageResource(R.drawable.icon_open_pwd);
            passwordEdtv.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mPwdOpen = true;
        }
    }

    public void onPasswordReVisiableClick(View view) {
        if (mRePwdOpen) {
            img_view_repwd.setImageResource(R.drawable.icon_secrecy_pwd);
            repasswordEdtv.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mRePwdOpen = false;
        } else {
            img_view_repwd.setImageResource(R.drawable.icon_open_pwd);
            repasswordEdtv.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mRePwdOpen = true;
        }
    }

    private TextWatcher textWatcherCode = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (phoneNumEdtv.getText().length() == 0)
                sendCodeBtn.setEnabled(false);
            else
                sendCodeBtn.setEnabled(true);
        }
    };

    private TextWatcher textWatcherRegister = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (phoneNumEdtv.getText().length() == 0 || checkCodeEdtv.getText().length() == 0
                    || passwordEdtv.getText().length() == 0 || repasswordEdtv.getText().length() == 0
                    || txtCapacheCode.getText().length() == 0)
                submitBtn.setEnabled(false);
            else
                submitBtn.setEnabled(true);
        }
    };

}
