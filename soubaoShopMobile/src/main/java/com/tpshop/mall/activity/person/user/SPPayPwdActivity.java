/*
 * FYRun
 * ============================================================================
 * * 版权所有 2015-2027 Ben，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * $Author: Ben on 2017/5/5
 * $description:
 */
package com.tpshop.mall.activity.person.user;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPCapitalRequest;
import com.tpshop.mall.http.person.SPPersonRequest;
import com.tpshop.mall.http.person.SPUserRequest;
import com.tpshop.mall.utils.RandomCode;
import com.tpshop.mall.utils.SPServerUtils;
import com.tpshop.mall.utils.SPUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_sppay_pwd)
public class SPPayPwdActivity extends SPBaseActivity {

    int mSmsTimeOut;                         //短信验证码超时时间
    String scene = "6";                      //发送短信场景
    String mPhoneNumber;                     //手机号码
    String strRandomCode = "";
    private boolean isFirst = true;
    CheckCodeCountTimer mCountDownTimer;

    @ViewById(R.id.validate_layout)
    RelativeLayout validateLayout;

    @ViewById(R.id.pwd_layout)
    RelativeLayout pwdLayout;

    @ViewById(R.id.tv_phone_number)
    TextView tvPhoneNumber;

    @ViewById(R.id.edit_pic_code)
    EditText editPicCode;

    @ViewById(R.id.txt_rand_code)
    ImageView randomCodeView;

    @ViewById(R.id.edit_verification_code)
    EditText editVerificationCode;

    @ViewById(R.id.btn_verification_code)
    Button btnVerification;

    @ViewById(R.id.ed_pwd)
    EditText edPwd;

    @ViewById(R.id.ed_pwd_again)
    EditText edPwdAgain;

    @ViewById(R.id.ok_btn)
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle arg0) {
        setCustomerTitle(true, true, getString(R.string.set_pay_pwd));
        super.onCreate(arg0);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        mSmsTimeOut = SPServerUtils.getSmsTimeOut();
        mCountDownTimer = new CheckCodeCountTimer(mSmsTimeOut * 2 * 1000, 1000);
    }

    @Override
    public void initEvent() {
        editVerificationCode.addTextChangedListener(textWatcher);
    }

    @Override
    public void initData() {
        mPhoneNumber = getIntent().getStringExtra("value");
        tvPhoneNumber.setText(mPhoneNumber);
        getVeridyCode();
    }

    public void onRandomCodeClick(View view) {
        getVeridyCode();
    }

    /**
     * 获取图形验证码
     */
    public void getVeridyCode() {
        SPCapitalRequest.getVerifyCodeSuccess(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                strRandomCode = (String) response;
                randomCodeView.setImageBitmap(RandomCode.getInstance().createBitmap(strRandomCode));
            }
        }, new SPFailuredListener(SPPayPwdActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showFailedToast(msg);
            }
        });
    }

    public void onResultOkClick(View view) {
        String picCode = editPicCode.getText().toString();
        String code = editVerificationCode.getText().toString();
        String pwd = edPwd.getText().toString();
        if (isFirst) {
            if (picCode.trim().isEmpty()) {
                showToast("请输入图形验证码");
                return;
            }
            if (code.trim().isEmpty()) {
                showToast("请输入验证码!");
                return;
            }
            if (!picCode.equalsIgnoreCase(strRandomCode)) {
                showToast("图形验证码错误!");
                getVeridyCode();
                return;
            }
            validateLayout.setVisibility(View.GONE);
            pwdLayout.setVisibility(View.VISIBLE);
            btnSubmit.setText(getString(R.string.change_mobile_submit));
            isFirst = false;
        } else {     //修改密码
            try {
                if (pwd.trim().equals("")) {
                    showToast("请输入密码");
                    return;
                }
                if (edPwdAgain.getText().toString().trim().equals("")) {
                    showToast("请输入确认密码");
                    return;
                }
                if (!edPwdAgain.getText().toString().equals(pwd)) {
                    showToast("两次密码输入不一致");
                    return;
                }
                RequestParams params = new RequestParams();
                params.put("mobile", mPhoneNumber);
                params.put("paypwd_code", code);
                params.put("new_password", SPUtils.md5WithAuthCode(pwd));
                SPPersonRequest.setPaypwd(params, new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        showSuccessToast(msg);
                        finish();
                    }
                }, new SPFailuredListener(SPPayPwdActivity.this) {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        showFailedToast(msg);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //发送短信验证码
    public void onBtnVerificationClick(View view) {
        if (SPMobileConstants.ENABLE_SMS_CODE) {      //启用短信验证码
            String picCode = editPicCode.getText().toString();
            if (picCode.trim().isEmpty()) {
                showToast("请输入图形验证码");
                return;
            }
            if (!picCode.equalsIgnoreCase(strRandomCode)) {
                showToast("图形验证码错误!");
                getVeridyCode();
                return;
            }
            SPUserRequest.sendSmsValidateCode(mPhoneNumber, scene, new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    showSuccessToast(msg);
                    mCountDownTimer.start();
                    setSendSmsButtonStatus(false);
                }
            }, new SPFailuredListener(SPPayPwdActivity.this) {
                @Override
                public void onRespone(String msg, int errorCode) {
                    showFailedToast(msg);
                    setSendSmsButtonStatus(true);
                }
            });
        } else {      //未启用短信验证码
            setSendSmsButtonStatus(false);
        }
    }

    private class CheckCodeCountTimer extends CountDownTimer {
        CheckCodeCountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btnVerification.setText(getString(R.string.register_btn_re_code, millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            btnVerification.setText(getString(R.string.register_btn_re_code_done));
            setSendSmsButtonStatus(true);
        }
    }

    //修改发送短信验证码状态
    public void setSendSmsButtonStatus(boolean enable) {
        if (enable)      //启用
            btnVerification.setEnabled(true);
        else      //禁用
            btnVerification.setEnabled(false);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 0)
                btnSubmit.setEnabled(false);
            else
                btnSubmit.setEnabled(true);
        }
    };

}
